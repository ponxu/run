package com.ponxu.run.ioc.info;

import java.util.ArrayList;
import java.util.List;

/**
 * bean描述
 * 
 * @author xwz
 */
public class BeanInfo {
	private String id;
	private String className;
	private String scope;
	private List<Property> properties = new ArrayList<Property>();

	private Object instance;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public List<Property> getProperties() {
		return properties;
	}

	public void setProperties(List<Property> properties) {
		this.properties = properties;
	}

	public Object getInstance() {
		return instance;
	}

	public void setInstance(Object instance) {
		this.instance = instance;
	}

	@Override
	public String toString() {
		return "Bean [id=" + id + ", class=" + className + ", scope=" + scope
				+ "]";
	}

}