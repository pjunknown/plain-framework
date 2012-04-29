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
package jp.techie.plain.framework;

/**
 * 定数クラス
 * 
 * @author bose999
 *
 */
public class Const {
    
    /*--- framework inner key name ---*/
    
    /**
     * Requestスコープに格納するクラアント
     */
    public static final String CLIENT_SIDE_SIRIARIZED_CAHCE_KEY = "clientSideSiriarizedCache";
    
    /**
     * Sessionスコープに格納する暗号Key キー文字列
     */
    public static final String SESSION_SECRET_KEY = "sessionSecretKey";
    
    /*--- application.properties framework inner key name ---*/

    /**
     * アクション実行時間タイムアウト キー文字列
     */
    public static final String ACTION_TIMEOUT_KEY = "actionTimeout";
    
    /**
     * アクション実行サーブレット実行時間タイムアウト キー文字列
     */
    public static final String ACTION_SERVLET_TIMEOUT_KEY = "actionServletTimeout";
    
    /**
     * エラー時遷移先 キー文字列
     */
    public static final String ERROR_URL_KEY = "errorUrl";
    
    /**
     * 画面表示用Requestスコープエラーメッセージ
     */
    public static final String ERROR_MESSAGES_REQUEST_KEY = "errorMessages";
    
    /**
     * アプリケーションHTMLエンコード
     */
    public static final String HTML_ENCODE_KEY = "htmlEncode";

    /*--- message.properties framework inner key name ---*/

    /**
     * アクションサーブレットタイムアウト時間不正 エラー画面表示メッセージ
     */
    public static final String ACTION_SERVLET_TIMEOUT_VALUE_ERROR_MESSAGE_KEY = "servletTimeoutValueErrorMessage";

    /**
     * アクションサーブレットタイムアウト時間数値フォーマット例外 エラー画面表示メッセージ
     */
    public static final String ACTION_SERVLET_TIMEOUT_FORMAT_ERROR_MESSAGE_KEY = "actionServletTimeoutFormatErrorMessage";

    /**
     * アクションタイムアウト時間不正 エラー画面表示メッセージ
     */
    public static final String ACTION_TIMEOUT_VALUE_ERROR_MESSAGE_KEY = "actionTimeoutValueErrorMessage";
    
    /**
     * アクションタイムアウト時間数値フォーマット例外 エラー画面表示メッセージ
     */
    public static final String ACTION_TIMEOUT_FORMAT_ERROR_MESSAGE_KEY = "actionTimeoutFormatErrorMessage";
    
    /**
     * アクション例外エラー時 エラー画面表示メッセージ
     */
    public static final String ACTION_EXECUTE_ERROR_MESSAGE_KEY = "actionExecuteErrorMessage";

    /**
     * アクションタイムアウト時 エラー画面表示メッセージ
     */
    public static final String ACTION_EXECUTE_TIMEOUT_ERROR_MESSAGE_KEY = "actionExecuteTimeoutErrorMessage";

    /**
     * URLからアクションが判別出来ない エラー画面表示メッセージ
     */
    public static final String ACTION_NOT_FOUND_ERROR_MESSAGE_KEY = "actionNotFoundErrorMessage";
}
