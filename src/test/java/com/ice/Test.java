package com.ice;

import java.util.Map;
import java.util.TreeMap;

import com.ice.common.SystemConfig;
import com.ice.utils.DateUtils;
import com.ice.utils.FastJSONUtil;
import com.ice.utils.HttpClientUtils;
import com.ice.utils.MD5Util;

public class Test {
	
//			sign=md5(appID + ts + token + false)
//			signature=md5(appID+ts+Token)

	public static void main(String[] args) throws Exception {

//		Map<String, String> params = new TreeMap<String, String>();
//		String ts = String.valueOf(System.currentTimeMillis());
//		ts=ts.substring(0, ts.length()-3);
//		params.put("corp_uuid", "a8c58297436f433787725a94f780a3c9");
//		params.put("app_uuid", String.valueOf(SystemConfig.appID));
//		params.put("signature",MD5Util.md5Encode(SystemConfig.appID + ts + SystemConfig.token, "utf-8"));
//		params.put("timestamp",ts);
//		params.put("sign",MD5Util.md5Encode(SystemConfig.appID + ts + SystemConfig.token+"false", "utf-8"));
//		params.put("ts", String.valueOf(ts));
//		params.put("appID", SystemConfig.appID);

//		System.out.println(HttpClientUtils.httpPost(SystemConfig.COLOURLIFESERVER_AUTH,params));
		
//		System.out.println("appID:" + SystemConfig.appID);
//		System.out.println("token:" + SystemConfig.token);
//		System.out.println("ts:" + ts);
//		System.out.println("sign:" + MD5Util.md5Encode(SystemConfig.appID + ts + SystemConfig.token, "utf-8"));
//		//{"code":0,"message":"","content":{"expireTime":1524745112664,"accessToken":"33c042ecf9a943de938e265766c80991","corpUuid":"a8c58297436f433787725a94f780a3c9","appUuid":"ICECHJMJ-3876-S26N-GCI3-GT9UOIYH3QFA","serviceUuid":""}}
//		
//		long str=Long.parseLong("1524745112664");
//		System.out.println(DateUtils.timestampToDateStr(str, false));
		
//		String str="{\"code\":0,\"message\":\"\",\"content\":{\"expireTime\":1524745112664,\"accessToken\":\"33c042ecf9a943de938e265766c80991\",\"corpUuid\":\"a8c58297436f433787725a94f780a3c9\",\"appUuid\":\"ICECHJMJ-3876-S26N-GCI3-GT9UOIYH3QFA\",\"serviceUuid\":\"\"}}";
//		String content=FastJSONUtil.parseJsonByKey("content",str);
//		
//
//		System.out.println(FastJSONUtil.parseJsonByKey("expireTime",content));
//		System.out.println(FastJSONUtil.parseJsonByKey("accessToken",content));
		
		
		System.out.println(System.currentTimeMillis()+20000);
	}

}
