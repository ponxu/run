package com.ponxu.run.web.loader;

import java.util.List;

import com.ponxu.run.lang.BeanUtils;
import com.ponxu.run.lang.ExceptionUtils;
import com.ponxu.run.lang.StringUtils;
import com.ponxu.run.log.Log;
import com.ponxu.run.log.LogFactory;
import com.ponxu.run.web.annotation.Handler;
import com.ponxu.run.web.info.Mapping;

/**
 * 从注解加载url映射信息
 * 
 * @author xwz
 */
public class AnnotationMappingLoader implements MappingLoader {
	private static final Log LOG = LogFactory.getLog();

	public void load(String pkg, List<Mapping> handlers) {
		try {
			LOG.debug("parse pkg: %s", pkg);
			List<String> classNames = BeanUtils.scanPackageClass(pkg);
			for (String className : classNames) {
				Class<?> clazz = Class.forName(className);
				String simpleName = clazz.getSimpleName();
				Handler handler = clazz.getAnnotation(Handler.class);
				if (handler != null) {
					String name = StringUtils.ifEmpty(handler.name(),
							StringUtils.firstLowerCase(simpleName));
					String url = handler.url();

					Mapping info = new Mapping();
					info.setName(name);
					info.setUrl(url);
					handlers.add(info);

					LOG.debug(info);
				}
			}
		} catch (ClassNotFoundException e) {
			ExceptionUtils.makeRuntime(e);
		}
	}
}
