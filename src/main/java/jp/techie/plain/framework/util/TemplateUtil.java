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

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;

import jp.techie.plain.framework.Const;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.templatemode.StandardTemplateModeHandlers;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

@Stateless
public class TemplateUtil {
    /**
     * ログユーティリティ
     */
    private static LogUtil logUtil = new LogUtil(TemplateUtil.class);
    /**
     * ServletContextTemplateResolver
     */
    private static ServletContextTemplateResolver servletContextTemplateResolver = new ServletContextTemplateResolver();
    
    /**
     * TemplateEngine
     */
    private static TemplateEngine engine = new TemplateEngine();
    
    /**
     * HTMLのエンコード
     */
    private static String htmlEncode = ApplicationSettingUtil.getMessage(Const.HTML_ENCODE_KEY);
    
    public String getHtmlEncode(){
        return htmlEncode;
    }
    
    public String buildHtml(HttpServletRequest request,String templateName){
        WebContext ctx = new WebContext(request, request.getServletContext(), request.getLocale());
        logUtil.debug("execute:templateName:" + templateName);
        return engine.process(templateName, ctx);
    }
    
    @PostConstruct
    public void postConstruct(){
        logUtil.debug("init:start template engine initialized.");
        servletContextTemplateResolver.setTemplateMode(StandardTemplateModeHandlers.XHTML.getTemplateModeName());
        servletContextTemplateResolver.setPrefix("/WEB-INF/template/");
        servletContextTemplateResolver.setCacheable(true);
        servletContextTemplateResolver.setCacheTTLMs(60000L);
        servletContextTemplateResolver.setCharacterEncoding(htmlEncode);
        engine.setTemplateResolver(servletContextTemplateResolver);
        logUtil.debug("init:end template engine initialized.");
    }
}
