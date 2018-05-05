package com.ice.service;

import java.util.List;
import java.util.Map;


public interface ModuleService {

    List<Map<String,Object>> selectModule();

    void moduleInsert(String mName) throws Exception;


}