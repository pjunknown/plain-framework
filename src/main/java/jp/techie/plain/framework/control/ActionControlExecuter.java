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
package jp.techie.plain.framework.control;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.MissingResourceException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.AsyncContext;

import jp.techie.plain.framework.Const;
import jp.techie.plain.framework.servlet.WebApplicationValue;
import jp.techie.plain.framework.util.ApplicationSettingUtil;
import jp.techie.plain.framework.util.LogLevel;
import jp.techie.plain.framework.util.LogOutputUtil;
import jp.techie.plain.framework.util.LogUtil;
import jp.techie.plain.framework.util.MessageUtil;

/**
 * Servletから非同期で切り離されて実行されるAction実行クラス
 * 
 * @author bose999
 *
 */
@Stateless
public class ActionControlExecuter {

    /**
     * ログユーティリティ
     */
    private static LogUtil logUtil = new LogUtil(ActionControlExecuter.class);

    /**
    * Actionを実行するクラス
    */
    @EJB
    public ActuionControler actuionControler;

    /**
     * サーブレットから非同期で呼び出されてActionを実行監視する
     * 
     * @param asyncContext AsyncContext
     * @param webApplicationValueBean WebApplicationValueBean
     */
    @Asynchronous
    public void execute(AsyncContext asyncContext, WebApplicationValue webApplicationValue) {
        // 処理開始ログ出力
        long startTime = LogOutputUtil.outputStartTime(logUtil, LogLevel.DEBUG, "Start ActionControlExecuter");
        // 非同期タイムアウト
        long actionTimeout = 0;
        try {
            // 非同期タイムアウトを設定から取得
            actionTimeout = Long.valueOf(ApplicationSettingUtil.getMessage(Const.ACTION_TIMEOUT_KEY));
        } catch (NumberFormatException e) {
            // 設定が不正なのでエラーページに遷移して終了
            logUtil.fatal("execute:actionTimeout format error.", e);
            dispatchErrorPage(asyncContext, webApplicationValue, Const.ACTION_TIMEOUT_FORMAT_ERROR_MESSAGE_KEY);
            return;
        } catch (MissingResourceException mre) {
            logUtil.fatal("servlet timeout setting error.", mre);
            dispatchErrorPage(asyncContext, webApplicationValue, Const.ACTION_TIMEOUT_VALUE_ERROR_MESSAGE_KEY);
            return;
        }
        // 非同期処理を実行
        String dispatchUrl = executeAsync(asyncContext, webApplicationValue, actionTimeout);

        if (dispatchUrl != null) {
            // 処理結果で遷移 nullの時はエラーページ線画済み
            asyncContext.dispatch(dispatchUrl);
        }

        // 処理終了ログ出力
        LogOutputUtil.outputEndTime(logUtil, LogLevel.DEBUG, startTime, "End ActionControlExecuter");
    }

    /**
     * 非同期処理実行
     * 
     * @param asyncContext AsyncContext
     * @param webApplicationValue WebApplicationValue
     * @param actionTimeout アクションタイムアウト時間
     * @return String 遷移先URL
     */
    protected String executeAsync(AsyncContext asyncContext, WebApplicationValue webApplicationValue,
            long actionTimeout) {
        String dispatchUrl = "/";
        Future<String> serviceRetrun = null;
        try {
            // 非同期実行
            serviceRetrun = actuionControler.execute(webApplicationValue);
            dispatchUrl = serviceRetrun.get(actionTimeout, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            // 非同期割り込み例外
            logUtil.fatal("execute:InterruptedException", e);
            dispatchUrl =
                    dispatchErrorPage(asyncContext, webApplicationValue, Const.ACTION_EXECUTE_ERROR_MESSAGE_KEY);
        } catch (ExecutionException e) {
            // 非同期処理例外
            if (e.getMessage().indexOf("jp.techie.plain.framework.exception.NoSuchActionException") != -1) {
                // ActionクラスがURLから判定できない
                logUtil.fatal("execute:NoSuchActionException", e);
                dispatchUrl =
                        dispatchErrorPage(asyncContext, webApplicationValue,
                            Const.ACTION_NOT_FOUND_ERROR_MESSAGE_KEY);
            } else {
                logUtil.fatal("execute:ExecutionException", e);
                dispatchUrl =
                        dispatchErrorPage(asyncContext, webApplicationValue,
                            Const.ACTION_EXECUTE_ERROR_MESSAGE_KEY);
            }
        } catch (TimeoutException e) {
            // タイムアウトなので処理をキャンセル
            // タイミングによってスレッドを止められないケースがあるがコール
            serviceRetrun.cancel(true);
            logUtil.fatal("execute:TimeoutException", e);
            dispatchUrl =
                    dispatchErrorPage(asyncContext, webApplicationValue,
                        Const.ACTION_EXECUTE_TIMEOUT_ERROR_MESSAGE_KEY);
        }
        return dispatchUrl;
    }

    /**
     * エラー時エラーページ遷移
     * 
     * @param asyncContext AsyncContext
     * @param webApplicationValueBean WebApplicationValueBean
     * @param errorMessageKey エラーメッセージKey文字列
     */
    protected String dispatchErrorPage(AsyncContext asyncContext, WebApplicationValue webApplicationValue,
            String errorMessageKey) {
        String errorUrl = null;
        try {
            // リクエストのエラーメッセージ格納領域にメッセージを格納してerrorUrlへ遷移する
            errorUrl = ApplicationSettingUtil.getMessage(Const.ERROR_URL_KEY);
            String errorMessages = MessageUtil.getMessage(errorMessageKey);
            webApplicationValue.putRequestAttribute(Const.ERROR_MESSAGES_REQUEST_KEY, errorMessages);
        } catch (MissingResourceException mre) {
            logUtil.fatal("errorUrl is empty.display empty.", mre);
            try {
                // errorUrlが取得出来なかったのでHTMLを書きだして明示
                PrintWriter printWriter = asyncContext.getResponse().getWriter();
                printWriter.println("<HTML>");
                printWriter.println("<TITLE>erorr</TITLE>");
                printWriter.println("<BODY>Exception error.<br />");
                printWriter.println("Please check application.properties(errorUrl parameter)<br />");
                printWriter.println("and message.properties(controlBeanErrorMessage parameter)");
                printWriter.println("</BODY>");
                printWriter.println("</HTML>");
            } catch (IOException e1) {
            }
            // dispatch出来ないので処理を終える
            asyncContext.complete();
        }
        return errorUrl;
    }
}
