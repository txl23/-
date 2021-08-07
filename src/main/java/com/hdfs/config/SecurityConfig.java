package com.hdfs.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hdfs.auth.DemoAuthenticationFailureHandler;
import com.hdfs.biz.BizImpl.UserBizImpl;
import com.hdfs.biz.UserBiz;
import com.hdfs.filter.ValidateCodeFilter;
import com.hdfs.utils.YcConstants;
import com.hdfs.vo.JsonModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @program: hdfs
 * @description:
 * @author: 汤僖龙
 * @create: 2021-06-03 13:35
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private UserBizImpl userBiz;
    @Autowired
    private DemoAuthenticationFailureHandler demoAuthenticationFailureHandler;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
      //security提供的加密
       auth.userDetailsService(userBiz).passwordEncoder(new BCryptPasswordEncoder());

    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        ValidateCodeFilter validateCodeFilter=new ValidateCodeFilter();
        validateCodeFilter.setDemoAuthenticationFailureHandler(demoAuthenticationFailureHandler);
        http.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class).formLogin()
                .loginPage("/login.html")
                .loginProcessingUrl("/doLogin.action")
               // .defaultSuccessUrl("/back/index.html")
                .usernameParameter("uname")
                .passwordParameter("upwd")
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
                        String uname=httpServletRequest.getParameter("uname");
                        HttpSession session=httpServletRequest.getSession();
                        session.setAttribute(YcConstants.LOGINUSER,uname);
                        httpServletResponse.sendRedirect("back/index.html");
                    }
                })
                .failureHandler(new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest rq, HttpServletResponse rp, AuthenticationException e) throws IOException, ServletException {
                       e.printStackTrace();
                       rp.setContentType("application/json;charset-utf-8");
                        PrintWriter out=rp.getWriter();
                        out.write("fail");
                        out.flush();
                    }
                })
                .permitAll()
                .and()
                .logout()
                .logoutUrl("/logout.action")
                .logoutSuccessHandler(new LogoutSuccessHandler() {
                    @Override
                    public void onLogoutSuccess(HttpServletRequest rq, HttpServletResponse rp, Authentication authentication) throws IOException, ServletException {
                        rq.getSession().removeAttribute(YcConstants.LOGINUSER);
                        rp.setContentType("application/json;charset-utf-8");
                        PrintWriter out=rp.getWriter();
                        JsonModel jm=new JsonModel();
                        jm.setCode(1);
                        //springMvc自带的json处理工具
                        String json=objectMapper.writeValueAsString(jm);
                        out.write(json);
                        out.flush();
                    }
                })
                .permitAll()
                .and()
                .httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers("/reg.html","/login.html","/verifyCodeServlet","/isUnamevalid.action","/reg").permitAll()
                .antMatchers("/back").hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .csrf().disable();


    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**","/js/**","/reg.html","/pics/**");
    }
}
