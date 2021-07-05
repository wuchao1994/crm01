package com.xxx.crm.controller;


import com.xxx.crm.annotation.RequirePermission;
import com.xxx.crm.base.BaseController;

import com.xxx.crm.base.ResultInfo;
import com.xxx.crm.bean.SaleChance;
import com.xxx.crm.bean.User;
import com.xxx.crm.query.SaleChanceQuery;
import com.xxx.crm.service.SaleChanceService;
import com.xxx.crm.service.UserService;
import com.xxx.crm.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
public class SaleChanceController extends BaseController {

    @Resource(name="saleChanceService")
    private SaleChanceService saleChanceService;

    @Autowired
    private UserService userService;


    @RequestMapping("sale_chance/index")
    public String pageQuery(){
        return "saleChance/sale_chance";
    }



    @RequirePermission(code = "101001")
    @RequestMapping("sale_chance/list")
    @ResponseBody
    public Map<String,Object> showPage(SaleChanceQuery saleChanceQuery,Integer flag,HttpServletRequest req){
        if(flag!=null&&flag==1){
            Integer userId = LoginUserUtil.releaseUserIdFromCookie(req);
            saleChanceQuery.setAssignMan(userId);
        }
        return saleChanceService.pageInfo(saleChanceQuery);
    }

//    跳到小弹窗
    @RequestMapping("sale_chance/addOrUpdateSaleChancePage")
    public String addAndUpdate(Integer id, Model model){

        //修改和添加主要的区别是表单中是否有ID,有id修改操作，否则添加
        if (id != null) {
            //查询销售机会对象
            SaleChance saleChance = saleChanceService.selectByPrimaryKey(id);
            //存储
            model.addAttribute("saleChance", saleChance);
        }
        return "saleChance/add_update";
    }

//    表单添加操作
    @RequestMapping("sale_chance/save")
    @ResponseBody
    public ResultInfo saveSaleChance(SaleChance saleChance, HttpServletRequest req){
        Integer id = LoginUserUtil.releaseUserIdFromCookie(req);
        User user = userService.selectByPrimaryKey(id);
        saleChance.setCreateMan(user.getTrueName());
        saleChanceService.saveSaleCheck(saleChance);
        return success("营销机会添加成功");
    }

    //    表单修改操作
    @RequestMapping("sale_chance/update")
    @ResponseBody
    public ResultInfo updateSaleChance(SaleChance saleChance){
        System.out.println("1111111111");
        System.out.println(saleChance);
        saleChanceService.updateSaleCheck(saleChance);
        return success("营销机会修改成功");
    }

    //单删以及批量删除
    @RequestMapping("sale_chance/dels")
    @ResponseBody
    public  ResultInfo dels(Integer[] ids){
        saleChanceService.deleteSaleChanceBatch(ids);
        return success("删除成功");
    }

    //    调用查询角色为销售人员的人员信息的方法
    @RequestMapping("sale_chance/querySales")
    @ResponseBody
    public List<Map<String,Object>> getSaleRolePersons(){
        return saleChanceService.getSaleRolePersons();
    }
}
