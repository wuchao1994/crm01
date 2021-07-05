package com.xxx.crm.service;

import com.xxx.crm.base.BaseService;
import com.xxx.crm.bean.Module;

import com.xxx.crm.dto.TreeDto;
import com.xxx.crm.mapper.ModuleMapper;
import com.xxx.crm.mapper.PermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ModuleService extends BaseService<Module,Integer> {

    @Autowired(required = false)
    private ModuleMapper moduleMapper;

    @Autowired(required = false)
    private PermissionMapper permissionMapper;
//根据角色Id找出permission表中所有角色信息，与对应资源匹配上的，将资源的checked属性改为true
    public List<TreeDto> queryAllModules(Integer roleId){
        List<TreeDto> treeDtos = moduleMapper.queryAllModules();
        List<Integer> mids = permissionMapper.selectMidsByRoleId(roleId);
        for (TreeDto treeDto: treeDtos){
            if (mids.contains(treeDto.getId())){
                treeDto.setChecked(true);
            }
        }
        return treeDtos;
    }
//查询所有module返回给table.render
    public Map<String,Object> moduleList(){
        Map<String,Object> result = new HashMap<String,Object>();
        List<Module> modules =moduleMapper.queryModules();
        result.put("count",modules.size());
        result.put("data",modules);
        result.put("code",0);
        result.put("msg","");
        return result;
    }
}
