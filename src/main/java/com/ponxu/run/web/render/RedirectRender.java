package com.ponxu.run.web.render;

import java.io.IOException;

import com.ponxu.run.lang.ExceptionUtils;
import com.ponxu.run.web.WebContext;

/**
 * 重定向渲染
 * 
 * @author xwz
 */
public class RedirectRender extends AbstractRender {
	@Override
	public Object render(WebContext context, Object what) {
		try {
			context.getResponse().sendRedirect(what.toString());
		} catch (IOException e) {
			ExceptionUtils.makeRuntime(e);
		}
		return null;
	}

	// ====== 单例 ====================================================
	private static RedirectRender instance;

	public static RedirectRender getInstance() {
		if (instance == null) {
			synchronized (RedirectRender.class) {
				if (instance == null) {
					instance = new RedirectRender();
				}
			}
		}
		return instance;
	}
}
