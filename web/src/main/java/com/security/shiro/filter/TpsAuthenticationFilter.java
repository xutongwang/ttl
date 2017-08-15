package com.security.shiro.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.web.filter.authc.AuthenticationFilter;
import org.apache.shiro.web.util.WebUtils;

public class TpsAuthenticationFilter extends AuthenticationFilter {

	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response)
			throws Exception {
		StringBuffer url = new StringBuffer("http://localhost:8082/web");
		WebUtils.toHttp(response).sendRedirect(url.append("/login").toString());
        return false;
	}

}
