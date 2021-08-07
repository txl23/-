package com.hdfs.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @program: hdfs
 * @description:
 * @author: 汤僖龙
 * @create: 2021-06-01 18:49
 */

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class User implements Serializable {
    private static final long serialVersionUID = -5111205546365330595L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer uid;
        private String upwd;
        private String uname;
        private String role;
}
