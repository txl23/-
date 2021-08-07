package com.hdfs.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hdfs.exception.ValidateCodeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

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
 * @create: 2021-06-03 14:45
 */
@Component
public class DemoAuthenticationFailureHandler implements AuthenticationFailureHandler {
   private Logger logger= LoggerFactory.getLogger(DemoAuthenticationFailureHandler.class);
   @Autowired
   private ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        logger.info("登陆失败");
        httpServletResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        PrintWriter out=httpServletResponse.getWriter();
        String jsonResult=objectMapper.writeValueAsString(e.getMessage());
        out.write(jsonResult);
        out.flush();
    }
}
