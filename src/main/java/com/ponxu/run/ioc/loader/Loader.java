package com.ponxu.run.ioc.loader;

import java.util.Map;

import com.ponxu.run.ioc.info.BeanInfo;

public interface Loader {
	public void load(String param, Map<String, BeanInfo> beans);
}