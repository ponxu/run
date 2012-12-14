package com.ponxu.run.web.loader;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.ponxu.run.lang.ExceptionUtils;
import com.ponxu.run.lang.FileUtils;
import com.ponxu.run.log.Log;
import com.ponxu.run.log.LogFactory;
import com.ponxu.run.web.info.Mapping;

/**
 * 从xml文件加载url映射信息
 * 
 * @author xwz
 */
public class XMLMappingLoader implements MappingLoader {
	private static final Log LOG = LogFactory.getLog();

	public void load(String xml, List<Mapping> handlers) {
		try {
			InputStream in = FileUtils.fromClassPath(xml);
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(in);
			Element root = document.getDocumentElement();
			LOG.debug("parse %s, root %s", xml, root.getNodeName());

			NodeList handlerList = root.getElementsByTagName("handler");
			for (int i = 0; i < handlerList.getLength(); i++) {
				Element node = (Element) handlerList.item(i);
				String name = node.getAttribute("name");
				String url = node.getAttribute("url");

				Mapping info = new Mapping();
				info.setName(name);
				info.setUrl(url);
				handlers.add(info);

				LOG.debug(info);
			}

		} catch (ParserConfigurationException e) {
			ExceptionUtils.makeRuntime(e);
		} catch (SAXException e) {
			ExceptionUtils.makeRuntime(e);
		} catch (IOException e) {
			ExceptionUtils.makeRuntime(e);
		}
	}
}
