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
package jp.techie.plain.framework.util;

/**
 * ログ出力ユーティリティ
 * 
 * @author bose999
 *
 */
public class LogOutputUtil {
    
    /**
     * コンストラクタ
     */
    private LogOutputUtil(){
    }

    /**
     * 処理開始時ログ出力
     * 
     * @param logUtil LogUtil
     * @param logLevel LogLevel ログレベル
     * @param message String ログ出力内容
     * @return long 処理開始時
     */
    public static long outputStartTime(LogUtil logUtil, LogLevel logLevel, String message) {
        long startTime = 0;
        switch (logLevel) {
        case FATAL:
            if (logUtil.isFatalEnabled()) {
                startTime = System.currentTimeMillis();
                logUtil.fatal(message);
            }
            break;
        case ERROR:
            if (logUtil.isErrorEnabled()) {
                startTime = System.currentTimeMillis();
                logUtil.error(message);
            }
            break;
        case WARN:
            if (logUtil.isWarnEnabled()) {
                startTime = System.currentTimeMillis();
                logUtil.warn(message);
            }
            break;
        case INFO:
            if (logUtil.isInfoEnabled()) {
                startTime = System.currentTimeMillis();
                logUtil.info(message);
            }
            break;
        case DEBUG:
            if (logUtil.isDebugEnabled()) {
                startTime = System.currentTimeMillis();
                logUtil.debug(message);
            }
            break;
        case TRACE:
            if (logUtil.isTraceEnabled()) {
                startTime = System.currentTimeMillis();
                logUtil.trace(message);
            }
            break;
        }
        return startTime;
    }
    
    /**
     * 処理終了時時ログ出力
     * 
     * @param logUtil LogUtil
     * @param logLevel LogLevel ログレベル
     * @param message String ログ出力内容
     */
    public static void outputEndTime(LogUtil logUtil, LogLevel logLevel, long startTime, String message) {
        switch (logLevel) {
        case FATAL:
            if (logUtil.isFatalEnabled()) {
                long doTime = System.currentTimeMillis() - startTime;
                logUtil.fatal(message + ":" + doTime + "ms.");
            }
            break;
        case ERROR:
            if (logUtil.isErrorEnabled()) {
                long doTime = System.currentTimeMillis() - startTime;
                logUtil.error(message + ":" + doTime + "ms.");
            }
            break;
        case WARN:
            if (logUtil.isWarnEnabled()) {
                long doTime = System.currentTimeMillis() - startTime;
                logUtil.warn(message + ":" + doTime + "ms.");
            }
            break;
        case INFO:
            if (logUtil.isInfoEnabled()) {
                long doTime = System.currentTimeMillis() - startTime;
                logUtil.info(message + ":" + doTime + "ms.");
            }
            break;
        case DEBUG:
            if (logUtil.isDebugEnabled()) {
                long doTime = System.currentTimeMillis() - startTime;
                logUtil.debug(message + ":" + doTime + "ms.");
            }
            break;
        case TRACE:
            if (logUtil.isTraceEnabled()) {
                long doTime = System.currentTimeMillis() - startTime;
                logUtil.trace(message + ":" + doTime + "ms.");
            }
            break;
        }
    }
}
