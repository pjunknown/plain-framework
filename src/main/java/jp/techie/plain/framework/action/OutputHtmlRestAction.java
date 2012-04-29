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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

/**
 * HTMLテンプレートからHTML生成
 * 
 * @author bose999
 *
 */
@Local
@Path("/rest/template")
public interface OutputHtmlRestAction {
    /**
     * HTMLテンプレートからHTMLを生成して返す
     * 
     * @param request HttpServletRequest
     * @param response HttpServletResponse
     * @param templateName テンプレート名
     * @return String htmlコード
     */
    @GET // GETメソッドに対応
    @Path("/{templateName}") // /コンテキスト名/sample/xxxx という形式のURLでxxxをパラメータとして定義
    @Produces("text/html") // xmlを返すように定義 jsonもここを変えるだけ
    public String getHtml(@Context HttpServletRequest request,@Context HttpServletResponse response,@PathParam("templateName") String templateName);
}
