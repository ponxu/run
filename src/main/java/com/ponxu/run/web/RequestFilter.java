package com.ponxu.run.web;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ponxu.run.log.Log;
import com.ponxu.run.log.LogFactory;

/**
 * RequestHandler控制过滤器
 * 
 * @author xwz
 */
public class RequestFilter implements Filter {
	private static final Log LOG = LogFactory.getLog();

	/** 静态文件后缀 */
	public static final List<String> STATIC_SUFFIX = Arrays
			.asList(new String[] { "js", "css", "gif", "png", "jpg" });

	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		// 设置编码
		request.setCharacterEncoding(WebApplication.getInstnce().getEncoding());
		response.setCharacterEncoding(WebApplication.getInstnce().getEncoding());

		// 记录开始时间
		long start = System.currentTimeMillis();
		request.setAttribute(WebApplication.ATTR_TIME_BEGIN, start);

		String uri = request.getRequestURI();
		LOG.debug(">=====================================uri: %s", uri);

		boolean processedByHandler = false;
		if (!isStaticFile(uri)) {// 非静态文件
			// 使用Handler处理
			processedByHandler = WebApplication.getInstnce().processRequest(
					request, response);
		}

		// 没有被Handler处理
		if (!processedByHandler) {
			chain.doFilter(request, response);
		}

		LOG.debug("<=====================================time: %dms",
				System.currentTimeMillis() - start);
	}

	public void init(FilterConfig fConfig) throws ServletException {
		WebApplication.getInstnce().init(fConfig);
	}

	public void destroy() {
		WebApplication.getInstnce().destroy();
	}

	/** 判断是否静态文件 */
	public boolean isStaticFile(String uri) {
		// int i = uri.lastIndexOf(".");
		// if (i > -1) {
		// String requestSufx = uri.substring(i + 1);
		// for (String sufx : STATIC_SUFFIX) {
		// if (StringUtils.equalsIgnoreCase(requestSufx, sufx))
		// return true;
		// }
		// }
		return false;
	}
}
