package com.xxx.crm.mapper;

import com.xxx.crm.base.BaseMapper;
import com.xxx.crm.bean.Role;

import java.util.List;
import java.util.Map;

public interface RoleMapper extends BaseMapper<Role, Integer> {
    //查询所有角色
    List<Map<String, Object>> queryAllRoles(Integer userId);

    Role queryRoleByRoleName(String roleName);


}

