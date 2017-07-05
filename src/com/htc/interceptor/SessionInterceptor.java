package com.htc.interceptor;

import org.apache.log4j.Logger;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

public class SessionInterceptor implements Interceptor {

	private static final long serialVersionUID = 1L;
	static Logger log = Logger.getLogger(SessionInterceptor.class);
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
	}

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		String actionName = invocation.getInvocationContext().getName();
		log.info("Inside Interceptor -- Action Name --> "+actionName);
		return invocation.invoke();
	}

}
