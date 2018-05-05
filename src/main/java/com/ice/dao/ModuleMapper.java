package com.ice.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository("moduleMapper")
public interface ModuleMapper {
  
    List<Map<String,Object>> selectModule();

    @Insert("insert into test_module(mName) values(#{mName})")
    void moduleInsert(@Param("mName") String mName) throws Exception;
}  