package com.xxx.crm.controller;


import com.xxx.crm.base.BaseController;
import com.xxx.crm.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("permission")
public class PermissionController  extends BaseController {
    @Autowired(required = false)
    private PermissionService permissionService;





}
