package com.ponxu.run.web.render;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ponxu.run.lang.ExceptionUtils;
import com.ponxu.run.web.WebContext;

/**
 * 渲染JSP
 * 
 * @author xwz
 */
public class JSPRender extends AbstractRender {
	@Override
	public Object render(WebContext context, Object what) {
		try {
			HttpServletRequest request = context.getRequest();
			HttpServletResponse response = context.getResponse();
			Map<String, Object> root = context.getRoot();

			for (String name : root.keySet())
				request.setAttribute(name, context.getRoot().get(name));

			request.getRequestDispatcher(String.valueOf(what)).forward(request,
					response);
		} catch (ServletException e) {
			ExceptionUtils.makeRuntime(e);
		} catch (IOException e) {
			ExceptionUtils.makeRuntime(e);
		}

		return null;
	}

	// ===== 单例 =======================================
	private static JSPRender instance;

	public static JSPRender getInstance() {
		if (instance == null) {
			synchronized (JSPRender.class) {
				if (instance == null) {
					instance = new JSPRender();
				}
			}
		}
		return instance;
	}
}
