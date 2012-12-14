package com.ponxu.run.ioc;

import java.util.HashMap;
import java.util.Map;

import com.ponxu.run.ioc.info.BeanInfo;
import com.ponxu.run.ioc.info.Property;
import com.ponxu.run.ioc.loader.AnnotationLoader;
import com.ponxu.run.ioc.loader.Loader;
import com.ponxu.run.ioc.loader.XMLLoader;
import com.ponxu.run.lang.BeanUtils;
import com.ponxu.run.lang.ExceptionUtils;
import com.ponxu.run.lang.StringUtils;
import com.ponxu.run.log.Log;
import com.ponxu.run.log.LogFactory;

/**
 * 工厂
 * 
 * @author xwz
 */
public class BeanFactory {
	private static final Log LOG = LogFactory.getLog();

	public static final String SCOPE_SINGLETON = "singleton";
	public static final String SCOPE_PROTYPE = "protype";
	public static final String SCOPE_DEFAULT = SCOPE_SINGLETON;
	public static final String DEFAULT_CONTEXT_NAME = "default_context";

	// bean描述缓存
	private static final Map<String, Map<String, BeanInfo>> beans = new HashMap<String, Map<String, BeanInfo>>();

	public static void loadXMLFile(String fileName) {
		loadXMLFile(DEFAULT_CONTEXT_NAME, fileName);
	}

	public static void loadPackage(String rootPackageName) {
		loadPackage(DEFAULT_CONTEXT_NAME, rootPackageName);
	}

	public static void loadXMLFile(String contextName, String fileName) {
		Loader loader = new XMLLoader();
		load(contextName, fileName, loader);
	}

	public static void loadPackage(String contextName, String rootPackageName) {
		Loader loader = new AnnotationLoader();
		load(contextName, rootPackageName, loader);
	}

	// 加载bean配置的唯一入口, 确保同步
	private synchronized static void load(String contextName, String param,
			Loader loader) {
		Map<String, BeanInfo> thisContextBeans = beans.get(contextName);
		if (thisContextBeans == null) {
			thisContextBeans = new HashMap<String, BeanInfo>();
			beans.put(contextName, thisContextBeans);
		}
		loader.load(param, thisContextBeans);
	}

	// -------------------------------------------------------
	public static Object get(String contextName, String id) {
		Object instance = null;
		Map<String, BeanInfo> thisContextBeans = beans.get(contextName);
		if (thisContextBeans != null) {
			BeanInfo info = thisContextBeans.get(id);
			if (info != null) {
				instance = info.getInstance();
				if (instance == null) {
					instance = BeanUtils.newInstance(info.getClassName());
					ExceptionUtils.makeRunTimeWhen(instance == null,
							"can not instantiate id=%s, class=%s", id,
							info.getClassName());

					// 设置属性
					for (Property prop : info.getProperties()) {
						String pname = prop.getName();
						Object pvalue = null;
						if (StringUtils.isNotEmpty(prop.getValue())) {
							// 普通属性
							pvalue = prop.getValue();
						} else if (StringUtils.isNotEmpty(prop.getRef())) {
							// 引用
							pvalue = get(contextName, prop.getRef());
						}
						BeanUtils.setProperty(instance, pname, pvalue);
						LOG.debug("Inject %s: %s", pname, pvalue);
					}

					// 初始化
					if (instance != null && instance instanceof Initializable) {
						((Initializable) instance).initialize();
					}

					// 缓存单例对象
					if (SCOPE_SINGLETON.equalsIgnoreCase(info.getScope())) {
						info.setInstance(instance);
					}
				} // end if (instance == null)
			} // end if (info != null)
		} // end if (thisContextBeans != null)

		if (instance == null) {
			LOG.warn("can not find bean[id=%s]", id);
			return null;
		}

		return instance;
	}

	public static Object get(String id) {
		return get(DEFAULT_CONTEXT_NAME, id);
	}
}