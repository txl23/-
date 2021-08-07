package com.hdfs.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @program: hdfs
 * @description:
 * @author: 汤僖龙
 * @create: 2021-06-03 15:32
 */
public class ValidateCodeException extends AuthenticationException {
    public ValidateCodeException(String msg) {
        super(msg);
    }
}
