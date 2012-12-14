package com.ponxu.run.web.factory;

import com.ponxu.run.ioc.BeanFactory;
import com.ponxu.run.web.RequestHandler;

/**
 * Run框架管理Handler
 * 
 * @author xwz
 */
public class RunRequestHandlerFactory extends AbstractHandlerFactory {
	@Override
	public RequestHandler getHandler(String name) {
		return (RequestHandler) BeanFactory.get(name);
	}
}
