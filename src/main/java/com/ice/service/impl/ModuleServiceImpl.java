package com.ice.service.impl;

import com.ice.dao.ModuleMapper;
import com.ice.service.ModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service("moduleService")
@Transactional(rollbackFor = Exception.class)
public class ModuleServiceImpl implements ModuleService {

    @Autowired
    private ModuleMapper moduleMapper;

    @Override
    public List<Map<String, Object>> selectModule() {
        return moduleMapper.selectModule();
    }


    @Override
    public void moduleInsert(String mName) throws Exception {
        moduleMapper.moduleInsert(mName);
        moduleMapper.moduleInsert(mName+"01");
    }
}