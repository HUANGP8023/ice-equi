package com.ice.core.version;
import java.lang.annotation.Documented;  
import java.lang.annotation.ElementType;  
import java.lang.annotation.Retention;  
import java.lang.annotation.RetentionPolicy;  
import java.lang.annotation.Target;  
  
import org.springframework.web.bind.annotation.Mapping;  
  
/**@Copyright CHJ
 * @Author HUANGP
 * @Date 2018年4月19日
 * @Desc 版本控制注解
 */
@Target({ElementType.METHOD,ElementType.TYPE})  
@Retention(RetentionPolicy.RUNTIME)  
@Documented  
@Mapping  
public @interface ApiVersion {  
  
    /** 
     * 标识版本号 
     * @return 
     */  
    double value();  
}  