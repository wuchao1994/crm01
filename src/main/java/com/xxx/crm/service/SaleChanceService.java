package com.xxx.crm.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xxx.crm.base.BaseService;
import com.xxx.crm.bean.SaleChance;
import com.xxx.crm.mapper.SaleChanceMapper;
import com.xxx.crm.query.SaleChanceQuery;
import com.xxx.crm.utils.AssertUtil;
import com.xxx.crm.utils.PhoneUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SaleChanceService extends BaseService<SaleChance, Integer> {

    @Autowired(required = false)
    private SaleChanceMapper saleChanceMapper;

    public Map<String, Object> pageInfo(SaleChanceQuery saleChanceQuery) {
//      测试代码
//       saleChanceMapper.selectByParams(saleChanceQuery).stream().forEach(System.out::println);
//        System.out.println( saleChanceMapper.selectByParams(saleChanceQuery));
//      分页初始化
        PageHelper.startPage(saleChanceQuery.getPage(), saleChanceQuery.getLimit());
//        分页查询
        List<SaleChance> saleChances = saleChanceMapper.selectByParams(saleChanceQuery);
        PageInfo<SaleChance> plist = new PageInfo<>(saleChances);
        Map<String, Object> map = new HashMap<>();
        map.put("code", 0);
        map.put("msg", "");
//        前端主要要一下信息,这四个信息为LayUI框架数据表格格式所需要
        map.put("count", plist.getTotal());
        map.put("data", plist.getList());

        return map;


    }
//添加操作
    public void saveSaleCheck(SaleChance saleChance){
          /*  * 营销机会数据添加
                *   1.参数校验
                *      customerName:非空
                *      linkMan:非空
                *      linkPhone:非空 11位手机号
                *
                *   2.设置相关参数默认值
                *      state:默认未分配  如果选择分配人  state 为已分配
     *      assignTime:如果  如果选择分配人   时间为当前系统时间
                *      devResult:默认未开发 如果选择分配人devResult为开发中 0-未开发 1-开发中 2-开发成功 3-开发失败
                *      isValid:默认有效数据(1-有效  0-无效)
                *      createDate updateDate:默认当前系统时间
                *   3.执行添加 判断结果*/
        paramCheck(saleChance.getCustomerName(), saleChance.getLinkMan(), saleChance.getLinkPhone());
        if(saleChance.getAssignMan()==null){
            saleChance.setState(0);
            saleChance.setDevResult(0);
            saleChance.setAssignTime(null);
            System.out.println("0000《《");
        }else {
            saleChance.setState(1);
            saleChance.setDevResult(1);
            saleChance.setAssignTime(new Date());
            System.out.println("1111<<");
        }
        saleChance.setCreateDate(new Date());
        saleChance.setUpdateDate(new Date());
        //添加是否成功
        AssertUtil.isTrue(saleChanceMapper.insertSelective(saleChance)<1,"添加失败");
    }

    public void updateSaleCheck(SaleChance saleChance) {
//        营销机会数据添加
//                * 1.参数校验
//                * customerName:非空
//                * linkMan:非空
//                * linkPhone:非空 11位手机号
        paramCheck(saleChance.getCustomerName(), saleChance.getLinkMan(), saleChance.getLinkPhone());
//                * 2.设置相关参数默认值
//                * state:默认未分配 如果选择分配人 state 为已分配
//                * assignTime: 如果选择分配人 时间为当前系统时间
//                * devResult:默认未开发 如果选择分配人
//                  devResult为开发中 0-未开发 1-开发中 2-开发成功 3-开发失败
//                * isValid:默认有效数据(1-有效 0-无效)
//                * createDate updateDate:默认当前系统时间
        SaleChance temp = saleChanceMapper.selectByPrimaryKey(saleChance.getId());
        AssertUtil.isTrue(temp == null, "待更新客户信息不存在");
        if (StringUtils.isBlank(temp.getAssignMan()) && StringUtils.isNotBlank(saleChance.getAssignMan())) {
            saleChance.setState(1);
            saleChance.setDevResult(1);
            saleChance.setAssignTime(new Date());
        }else if(StringUtils.isBlank(saleChance.getAssignMan()) && StringUtils.isNotBlank(temp.getAssignMan())){
            saleChance.setState(0);
            saleChance.setDevResult(0);
            saleChance.setAssignTime(null);
            saleChance.setAssignMan("");
        }
        saleChance.setUpdateDate(new Date());
        AssertUtil.isTrue(saleChanceMapper.updateByPrimaryKeySelective(saleChance)<1,"销售机会修改失败");
    }

//                * 3.执行添加 判断结果


    public void paramCheck(String customerName, String linkMan, String linkPhone) {
        AssertUtil.isTrue(StringUtils.isBlank(customerName), "客户名不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(linkMan), "联系人不能为空");
        AssertUtil.isTrue(StringUtils.isBlank(linkPhone), "联系电话不能为空");
        AssertUtil.isTrue(!PhoneUtil.isMobile(linkPhone), "请输入合法的手机号");
    }

//    批量删除及单删操作
    public void deleteSaleChanceBatch(Integer[] ids){
        if (ids!=null&&ids.length>0){
            AssertUtil.isTrue(saleChanceMapper.deleteSaleChanceBatch(ids)<1,"删除失败");
        }
    }

//    调用查询角色为销售人员的人员信息的方法
    public List<Map<String,Object>> getSaleRolePersons(){
        return  saleChanceMapper.querySalePersons();
    }
}
