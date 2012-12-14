package com.ponxu.run.web;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.ponxu.run.ioc.BeanFactory;
import com.ponxu.run.lang.StringUtils;
import com.ponxu.run.log.Log;
import com.ponxu.run.log.LogFactory;

/**
 * 加载bean描述
 * 
 * @author xwz
 */
public class BeanLoaderListener implements ServletContextListener {
	private static final Log LOG = LogFactory.getLog();

	public static final String SPLIT = ",";
	public static final String XML_PARAM = "xml";
	public static final String PACKAGE_PARAM = "package";

	public void contextInitialized(ServletContextEvent event) {
		LOG.debug("contextInitialized......");

		ServletContext sc = event.getServletContext();
		loadBean(sc);
	}

	public void contextDestroyed(ServletContextEvent event) {
		LOG.debug("contextDestroyed.......");
	}

	public static void loadBean(ServletContext sc) {
		String xml = sc.getInitParameter(XML_PARAM);
		String pg = sc.getInitParameter(PACKAGE_PARAM);

		if (StringUtils.isNotEmpty(xml)) {
			for (String fileName : xml.split(SPLIT)) {
				BeanFactory.loadXMLFile(fileName.trim());
			}
		}

		if (StringUtils.isNotEmpty(pg)) {
			for (String rootPackageName : pg.split(SPLIT)) {
				BeanFactory.loadPackage(rootPackageName.trim());
			}
		}
	}
}