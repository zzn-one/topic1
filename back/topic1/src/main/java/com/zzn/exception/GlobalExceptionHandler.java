package com.zzn.exception;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;

import com.zzn.pojo.Result;
import com.zzn.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;


/**
 * @author zzn
 * @create 2022-11-10 9:31
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /*jwt异常*/
    @ExceptionHandler(TokenExpiredException.class)
    public Result<String> handleTokenExpiredException() {
        return new Result<>(HttpStatus.UNAUTHORIZED.value(), "jwt认证过期", null);
    }

    @ExceptionHandler(SignatureVerificationException.class)
    public Result<String> handleSignatureVerificationException() {

        return new Result<>(HttpStatus.UNAUTHORIZED.value(), null, "jwt的签名被更改");
    }

    @ExceptionHandler(AlgorithmMismatchException.class)
    public Result<String> handleAlgorithmMismatchException() {
        return new Result<>(HttpStatus.UNAUTHORIZED.value(), null, "加密算法不匹配");
    }

    @ExceptionHandler(InvalidClaimException.class)
    public Result<String> handleInvalidClaimException() {
        return new Result<>(HttpStatus.UNAUTHORIZED.value(), null, "无效的jwt");
    }

    /*
     * 文件导入异常
     * */
    @ExceptionHandler(FileDataTitleFormatErrorException.class)
    public Result<String> handleFileDataTitleFormatErrorException() {
        return new Result<>(1001, null, "文件数据的标题格式错误！");
    }

    @ExceptionHandler(FileDataBodyFormatErrorException.class)
    public Result<String> handleFileDataBodyFormatErrorException() {
        return new Result<>(1002, null, "文件数据的数据部分格式错误！");
    }


    /*
     * 通用异常
     * */
    @ExceptionHandler(Exception.class)
    public Result<String> handleException(Exception e) {
        return new Result<>(403, null, e.getMessage());

    }


}
