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

import java.util.concurrent.Future;

import javax.ejb.AsyncResult;
import javax.ejb.Asynchronous;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import jp.techie.plain.framework.action.Action;
import jp.techie.plain.framework.exception.NoSuchActionException;
import jp.techie.plain.framework.servlet.WebApplicationValue;
import jp.techie.plain.framework.util.LogLevel;
import jp.techie.plain.framework.util.LogOutputUtil;
import jp.techie.plain.framework.util.LogUtil;

/**
 * Action制御及びトランザクション制御クラス
 * 
 * @author bose999
 *
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class ActuionControler {

    /**
     * ログユーティリティ
     */
    private static LogUtil logUtil = new LogUtil(ActuionControler.class);

    /**
     * Action実行&CMTトランザクション制御
     * 
     * @return Future<String> dispatchUrl
     * @throws NoSuchActionException
     */
    @Asynchronous
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public Future<String> execute(WebApplicationValue webApplicationValue) {
        // 非同期メソッドなのでここでトランザクションがREQUREIRES NEWされる
        // 実際に実装する時はここからトランザクションが始まると考えて実装する
        // 処理開始ログ出力
        long startTime = LogOutputUtil.outputStartTime(logUtil, LogLevel.DEBUG, "Start ActionControler");

        // 処理後のURL初期値としてContextRoot
        String dispatchUrl = "/";

        try {
            //JNDI名でActionクラスを取得し処理を行う
            Context context = new InitialContext();
            Action action = (Action) context.lookup(webApplicationValue.actionClassJndiName);
            dispatchUrl = action.execute(webApplicationValue);
        } catch (NamingException e) {
            logUtil.debug("execute:Bad URL. action missing.");
            throw new NoSuchActionException("action messing");
        }
        LogOutputUtil.outputEndTime(logUtil, LogLevel.DEBUG, startTime, "End ActionControler");
        return new AsyncResult<String>(dispatchUrl);
    }
}
