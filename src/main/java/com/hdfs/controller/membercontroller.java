package com.hdfs.controller;

import com.hdfs.bean.User;
import com.hdfs.biz.UserBiz;
import com.hdfs.utils.YcConstants;
import com.hdfs.vo.JsonModel;
import com.hdfs.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * @program: hdfs
 * @description:
 * @author: 汤僖龙
 * @create: 2021-06-01 19:16
 */
@RestController
public class membercontroller {
    @Autowired
    private UserBiz userBiz;

    @RequestMapping(value = "/reg", method = {RequestMethod.POST, RequestMethod.GET})
    public JsonModel register(JsonModel jm,UserVO userVO) {
       int i=userBiz.add(userVO);
       if (i!=0){
           jm.setCode(1);
       }else {
           jm.setCode(0);
       }

        return jm;

    }
    @RequestMapping(value = "/isUnamevalid.action",method = {RequestMethod.GET,RequestMethod.POST})
    public JsonModel isunamevalid(JsonModel jm,String uname){
        String regExp="^\\w{6,10}$";
        if (!uname.matches(regExp)){
            jm.setCode(0);
            jm.setMsg("用户名错误");
            return  jm;
        }
        boolean flag= userBiz.isUnaeVaild(uname);
        if (flag){
            jm.setCode(1);
        }else {
            jm.setCode(0);
            jm.setMsg("用户名重名");
        }
        return jm;
    }
    @RequestMapping(value = "back/checkLogin",method = {RequestMethod.GET,RequestMethod.POST})
    public JsonModel checkLoginOp(JsonModel jm,HttpSession session){
        if (session.getAttribute(YcConstants.LOGINUSER)==null){
            jm.setCode(0);
            jm.setMsg("用户没有登录");
        }else {
            jm.setCode(1);
            String uname= (String) session.getAttribute(YcConstants.LOGINUSER);
            jm.setObj(uname);
        }
        return jm;
    }
}
