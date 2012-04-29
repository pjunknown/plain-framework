/*
 * Copyright 2012 Project UNKONW.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package jp.techie.plain.framework.servlet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.RandomStringUtils;

import jp.techie.plain.framework.Const;
import jp.techie.plain.framework.util.LogUtil;

/**
 * Webアプリケーション変数格納JavaBean
 * 
 * @author bose999
 * 
 */
@SuppressWarnings("serial")
public class WebApplicationValue implements Serializable {

    /**
     * ActionクラスのJNDI名
     */
    public String actionClassJndiName;

    /**
     * ServletContext
     */
    public ServletContext application;

    /**
     * HttpServletRequest
     */
    public HttpServletRequest request;

    /**
     * HttpServletResponse
     */
    public HttpServletResponse response;

    /**
     * HttpSession
     */
    public HttpSession session;

    /**
     * URLから引数を解析しListに格納
     */
    public List<String> urlParamList;

    /**
     * セッションスコープメモリ節約用のリクエストスコープ内ブラウザクライアントでのパラメータキャッシュ<br />
     * クライアント側へ渡してしまうがセッションごとでランダムに生成されるkeyでBlowfish暗号化されている
     */
    public Map<String, Object> clientSideSiriarizedCache;

    /**
     * ログユーティリティ
     */
    public static LogUtil logUtil = new LogUtil(WebApplicationValue.class);

    /**
     * コンストラクタ
     */
    public WebApplicationValue() {
    }

    /**
     * コンストラクタ
     * 
     * @param application
     *            ServletContext
     * @param request
     *            HttpServletRequest
     * @param response
     *            HttpServletResponse
     */
    public WebApplicationValue(String actionClassJndiName, ServletContext application, HttpServletRequest request,
            HttpServletResponse response) {
        this.actionClassJndiName = actionClassJndiName;
        this.application = application;
        this.request = request;
        this.response = response;
        this.session = request.getSession();
        this.urlParamList = makeUrlParamList(request);
        this.clientSideSiriarizedCache =
                makeMapFromBase64SiriarizedValue(request.getParameter(Const.CLIENT_SIDE_SIRIARIZED_CAHCE_KEY));
    }

    /**
     * アプリケーションスコープ 属性設定
     * 
     * @param attributeName
     *            属性名
     * @param attribute
     *            値
     */
    public void putApplicationAttribute(String attributeName, Object attribute) {
        application.setAttribute(attributeName, attribute);
        logUtil.trace("ApplicationValue put:attributeName:" + attributeName + " :" + attribute.toString());
    }

    /**
     * アプリケーションスコープ値取得
     * 
     * @param attributeName
     *            属性名
     * @return 値
     */
    @SuppressWarnings("unchecked")
    public <T> T getApplicationAttribute(String attributeName) {
        Object attribute = application.getAttribute(attributeName);
        logUtil.trace("ApplicationValue get:attributeName:" + attributeName + " :" + attribute.toString());
        return (T) attribute;
    }

    /**
     * リクエストスコープパラメータMap取得
     * 
     * @return リクエストスコープパラメータMap
     */
    public Map<String, String[]> getRequestParameterMap() {
        Map<String, String[]> parameterMap = request.getParameterMap();
        if (logUtil.isTraceEnabled()) {
            Set<Entry<String, String[]>> set = parameterMap.entrySet();
            logUtil.trace("request parameterMap:");
            for (Entry<String, String[]> entry : set) {
                logUtil.trace("    key:" + entry.getKey());
                for (String value : entry.getValue()) {
                    logUtil.trace("        value:" + value);
                }
            }
        }
        return parameterMap;
    }

    /**
     * リクエストスコープ パラメータ複数値取得
     * 
     * @param key
     *            値名
     * @return パタメータ複数値格納リスト
     */
    public List<String> getRequestParameters(String key) {
        String[] requestPrameterStrings = request.getParameterValues(key);
        List<String> requestPrameters = Arrays.asList(requestPrameterStrings);
        if (logUtil.isTraceEnabled()) {
            for (String requestPrameter : requestPrameters) {
                logUtil.trace("request parameters(" + key + "):");
                logUtil.trace("    value:" + requestPrameter);
            }
        }
        return requestPrameters;
    }

    /**
     * リクエストスコープ パラメータ取得
     * 
     * @param key
     *            値名
     * @return パタメータ値
     */
    public String getRequestParameter(String key) {
        String requestPrameter = request.getParameter(key);
        if (logUtil.isTraceEnabled()) {
            logUtil.trace("request parameter(" + key + "):" + requestPrameter);
        }
        return requestPrameter;
    }

    /**
     * リクエストスコープ 属性設定
     * 
     * @param attributeName
     *            属性名
     * @param attribute
     *            値
     */
    public void putRequestAttribute(String attributeName, Object attribute) {
        request.setAttribute(attributeName, attribute);
        logUtil.trace("RequestValue put:attributeName:" + attributeName + " :" + attribute.toString());
    }

    /**
     * リクエストスコープ 属性取得
     * 
     * @param attributeName
     *            属性名
     * @return 値
     */
    @SuppressWarnings("unchecked")
    public <T> T getRequestAttribute(String attributeName) {
        Object attribute = request.getAttribute(attributeName);
        logUtil.trace("RequestValue get:attributeName:" + attributeName + " :" + attribute.toString());
        return (T) attribute;
    }

    /**
     * セッションスコープ 属性設定
     * 
     * @param attributeName
     *            属性名
     * @param attribute
     *            値
     */
    public void putSessionAttribute(String attributeName, Object attribute) {
        session.setAttribute(attributeName, attribute);
        logUtil.trace("SessionValue put:attributeName:" + attributeName + " :" + attribute.toString());
    }

    /**
     * セッションスコープ 属性取得
     * 
     * @param attributeName
     *            属性名
     * @return 値
     */
    @SuppressWarnings("unchecked")
    public <T> T getSessionAttribute(String attributeName) {
        Object attribute = session.getAttribute(attributeName);
        logUtil.trace("SessionValue get:attributeName:" + attributeName + " :" + attribute.toString());
        return (T) attribute;
    }

    /**
     * URLによるパラメータ値List取得
     * 
     * @return URLによるパラメータ値List
     */
    public List<String> getUrlParamList() {
        return urlParamList;
    }

    /**
     * Sessionメモリ容量節約用にリクエストスコープへclientSideSiriarizedCacheとして<br />
     * Map<String,Object>を格納しClientとやり取りを可能にする際のリクエストスコープへの設定
     * 
     * @param clientSideSiriarizedCache
     *            ブラウザクライアント用キャッシュ
     */
    public void putClientSideSiriarizedCache(Map<String, Object> clientSideSiriarizedCache) {
        // TODO 実験実装メンバーで検討
        request.setAttribute(Const.CLIENT_SIDE_SIRIARIZED_CAHCE_KEY,
            makeBase64SiriarizedValueFromMap(clientSideSiriarizedCache));
    }

    /**
     * Sessionメモリ容量節約用にリクエストスコープへclientSideSiriarizedCacheとして<br />
     * Map<String,Object>を格納しClientとやり取りを可能にする際のリクエストスコープからの取得
     * 
     * @return clientSideSiriarizedCache ブラウザクライアント用キャッシュ
     */
    public Map<String, Object> getClientSideSiriarizedCache() {
        // TODO 実験実装メンバーで検討
        return this.clientSideSiriarizedCache;
    }

    /**
     * URLのメソッド移行のパラメータを/区切りからListに変換
     * 
     * @param request
     *            HttpServletRequest
     * @return パラメータ格納リスト
     */
    protected List<String> makeUrlParamList(HttpServletRequest request) {

        String contextPath = request.getContextPath();
        String requestURI = request.getRequestURI();
        String urlParamString = requestURI.substring(contextPath.length(), requestURI.length());
        String[] paramStrings = urlParamString.split("/", 0);
        int paramStringsLength = paramStrings.length;
        int nowLength = 0;
        List<String> paramValue = new ArrayList<String>();
        while (paramStringsLength > nowLength) {
            if (nowLength > 2) {
                // Contextとクラス名を除いてから格納
                paramValue.add(paramStrings[nowLength]);
            }
            nowLength++;
        }
        return paramValue;
    }

    /**
     * Mapを暗号化された文字列にする
     * 
     * @param Map<String, Object> 暗号化するMap
     * @return String 暗号化文字列
     */
    protected String makeBase64SiriarizedValueFromMap(Map<String, Object> clientSideSiriarizedCache) {
        String base64SiriarizedValue = "";
        OutputStream outputStream = null;
        ObjectOutputStream objectOutputStream = null;
        try {
            // ResponseBeanをシリアライズしてByteArrayOutputStreamにする
            outputStream = new ByteArrayOutputStream();
            objectOutputStream = new ObjectOutputStream(outputStream);
            objectOutputStream.writeObject(clientSideSiriarizedCache);
            ByteArrayOutputStream byteArrayOutputStream = (ByteArrayOutputStream) outputStream;

            // 暗号化に使うkeyを取得する
            String key = (String) session.getAttribute(Const.SESSION_SECRET_KEY);
            if (key == null || key.equals("")) {
                session.setAttribute(Const.SESSION_SECRET_KEY, RandomStringUtils.randomAscii(100));
            }

            // シリアライズ結果をBlowfish暗号化する
            byte[] sirializedObject = byteArrayOutputStream.toByteArray();
            SecretKeySpec sksSpec = new SecretKeySpec(key.getBytes(), "Blowfish");
            Cipher cipher = Cipher.getInstance("Blowfish");
            cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, sksSpec);
            byte[] encryptSirializedObject = cipher.doFinal(sirializedObject);

            // シリアライズ結果をBlowfish暗号化したbyte[]をBase64エンコーディングしてUTF-8文字列にする
            byte[] encoded = Base64.encodeBase64(encryptSirializedObject);
            base64SiriarizedValue = new String(encoded, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (BadPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            try {
                objectOutputStream.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                outputStream.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return base64SiriarizedValue;
    }

    /**
     * 暗号化された文字列からMapを復元する
     * 
     * @param String 暗号化された文字列
     * @return Map<String, Object> 文字列から復元したMap
     */
    @SuppressWarnings("unchecked")
    protected Map<String, Object> makeMapFromBase64SiriarizedValue(String base64SiriarizedValue) {
        // TODO 実験実装メンバーで検討
        if (base64SiriarizedValue == null || base64SiriarizedValue.equals("")) {
            return null;
        } else {
            InputStream inputStream = null;
            ObjectInputStream objectInputStream = null;
            Map<String, Object> clientSideSiriarizedCacheMap = null;
            try {
                // UTF-8のBase64文字列からbyte[]を復元
                byte[] decode = Base64.decodeBase64(base64SiriarizedValue.getBytes("UTF-8"));

                // Blowfish暗号化されているバイト配列をセッションに格納しているkeyからデコード
                String key = (String) session.getAttribute(Const.SESSION_SECRET_KEY);
                SecretKeySpec sksSpec = new SecretKeySpec(key.getBytes(), "Blowfish");
                Cipher cipher = Cipher.getInstance("Blowfish");
                cipher.init(Cipher.DECRYPT_MODE, sksSpec);

                byte[] decrypt = cipher.doFinal(decode);

                // 暗号化を解いたbyte[]からclientSideSiriarizedCacheを復元
                inputStream = new ByteArrayInputStream(decrypt);
                objectInputStream = new ObjectInputStream(inputStream);
                clientSideSiriarizedCacheMap = (Map<String, Object>) objectInputStream.readObject();
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (NoSuchPaddingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (InvalidKeyException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IllegalBlockSizeException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (BadPaddingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                try {
                    objectInputStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                try {
                    inputStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            return clientSideSiriarizedCacheMap;
        }
    }
}
