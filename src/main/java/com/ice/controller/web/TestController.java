package com.ice.controller.web;



import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ice.core.version.ApiVersion;
import com.ice.service.ModuleService;
import com.ice.utils.PropertyUtils;

@Controller
@RequestMapping("/{version}/test")
public class TestController {

	@Autowired
	private ModuleService moduleService;

	@RequestMapping("/initSession")
	public @ResponseBody Object initSession(HttpSession session){
		return session.getId();
	}

	/**@Copyright CHJ
	 * @Author HUANGP
	 * @Date 2018年4月18日
	 * @Desc
	 *
	 * @param session
	 * @return
	 */
	@ApiVersion(1.1)
	@RequestMapping("/aa")
	public @ResponseBody Object test01(HttpSession session){
		List<Map<String,Object>>  mapList=(List<Map<String,Object>>)session.getAttribute("moduleList");
		if(mapList==null){
			System.out.println("=====1.1========read db==============");
			mapList=moduleService.selectModule();
			Map<String,Object> maps=new HashMap<String,Object>();
			maps.put("version",1.1);
			mapList.add(maps);
			session.setAttribute("moduleList",mapList);
		}else{
			System.out.println("<<<<<<<1.1<<<<<read session>>>>>>>>>>");
		}
		return mapList;
	}
	
	@ApiVersion(1.2)
	@RequestMapping("/aa")
	public @ResponseBody Object test02(HttpSession session){
		List<Map<String,Object>>  mapList=(List<Map<String,Object>>)session.getAttribute("moduleList");
		if(mapList==null){
			System.out.println("=======1.2======read db==============");
			mapList=moduleService.selectModule();
			Map<String,Object> maps=new HashMap<String,Object>();
			maps.put("version",1.2);
			mapList.add(maps);
			session.setAttribute("moduleList",mapList);
		}else{
			System.out.println("<<<<<<1.2<<<<<<read session>>>>>>>>>>");
		}
		return mapList;
	}


	/**@Copyright CHJ
	 * @Author HUANGP
	 * @Date 2018年4月18日
	 * @Desc
	 *
	 * @param mName
	 * @return
	 */
	@RequestMapping("/aaInsert")
	public @ResponseBody Object testInsert(@RequestParam("mName") String mName,HttpServletRequest request){
		try {
			moduleService.moduleInsert(mName);
			return PropertyUtils.getProperty("accessToken");
		}catch (Exception e){
			e.printStackTrace();
			return "fail";
		}
	}

	@RequestMapping("/toHtml")
	public String testHTML(HttpServletRequest request) {
		request.setAttribute("key", "hello world");
		return "test";
	}


}
