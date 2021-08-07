package com.hdfs.dao;

import com.hdfs.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @program: hdfs
 * @description:
 * @author: 作者
 * @create: 2021-06-01 18:31
 */
public interface memberdao extends JpaRepository<User,Integer> {
}
