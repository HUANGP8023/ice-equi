/**
 * Project Name:ice-equi
 * File Name:OwnController.java
 * Package Name:com.ice.controller
 * Date:2018年4月24日下午2:54:32
 * Copyright (c) 2018, 彩慧居 All Rights Reserved.
 *
*/

package com.ice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * ClassName:OwnController <br/>
 * Date:     2018年4月24日 下午2:54:32 <br/>
 * @author   R.ZENG
 * @version  
 * @since    JDK 1.8
 */


@Controller
@RequestMapping("/{version}/own")
public class OwnController {
	
	/**
	 * 
	 * open:(这里用一句话描述这个方法的作用). <br/>
	 * TODO(这里描述这个方法适用条件 – 可选).<br/>
	 *
	 * @author R.ZENG
	 * @param deviceId 设备ID
	 * @param openType 开门方式
	 * @param ownId 业主ID
	 * @return
	 * @since JDK 1.8
	 */
	@RequestMapping("/device/open")
	public @ResponseBody Object open(long deviceId,short openType,long ownId){
		return  null;
	}
   /**
    * 
    * deviceList:(这里用一句话描述这个方法的作用). <br/>
    * 获取用户能使用的设备列表.<br/>
    *
    * @author R.ZENG
    * @param deviceId
    * @param openType
    * @param ownId
    * @return
    * @since JDK 1.8
    */
	@RequestMapping("/device/list")
	public @ResponseBody Object deviceList(long deviceId,
			short openType,long ownId,int pageSize,int pageNo){
		return  null;
	}
	/**
	 * 
	 * cardBind:(这里用一句话描述这个方法的作用). <br/>
	 * 业主门禁卡绑定<br/>
	 *
	 * @author R.ZENG
	 * @param cardNo
	 * @param ownId
	 * @return
	 * @since JDK 1.8
	 */
	@RequestMapping("/device/card/bind")
	public @ResponseBody Object cardBind(
			String cardNo,long ownId){
		return  null;
	}
	
	/**
	 * 
	 * faceBind:(这里用一句话描述这个方法的作用). <br/>
	 *  先上传人脸后再绑定地址.<br/>
	 *
	 * @author R.ZENG
	 * @param face
	 * @param ownId
	 * @return
	 * @since JDK 1.8
	 */
	@RequestMapping("/device/face/bind")
	public @ResponseBody Object faceBind(String face,long ownId){
		return  null;
	}
	
	/**
	 * 
	 * ownList:(这里用一句话描述这个方法的作用). <br/>
	 * 通过设备 获取设备所管辖的 业主信息（卡，人脸地址）.<br/>
	 *
	 * @author R.ZENG
	 * @param deviceId
	 * @param pageSize
	 * @param pageNo
	 * @return
	 * @since JDK 1.8
	 */
	@RequestMapping("/info/list/info")
	public @ResponseBody Object ownList(long deviceId,int pageSize,int pageNo){
		return  null;
	}
}

