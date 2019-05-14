package cn.com.imaginary.ms.apigateway.util;

import cn.com.imaginary.ms.apigateway.common.enums.HttpCode;
import cn.com.imaginary.ms.apigateway.model.ReturnData;
import com.alibaba.fastjson.JSONObject;
import org.springframework.util.StringUtils;

/**
 * @author : Imaginary
 * @version : V1.0
 * @date : 2018/9/6 21:53
 */
public class ReturnDataUtil<T> {

    private static final JSONObject defaultData = new JSONObject();

    public static boolean isSuccess(ReturnData returnData) {
        return returnData != null && HttpCode.OK.getCode() == returnData.getCode();
    }

    public static ReturnData getReturn(int code, String msg) {
        return getReturn(code, msg, defaultData);
    }

    public static ReturnData getReturn(HttpCode httpCode) {
        return getReturn(httpCode.getCode(), httpCode.getDesc(), defaultData);
    }

    public static <T> ReturnData getReturn(int code, String msg, T data) {
        ReturnData returnData = new ReturnData();
        returnData.setCode(code);
        returnData.setMessage(msg);
        returnData.setData(data);
        return returnData;
    }

    public static <T> ReturnData getSuccussReturn(String name, T data) {
        ReturnData returnData = new ReturnData();
        if (data != null && !StringUtils.isEmpty(name)) {
            JSONObject result = new JSONObject();
            result.put(name, data);
            returnData.setData(result);
        } else {
            returnData.setData(data == null ? defaultData : data);
        }
        return returnData;
    }

    public static <T> ReturnData getSuccussReturn(T data) {
        ReturnData returnData = getSuccussReturn();
        if (data != null) {
            returnData.setData(data);
        } else {
            returnData.setData(defaultData);
        }
        return returnData;
    }

    public static ReturnData getSuccussReturn() {
        return getReturn(HttpCode.OK.getCode(), "success");
    }


    public static ReturnData getErrorReturn(String msg) {
        return getReturn(HttpCode.INVALID_ARGUMENT.getCode(), msg);
    }


    //... 常用错误类型方法

    public static ReturnData systemError() {
        return getReturn(HttpCode.INTERNAL);
    }

    public static ReturnData systemError(String msg) {
        return StringUtils.isEmpty(msg) ? systemError() : getReturn(HttpCode.INTERNAL.getCode(), msg);
    }

    public static ReturnData invalidArgument() {
        return getReturn(HttpCode.INVALID_ARGUMENT);
    }

    public static ReturnData invalidArgument(String msg) {
        return StringUtils.isEmpty(msg) ? invalidArgument() : getReturn(HttpCode.INVALID_ARGUMENT.getCode(), msg);
    }

    public static ReturnData permissionDeny() {
        return getReturn(HttpCode.PERMISSION_DENIED);
    }

    public static ReturnData permissionDeny(String msg) {
        return StringUtils.isEmpty(msg) ? permissionDeny() : getReturn(HttpCode.PERMISSION_DENIED.getCode(), msg);
    }

    public static ReturnData unAuth() {
        return getReturn(HttpCode.UNAUTHENTICATED);
    }

    public static ReturnData unAuth(String msg) {
        return StringUtils.isEmpty(msg) ? unAuth() : getReturn(HttpCode.UNAUTHENTICATED.getCode(), msg);
    }

    public static ReturnData apiNotImplement() {
        return getReturn(HttpCode.NOT_IMPLEMENTED);
    }

    public static ReturnData apiNotImplement(String msg) {
        return StringUtils.isEmpty(msg) ? apiNotImplement() : getReturn(HttpCode.NOT_IMPLEMENTED.getCode(), msg);
    }


    public static ReturnData networkTimeout() {
        return getReturn(HttpCode.DEADLINE_EXCEEDED);
    }

    public static ReturnData networkTimeout(String msg) {
        return StringUtils.isEmpty(msg) ? networkTimeout() : getReturn(HttpCode.DEADLINE_EXCEEDED.getCode(), msg);
    }


    public static ReturnData resourceExists() {
        return getReturn(HttpCode.ALREADY_EXISTS);
    }

    public static ReturnData resourceExists(String msg) {
        return getReturn(HttpCode.ALREADY_EXISTS.getCode(), msg);
    }

    public static ReturnData resourceExhausted() {
        return getReturn(HttpCode.RESOURCE_EXHAUSTED);
    }

    public static ReturnData resourceExhausted(String msg) {
        return StringUtils.isEmpty(msg) ? resourceExhausted() : getReturn(HttpCode.RESOURCE_EXHAUSTED.getCode(), msg);
    }

    public static ReturnData resourceNotFound() {
        return getReturn(HttpCode.NOT_FOUND);
    }

    public static ReturnData resourceNotFound(String msg) {
        return StringUtils.isEmpty(msg) ? resourceNotFound() : getReturn(HttpCode.NOT_FOUND.getCode(), msg);
    }

    public static ReturnData unavailable() {
        return getReturn(HttpCode.UNAVAILABLE);
    }

    public static ReturnData unavailable(String msg) {
        return StringUtils.isEmpty(msg) ? unavailable() : getReturn(HttpCode.UNAVAILABLE.getCode(), msg);
    }

}
