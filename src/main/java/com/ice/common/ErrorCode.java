package com.ice.common;

import lombok.Getter;

/**@Copyright CHJ
 * @Author HUANGP
 * @Date 2018年5月4日
 * @Desc 系统异常代码定义
 */
@Getter
public enum ErrorCode{
	
	OPERATE_SUCCESS_EXCEPTION(0,"操作成功"),
	OPERATE_FAIL_EXCEPTION(1,"操作失败"),
	
	
	//设备添加异常
	DEVICE_ADD_EXCEPTION(10001,"设备添加异常"),
	//设备查询异常
	DEVICE_QUERY_EXCEPTION(10002,"设备查询异常"),
	//设备修改异常
	DEVICE_UPDATE_EXCEPTION(10003,"操作更新异常"),
	//设备删除（逻辑删除）异常
	DEVICE_DEL_EXCEPTION(10004,"操作删除异常"),
	//流量开门异常
	FLOW_OPEN_DOOR_EXCEPTION(10006,"流量开门异常"),
	//门禁设备心跳异常
	DEVICE_HEART_BEAT_EXCEPTION(10007,"门禁设备心跳异常");
	
	
	

	private int code;
	private String msg;
	
	private  ErrorCode(int code,String msg){
		this.code=code;
		this.msg=msg;
	}
	

}
