package com.ice.common;

/**@Copyright CHJ
 * @Author HUANGP
 * @Date 2018年4月19日
 * @Desc 系统常量配置
 */
public class SystemConfig {
	
	
	//定时器提前两小时鉴权
	public static long authTime=1000*60*60*2;
	

	public static final String  code="chjmj"; 

	public static final String  appID="ICECHJMJ-3876-S26N-GCI3-GT9UOIYH3QFA";
	
	public static final String token="DAdXiLQpS6wq3IyGMhK4";

	public static final String clientSecret="oicIg6mnBMvpMhoXWONS";
	
	public static final String corpuuid="a8c58297436f433787725a94f780a3c9";
	
	
	
	
	//彩生活测试环境
	public static final String COLOURLIFESERVER="https://openapi-test.colourlife.com";
	
	//彩生活正式环境
	//public static final String COLOURLIFESERVER="";
	
	
	
	
	/*************************************************************************************/
	/***********************************业务接口**START************************************/
	/*************************************************************************************/
	
	
	//搜索租户
	public static  String COLOURLIFESERVER_QUERY=COLOURLIFESERVER+"/v1/authms/corp/search";
	
	//彩生活鉴权接口
	public static  String COLOURLIFESERVER_AUTH=COLOURLIFESERVER+"/v1/authms/auth/app";
	
	//行政区域查询
	public static String COLOURLIFESERVER_AREAS=COLOURLIFESERVER+"/v1/regionms/administrativeDivision";
	
	//彩生活根据行政区域查询小区列表
	public static String COLOURLIFESERVER_VILLAGE=COLOURLIFESERVER+"/v1/resourcems/community/search";
	
	//根据小区ID查询楼栋
	public static String COLOURLIFESERVER_BUILDING=COLOURLIFESERVER+"/v1/resourcems/building/search";
	
	//根据小区查询单元
	public static String COLOURLIFESERVER_UNIT=COLOURLIFESERVER+"/v1/resourcems/unit/search";
	
	//查询结构关系（业主、小区结构）
	public static String COLOURLIFESERVER_ISBELONG=COLOURLIFESERVER+"/v1/resourcems/dataRelation/search";
	
	//获取小区住户接口
	public static String COLOURLIFESERVER_USER=COLOURLIFESERVER+"/v1/residentms/resident/randomData";
	
	//条件查询住户
	public static String COLOURLIFESERVER_USER_QUERY=COLOURLIFESERVER+"/v1/residentms/resident/search";
	
	//房间查询
	public static String COLOURLIFESERVER_QUERYHOUSE=COLOURLIFESERVER+"/v1/resourcems/house";
	

	
	/*************************************************************************************/
	/***********************************业务接口**END**************************************/
	/*************************************************************************************/
	
	/*####################################################################################*/
	
	/*************************************************************************************/
	/***********************************功能接口**start************************************/
	/*************************************************************************************/
	
	
	//添加队列
	public static String COLOURLIFESERVER_CHACHECREATE=COLOURLIFESERVER+"/v1/hcfw/cache/queue";
	//添加缓存数据
	public static String COLOURLIFESERVER_CACHEADD=COLOURLIFESERVER+"/v1/hcfw/cache/data";
	//获取缓存数据
	public static String COLOURLIFESERVER_GETCACHE=COLOURLIFESERVER+"/v1/hcfw/cache/data";
	//文件上传
	public static String COLOURLIFESERVER_UPLOADFILE=COLOURLIFESERVER+"/v1/newfileup/uploadFile";
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	

}
