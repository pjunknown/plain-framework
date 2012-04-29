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
package jp.techie.plain.framework.exception;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * ActionHandlingExceptionTest
 * 
 * @author bose999
 *
 */
public class ActionHandlingExceptionTest {
    /**
     * testConstractor<br />
     * コンストラクタに渡した文字列が返ってくるか？
     */
    @Test
    public void testConstractor(){
        String TEST_REASON ="testReaseon";
        ActionHandlingException  actionHandlingException = new ActionHandlingException(TEST_REASON);
        assertEquals(actionHandlingException.getMessage(), TEST_REASON);
    }
}
