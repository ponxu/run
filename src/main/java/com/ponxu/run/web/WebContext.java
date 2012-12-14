package com.ponxu.run.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ponxu.run.web.info.Mapping;

/**
 * handler上下文
 * 
 * @author xwz
 */
public class WebContext {
	// Servlet
	private HttpServletRequest request;
	private HttpServletResponse response;

	/** 此次请求 */
	private String uri;

	/** 处理请求的handler 映射信息 */
	private Mapping mapping;

	/** 处理请求的handler, 禁止单例 */
	private RequestHandler handler;

	/** uri上的参数 */
	private Object[] restParams;

	/** 在处理过程中需要保存的值 */
	private Map<String, Object> root;

	public WebContext(HttpServletRequest request, HttpServletResponse response,
			String uri, Mapping mapping, RequestHandler handler,
			Object[] restParams) {
		this.request = request;
		this.response = response;
		this.uri = uri;
		this.mapping = mapping;
		this.handler = handler;
		this.restParams = restParams;

		this.handler.setContext(this);
		this.root = new HashMap<String, Object>();
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public HttpServletResponse getResponse() {
		return response;
	}

	public String getUri() {
		return uri;
	}

	public Mapping getMapping() {
		return mapping;
	}

	public RequestHandler getHandler() {
		return handler;
	}

	public Object[] getRestParams() {
		return restParams;
	}

	public Map<String, Object> getRoot() {
		return root;
	}

	public Object getRestParam(int index) {
		if (restParams != null && index < restParams.length)
			return restParams[index];

		return null;
	}

	public long howLong() {
		return System.currentTimeMillis()
				- (Long) request.getAttribute(WebApplication.ATTR_TIME_BEGIN);
	}
}
