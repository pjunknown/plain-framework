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
package jp.techie.plain.framework.filter;

import java.io.IOException;
import java.util.MissingResourceException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import jp.techie.plain.framework.Const;
import jp.techie.plain.framework.util.ApplicationSettingUtil;
import jp.techie.plain.framework.util.LogUtil;

public class EncodingFilter implements Filter {

    /**
     * ログユーティリティ
     */
    private static LogUtil logUtil = new LogUtil(EncodingFilter.class);

    /**
     * htmlエンコーディング
     */
    private String htmlEncoding = "utf-8";

    /* (non-Javadoc)
     * @see javax.servlet.Filter#destroy()
     */
    @Override
    public void destroy() {
    }

    /* (non-Javadoc)
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        request.setCharacterEncoding(htmlEncoding);
        response.setCharacterEncoding(htmlEncoding);
        filterChain.doFilter(request, response);
    }

    /* (non-Javadoc)
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        try {
            htmlEncoding = ApplicationSettingUtil.getMessage(Const.HTML_ENCODE_KEY);
            logUtil.debug("init: " + Const.HTML_ENCODE_KEY + ": " + htmlEncoding);
        } catch (MissingResourceException e) {
            logUtil.fatal("init: " + Const.HTML_ENCODE_KEY + " is missing. setting utf-8.", e);
        }
    }
}
