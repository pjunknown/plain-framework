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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * ログ出力ユーティリティ
 * 
 * @author bose999
 *
 */
public class LogUtil {

	/**
	 * ログクラス
	 */
	private Log log = null;
	
	/**
	 * ログ出力元クラス
	 */
	private Class<?> clz;

	/**
	 * コンストラクタ
	 * 
	 * @param clz Class
	 */
	public LogUtil(Class<?> clz) {
		this.clz = clz;
		log = LogFactory.getLog(clz);
	}
	
    /**
     * FATAL出力判定メソッド
     * 
     * @return boolean
     */
    public boolean isFatalEnabled(){		
		return log.isFatalEnabled();
	}
    
    /**
     * ERROR出力判定メソッド
     * 
     * @return boolean
     */
    public boolean isErrorEnabled(){		
		return log.isErrorEnabled();
	}
    
    /**
     * WARN出力判定メソッド
     * 
     * @return boolean
     */
    public boolean isWarnEnabled(){		
		return log.isWarnEnabled();
	}
    
    /**
     * INFO出力判定メソッド
     * 
     * @return boolean
     */
    public boolean isInfoEnabled(){		
		return log.isInfoEnabled();
	}
    
    /**
     * DEBUG出力判定メソッド
     * 
     * @return boolean
     */
    public boolean isDebugEnabled(){		
		return log.isDebugEnabled();
	}
    
    /**
     * TRACE出力判定メソッド
     * 
     * @return boolean
     */
    public boolean isTraceEnabled(){		
		return log.isTraceEnabled();
	}
    
    /**
     * FATAL出力メソッド
     */
    public void fatal(String message,Exception e){
    	if(log.isFatalEnabled()){
			log.fatal(makeLogMessage(message),e);
		}
    }

    /**
     * FATAL出力メソッド
     */
	public void fatal(String message){
		if(log.isFatalEnabled()){
			log.fatal(makeLogMessage(message));
		}
	}
	
	/**
     * ERROR出力メソッド
     */
	public void error(String message){
		if(log.isErrorEnabled()){
			log.error(makeLogMessage(message));
		}
	}
	
	/**
     * WARN出力メソッド
     */
	public void warn(String message){
		if(log.isWarnEnabled()){
			log.warn(makeLogMessage(message));
		}
	}
	
	/**
     * INFO出力メソッド
     */
	public void info(String message){
		if(log.isInfoEnabled()){
			log.info(makeLogMessage(message));
		}
	}
	
	/**
     * DEBUG出力メソッド
     */
	public void debug(String message){
		if(log.isDebugEnabled()){
			log.debug(makeLogMessage(message));
		}
	}
	
	/**
     * TRACE出力メソッド
     */
	public void trace(String message){
		if(log.isTraceEnabled()){
			log.trace(makeLogMessage(message));
		}
	}
	
	/**
	 * 行数を付加してログメッセージ作成
	 * 
	 * @param message 行数なしログメッセージ
	 * @return 行数ありログメッセージ
	 */
	protected String makeLogMessage(String message){
		int lineNumber = getLineNumber(clz.getName());
		StringBuffer logMessageStringBuffer = new StringBuffer("Line:");
		logMessageStringBuffer.append(lineNumber);
		logMessageStringBuffer.append(" ");
		logMessageStringBuffer.append(message);
		return logMessageStringBuffer.toString();
	}
	
	/**
	 * ログを出力する行数を返す
	 * 
	 * @param className クラス名
	 * @return ログ出力行数
	 */
	protected static int getLineNumber(String className) {
	    try {
	        throw new Exception();
	    } catch (Exception e) {
	        StackTraceElement[] stackTraceElements = e.getStackTrace();
	        for (int i = 0; i < stackTraceElements.length; i++) {
	            StackTraceElement element = stackTraceElements[i];
	            if (element != null
	                && className.equals(element.getClassName())) {
	                return element.getLineNumber();
	            }
	        }
	    }
	    return 0;
	}
}
