/**
 * Project Name:ice-equi
 * File Name:DeviceController.java
 * Package Name:com.ice.controller
 * Date:2018年4月24日下午2:27:34
 * Copyright (c) 2018, 彩慧居 All Rights Reserved.
 *
*/

package com.ice.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * ClassName:DeviceController <br/>
 * Date:     2018年4月24日 下午2:27:34 <br/>
 * @author   R.ZENG
 * @version  
 * @since    JDK 1.8
 */
@Controller
@RequestMapping("/{version}/device")
public class DeviceController {
	/**
	 * 
	 * add:(这里用一句话描述这个方法的作用). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 *
	 * @author R.ZENG
	 * @param session 
	 * @param villageId 设备所在小区
 	 * @param categoryId 设备分类
	 * @param deviceName 设备名称
	 * @param sipAction 开启sip服务
	 * @param  bind  bindType = 0  表示bind 的Id 为房屋ID
	 * @param  bindType 0:房屋,1:单元,2:楼栋,3:小区
	 * 
	 * @return
	 * @since JDK 1.8
	 */
	@RequestMapping("/add")
	public @ResponseBody Object add(
			String villageId,String  bind,long categoryId,
			String deviceName,int sipAction,short  bindType){
		
		
		
		return null;
	}
	/**
	 * 
	 * report:(这里用一句话描述这个方法的作用). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 *
	 * @author R.ZENG
	 * @param deviceId 设备ID
	 * @return
	 * @since JDK 1.8
	 */
	@RequestMapping("/handshake")
	public @ResponseBody Object handshake(long deviceId){
		
		return null;
	}
	/**
	 * 
	 * error: 设备异常上传. <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 *
	 * @author R.ZENG
	 * @param deviceId 设备Id 
	 * @param content  异常内容
	 * @return
	 * @since JDK 1.8
	 */
	@RequestMapping("/error")
	public @ResponseBody Object error(long deviceId,String content){
		
		return null;
	}
	
	/**
	 * 
	 * volume:(这里用一句话描述这个方法的作用). <br/>
	 * 后台超管设备音量调节.<br/>
	 *
	 * @author R.ZENG
	 * @param deviceId 设备ID 多个用逗号分隔
	 * @param volume 音量1-15
	 * @return
	 * @since JDK 1.8
	 */
    @RequestMapping("/volume")
	public @ResponseBody Object volume(List<String> deviceIds,short volume){
		
		return null;
	} 
	/**
	 * 
	 * restart:后台设备重启. <br/>
	 * 激活设备重启.<br/>
	 *
	 * @author R.ZENG
	 * @param deviceIds
	 * @return
	 * @since JDK 1.8
	 */
    @RequestMapping("/restart")
  	public @ResponseBody Object restart(List<String> deviceIds){
  		
  		return null;
  	} 
	
	
}

