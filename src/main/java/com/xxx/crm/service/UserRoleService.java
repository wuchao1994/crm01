package com.xxx.crm.service;

import com.xxx.crm.base.BaseService;
import com.xxx.crm.bean.UserRole;
import com.xxx.crm.mapper.UserRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserRoleService extends BaseService<UserRole,Integer> {
    @Autowired(required = false)
    private UserRoleMapper userRoleMapper;
}
