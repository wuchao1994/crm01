package com.xxx.crm.mapper;

import com.xxx.crm.base.BaseMapper;
import com.xxx.crm.bean.Module;
import com.xxx.crm.dto.TreeDto;

import java.util.List;

public interface ModuleMapper extends BaseMapper<Module,Integer> {
    List<TreeDto> queryAllModules();
    List<Module> queryModules();
}