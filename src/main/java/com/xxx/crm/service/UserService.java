package com.xxx.crm.service;

import com.xxx.crm.base.BaseService;
import com.xxx.crm.bean.User;
import com.xxx.crm.bean.UserRole;
import com.xxx.crm.mapper.UserMapper;
import com.xxx.crm.mapper.UserRoleMapper;
import com.xxx.crm.model.UserModel;
import com.xxx.crm.query.UserQuery;
import com.xxx.crm.utils.AssertUtil;
import com.xxx.crm.utils.Md5Util;
import com.xxx.crm.utils.PhoneUtil;
import com.xxx.crm.utils.UserIDBase64;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class UserService  extends BaseService<User,Integer> {

    @Autowired(required = false)
    private UserMapper userMapper;


    @Autowired(required = false)
    private UserRoleMapper userRoleMapper;
/*
    @Autowired(required = false)
    private */

/**
 * 用户登录校验
 */
    public UserModel loginCheck(String userName,String userPwd){

        checkLoginParam(userName,userPwd);
        User user = userMapper.queryOneByUserName(userName);
        AssertUtil.isTrue(user==null,"该用户名不存在");

        AssertUtil.isTrue(!user.getUserPwd().equals(Md5Util.encode(userPwd)),"登录密码错误");
//        登录成功，返回用户的信息前端存入cookie
        return buildUserInfo(user);
    }
//创建用户返回信息
    private UserModel buildUserInfo(User user) {
        UserModel userModel = new UserModel();
        userModel.setUserId(UserIDBase64.encoderUserID(user.getId()));
        userModel.setUserName(user.getUserName());
        userModel.setTrueName(user.getTrueName());
        return userModel;
    }

    //登录校验用户名及密码
    private void checkLoginParam(String userName, String userPwd) {
        AssertUtil.isTrue(StringUtils.isBlank(userName),"用户名不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(userPwd),"用户密码不能为空");
    }


//    修改密码校验

    /**
     * ⽤户密码修改
     * 1. 参数校验
     * ⽤户ID：userId ⾮空 ⽤户对象必须存在
     * 原始密码：oldPassword ⾮空 与数据库中密⽂密码保持⼀致
     * 新密码：newPassword ⾮空 与原始密码不能相同
     * 确认密码：confirmPassword ⾮空 与新密码保持⼀致
     * 2. 设置⽤户新密码
     * 新密码进⾏加密处理
     * 3. 执⾏更新操作
     * 受影响的⾏数⼩于1，则表示修改失败
     *
     * 注：在对应的更新⽅法上，添加事务控制
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public UserModel updatePwdCheck(Integer id,String oldPwd,String newPwd,String confirmPwd){
        return  paramCheck(id,oldPwd,newPwd,confirmPwd);
    }

    private UserModel paramCheck(Integer id, String oldPwd, String newPwd, String confirmPwd) {
        User user = userMapper.selectByPrimaryKey(id);
        AssertUtil.isTrue(user==null,"用户不存在");
        AssertUtil.isTrue(StringUtils.isBlank(oldPwd),"原密码不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(newPwd),"新密码不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(confirmPwd),"确认密码不能为空");
        String userPwd = user.getUserPwd();
        AssertUtil.isTrue(!Md5Util.encode(oldPwd).equals(userPwd),"原密码有误，请重新输入");
        AssertUtil.isTrue(oldPwd.equals(newPwd),"新密码不能与原密码相同");
        AssertUtil.isTrue(!confirmPwd.equals(newPwd),"确认密码需与新密码一致");
//        新密码修改操作
        User temp = new User();
        temp.setId(id);
        temp.setUserPwd(Md5Util.encode(newPwd));

        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(temp)<1,"修改失败");

        UserModel userModel = new UserModel();
        userModel.setTrueName(user.getTrueName());
        userModel.setUserName(user.getUserName());
        userModel.setUserId(UserIDBase64.encoderUserID(user.getId()));
        return userModel;
    }


//    用户管理模块
//    分页查询
    public List<User> pageInfo(UserQuery userQuery){
        return userMapper.selectByParams(userQuery);
    }

//    添加用户
    /**
     * 添加⽤户
     * 1. 参数校验
     * ⽤户名 ⾮空 唯⼀性
     * 邮箱 ⾮空
     * ⼿机号 ⾮空 格式合法
     * 2. 设置默认参数
     * isValid 1
     * creteDate 当前时间
     * updateDate 当前时间
     * userPwd 123456 -> md5加密
     * 3. 执⾏添加，判断结果
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public void addUser(User user){
        checkParam(user.getUserName(),user.getEmail(),user.getPhone());
        user.setIsValid(1);
        user.setCreateDate(new Date());
        user.setUpdateDate(new Date());
        //密码加密,默认密码
        user.setUserPwd(Md5Util.encode("123456"));
        AssertUtil.isTrue(userMapper.insertSelective(user)<1,"添加失败");
//        用户角色表上添加角色
        System.out.println(user.getId()+"<<");
        relationUserRole(user.getId(), user.getRoleIds());

    }

    public void relationUserRole(Integer userId,String roleIds){
        /**
         * ⽤户⻆⾊分配
         * 原始⻆⾊不存在 添加新的⻆⾊记录
         * 原始⻆⾊存在 添加新的⻆⾊记录
         * 原始⻆⾊存在 清空所有⻆⾊
         * 原始⻆⾊存在 移除部分⻆⾊
         * 如何进⾏⻆⾊分配???
         * 如果⽤户原始⻆⾊存在 ⾸先清空原始所有⻆⾊ 添加新的⻆⾊记录到⽤户⻆⾊表
         */
        int count = userRoleMapper.countByUserId(userId);
        if(count>0){
            AssertUtil.isTrue(userRoleMapper.deleteByUserId(userId)<count,"⽤户⻆⾊分配失败!");
        }
        if(StringUtils.isNotBlank(roleIds)){
            List<UserRole> list = new ArrayList<>();
            for (String roleId: roleIds.split(",")) {
                UserRole userRole = new UserRole();
                userRole.setUserId(userId);
                userRole.setRoleId(Integer.parseInt(roleId));
                userRole.setCreateDate(new Date());
                userRole.setUpdateDate(new Date());
                list.add(userRole);
            }
            AssertUtil.isTrue(userRoleMapper.insertBatch(list)<list.size(),"⽤户⻆⾊分配失败!");;
        }


    }

//    校验参数
    public void checkParam(String userName,String email,String phone){
        AssertUtil.isTrue(StringUtils.isBlank(userName),"用户名不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(email),"邮箱不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(phone),"手机号不能为空");
        AssertUtil.isTrue(!PhoneUtil.isMobile(phone),"请输入合法的手机号");
    }


    /**
     * 更新⽤户
     * 1. 参数校验
     * id ⾮空 记录必须存在
     * ⽤户名 ⾮空 唯⼀性
     * email ⾮空
     * ⼿机号 ⾮空 格式合法
     * 2. 设置默认参数
     * updateDate
     * 3. 执⾏更新，判断结果
     * @param user
     */
//更新功能
    public void updateUser(User user){
        Integer id = user.getId();
        User temp = userMapper.selectByPrimaryKey(id);
        AssertUtil.isTrue(temp==null,"待修改用户不存在");
        checkParam(user.getUserName(),user.getEmail(),user.getPhone());
        user.setUpdateDate(new Date());
        AssertUtil.isTrue(userMapper.updateByPrimaryKeySelective(user)<1,"用户信息修改失败");
//        修改用户角色中间表
        Integer userId = userMapper.queryOneByUserName(user.getUserName()).getId();
        relationUserRole(userId,user.getRoleIds());
    }


//    批量删除及单删

    public void deleteUserBatch(Integer[] ids){
        for (Integer userId:ids) {
            int count = userRoleMapper.countByUserId(userId);
            if(count>0){
                AssertUtil.isTrue(userRoleMapper.deleteByUserId(userId)!=count,"用户角色删除失败");
            }
        }
        AssertUtil.isTrue(userMapper.deleteUserBatch(ids)<1,"用户信息删除失败！");
    }



}
