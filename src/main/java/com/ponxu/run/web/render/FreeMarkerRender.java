package com.ponxu.run.web.render;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import com.ponxu.run.lang.ExceptionUtils;
import com.ponxu.run.web.WebApplication;
import com.ponxu.run.web.WebContext;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * 渲染JSP
 * 
 * @author xwz
 */
public class FreeMarkerRender extends AbstractRender {
	// 参数: 模板目录
	public static final String PARAM_TEMPLATE = "template";
	// 默认值: 模板目录
	public static final String DEFAULT_TEMPLATE = "themes";

	// FreeMarker配置
	private Configuration cfg;
	private String encoding;

	@Override
	public void init() {
		encoding = WebApplication.getInstnce().getEncoding();
		String dir = WebApplication.getInstnce().getInitParameter(
				PARAM_TEMPLATE, DEFAULT_TEMPLATE);
		ServletContext sc = WebApplication.getInstnce().getServletContext();

		// 初始化配置
		cfg = new Configuration();
		cfg.setServletContextForTemplateLoading(sc, dir);
		cfg.setDefaultEncoding(encoding);
	}

	/**
	 * 返回模板生成的结果
	 */
	@Override
	public Object render(WebContext context, Object what) {
		try {
			checkAndInit();

			HttpServletResponse response = context.getResponse();
			Map<String, Object> root = context.getRoot();
			Template t = cfg.getTemplate(String.valueOf(what));

			StringWriter temp = new StringWriter();
			t.process(root, temp);

			response.setContentType("text/html; charset=" + encoding);
			response.getWriter().print(temp);

			return temp.toString();
		} catch (IOException e) {
			ExceptionUtils.makeRuntime(e);
		} catch (TemplateException e) {
			ExceptionUtils.makeRuntime(e);
		}

		return null;
	}

	public Configuration getCfg() {
		return cfg;
	}

	// ======= 单例 ============================================
	private static FreeMarkerRender instance;

	public static FreeMarkerRender getInstance() {
		if (instance == null) {
			synchronized (FreeMarkerRender.class) {
				if (instance == null) {
					instance = new FreeMarkerRender();
				}
			}
		}
		return instance;
	}
}
