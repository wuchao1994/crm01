package com.xxx.crm.mapper;

import com.xxx.crm.base.BaseMapper;
import com.xxx.crm.bean.UserRole;

public interface UserRoleMapper extends BaseMapper<UserRole,Integer> {
    //<!--按userId查询用户的角色数量-->
    int countByUserId(Integer userId);
    //<!--根据userId删除用户与该角色关系-->
    int deleteByUserId(Integer userId);
}