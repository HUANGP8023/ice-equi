package com.ice.core.task;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ice.utils.PropertyUtils;

/**@Copyright CHJ
 * @Author HUANGP
 * @Date 2018年4月20日
 * @Desc 定时器
 */
@Component
public class SchedulerTask {
	
	
    private final Logger logger = LoggerFactory.getLogger(SchedulerTask.class);
    
    @Scheduled(cron="*/5 * * * * ?")
    public void dateTask(){
    	String expireTime=PropertyUtils.getProperty("expireTime");
		String accessToken=PropertyUtils.getProperty("accessToken");
		System.out.println("task expireTime:"+expireTime+",task accessToken:"+accessToken);
    }



	
}