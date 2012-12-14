package com.ponxu.run.web.render;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.ponxu.run.lang.ExceptionUtils;
import com.ponxu.run.web.WebApplication;
import com.ponxu.run.web.WebContext;

/**
 * 输出字符串
 * 
 * @author xwz
 */
public class StringRender extends AbstractRender {
	@Override
	public Object render(WebContext context, Object what) {
		try {
			HttpServletResponse response = context.getResponse();
			response.setContentType("text/html; charset="
					+ WebApplication.getInstnce().getEncoding());
			context.getResponse().getWriter().write(String.valueOf(what));
		} catch (IOException e) {
			ExceptionUtils.makeRuntime(e);
		}
		return null;
	}

	// ====== 单例 ====================================================
	private static StringRender instance;

	public static StringRender getInstance() {
		if (instance == null) {
			synchronized (StringRender.class) {
				if (instance == null) {
					instance = new StringRender();
				}
			}
		}
		return instance;
	}
}
