package com.xxx.crm.mapper;

import com.xxx.crm.base.BaseMapper;
import com.xxx.crm.bean.Permission;

import java.util.List;

public interface PermissionMapper  extends BaseMapper<Permission,Integer> {
    Integer countPermissionByRoleId(Integer roleId);
    Integer deletePermissionsByRoleId(Integer roleId);

    Integer insertPermissionBatch(List<Permission> permissions);

    List<Integer> selectMidsByRoleId(Integer roleId);

    List<String> queryUserPermissionsByUserId(Integer userId);
}