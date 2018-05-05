package com.ice.common;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ice.core.task.SchedulerTask;
import com.ice.utils.FastJSONUtil;
import com.ice.utils.PropertyUtils;

/**@Copyright CHJ
 * @Author HUANGP
 * @Date 2018年4月25日
 * @Desc 公共服务
 */
@Service("commonPublic")
public class CommonPublic {
	
	private final Logger logger = LoggerFactory.getLogger(SchedulerTask.class);
	
	public void executeAuthToken() throws Exception {
		String authStr=HttpColourLifeServer.httpAuth();
		int i=0;
		while(true){
			logger.info("鉴权 : "+authStr);
			if(StringUtils.isNotEmpty(authStr)){
				String code=FastJSONUtil.parseJsonByKey("code",authStr);
				if(code.equals("0")){
					String content=FastJSONUtil.parseJsonByKey("content",authStr);
					String exTime=FastJSONUtil.parseJsonByKey("expireTime",content);
					String accessToken=FastJSONUtil.parseJsonByKey("accessToken",content);
					PropertyUtils.setProperty("expireTime", exTime);
					PropertyUtils.setProperty("accessToken", accessToken);
					break;
				}
				if(i==3)break;
				Thread.currentThread().sleep(1000*2);
				authStr=HttpColourLifeServer.httpAuth();
				i++;
			}
		}
	}
	
	
	

}
