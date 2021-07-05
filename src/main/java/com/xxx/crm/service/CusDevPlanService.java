package com.xxx.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxx.crm.base.BaseService;
import com.xxx.crm.bean.CusDevPlan;
import com.xxx.crm.mapper.CusDevPlanMapper;
import com.xxx.crm.query.CusDevPlanQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CusDevPlanService  extends BaseService<CusDevPlan,Integer> {
    @Autowired(required = false)
    private CusDevPlanMapper cusDevPlanMapper;

    /**
     * 多条件计划项列表
     * @param cusDevPlanQuery
     * @return
     */
    public Map<String, Object> queryCusDevPlan(CusDevPlanQuery cusDevPlanQuery) {

        //实例化Map
        Map<String, Object> map = new HashMap<>();
        //开始分页
        PageHelper.startPage(cusDevPlanQuery.getPage(), cusDevPlanQuery.getLimit());
        PageInfo<CusDevPlan> pageInfo = new PageInfo(cusDevPlanMapper.selectByParams(cusDevPlanQuery));
        //存储
        map.put("code", 0);
        map.put("msg", "success");
        map.put("count", pageInfo.getTotal());
        map.put("data", pageInfo.getList());
        //返回map
        return map;
    }
}
