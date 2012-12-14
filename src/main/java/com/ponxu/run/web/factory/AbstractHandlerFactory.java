package com.ponxu.run.web.factory;

import com.ponxu.run.web.RequestHandler;

/**
 * Handler工厂
 * 
 * @author xwz
 */
public abstract class AbstractHandlerFactory {
	public abstract RequestHandler getHandler(String name);
}