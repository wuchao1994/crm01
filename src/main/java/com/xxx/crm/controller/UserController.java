package com.xxx.crm.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxx.crm.base.BaseController;
import com.xxx.crm.base.ResultInfo;
import com.xxx.crm.bean.User;
import com.xxx.crm.exceptions.ParamsException;
import com.xxx.crm.model.UserModel;
import com.xxx.crm.query.UserQuery;
import com.xxx.crm.service.UserService;
import com.xxx.crm.utils.CookieUtil;
import com.xxx.crm.utils.LoginUserUtil;
import com.xxx.crm.utils.UserIDBase64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("user")
public class UserController extends BaseController {


    @Autowired
    private UserService userService;

    //    登录
    @RequestMapping("login")
    @ResponseBody
    public ResultInfo login(String userName,String userPwd){
//        有初始值，为登录成功
        ResultInfo resultInfo = success();
        UserModel userModel=null;
        try{
            userModel = userService.loginCheck(userName, userPwd);
        }catch (ParamsException pe){
            resultInfo.setCode(pe.getCode());
            resultInfo.setMsg(pe.getMsg());
        }catch (Exception e){
            resultInfo.setCode(300);
            resultInfo.setMsg(e.getMessage());
            e.printStackTrace();
        }
        resultInfo.setResult(userModel);
        return  resultInfo;
    }

    @RequestMapping("toPasswordPage")
    public String updateOperate(){
        return "user/password";
    }
//    无全局异常设置时从此方法捕捉异常
/*    @RequestMapping("user/confirm")
    @ResponseBody
    public ResultInfo updatePwd(HttpServletRequest request,String oldPwd,String newPwd,String confirmPwd){

        String userId = CookieUtil.getCookieValue(request, "userIdStr");
        ResultInfo resultInfo = success();
        Integer id = UserIDBase64.decoderUserID(userId);
        UserModel userModel=null;
        try {
            userModel = userService.updatePwdCheck(id, oldPwd, newPwd, confirmPwd);
        }catch (ParamsException pe){
            resultInfo.setMsg(pe.getMsg());
            resultInfo.setCode(pe.getCode());
        }catch (Exception e){
            resultInfo.setCode(500);
            resultInfo.setMsg(e.getMessage());
            e.printStackTrace();
        }
        resultInfo.setResult(userModel);
        return  resultInfo;
    }*/
//测试全局异常
    @RequestMapping("confirm")
    @ResponseBody
    public ResultInfo updatePwd(HttpServletRequest request,String oldPwd,String newPwd,String confirmPwd){

        String userId = CookieUtil.getCookieValue(request, "userIdStr");
        ResultInfo resultInfo = success();
        Integer id = UserIDBase64.decoderUserID(userId);
        UserModel userModel= userService.updatePwdCheck(id, oldPwd, newPwd, confirmPwd);
        resultInfo.setResult(userModel);
        return  resultInfo;
    }
//    用户管理模块
    @RequestMapping("index")
    public String userPart(){
        return "user/user";
    }

//
    @RequestMapping("list")
    @ResponseBody
    public Map<String, Object> userPage(UserQuery userQuery){
        PageHelper.startPage(userQuery.getPage(),userQuery.getLimit());
        List<User> users = userService.pageInfo(userQuery);
        PageInfo<User> plist = new PageInfo<>(users);
        Map<String,Object> map = new HashMap<>();
        map.put("code",0);
        map.put("msg","");
        map.put("count",plist.getTotal());
        map.put("data",plist.getList());
        return map;
    }

    @RequestMapping("addOrUpdatePage")
    public String addOrUpdate(Integer id, Model model){
        if(id!=null){
            User user = userService.selectByPrimaryKey(id);
            model.addAttribute("user",user);
        }
        return "user/add_update";
    }

    @RequestMapping("save")
    @ResponseBody
    public ResultInfo save(User user){
        System.out.println(user);
        userService.addUser(user);
        return success("用户信息添加成功");
    }

    @RequestMapping("update")
    @ResponseBody
    public ResultInfo update(User user){
        System.out.println(user);
        userService.updateUser(user);
        return success("用户信息修改成功");
    }

    @RequestMapping("delete")
    @ResponseBody
    public ResultInfo dels(Integer[] ids){
        System.out.println(ids);
        userService.deleteUserBatch(ids);
        return success("用户信息修改成功");
    }

//用户基本信息设置跳转
    @RequestMapping("toSettingPage")
    public String toSettingPage(HttpServletRequest req){
        int userId = LoginUserUtil.releaseUserIdFromCookie(req);
        User user = userService.selectByPrimaryKey(userId);
        req.setAttribute("user",user);
        return "user/setting";
    }


}
