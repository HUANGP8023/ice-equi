package com.ice.core.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**@Copyright CHJ
 * @Author HUANGP
 * @Date 2018年4月20日
 * @Desc 全局异常处理
 */
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {
    /**
     * 所有异常报错处理
     * @param request
     * @param exception
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value=Exception.class)  
    public String allExceptionHandler(HttpServletRequest request,Exception exception) throws Exception {
        exception.printStackTrace();
        return "服务器异常，请联系管理员！";  
    }  

}