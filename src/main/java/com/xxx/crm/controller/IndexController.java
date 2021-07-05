package com.xxx.crm.controller;

import com.xxx.crm.base.BaseController;
import com.xxx.crm.service.PermissionService;
import com.xxx.crm.service.UserService;
import com.xxx.crm.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.print.DocFlavor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class IndexController extends BaseController {
    @Autowired
    private UserService userService;
    @Autowired
    private PermissionService permissionService;

    //    跳转登录界面
    @RequestMapping("index")
    public String showIndex() {
        return "index";
    }

    //    跳转欢迎界面
    @RequestMapping("welcome")
    public String showWelcome() {
        return "welcome";
    }

    //    跳转到主页面
    @RequestMapping("main")
    public String showMain(HttpServletRequest request) {

        Integer userId = LoginUserUtil.releaseUserIdFromCookie(request);

        request.setAttribute("user",userService.selectByPrimaryKey(userId));


        List<String> permissions=  permissionService.queryUserPermissionsByUserId(userId);

        request.getSession().setAttribute("permissions",permissions);
        System.out.println(permissions);
        return "main";
    }
}
