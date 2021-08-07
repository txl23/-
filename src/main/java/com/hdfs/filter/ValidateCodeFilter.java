package com.hdfs.filter;

import com.hdfs.auth.DemoAuthenticationFailureHandler;
import com.hdfs.exception.ValidateCodeException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.filter.OncePerRequestFilter;
import sun.misc.Request;

import javax.naming.AuthenticationException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.ValidationException;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @program: hdfs
 * @description:
 * @author: 汤僖龙
 * @create: 2021-06-03 14:44
 */
public class ValidateCodeFilter extends OncePerRequestFilter {
private DemoAuthenticationFailureHandler demoAuthenticationFailureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException,IOException {
        if ("/doLogin.action".equals(httpServletRequest.getRequestURI())&&"post".equalsIgnoreCase(httpServletRequest.getMethod())){
            try {
                volidate(httpServletRequest);
            }catch (ValidateCodeException e){
               demoAuthenticationFailureHandler.onAuthenticationFailure(httpServletRequest,httpServletResponse,e);
               return;
            }
        }
        filterChain.doFilter(httpServletRequest,httpServletResponse);
    }

    private void volidate(HttpServletRequest request) throws ServletRequestBindingException, ValidateCodeException {
        String validateCode= (String) request.getSession().getAttribute("validateCode");
        String imageCode=request.getParameter("imageCode");
        if (imageCode==null||"".equalsIgnoreCase(imageCode)){
            throw new ValidateCodeException("验证码不能为空");
        }
        if (!validateCode.equalsIgnoreCase(imageCode)){
            throw new ValidateCodeException("验证码不匹配");
        }
    }


    public DemoAuthenticationFailureHandler getDemoAuthenticationFailureHandler(){
        return demoAuthenticationFailureHandler;
    }
    public void setDemoAuthenticationFailureHandler(DemoAuthenticationFailureHandler demoAuthenticationFailureHandler){
        this.demoAuthenticationFailureHandler=demoAuthenticationFailureHandler;

    }

}
