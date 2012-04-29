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

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.techie.plain.framework.util.LogUtil;
import jp.techie.plain.framework.util.TemplateUtil;

/**
 * HTMLテンプレートからHTML生成実装
 * 
 * @author bose999
 *
 */
@Stateless
public class OutputHtmlRestActionImpl implements OutputHtmlRestAction {
    /**
     * ログユーティリティ
     */
    private static LogUtil logUtil = new LogUtil(OutputHtmlRestActionImpl.class);

    /**
     * テンプレートユーティリティ
     */
    @EJB
    public TemplateUtil templateUtil;

    /* (non-Javadoc)
     * @see jp.techie.plain.framework.action.OutputHtmlRestAction#getSample(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.String)
     */
    @Override
    public String getHtml(HttpServletRequest request, HttpServletResponse response, String templateName) {
        // 出力するレスポンスの文字コードを設定 テンプレートの文字コードと合わせる
        response.setCharacterEncoding(templateUtil.getHtmlEncode());

        // htmlを生成する
        logUtil.debug("execute:templateName:" + templateName);
        String result = templateUtil.buildHtml(request, templateName);

        // RestEasyにhtmlを返してもらう
        return result;
    }
}
