package com.ponxu.run.ioc.loader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.ponxu.run.ioc.BeanFactory;
import com.ponxu.run.ioc.info.BeanInfo;
import com.ponxu.run.ioc.info.Property;
import com.ponxu.run.lang.ExceptionUtils;
import com.ponxu.run.lang.FileUtils;
import com.ponxu.run.lang.StringUtils;
import com.ponxu.run.log.Log;
import com.ponxu.run.log.LogFactory;

/**
 * xml加载
 * 
 * @author xwz
 */
public class XMLLoader implements Loader {
	private static final Log LOG = LogFactory.getLog();

	public void load(String param, Map<String, BeanInfo> beans) {
		try {
			InputStream in = FileUtils.fromClassPath(param);
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(in);
			Element root = document.getDocumentElement();
			String defaultScoep = root.getAttribute("scope");
			defaultScoep = StringUtils.ifEmpty(defaultScoep,
					BeanFactory.SCOPE_DEFAULT);

			LOG.debug("parse %s, root %s, scope %s", param, root.getNodeName(),
					defaultScoep);

			NodeList beanList = root.getElementsByTagName("bean");
			for (int i = 0; i < beanList.getLength(); i++) {
				Element beanNode = (Element) beanList.item(i);

				// 基本信息
				String id = beanNode.getAttribute("id");
				String className = beanNode.getAttribute("class");
				String scope = beanNode.getAttribute("scope");

				ExceptionUtils.makeRunTimeWhen(StringUtils.isEmpty(id),
						"bean id is empty at %d", i);
				ExceptionUtils.makeRunTimeWhen(StringUtils.isEmpty(className),
						"bean[id=%s] class is empty", id);

				BeanInfo info = new BeanInfo();
				info.setId(id);
				info.setClassName(className);
				info.setScope(StringUtils.ifEmpty(scope, defaultScoep));

				// 属性
				NodeList properties = beanNode.getElementsByTagName("property");
				for (int j = 0; j < properties.getLength(); j++) {
					Element propNode = (Element) properties.item(j);

					String name = propNode.getAttribute("name");
					String ref = propNode.getAttribute("ref");
					String value = propNode.getAttribute("value");

					ExceptionUtils.makeRunTimeWhen(StringUtils.isEmpty(name),
							"bean[id=%s] prop's name is empty at %d", id, j);
					if (StringUtils.isEmpty(ref) && StringUtils.isEmpty(value)) {
						ref = name; // 同名查找
					}

					Property propInfo = new Property();
					propInfo.setName(name);
					propInfo.setRef(ref);
					propInfo.setValue(value);

					info.getProperties().add(propInfo);
				}

				beans.put(info.getId(), info);
				LOG.debug(info);
			}
			in.close();
		} catch (ParserConfigurationException e) {
			ExceptionUtils.makeRuntime(e);
		} catch (SAXException e) {
			ExceptionUtils.makeRuntime(e);
		} catch (IOException e) {
			ExceptionUtils.makeRuntime(e);
		}
	}
}
