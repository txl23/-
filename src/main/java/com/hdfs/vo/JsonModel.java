package com.hdfs.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

/**
 * @program: testspring
 * @description:
 * @author: 汤僖龙
 * @create: 2021-05-05 15:02
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class JsonModel implements Serializable {
    private static final long serialVersionUID = 5897449533099355749L;
    private Integer code;
    private String msg;
    private Object obj;
    private String url;

}
