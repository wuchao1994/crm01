package com.xxx.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxx.crm.base.BaseService;
import com.xxx.crm.bean.Module;
import com.xxx.crm.bean.Permission;
import com.xxx.crm.bean.Role;
import com.xxx.crm.mapper.ModuleMapper;
import com.xxx.crm.mapper.PermissionMapper;
import com.xxx.crm.mapper.RoleMapper;
import com.xxx.crm.query.RoleQuery;
import com.xxx.crm.utils.AssertUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class RoleService extends BaseService<Role, Integer> {
    @Autowired(required = false)
    private RoleMapper roleMapper;
    @Autowired(required = false)
    private PermissionMapper permissionMapper;

    @Autowired(required = false)
    private ModuleMapper moduleMapper;

    public List<Map<String, Object>> getRoles(Integer userId) {
        System.out.println(roleMapper.queryAllRoles(userId));
        return roleMapper.queryAllRoles(userId);
    }
    //    条件查询角色
    public Map<String, Object> queryRoleByParams(RoleQuery roleQuery) {
        Map<String, Object> map = new HashMap<>();
//        开启分页
        PageHelper.startPage(roleQuery.getPage(), roleQuery.getLimit());
        List<Role> roles = roleMapper.selectByParams(roleQuery);
//        获得分页信息对象
        PageInfo plist = new PageInfo(roles);
        map.put("code", 0);
        map.put("msg", "");
        map.put("coount", plist.getTotal());
        map.put("data", plist.getList());
        return map;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void saveRole(Role role) {
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()), "请输⼊⻆⾊名!");
        Role temp = roleMapper.queryRoleByRoleName(role.getRoleName());
        AssertUtil.isTrue(null != temp, "该⻆⾊已存在!");
        role.setIsValid(1);
        role.setCreateDate(new Date());
        role.setUpdateDate(new Date());
        AssertUtil.isTrue(insertSelective(role) < 1, "⻆⾊记录添加失败!");
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateRole(Role role) {
        AssertUtil.isTrue(null == role.getId() || null == selectByPrimaryKey(role.getId()),"待修改的记录不存在!");
        AssertUtil.isTrue(StringUtils.isBlank(role.getRoleName()), "请输⼊⻆⾊名!");
        Role temp = roleMapper.queryRoleByRoleName(role.getRoleName());
        AssertUtil.isTrue(null != temp && !(temp.getId().equals(role.getId())), "该⻆⾊已存在!");
        role.setUpdateDate(new Date());
        AssertUtil.isTrue(updateByPrimaryKeySelective(role) < 1, "⻆⾊记录更新失败!");
    }

    public void deleteRole(Integer roleId) {
        Role temp = selectByPrimaryKey(roleId);
        AssertUtil.isTrue(null == roleId || null == temp, "待删除的记录不存在!");
        temp.setIsValid(0);
        AssertUtil.isTrue(updateByPrimaryKeySelective(temp) < 1, "⻆⾊记录删除失败!");
    }

    //给角色授权
    public void addGrant(Integer roleId, Integer[] mids) {
//        校验
        Role temp =roleMapper.selectByPrimaryKey(roleId);
        AssertUtil.isTrue(null==roleId||null==temp,"待授权的⻆⾊不存在!");
        int count = permissionMapper.countPermissionByRoleId(roleId);
        if(count>0){
            AssertUtil.isTrue(permissionMapper.deletePermissionsByRoleId(roleId)<count,"权限分配失败!");
        }
        if(mids!=null||mids.length!=0){
            List<Permission> permissions = new ArrayList<>();
            for (Integer mid : mids) {
                Permission p = new Permission();
                p.setRoleId(roleId);
                p.setModuleId(mid);
                permissions.add(p);
                p.setCreateDate(new Date());
                p.setUpdateDate(new Date());
                Module module = moduleMapper.selectByPrimaryKey(mid);
                p.setAclValue(module.getOptValue());
            }
            permissionMapper.insertPermissionBatch(permissions);
        }
    }
}
