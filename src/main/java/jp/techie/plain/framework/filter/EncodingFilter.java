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
        if (request.getCharacterEncoding() == null) {
            request.setCharacterEncoding(htmlEncoding);
        }
        if (response.getCharacterEncoding() == null) {
            response.setCharacterEncoding(htmlEncoding);
        }
        filterChain.doFilter(request, response);
    }

    /* (non-Javadoc)
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        try {
            htmlEncoding = ApplicationSettingUtil.getMessage(Const.HTML_ENCODE_KEY);
        } catch (MissingResourceException e) {
            logUtil.fatal("init: " + Const.HTML_ENCODE_KEY + " is missing. setting utf-8.", e);
        }
    }
}
