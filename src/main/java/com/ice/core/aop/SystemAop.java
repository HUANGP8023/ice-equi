package com.ice.core.aop;


import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.ice.common.CommonPublic;
import com.ice.common.SystemConfig;
import com.ice.utils.PropertyUtils;


/**
 * @author HUANGP
 * @date 2017年12月26日
 * @des
 */
@Component
@Aspect
@Order(0)
public class SystemAop{

	private final static Log log = LogFactory.getLog(SystemAop.class);
	
	
	@Autowired
    private CommonPublic commonPublic;

	// 配置切入点,该方法无方法体,主要为方便同类中其他方法使用此处配置的切入点
	@Pointcut("execution(* com.ice.controller..*.*(..))")
	public void aspect() {}

	/*
	 * 配置前置通知,使用在方法aspect()上注册的切入点 同时接受JoinPoint切入点对象,可以没有该参数
	 */
	@Before("aspect()")
	public void before(JoinPoint joinPoint) {
		if (log.isInfoEnabled()) {
			log.info("before " + joinPoint);
		}
	}

	// 配置后置通知,使用在方法aspect()上注册的切入点
	@After("aspect()")
	public void after(JoinPoint  joinPoint) {
		if (log.isInfoEnabled()) {
			log.info("after " + joinPoint);
		}
	}

	// 配置环绕通知,使用在方法aspect()上注册的切入点
	@Around("aspect()")
	public Object  around(ProceedingJoinPoint point) {
		try {
			String expireTime=PropertyUtils.getProperty("expireTime");
			String accessToken=PropertyUtils.getProperty("accessToken");
	    	if(StringUtils.isEmpty(accessToken)||
	    			(StringUtils.isNotEmpty(expireTime)&&(Long.parseLong(expireTime)-System.currentTimeMillis()<SystemConfig.authTime))){
	    		commonPublic.executeAuthToken();
	    	}
			return point.proceed();
		} catch (Throwable e) {
			return null;
		}

	}

	// 配置后置返回通知,使用在方法aspect()上注册的切入点
	@AfterReturning("aspect()")
	public void afterReturn(JoinPoint joinPoint) {
		if (log.isInfoEnabled()) {
			log.info("afterReturn " + joinPoint);
		}
	}

	// 配置抛出异常后通知,使用在方法aspect()上注册的切入点
	@AfterThrowing(pointcut = "aspect()", throwing = "ex")
	public void afterThrow(JoinPoint joinPoint, Exception ex) {
		if (log.isInfoEnabled()) {
			log.info("<<<<<<<<"+ex.getMessage()+">>>>>>>>");
			log.info("afterThrow " + joinPoint + "\t" + ex.getMessage());
		}
	}

}
