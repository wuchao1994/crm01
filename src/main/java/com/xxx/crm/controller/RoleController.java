package com.xxx.crm.controller;

import com.xxx.crm.base.BaseController;
import com.xxx.crm.base.ResultInfo;
import com.xxx.crm.bean.Role;
import com.xxx.crm.query.RoleQuery;
import com.xxx.crm.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("role")
public class RoleController extends BaseController {
    @Autowired(required = false)
    private RoleService roleService;

    @RequestMapping("queryAllRoles")
    @ResponseBody
    public List<Map<String,Object>> queryAllRoles(Integer userId){
        return roleService.getRoles(userId);
    }
//  转发显示角色模块主页面
    @RequestMapping("index")
    public String index(){
        return "role/role";
    }

//  返回分页信息给前端条件查询
    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> queryRolePageInfo(RoleQuery roleQuery){
        return  roleService.queryRoleByParams(roleQuery);
    }

    @RequestMapping("addOrUpdateRolePage")
    public String addUserPage(Integer id, Model model){
        if(null !=id){
        model.addAttribute("role",roleService.selectByPrimaryKey(id));
    }
        return "role/add_update";
    }
    @RequestMapping("save")
    @ResponseBody
    public ResultInfo saveRole(Role role){
        roleService.saveRole(role);
        return success("⻆⾊记录添加成功");
    }
    @RequestMapping("update")
    @ResponseBody
    public ResultInfo updateRole(Role role){
        roleService.updateRole(role);
        return success("⻆⾊记录更新成功");
    }

    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo deleteRole(Integer id){
        roleService.deleteRole(id);
        return success("⻆⾊记录删除成功");
    }

    @RequestMapping("toAddGrantPage")
    public String toAddGrantPage(Integer roleId,Model model){
        model.addAttribute("roleId",roleId);
        System.out.println(roleId);
        return "role/grant";
    }

 /*   @RequestMapping("addGrant")
    @ResponseBody
    public ResultInfo addGrant(Integer[] mids,Integer roleId){
        roleService.addGrant(mids,roleId);
        return success("权限添加成功");
    }
*/
    @RequestMapping("addGrant")
    @ResponseBody
    public ResultInfo addGrant(Integer roleId,Integer[] mids){
        roleService.addGrant(roleId,mids);
        System.out.println("mids="+mids);
        Arrays.stream(mids).forEach(System.out::println);
        return success("角色授权成功");
    }
}
