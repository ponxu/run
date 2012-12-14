package com.ponxu.run.web;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ponxu.run.lang.BeanUtils;
import com.ponxu.run.lang.StringUtils;
import com.ponxu.run.log.Log;
import com.ponxu.run.log.LogFactory;
import com.ponxu.run.web.factory.AbstractHandlerFactory;
import com.ponxu.run.web.info.Mapping;
import com.ponxu.run.web.loader.AnnotationMappingLoader;
import com.ponxu.run.web.loader.MappingLoader;
import com.ponxu.run.web.loader.XMLMappingLoader;

/**
 * 整个web程序核心: 使用Handler处理请求
 * 
 * @author xwz
 */
@SuppressWarnings("unchecked")
public class WebApplication {
	private static final Log LOG = LogFactory.getLog();

	// 参数名字
	public static final String PARAM_HANDLER_MAPPING = "handlerMapping";
	public static final String PARAM_HANDLER_PACKAGE = "handlerPackage";
	public static final String PARAM_HANDLER_FACTORY = "handlerFactory";
	public static final String PARAM_ENCODING = "encoding";

	// 默认值
	public static final String SPLIT = ",";
	public static final String DEFAULT_ENCODING = "UTF-8";
	public static final String DEFAULT_HANDLER_MAPPING = "mapping.xml";
	public static final String DEFAULT_HANDLER_FACTORY = "com.ponxu.run.web.factory.RunRequestHandlerFactory";

	// 属性名字
	public static final String ATTR_TIME_BEGIN = "ATTR_TIME_BEGIN";

	/** 所有初始化参数 */
	private Map<String, String> initParams;
	/** servlet application */
	private ServletContext sc;
	/** Handler创建工厂 */
	private AbstractHandlerFactory factory;
	/** url映射信息 */
	private List<Mapping> mappings;

	// ====== 初始化 =============================================================
	/** 初始化 */
	public void init(FilterConfig fConfig) {
		sc = fConfig.getServletContext();
		// 所有参数
		initParams = new HashMap<String, String>();
		Enumeration<String> names = fConfig.getInitParameterNames();
		while (names.hasMoreElements()) {
			String name = names.nextElement();
			String value = fConfig.getInitParameter(name);
			initParams.put(name, value);
			LOG.debug("InitParameter: %s=%s", name, value);
		}

		initHandlerFactory();
		loadMappings();
	}

	// 初始化: 工厂
	private void initHandlerFactory() {
		String cls = getFactoryClassName();
		factory = (AbstractHandlerFactory) BeanUtils.newInstance(cls);
		LOG.debug("HandlerFactory: %s", cls);
	}

	// 初始化: 加载路径映射信息
	private void loadMappings() {
		String xml = getInitParameter(PARAM_HANDLER_MAPPING);
		String pkg = getInitParameter(PARAM_HANDLER_PACKAGE);

		mappings = new ArrayList<Mapping>();
		// 默认使用xml加载
		if (StringUtils.isEmpty(xml) && StringUtils.isEmpty(pkg)) {
			xml = DEFAULT_HANDLER_MAPPING;
		}

		if (StringUtils.isNotEmpty(xml)) {
			MappingLoader lodader = new XMLMappingLoader();
			for (String fileName : xml.split(SPLIT)) {
				lodader.load(fileName.trim(), mappings);
			}
		}
		if (StringUtils.isNotEmpty(pkg)) {
			MappingLoader lodader = new AnnotationMappingLoader();
			for (String rootPackageName : pkg.split(SPLIT)) {
				lodader.load(rootPackageName.trim(), mappings);
			}
		}
	}

	public void destroy() {
		// ???
	}

	// ====== 处理 =============================================================
	/**
	 * 使用Handler处理请求
	 * 
	 * @param request
	 * @param response
	 * @return true: 以处理, false: 未处理
	 */
	public boolean processRequest(HttpServletRequest request,
			HttpServletResponse response) {
		// 1 查找handler
		WebContext context = lookpup(request, response);

		// 2 beforeHandle
		if (context != null && context.getHandler().beforeHandle()) {
			// 3 调用 get,post...
			String method = context.getRequest().getMethod().toLowerCase();
			BeanUtils.invokeMehodByName(context.getHandler(), method,
					context.getRestParams());

			// 4 回调Handler的afterHandle
			context.getHandler().afterHandle();
		}

		return context != null;
	}

	// 查找处理器, 返回Handler上下文
	private WebContext lookpup(HttpServletRequest request,
			HttpServletResponse response) {
		String uri = getUri(request);
		WebContext context = null;
		for (Mapping info : mappings) {
			LOG.debug("lookup: %s", info);
			Matcher matcher = info.getUrlPattern().matcher(uri);
			if (!matcher.find())
				continue;

			// 1 handler生成
			RequestHandler handler = factory.getHandler(info.getName());
			if (handler == null) {
				LOG.warn("can't find handler %s", info.getName());
				break;
			}
			LOG.debug("%s, will handle by: %s", uri, handler);

			// 2 解析REST参数
			int restParamCount = matcher.groupCount();
			Object[] restParams = new Object[restParamCount];
			for (int i = 1; i <= restParamCount; i++) {
				restParams[i - 1] = decode(matcher.group(i));
			}

			// 3 构建处理上下文
			context = new WebContext(request, response, uri, info, handler,
					restParams);
			break;
		}
		return context;
	}

	// 去除ctxpath的uri
	private String getUri(HttpServletRequest request) {
		// TODO Struts2?????
		String contextPath = request.getContextPath();
		String uri = request.getRequestURI();
		if (StringUtils.isNotEmpty(contextPath) && uri.startsWith(contextPath)) {
			uri = uri.substring(contextPath.length());
		}
		return uri;
	}

	// 解码
	private String decode(String str) {
		try {
			return URLDecoder.decode(str, getEncoding());
		} catch (UnsupportedEncodingException e) {
			// quiet
		}
		return str;
	}

	// ====== 获取参数 =============================================================
	public ServletContext getServletContext() {
		return sc;
	}

	public String getInitParameter(String name) {
		return initParams.get(name);
	}

	public String getInitParameter(String name, String def) {
		String p = getInitParameter(name);
		return StringUtils.ifEmpty(p, def);
	}

	public String getEncoding() {
		return getInitParameter(PARAM_ENCODING, DEFAULT_ENCODING);
	}

	public String getFactoryClassName() {
		return getInitParameter(PARAM_HANDLER_FACTORY, DEFAULT_HANDLER_FACTORY);
	}

	// ===单例====================================================================
	private static final WebApplication INSTANCE = new WebApplication();

	private WebApplication() {
	}

	public static WebApplication getInstnce() {
		return INSTANCE;
	}
}