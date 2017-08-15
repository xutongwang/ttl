package com.common.readwriteseparate;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.aop.AfterReturningAdvice;
import org.springframework.aop.MethodBeforeAdvice;
import org.springframework.aop.ThrowsAdvice;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class DataSourceAdvice implements MethodBeforeAdvice,
		AfterReturningAdvice, ThrowsAdvice {

	private static Logger log = Logger.getLogger(DataSourceAdvice.class);

	public void before(Method method, Object[] args, Object target)
			throws Throwable {
		if (method.getName().startsWith("add")
				|| method.getName().startsWith("update")
				|| method.getName().startsWith("edit")
				|| method.getName().startsWith("save")) {
			DynamicDataSourceHolder.setMaster();
		} else {
			DynamicDataSourceHolder.setSlave();
		}
	}

	public void afterReturning(Object var, Method method, Object[] args,
			Object target) throws Throwable {
		DynamicDataSourceHolder.setMaster();
	}

	public void afterThrowing(Method method, Object[] args, Object target,
			Exception ex) throws Throwable {
		DynamicDataSourceHolder.setSlave();
	}
}