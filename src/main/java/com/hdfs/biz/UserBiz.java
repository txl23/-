package com.hdfs.biz;

import com.hdfs.bean.User;
import com.hdfs.vo.UserVO;

/**
 * @program: hdfs
 * @description:
 * @author: 作者
 * @create: 2021-06-01 19:03
 */
public interface UserBiz {
    public  int add(UserVO user);
    public boolean isUnaeVaild(String uname);
}
