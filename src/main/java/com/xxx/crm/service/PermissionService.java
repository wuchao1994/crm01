package com.xxx.crm.service;

import com.xxx.crm.base.BaseService;
import com.xxx.crm.bean.Permission;
import com.xxx.crm.mapper.PermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PermissionService extends BaseService<Permission,Integer> {
    @Autowired(required = false)
    private PermissionMapper permissionMapper;

    public List<String> queryUserPermissionsByUserId(Integer userId){
        return permissionMapper.queryUserPermissionsByUserId(userId);
    }
}
