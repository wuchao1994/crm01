package com.xxx.crm.mapper;

import com.xxx.crm.base.BaseMapper;
import com.xxx.crm.bean.SaleChance;

import java.util.List;
import java.util.Map;

public interface SaleChanceMapper extends BaseMapper<SaleChance,Integer> {
    //批量删除或者单删
    Integer deleteSaleChanceBatch(Integer[] ids);
    //    连表查询所有的角色
    List<Map<String,Object>> querySalePersons();
}