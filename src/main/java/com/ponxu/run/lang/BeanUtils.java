package com.ponxu.run.lang;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.ponxu.run.log.Log;
import com.ponxu.run.log.LogFactory;

@SuppressWarnings("unchecked")
public class BeanUtils {
	private static final Log LOG = LogFactory.getLog();

	// ------------------------------------------------------
	/** 新建对象 */
	public static Object newInstance(String className) {
		Object obj = null;
		try {
			Class<?> clazz = Class.forName(className);
			obj = clazz.newInstance();
			LOG.debug("new %s", className);
		} catch (Exception e) {
			// quiet
		}
		return obj;
	}

	/** 用setter设置bean属性 */
	public static void setProperty(Object bean, String name, Object value) {
		String methodName = "set" + StringUtils.firstUpperCase(name);
		invokeMehodByName(bean, methodName, value);
	}

	/** 用getter获取bean属性 */
	public static Object getProperty(Object bean, String name) {
		String methodName = "get" + StringUtils.firstUpperCase(name);
		return invokeMehodByName(bean, methodName);
	}

	/** 类型转换 */
	public static <T> T cast(Object value, Class<T> type) {
		if (value != null && !type.isAssignableFrom(value.getClass())) {
			if (is(type, int.class, Integer.class)) {
				value = Integer.parseInt(String.valueOf(value));
			} else if (is(type, long.class, Long.class)) {
				value = Long.parseLong(String.valueOf(value));
			} else if (is(type, float.class, Float.class)) {
				value = Float.parseFloat(String.valueOf(value));
			} else if (is(type, double.class, Double.class)) {
				value = Double.parseDouble(String.valueOf(value));
			} else if (is(type, boolean.class, Boolean.class)) {
				value = Boolean.parseBoolean(String.valueOf(value));
			} else if (is(type, String.class)) {
				value = String.valueOf(value);
			}
		}
		return (T) value;
	}

	/** 查找方法 */
	public static Method getMethodByName(Object classOrBean, String methodName) {
		Method ret = null;
		if (classOrBean != null) {
			Class<?> clazz = null;
			if (classOrBean instanceof Class<?>) {
				clazz = (Class<?>) classOrBean;
			} else {
				clazz = classOrBean.getClass();
			}
			for (Method method : clazz.getMethods()) {
				if (method.getName().equals(methodName)) {
					ret = method;
					break;
				}
			}
		}
		return ret;
	}

	/** 调用方法(简单类型自动转) */
	public static Object invokeMehodByName(Object bean, String methodName,
			Object... args) {
		try {
			Method method = getMethodByName(bean, methodName);
			Class<?>[] types = method.getParameterTypes();

			int argCount = args == null ? 0 : args.length;

			// 参数个数对不上
			ExceptionUtils.makeRunTimeWhen(argCount != types.length,
					"%s in %s", methodName, bean);

			// 转参数类型
			for (int i = 0; i < argCount; i++) {
				args[i] = cast(args[i], types[i]);
			}

			return method.invoke(bean, args);
		} catch (Exception e) {
			ExceptionUtils.makeRuntime(e);
		}
		return null;
	}

	// ------------------------------------------------------

	/** 对象是否其中一个 */
	public static boolean is(Object obj, Object... mybe) {
		if (obj != null && mybe != null) {
			for (Object mb : mybe)
				if (obj.equals(mb))
					return true;
		}
		return false;
	}

	public static boolean isNot(Object obj, Object... mybe) {
		return !is(obj, mybe);
	}

	// ------------------------------------------------------

	/** 扫描包下面所有的类 */
	public static List<String> scanPackageClass(String rootPackageName) {
		List<String> classNames = new ArrayList<String>();
		try {
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			URL url = loader.getResource(rootPackageName.replace('.', '/'));

			ExceptionUtils.makeRunTimeWhen(url == null,
					"package[%s] not found!", rootPackageName);

			String protocol = url.getProtocol();
			if ("file".equals(protocol)) {
				LOG.debug("scan in file");
				File[] files = new File(url.toURI()).listFiles();
				for (File f : files) {
					scanPackageClassInFile(rootPackageName, f, classNames);
				}
			} else if ("jar".equals(protocol)) {
				LOG.debug("scan in jar");
				JarFile jar = ((JarURLConnection) url.openConnection())
						.getJarFile();
				scanPackageClassInJar(jar, rootPackageName, classNames);
			}

		} catch (URISyntaxException e) {
		} catch (IOException e) {
		}
		return classNames;
	}

	/** 扫描文件夹下所有class文件 */
	private static void scanPackageClassInFile(String rootPackageName,
			File rootFile, List<String> classNames) {
		String absFileName = rootPackageName + "." + rootFile.getName();
		if (rootFile.isFile() && absFileName.endsWith(".class")) {
			classNames.add(absFileName.substring(0, absFileName.length() - 6));
		} else if (rootFile.isDirectory()) {
			String tmPackageName = rootPackageName + "." + rootFile.getName();
			for (File f : rootFile.listFiles()) {
				scanPackageClassInFile(tmPackageName, f, classNames);
			}
		}
	}

	/** 扫描jar里面的类 */
	private static void scanPackageClassInJar(JarFile jar,
			String packageDirName, List<String> classNames) {
		Enumeration<JarEntry> entries = jar.entries();
		while (entries.hasMoreElements()) {
			JarEntry entry = entries.nextElement();
			String name = entry.getName().replace('/', '.');
			if (name.startsWith(packageDirName) && name.endsWith(".class")) {
				classNames.add(name.substring(0, name.length() - 6));
			}
		}
	}

	// ------------------------------------------------------
}
