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
package jp.techie.plain.framework.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.ejb.EJB;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jp.techie.plain.framework.util.LogLevel;
import jp.techie.plain.framework.util.LogOutputUtil;
import jp.techie.plain.framework.util.LogUtil;
import jp.techie.plain.framework.util.TemplateUtil;

/**
 * thymeleafを利用したHTMLテンプレートからの出力Servlet
 * 
 * @author bose999
 *
 */
@SuppressWarnings("serial")
public class TemplateBuildServlet extends HttpServlet {
    
    /**
     * ログユーティリティ
     */
    private static LogUtil logUtil = new LogUtil(TemplateBuildServlet.class);
    
    /**
     * テンプレートユーティリティ
     */
    @EJB
    private TemplateUtil templateUtil;
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest
     * , javax.servlet.http.HttpServletResponse)
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response) {
        execute(request, response);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest
     * , javax.servlet.http.HttpServletResponse)
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) {
        execute(request, response);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.servlet.http.HttpServlet#doPut(javax.servlet.http.HttpServletRequest
     * , javax.servlet.http.HttpServletResponse)
     */
    @Override
    public void doPut(HttpServletRequest request, HttpServletResponse response) {
        execute(request, response);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * javax.servlet.http.HttpServlet#doDelete(javax.servlet.http.HttpServletRequest
     * , javax.servlet.http.HttpServletResponse)
     */
    @Override
    public void doDelete(HttpServletRequest request, HttpServletResponse response) {
        execute(request, response);
    }

    /**
     * GET/POST/PUT/DELETEメソッドから呼び出される共通処理
     * 
     * @param request
     *            HttpServletRequest
     * @param response
     *            HttpServletResponse
     * @throws Exception
     */
    protected void execute(HttpServletRequest request, HttpServletResponse response) {
        long startTime = LogOutputUtil.outputStartTime(logUtil, LogLevel.DEBUG, "Start TemplateBuildServlet Method");
        // 出力するレスポンスの文字コードを設定 テンプレートの文字コードと合わせる
        response.setCharacterEncoding(templateUtil.getHtmlEncode());
        String result = templateUtil.buildHtml(request, getTemplateName(request));
        PrintWriter out = null;
        try {
            logUtil.debug("execute:start html print");
            out = response.getWriter();
            out.println(result);
        } catch (IOException e) {
           logUtil.fatal("execute:html OutPut Exception",e);
        } finally {
            out.close();
        }
        LogOutputUtil.outputEndTime(logUtil, LogLevel.DEBUG, startTime, "End TemplateBuildServlet Method");
    }
    
    /**
     * リクエストからテンプレート名を生成する
     * 
     * @param request HttpServletRequest
     * @return String テンプレート名
     */
    protected String getTemplateName(HttpServletRequest request){   
        String contextPath = request.getContextPath();
        if (contextPath == null) {
            contextPath = "";
        }
        String templatePath = contextPath + "/tamplate/";
        String requestURI = request.getRequestURI();
        String templateName = requestURI.substring(templatePath.length(), requestURI.length());
        return templateName;
    }
}
