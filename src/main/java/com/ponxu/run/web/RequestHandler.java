package com.ponxu.run.web;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ponxu.run.lang.StringUtils;
import com.ponxu.run.web.render.AbstractRender;
import com.ponxu.run.web.render.FreeMarkerRender;
import com.ponxu.run.web.render.JSPRender;
import com.ponxu.run.web.render.RedirectRender;
import com.ponxu.run.web.render.StringRender;

/**
 * 请求处理 基类
 * 
 * @author xwz
 */
public abstract class RequestHandler {
	protected WebContext context;

	// ======================================================
	/** 返回true: 可继续处理, false: 不可继续处理 */
	public boolean beforeHandle() {
		return true;
	}

	public void afterHandle() {
	}

	// -------------
	public void beforeRender(AbstractRender render) {
	}

	public void afterRender(Object renderResult) {
	}

	// ======================================================
	public void setContext(WebContext context) {
		this.context = context;
	}

	protected HttpServletRequest request() {
		return context.getRequest();
	}

	protected HttpServletResponse response() {
		return context.getResponse();
	}

	// ======================================================
	protected String getParam(String name, String def) {
		String param = request().getParameter(name);
		return StringUtils.isNotEmpty(param) ? param : def;
	}

	protected String getParam(String name) {
		return getParam(name, "");
	}

	protected int getIntParam(String name, int def) {
		String param = getParam(name);
		return StringUtils.isNotEmpty(param) ? Integer.parseInt(param) : def;
	}

	protected int getIntParam(String name) {
		return getIntParam(name, 0);
	}

	protected String[] getParams(String name) {
		return request().getParameterValues(name);
	}

	protected int[] getIntParams(String name) {
		String[] values = getParams(name);

		if (values == null)
			return new int[0];

		int[] is = new int[values.length];
		for (int i = 0; i < values.length; i++)
			is[i] = Integer.parseInt(values[i]);

		return is;
	}

	// ===== 值 =================================================
	/** 添加请求范围内数据(相当于: request.setAttribute) */
	protected void putVal(String key, Object value) {
		context.getRoot().put(key, value);
	}

	/** 获取请求范围内数据(相当于: request.getAttribute) */
	protected Object getVal(String key) {
		return context.getRoot().get(key);
	}

	/** 移除请求范围内数据(相当于: request.removeAttribute) */
	protected void removeVal(String key) {
		context.getRoot().remove(key);
	}

	// ====== cookie ================================================
	protected String cookiePath() {
		return "/";
	}

	/** 获取cookie */
	protected String getCookie(String name) {
		Cookie[] cookies = request().getCookies();
		if (cookies == null)
			return null;

		for (Cookie ck : cookies) {
			if (StringUtils.equalsIgnoreCase(name, ck.getName()))
				return ck.getValue();
		}

		return null;
	}

	/** 设置cookie */
	protected void setCookie(String name, String value) {
		setCookie(name, value, 365 * 24 * 60 * 60, true);
	}

	/** 删除cookie */
	protected void removeCookie(String name) {
		setCookie(name, "", 0, true);
	}

	/** 设置cookie */
	public void setCookie(String name, String value, int maxAge,
			boolean allSubDomain) {
		Cookie cookie = new Cookie(name, value);
		cookie.setMaxAge(maxAge);

		// SAE路径设置有问题
		/*
		 * cookie.setPath(cookiePath()); if (allSubDomain) { String serverName =
		 * request().getServerName(); char c =
		 * serverName.charAt(serverName.length() - 1); // 字母结尾 if ((c >= 'a' &&
		 * c <= 'z') || (c >= 'A' && c <= 'Z')) { String[] parts =
		 * serverName.split("\\."); if (parts.length >= 2) { String domain =
		 * parts[parts.length - 2] + "." + parts[parts.length - 1];
		 * cookie.setDomain('.' + domain); } } }
		 */

		response().addCookie(cookie);
	}

	// ======================================================
	/** 获取ContextPath, 可复写 */
	protected String getContextPath() {
		return context.getRequest().getContextPath();
	}

	// ===== render =================================================
	/** 重定向(调用时不需要加ContextPath) */
	protected void redirect(String path) {
		if (!path.startsWith(getContextPath())) {
			path = getContextPath() + path;
		}
		render(RedirectRender.getInstance(), path);
	}

	protected void renderJSP(String jsp) {
		render(JSPRender.getInstance(), jsp);
	}

	protected void renderTemplate(String name) {
		render(FreeMarkerRender.getInstance(), name);
	}

	protected void renderString(String str) {
		render(StringRender.getInstance(), str);
	}

	protected void render(AbstractRender er, Object what) {
		beforeRender(er);
		Object result = er.render(context, what);
		afterRender(result);
	}

}