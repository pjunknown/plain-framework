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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.MissingResourceException;

import javax.ejb.EJB;
import javax.servlet.AsyncContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.techie.plain.framework.Const;
import jp.techie.plain.framework.control.ActionControlExecuter;
import jp.techie.plain.framework.exception.NoSuchActionException;
import jp.techie.plain.framework.util.ApplicationSettingUtil;
import jp.techie.plain.framework.util.LogLevel;
import jp.techie.plain.framework.util.LogOutputUtil;
import jp.techie.plain.framework.util.LogUtil;
import jp.techie.plain.framework.util.MessageUtil;

/**
 * フレームワーク処理サーブレット<br />
 * web.xmlにて自由にurlを設定可能
 * 
 * @author bose999
 *
 */
@SuppressWarnings("serial")
public class ActionServlet extends HttpServlet {

    /**
     * ログユーティリティ
     */
    private static LogUtil logUtil = new LogUtil(ActionServlet.class);

    /**
     * アクションコントローラ非同期実行クラス
     */
    @EJB
    private ActionControlExecuter actionControlExecuter;

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest
     * , javax.servlet.http.HttpServletResponse)
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        execute(request, response);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest
     * , javax.servlet.http.HttpServletResponse)
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        execute(request, response);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.servlet.http.HttpServlet#doPut(javax.servlet.http.HttpServletRequest
     * , javax.servlet.http.HttpServletResponse)
     */
    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response) {
        execute(request, response);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.servlet.http.HttpServlet#doDelete(javax.servlet.http.HttpServletRequest
     * , javax.servlet.http.HttpServletResponse)
     */
    @Override
    public void doDelete(HttpServletRequest request, HttpServletResponse response) {
        execute(request, response);
    }

    /**
     * GET/POST/PUT/DELETEメソッドから呼び出される共通処理
     * 
     * @param request
     *            HttpServletRequest
     * @param response
     *            HttpServletResponse
     * @throws Exception
     */
    protected void execute(HttpServletRequest request, HttpServletResponse response) {
        long startTime = LogOutputUtil.outputStartTime(logUtil, LogLevel.DEBUG, "Start ActionServlet Method");
        AsyncContext asyncContext = null;
        try {
            // 非同期処理準備
            asyncContext = request.startAsync();
            Long actionServletTimeout =
                    Long.valueOf(ApplicationSettingUtil.getMessage(Const.ACTION_SERVLET_TIMEOUT_KEY) + "000");
            asyncContext.setTimeout(actionServletTimeout);

            // MVCのCのクラスにてビジネスロジックを行うクラスへつなげる処理をする
            actionControlExecuter.execute(asyncContext, new WebApplicationValue(makeJNDIName(request),
                getServletContext(), request, response));
        } catch (NoSuchActionException e) {
            logUtil.fatal("actionClassName error.", e);
            dispatchErrorPage(request, response, asyncContext, Const.ACTION_NOT_FOUND_ERROR_MESSAGE_KEY);
        } catch (MissingResourceException mre) {
            logUtil.fatal("servlet timeout setting error.", mre);
            dispatchErrorPage(request, response, asyncContext, Const.ACTION_SERVLET_TIMEOUT_VALUE_ERROR_MESSAGE_KEY);
        } catch (NumberFormatException nfe) {
            logUtil.fatal("servlet timeout setting error.", nfe);
            dispatchErrorPage(request, response, asyncContext, Const.ACTION_SERVLET_TIMEOUT_FORMAT_ERROR_MESSAGE_KEY);
        }
        LogOutputUtil.outputEndTime(logUtil, LogLevel.DEBUG, startTime, "End ActionServlet Method");
    }

    /**
     * URLからActionBeanのJNDI名を生成
     * 
     * @param request
     *            HttpServletRequest
     * @return クラス名
     */
    protected String makeJNDIName(HttpServletRequest request) {
        String contextPath = request.getContextPath();
        String requestURI = request.getRequestURI();
        String urlParamString = requestURI.substring(contextPath.length(), requestURI.length());
        String[] paramStrings = urlParamString.split("/", 0);
        String jndiName = null;
        if (paramStrings.length >= 3) {
            // 0:emptyString 1:action 2:componet name after: param
            String actionName = paramStrings[2];
            logUtil.info("actionName:" + actionName);
            jndiName =
                    "java:module/" + actionName.substring(0, 1).toUpperCase() + actionName.substring(1) + "Action";
        } else {
            logUtil.fatal("jndiName:null Can't use application.");
            throw new NoSuchActionException("action is null");
        }
        return jndiName;
    }

    /**
     * エラー時エラーページ遷移
     * 
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param errorMessageKey エラーメッセージKey文字列
     */
    protected void dispatchErrorPage(HttpServletRequest request, HttpServletResponse response,
            AsyncContext asyncContext, String errorMessageKey) {
        try {
            // リクエストのエラーメッセージ格納領域にメッセージを格納してerrorUrlへ遷移する
            String errorUrl = ApplicationSettingUtil.getMessage(Const.ERROR_URL_KEY);
            String errorMessages = MessageUtil.getMessage(errorMessageKey);
            request.setAttribute(Const.ERROR_MESSAGES_REQUEST_KEY, errorMessages);
            asyncContext.complete();
            RequestDispatcher dispatch = request.getRequestDispatcher(errorUrl);
            dispatch.forward(request, response);
            logUtil.fatal("dispatch errorPage");
        } catch (MissingResourceException mre) {
            logUtil.fatal("errorUrl is empty.display empty.", mre);
            try {
                // errorUrlが取得出来なかったのでHTMLを書きだして明示
                asyncContext.complete();
                PrintWriter printWriter = response.getWriter();
                printWriter.println("<html>\n");
                printWriter.println("<head><title>erorr</title></haed>\n");
                printWriter.println("<body>\n");
                printWriter.println("<div>Exception error.</div>\n");
                printWriter.println("<div>Please check application.properties(errorUrl parameter)</div>\n");
                printWriter.println("<div>and message.properties(controlBeanErrorMessage parameter)</div>\n");
                printWriter.println("</body>\n");
                printWriter.println("</html>\n");
            } catch (IOException ioe) {
                logUtil.fatal("error.but can't write error page.", ioe);
            }
        } catch (Exception e) {
            logUtil.fatal("error.but can't move error page.", e);
        }
    }
}
