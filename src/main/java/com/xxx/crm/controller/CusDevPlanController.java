package com.xxx.crm.controller;


import com.xxx.crm.base.BaseController;
import com.xxx.crm.bean.CusDevPlan;
import com.xxx.crm.bean.SaleChance;
import com.xxx.crm.query.CusDevPlanQuery;
import com.xxx.crm.query.SaleChanceQuery;
import com.xxx.crm.service.CusDevPlanService;
import com.xxx.crm.service.SaleChanceService;
import com.xxx.crm.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;


@Controller
@RequestMapping("cus_dev_plan")
public class CusDevPlanController extends BaseController {
    @Autowired(required = false)
    private CusDevPlanService cusDevPlanService;

    @Autowired(required = false)
    private SaleChanceService saleChanceService;

    @RequestMapping("index")
    public String goCusDevPlan(){
        return "cusDevPlan/cus_dev_plan";
    }

    @RequestMapping("toCusDevPlanDataPage")
    public String toCusDevPlanDataPage(Integer sid, HttpServletRequest req){
        System.out.println("test11111");
        SaleChance saleChance = saleChanceService.selectByPrimaryKey(sid);
        req.setAttribute("saleChance",saleChance);
        System.out.println(saleChance);
        return "cusDevPlan/cus_dev_plan_data";
    }

    @RequestMapping("list")
    @ResponseBody
    public Map<String,Object> sayList(CusDevPlanQuery query){

        return cusDevPlanService.queryCusDevPlan(query);
    }

}
