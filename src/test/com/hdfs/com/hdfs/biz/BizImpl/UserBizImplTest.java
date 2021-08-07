package com.hdfs.biz.BizImpl;

import com.hdfs.biz.UserBiz;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class UserBizImplTest {
    @Autowired
    private UserBiz userBiz;
    @Test
    public void add() {
    }

    @Test
    public void isUnaeVaild() {
        Assert.assertTrue(userBiz.isUnaeVaild("12313"));
    }


}