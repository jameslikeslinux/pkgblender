package com.thestaticvoid.blender.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class RepoAddContentTypeFilter implements Filter {
	@Override
	public void init(FilterConfig config) throws ServletException {

	}
	
	@Override
	public void destroy() {

	}

	/*
	 * HACK:
	 * pkgsend(1) sends files to the repo in the request body but makes the
	 * content type application/x-www-form-urlencoded.  This filter reads
	 * from the input stream to prevent the request body from being processed
	 * as actual url-encoded data.
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		if (request instanceof HttpServletRequest) {
			HttpServletRequest httpRequest = (HttpServletRequest) request;
			if (httpRequest.getRequestURI().matches(".*/repo/.*/add/0/.*"))
				request.getInputStream().read(new byte[0]);
		}
		chain.doFilter(request, response);
	}
}
