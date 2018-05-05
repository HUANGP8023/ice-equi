package com.ice.common;

import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;

import com.ice.utils.HttpClientUtils;
import com.ice.utils.MD5Util;
import com.ice.utils.PropertyUtils;

/**@Copyright CHJ
 * @Author HUANGP
 * @Date 2018年4月23日
 * @Desc ICE请求方法封装
 */
public class HttpColourLifeServer {
	
	
	/**@Copyright CHJ
	 * @Author HUANGP
	 * @Date 2018年4月23日
	 * @Desc 时间戳获取
	 *
	 * @return
	 */
	public static String getTs(){
		String ts = String.valueOf(System.currentTimeMillis());
		ts=ts.substring(0, ts.length()-3);
		return ts;
	}
	
	
	/**@Copyright CHJ
	 * @Author HUANGP
	 * @Date 2018年4月23日
	 * @Desc md5加密
	 *
	 * @param ts
	 * @return
	 */
	public static String Md5Sign(String appID,String ts) {
		return MD5Util.md5Encode(appID + ts + SystemConfig.token+"false", "utf-8");
	} 
	
	/**@Copyright CHJ
	 * @Author HUANGP
	 * @Date 2018年4月24日
	 * @Desc 鉴权方法
	 *
	 * @return
	 * @throws Exception
	 */
	public static String httpAuth() throws Exception{
		Map<String, String> params = new TreeMap<String, String>();
		String ts = getTs();
		params.put("corp_uuid", SystemConfig.corpuuid);
		params.put("app_uuid", SystemConfig.appID);
		params.put("signature",MD5Util.md5Encode(SystemConfig.appID + ts + SystemConfig.token, "utf-8"));
		params.put("timestamp",ts);
		params.put("sign",Md5Sign(SystemConfig.appID,ts));
		params.put("ts", String.valueOf(ts));
		params.put("appID", SystemConfig.appID);
		return HttpClientUtils.httpPost(SystemConfig.COLOURLIFESERVER_AUTH,params);
	}
	/**@Copyright CHJ
	 * @Author HUANGP
	 * @Date 2018年4月23日
	 * @Desc 彩生活区域查询
	 *
	 * @param pid pid(0获取最上级)
	 * @return
	 * @throws Exception
	 */
	public static String httpGetArea(long pid) throws Exception{
		String ts=getTs();
		Map<String,Object> params = new TreeMap<String,Object>();
		params.put("token",PropertyUtils.getProperty("accessToken"));
		params.put("pid",pid<=0?0:pid);
		params.put("sign",Md5Sign(SystemConfig.appID,ts));
		params.put("ts",ts);
		params.put("appID", SystemConfig.appID);
		String respMsg=HttpClientUtils.httpGet(SystemConfig.COLOURLIFESERVER_AREAS,params);
		if(StringUtils.isEmpty(respMsg)){
			return "";
		}
		return respMsg;
	}
	
	
	
	
	/**@Copyright CHJ
	 * @Author HUANGP
	 * @Date 2018年4月23日
	 * @Desc 通过省、市、区获取小区列表
	 *
	 * @param pageNo 
	 * @param pageSize
	 * @param provinceId  省ID
	 * @param cityId      市ID
	 * @param regionId    区ID
	 * @return
	 * @throws Exception
	 */
	public static String httpGetVillage(long pageNo,long pageSize,String provinceName,String cityName,String regionName) throws Exception{
		String ts=getTs();
		Map<String,Object> params = new LinkedHashMap<String,Object>();
		
		params.put("ts",ts);
		params.put("sign",Md5Sign(SystemConfig.appID,ts));
		params.put("appID", SystemConfig.appID);
		params.put("token",PropertyUtils.getProperty("accessToken"));
		
		params.put("pageIndex",pageNo);
		params.put("pageSize",pageSize);
		
		
		if(StringUtils.isNotEmpty(provinceName))params.put("province",URLEncoder.encode(provinceName,"UTF-8"));
		if(StringUtils.isNotEmpty(cityName))params.put("city",URLEncoder.encode(cityName,"UTF-8"));
		if(StringUtils.isNotEmpty(regionName))params.put("region",URLEncoder.encode(regionName,"UTF-8"));
		
		params.put("status",0);
		
		String respMsg=HttpClientUtils.httpGet(SystemConfig.COLOURLIFESERVER_VILLAGE,params);
		
		if(StringUtils.isEmpty(respMsg)){
			return "";
		}
		return respMsg;	
	}
	
	
	
	/**@Copyright CHJ
	 * @Author HUANGP
	 * @Date 2018年4月24日
	 * @Desc 条件查询单元
	 *
	 * @param pageNo    【必传】
	 * @param pageSize  【必传】
	 * @param vilId    小区ID
	 * @param builId   栋ID
	 * @param bTypeId  建筑类型ID
	 * @param bType  建筑类型
	 * @return
	 * @throws Exception
	 */
	public static String httpGetUnit(long pageNo,long pageSize,String vilId,String builId,String bTypeId,String bType) throws Exception{
		String ts=getTs();
		Map<String,Object> params = new LinkedHashMap<String,Object>();
		
		params.put("ts",ts);
		params.put("sign",Md5Sign(SystemConfig.appID,ts));
		params.put("appID", SystemConfig.appID);
		params.put("token",PropertyUtils.getProperty("accessToken"));
		
		params.put("pageIndex",pageNo);
		params.put("pageSize",pageSize);
		
		if(StringUtils.isNotEmpty(vilId))params.put("communityUuid",vilId);
		
		if(StringUtils.isNotEmpty(builId))params.put("buildingUuid",builId);
		
		if(StringUtils.isNotEmpty(bTypeId))params.put("buildingTypeId",bTypeId);
		
		if(StringUtils.isNotEmpty(bType))params.put("buildingType",bType);
		
		params.put("status",0);
		
		String respMsg=HttpClientUtils.httpGet(SystemConfig.COLOURLIFESERVER_UNIT,params);
		
		if(StringUtils.isEmpty(respMsg)){
			return "";
		}
		return respMsg;	
	}
	
	
	
	/**@Copyright CHJ
	 * @Author HUANGP
	 * @Date 2018年4月24日
	 * @Desc  条件查询小区楼栋信息
	 *
	 * @param pageNo   【必传】
	 * @param pageSize 【必传】
	 * @param vilId   小区ID
	 * @param bTypeId  建筑类型ID
	 * @param bType   建筑类型
	 * @return
	 * @throws Exception
	 */
	public static String httpGetBuilding(long pageNo,long pageSize,String vilId,String bTypeId,String bType) throws Exception{
		String ts=getTs();
		Map<String,Object> params = new LinkedHashMap<String,Object>();
		
		params.put("ts",ts);
		params.put("sign",Md5Sign(SystemConfig.appID,ts));
		params.put("appID", SystemConfig.appID);
		params.put("token",PropertyUtils.getProperty("accessToken"));
		
		params.put("pageIndex",pageNo);
		params.put("pageSize",pageSize);
		
		if(StringUtils.isNotEmpty(vilId))params.put("communityUuid",vilId);
				
		if(StringUtils.isNotEmpty(bTypeId))params.put("buildingTypeId",bTypeId);
		
		if(StringUtils.isNotEmpty(bType))params.put("buildingType",bType);
		
		params.put("status",0);
		
		String respMsg=HttpClientUtils.httpGet(SystemConfig.COLOURLIFESERVER_BUILDING,params);
		
		if(StringUtils.isEmpty(respMsg)){
			return "";
		}
		return respMsg;	
	}
	
	
	/**@Copyright CHJ
	 * @Author HUANGP
	 * @Date 2018年4月24日
	 * @Desc 根据房间号获取房间
	 *
	 * @param houseUuid
	 * @return
	 * @throws Exception
	 */
	public static String httpGetHouse(String houseUuid) throws Exception{
		String ts=getTs();
		Map<String,Object> params = new LinkedHashMap<String,Object>();
		
		params.put("ts",ts);
		params.put("sign",Md5Sign(SystemConfig.appID,ts));
		params.put("appID", SystemConfig.appID);
		params.put("token",PropertyUtils.getProperty("accessToken"));
		params.put("houseUuid",houseUuid);
		
		String respMsg=HttpClientUtils.httpGet(SystemConfig.COLOURLIFESERVER_QUERYHOUSE,params);
		
		if(StringUtils.isEmpty(respMsg)){
			return "";
		}
		return respMsg;	
	}
	


	
	/**@Copyright CHJ
	 * @Author HUANGP
	 * @Date 2018年4月23日
	 * @Desc 查询业主[小区结构]是否属于该结构下
	 *
	 * @param pageNo
	 * @param pageSize
	 * @param struId  结构ID
	 * @param struType  数据类型(0:房屋,1:单元,2:楼栋,3:小区)
	 * @param userId  住户ID
	 * @param type  绑定关系的类型(0:住户[resident],1:组织架构[org])
	 * @return
	 * @throws Exception
	 */
	public static String httpOwnerIsBelongTheStructure(int pageNo,int pageSize,String struId,String struType,String userId,String rtype) throws Exception{
		String ts=getTs();
		Map<String,Object> params = new TreeMap<String,Object>();
		
		params.put("resourceType",struType);
		
		params.put("pageIndex",pageNo);
		
		params.put("pageSize",pageSize);
		
		if(StringUtils.isNotEmpty(rtype))params.put("relationType",rtype);
		
		if(StringUtils.isNotEmpty(struId))params.put("ownerId",struId);
		
		if(StringUtils.isNotEmpty(userId))params.put("relationId",userId);
		
		params.put("token",PropertyUtils.getProperty("accessToken"));
		params.put("sign",Md5Sign(SystemConfig.appID,ts));
		params.put("ts",ts);
		params.put("appID", SystemConfig.appID);
		String respMsg=HttpClientUtils.httpGet(SystemConfig.COLOURLIFESERVER_ISBELONG,params);
		if(StringUtils.isEmpty(respMsg)){
			return "";
		}
		return respMsg;	
	}



	/**@Copyright CHJ
	 * @Author HUANGP
	 * @Date 2018年4月24日
	 * @Desc 获取小区住户
	 *
	 * @param vilId         小区ID
	 * @param number        数量
	 * @return
	 * @throws Exception
	 */
	public static String httpVillageUser(String vilId,String number) throws Exception{
		String ts=getTs();
		Map<String,Object> params = new TreeMap<String,Object>();
		
		if(StringUtils.isNotEmpty(vilId))params.put("communityUuid",vilId);
		
		if(StringUtils.isNotEmpty(number))params.put("number",number);
		
		params.put("token",PropertyUtils.getProperty("accessToken"));
		params.put("sign",Md5Sign(SystemConfig.appID,ts));
		params.put("ts",ts);
		params.put("appID", SystemConfig.appID);
		String respMsg=HttpClientUtils.httpGet(SystemConfig.COLOURLIFESERVER_USER,params);
		if(StringUtils.isEmpty(respMsg)){
			return "";
		}
		return respMsg;	
	}
	
	/**@Copyright CHJ
	 * @Author HUANGP
	 * @Date 2018年4月24日
	 * @Desc 根据条件查询住户
	 *
	 * @param pageNo
	 * @param pageSize
	 * @param name 名字
	 * @param idNumber 证件号码
	 * @param mobile 手机号
	 * @param telephone 固定电话
	 * @param email 邮箱
	 * @param type 身份,0户主，1租客，2家庭成员, 3历史租客
	 * @param status 状态，0正常，1禁用
	 * @return
	 * @throws Exception
	 */
	public static String httpVillageUserQuery(int pageNo,int pageSize,String name,String idNumber,String mobile,
			String telephone,String email,String type,String status) throws Exception{
		String ts=getTs();
		Map<String,Object> params = new TreeMap<String,Object>();
		
		params.put("pageIndex",pageNo);
		params.put("pageSize",pageSize);
		
		if(StringUtils.isNotEmpty(name))params.put("name",name);
		if(StringUtils.isNotEmpty(idNumber))params.put("idNumber",idNumber);
		if(StringUtils.isNotEmpty(mobile))params.put("mobile",mobile);
		if(StringUtils.isNotEmpty(telephone))params.put("telephone",telephone);
		if(StringUtils.isNotEmpty(email))params.put("email",email);
		if(StringUtils.isNotEmpty(type))params.put("type",type);
		if(StringUtils.isNotEmpty(status))params.put("status",status);
		
		params.put("token",PropertyUtils.getProperty("accessToken"));
		params.put("sign",Md5Sign(SystemConfig.appID,ts));
		params.put("ts",ts);
		params.put("appID", SystemConfig.appID);
		String respMsg=HttpClientUtils.httpGet(SystemConfig.COLOURLIFESERVER_USER_QUERY,params);
		if(StringUtils.isEmpty(respMsg)){
			return "";
		}
		return respMsg;	
	}
}
