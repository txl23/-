package com.hdfs.biz.BizImpl;

import com.hdfs.bean.User;
import com.hdfs.biz.UserBiz;
import com.hdfs.dao.memberdao;
import com.hdfs.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.data.domain.Example;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * @program: hdfs
 * @description:
 * @author: 汤僖龙
 * @create: 2021-06-01 19:03
 */
@Service
@Transactional
public class UserBizImpl implements UserBiz,UserDetailsService{

    @Autowired
            private memberdao memberdao;
    PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();
    @Override
    public int add(UserVO user) {
        User r = new User();
        r.setUname(user.getUname());
        r.setUpwd(passwordEncoder.encode(user.getUpwd()));
        r.setRole("ROLE_ADIN");
        r= memberdao.save(r);
        return r.getUid();
    }

    @Override
    public boolean isUnaeVaild(String uname) {
        User u= new User();
        u.setUname(uname);
        Example<User> example = Example.of(u);
        Optional<User> optional = memberdao.findOne(example);
        u = optional.orElseGet(new Supplier<User>() {
            @Override
            public User get() {
                return null;
            }
        });
        if(u==null){
            return true;
        }
        return false;

    }


    @Override
    public UserDetails loadUserByUsername(String uname) throws UsernameNotFoundException {
        User u=new User();
        u.setUname(uname);
        Example<User>example=Example.of(u);
        Optional<User>optional=memberdao.findOne(example);
        u=optional.orElseGet(new Supplier<User>() {
            @Override
            public User get() {
                return null;
            }
        });
        if (u==null){
            return null;
        }
        Collection<GrantedAuthority>authorities=new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(u.getRole()));
        org.springframework.security.core.userdetails.User user2=new org.springframework.security.core.userdetails.User(u.getUname(),u.getUpwd(),authorities);
        return user2;
    }
}
