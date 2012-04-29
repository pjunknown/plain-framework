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

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.concurrent.Future;

import javax.ejb.AsyncResult;
import javax.servlet.AsyncContext;
import javax.servlet.AsyncListener;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import jp.techie.plain.framework.servlet.WebApplicationValue;

import org.junit.Test;

/**
 * ActionControlExecuterTest
 * 
 * @author bose999
 *
 */
public class ActionControlExecuterTest {

    /**
     * executeテスト01<br />
     * dispatchUrlに文字列が返ってきた場合にExceptionにならない
     */
    @Test
    public void testExecute01() {

        ActionControlExecuter actionControlExecuter = new ActionControlExecuterMock();
        actionControlExecuter.actuionControler = new ActuionControlerMock();
        AsyncContext asyncContext = new AsyncContextMock();
        WebApplicationValue webApplicationValue = new WebApplicationValue();
        // Mockでnullではない場合に文字列を返す判定に使用
        webApplicationValue.urlParamList = new ArrayList<String>();
        try {
            actionControlExecuter.execute(asyncContext, webApplicationValue);
        } catch (Exception e) {
            // テスト失敗
            fail();
        }
    }

    /**
     * executeテスト02<br />
     * dispatchUrlにnullが返ってきた場合にExceptionにならない
     */
    @Test
    public void testExecute02() {

        ActionControlExecuter actionControlExecuter = new ActionControlExecuterMock();
        actionControlExecuter.actuionControler = new ActuionControlerMock();
        AsyncContext asyncContext = new AsyncContextMock();
        WebApplicationValue webApplicationValue = new WebApplicationValue();
        // Mockでnullではない場合に文字列を返す判定に使用
        webApplicationValue.urlParamList = null;
        try {
            actionControlExecuter.execute(asyncContext, webApplicationValue);
        } catch (Exception e) {
            // テスト失敗
            fail();
        }
    }

    private final static class ActionControlExecuterMock extends ActionControlExecuter {
        protected String executeAsync(AsyncContext asyncContext, WebApplicationValue webApplicationValue,
                long actionTimeout) {
            if (webApplicationValue.urlParamList == null) {
                return null;
            } else {
                return "testUrl";
            }
        }
    }

    private final static class ActuionControlerMock extends ActuionControler {

        public Future<String> execute(WebApplicationValue webApplicationValue) {
            return new AsyncResult<String>("testUrl");

        }

    }

    private static class AsyncContextMock implements AsyncContext {

        @Override
        public void addListener(AsyncListener arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void addListener(AsyncListener arg0, ServletRequest arg1, ServletResponse arg2) {
            // TODO Auto-generated method stub

        }

        @Override
        public void complete() {
            // TODO Auto-generated method stub

        }

        @Override
        public <T extends AsyncListener> T createListener(Class<T> arg0) throws ServletException {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public void dispatch() {
            // TODO Auto-generated method stub

        }

        @Override
        public void dispatch(String arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void dispatch(ServletContext arg0, String arg1) {
            // TODO Auto-generated method stub

        }

        @Override
        public ServletRequest getRequest() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public ServletResponse getResponse() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getTimeout() {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public boolean hasOriginalRequestAndResponse() {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public void setTimeout(long arg0) {
            // TODO Auto-generated method stub

        }

        @Override
        public void start(Runnable arg0) {
            // TODO Auto-generated method stub

        }

    }

}
