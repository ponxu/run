package com.ponxu.run.web.info;

import java.util.regex.Pattern;

/**
 * 映射信息
 * 
 * @author xwz
 */
public class Mapping {
	private String name;
	private String url;

	private Pattern urlPattern;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;

		if (!this.url.startsWith("^")) {
			this.url = "^" + this.url;
		}

		if (!this.url.endsWith("$")) {
			this.url = this.url + "$";
		}

		setUrlPattern(Pattern.compile(this.url));
	}

	public Pattern getUrlPattern() {
		return urlPattern;
	}

	public void setUrlPattern(Pattern urlPattern) {
		this.urlPattern = urlPattern;
	}

	@Override
	public String toString() {
		return "Mapping [name=" + name + ", url=" + url + "]";
	}

}
