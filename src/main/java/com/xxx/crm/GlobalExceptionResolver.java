package com.xxx.crm;

import com.alibaba.fastjson.JSON;
import com.xxx.crm.base.ResultInfo;
import com.xxx.crm.exceptions.NoLoginException;
import com.xxx.crm.exceptions.ParamsException;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


@Component
public class GlobalExceptionResolver implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handle, Exception e) {
        ModelAndView mav = new ModelAndView();

//        未登陆异常处理
        if(e instanceof NoLoginException){
            mav.setViewName("redirect:/index");
            return mav;
        }

        mav.setViewName("error");
        mav.addObject("code",300);
        mav.addObject("msg","系统异常,请稍后再试！");
        if (handle instanceof HandlerMethod) {
            ResponseBody responseBody = ((HandlerMethod) handle).getMethod().getDeclaredAnnotation(ResponseBody.class);
//          无ResponseBody注解修饰，返回视图
            if (responseBody==null){
                if (e instanceof ParamsException){
                    ParamsException pe = (ParamsException)e;
                    mav.addObject("code",pe.getCode());
                    mav.addObject("msg",pe.getMsg());
                }else {
                    mav.addObject("code",500);
                    mav.addObject("msg",e.getMessage());
                }
                return mav;
            }else {
//                有ResponseBody注解修饰，返回json字符串
                ResultInfo resultInfo =new ResultInfo();
                if (e instanceof ParamsException){
                    ParamsException pe = (ParamsException)e;
                    resultInfo.setCode(pe.getCode());
                    resultInfo.setMsg(pe.getMsg());
                    resultInfo.setResult(pe);
                }else {
                    resultInfo.setCode(500);
                    resultInfo.setMsg(e.getMessage());
                    resultInfo.setResult(e);
                }
//                设置响应头
                response.setContentType("application/json;charset=utf-8");
                PrintWriter out=null;
                try {
                    out = response.getWriter();
                    out.write(JSON.toJSONString(resultInfo));
                    out.flush();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }finally {
                    if (out!=null){
                        out.close();
                    }
                }
                return null;
            }
        }
            return mav;
    }
}
