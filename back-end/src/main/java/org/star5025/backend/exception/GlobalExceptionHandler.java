package org.star5025.backend.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.star5025.backend.result.Result;

import java.sql.SQLIntegrityConstraintViolationException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 全局异常处理
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e) {
        log.error("发生了异常，异常信息：" , e );
//        return Result.error("-1");
        return Result.error(StringUtils.hasLength(e.getMessage()) ? e.getMessage() : "操作失败");
    }

    /**
     * SQL异常处理，现在只有数据库中用户重复的异常处理
     * @param ex
     * @return
     */
    @ExceptionHandler(DuplicateKeyException.class)
    public Result exceptionHandler(SQLIntegrityConstraintViolationException ex) {
        log.error("注册时用户名已被注册");
        String message = ex.getMessage();
        if(message.contains("Duplicate entry")){
            //Duplicate entry 'admin' for key 'user.user_name'\r\n### The error may exist
            String[] split = message.split("'");
            String userName = split[1];
            String msg = userName + "已被注册";
            return Result.error(msg);
        }else{
            return Result.error("未知错误");
        }
    }
}
