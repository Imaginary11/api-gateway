package cn.com.imaginary.ms.apigateway.model;

import lombok.Data;

/**
 * 返回实体
 *
 * @author : Imaginary
 * @version : V1.0
 * @date : 2018/8/10 21:53
 */
@Data
public class ReturnData<T> {
    private int code = 200;
    private String message = "success";

    private T data;
}
