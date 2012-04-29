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
package jp.techie.plain.framework.action;

import javax.ejb.Local;

import jp.techie.plain.framework.servlet.WebApplicationValue;

/**
 * Actionクラスインターフェイス<br />
 * このクラスを実装してアクションクラスを作成する
 * 
 * @author bose999
 *
 */
@Local
public interface Action {
    /**
     * Actionクラスを実行する<br />
     * アクションクラス以降でWebApplicationValueBean値を処理して<br />
     * 返り値のURLで表示処理を行う
     * 
     * @param webApplicationValueBean
     * @return String dispatchUrl
     */
    public String execute(WebApplicationValue webApplicationValue);
}
