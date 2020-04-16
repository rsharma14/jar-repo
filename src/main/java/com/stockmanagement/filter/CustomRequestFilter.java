package com.stockmanagement.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.stockmanagement.util.Utils;

@Component
@Order(1)
public class CustomRequestFilter implements Filter {

	private static Log logger = LogFactory.getLog(CustomRequestFilter.class);
	
	private List<String> restrictedURL=new ArrayList<>(Arrays.asList("swagger","swagger"));

	@Autowired
	private Environment environment;
	
	boolean showReqRespLog;
	
	String restrictLogUrls;
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		showReqRespLog=environment.getProperty( "showReqRespLog", Boolean.class, Boolean.FALSE );
		restrictLogUrls=environment.getProperty( "filteLogUrls", String.class, "" );
		restrictedURL.addAll(Arrays.asList(restrictLogUrls.split(",")).stream().map(s->s.trim()).collect(Collectors.toList()));
		restrictedURL.removeIf(f->(Utils.isNullOrEmpty(f)));
		
		logger.info("showReqRespLog= "+showReqRespLog);
		logger.info("restrictedURL= "+restrictedURL);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		if (showReqRespLog) {
			showLog(request, response, chain);
		} else {
			chain.doFilter(request, response);
		}

	}

	private void showLog(ServletRequest request, ServletResponse response, FilterChain chain) {

		RequestWrapper requestWrapper = null;
		ResponseWrapper responseWrapper = null;

		try {
			requestWrapper = new RequestWrapper((HttpServletRequest) request);
			responseWrapper = new ResponseWrapper((HttpServletResponse) response);

			logger.info(getRequestLog(requestWrapper, responseWrapper));
			chain.doFilter(requestWrapper, responseWrapper);
			logger.info(getResponseLog(requestWrapper, responseWrapper));

		} catch (Exception e) {
			logger.info("doFilter Exception:" + e.getCause());
		}

	}

	@Override
	public void destroy() {
	}

	private String getRequestLog(HttpServletRequest request, HttpServletResponse response) {
		return !restrictURL(request,response)?("\nRequestURL:" + request.getMethod() + " " + request.getRequestURL() + "\nRequestHeaders:"
				+ getRequestHeaders(request) + "\nRequestParams:" + getRequestParams(request) + "\nRequestBody:"
				+ getRequestBody(request)):"";
	}


	private Object getResponseLog(HttpServletRequest request, HttpServletResponse response) throws IOException {
		return !restrictURL(request, response)?("\nResponse URL:" + request.getMethod() + " " + request.getRequestURL() + "\nResponseHeaders:"
				+ getResponseHeaders(response) + "\nResponseBody:" + getResponseBody(request, response)):"";
	}
	private boolean restrictURL(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		return restrictedURL.stream().anyMatch(p->request.getRequestURL().toString().contains(p));
	}
	private Map<String, String> getRequestHeaders(HttpServletRequest request) {

		return Collections.list(request.getHeaderNames()).stream().map(m -> m.toString())
				.collect(Collectors.toMap(k -> k, v -> request.getHeader(v)));
	}

	private Map<String, String> getResponseHeaders(HttpServletResponse response) {

		return response.getHeaderNames().stream().map(m -> m.toString())
				.collect(Collectors.toMap(k -> k, v -> response.getHeader(v)));
	}

	private Map<Object, String[]> getRequestParams(HttpServletRequest request) {
		return Collections.list(request.getParameterNames()).stream()
				.collect(Collectors.toMap(parameterName -> parameterName, request::getParameterValues));
	}

	public static String getRequestBody(HttpServletRequest request) {
		try {
			return request.getReader().lines().map(l -> l.trim()).collect(Collectors.joining());
		} catch (IOException e) {
			logger.error("getRequestBody" + e.getMessage());
		}
		return null;
	}

	public static String getResponseBody(HttpServletRequest request, HttpServletResponse response) {
		try {
			return new String(((ResponseWrapper) response).getCopy(), response.getCharacterEncoding());
		} catch (Exception e) {
			logger.error("getResponseBody" + e.getMessage());
		}
		return null;
	}

}
