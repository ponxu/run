package com.ponxu.run.web.render;

import com.ponxu.run.web.WebContext;

/**
 * 渲染输出
 * 
 * @author xwz
 */
public abstract class AbstractRender {
	/** 是否初始化 */
	private boolean isInitialized = false;

	/** 渲染,返回渲染结果 */
	public abstract Object render(WebContext context, Object what);

	/** 检查是否已经初始化, 若没有, 那么初始化 */
	protected final void checkAndInit() {
		if (!isInitialized) {
			synchronized (getClass()) {
				if (!isInitialized) {
					init();
					isInitialized = true;
				}
			}
		}
	}

	protected void init() {
	}
}
