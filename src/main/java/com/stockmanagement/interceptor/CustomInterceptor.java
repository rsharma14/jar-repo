package com.stockmanagement.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Component
public class CustomInterceptor extends HandlerInterceptorAdapter {

	private static Log logger = LogFactory.getLog(CustomInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request,

			HttpServletResponse response, Object handler) throws Exception {

		try {
			logger.info("Request URL: " + request.getRequestURL());
			// updateRequest(request);
			return true;

		} catch (SystemException e) {
			logger.info("request update failed");
			return false;

		}

	}

	private void updateRequest(HttpServletRequest request) {
		logger.info("Updating request object");
	}

	private class SystemException extends RuntimeException {
		private static final long serialVersionUID = 1L;
	}

}