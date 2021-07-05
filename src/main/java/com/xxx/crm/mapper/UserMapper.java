package com.xxx.crm.mapper;

import com.xxx.crm.base.BaseMapper;
import com.xxx.crm.bean.User;


public interface UserMapper extends BaseMapper<User,Integer> {
//    新增一个根据用户名查询用户
   User queryOneByUserName(String userName);

//批量删除及单删

   public Integer deleteUserBatch(Integer[] ids);

}