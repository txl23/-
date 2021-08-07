package com.hdfs.vo;

import lombok.Data;

/**
 * @program: hdfs
 * @description:
 * @author: 汤僖龙
 * @create: 2021-06-02 19:52
 */
@Data
public class UserVO {
    private Integer uid;
    private String uname;
    private String upwd;
    private String role;
    private String imageCod;
}
