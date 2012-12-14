package com.ponxu.run.ioc.loader;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import com.ponxu.run.ioc.BeanFactory;
import com.ponxu.run.ioc.annotation.Bean;
import com.ponxu.run.ioc.annotation.Inject;
import com.ponxu.run.ioc.info.BeanInfo;
import com.ponxu.run.ioc.info.Property;
import com.ponxu.run.lang.BeanUtils;
import com.ponxu.run.lang.ExceptionUtils;
import com.ponxu.run.lang.StringUtils;
import com.ponxu.run.log.Log;
import com.ponxu.run.log.LogFactory;

/**
 * @author xwz
 */
public class AnnotationLoader implements Loader {
	private static final Log LOG = LogFactory.getLog();

	public void load(String param, Map<String, BeanInfo> beans) {
		try {
			List<String> classNames = BeanUtils.scanPackageClass(param);
			for (String className : classNames) {
				Class<?> clazz = Class.forName(className);
				String simpleName = clazz.getSimpleName();

				Bean bean = clazz.getAnnotation(Bean.class);
				if (bean != null) {
					// 基本信息
					BeanInfo info = new BeanInfo();
					String id = StringUtils.ifEmpty(bean.id(),
							StringUtils.firstLowerCase(simpleName));
					String scope = StringUtils.ifEmpty(bean.scope(),
							BeanFactory.SCOPE_DEFAULT);

					info.setId(id);
					info.setClassName(className);
					info.setScope(scope);

					// 属性
					for (Field field : clazz.getDeclaredFields()) {
						Inject inject = field.getAnnotation(Inject.class);
						if (inject != null) {
							String name = field.getName();
							String ref = inject.ref();
							String value = inject.val();

							if (StringUtils.isEmpty(ref)
									&& StringUtils.isEmpty(value)) {
								ref = name; // 同名查找
							}

							Property propInfo = new Property();
							propInfo.setName(name);
							propInfo.setRef(ref);
							propInfo.setValue(value);

							info.getProperties().add(propInfo);
						}
					}

					beans.put(info.getId(), info);
					LOG.debug(info);
				}
			}
		} catch (ClassNotFoundException e) {
			ExceptionUtils.makeRuntime(e);
		}
	}

}
