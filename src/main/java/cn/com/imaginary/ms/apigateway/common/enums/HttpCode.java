package cn.com.imaginary.ms.apigateway.common.enums;

/**
 * @author : Imaginary
 * @version : V1.0
 * @date : 2019/2/22 10:08
 */
public enum HttpCode {
    OK(200, "请求成功"),
    INVALID_ARGUMENT(400,"客户端指定了无效参数，检查错误信息和错误详细信息以获取更多信息"),
    UNAUTHENTICATED(401,"由于缺失、无效或过期的OAuth token，请求未通过身份验证"),
    PERMISSION_DENIED(403,"客户端没有足够的权限。这可能是因为OAuth token没有正确的范围，客户端没有权限，还没有为客户端项目启用API"),
    NOT_FOUND(404,"指定资源没有被发现，或者该请求被未公开的原因拒绝，例如白名单"),
    ALREADY_EXISTS(409,"客户端尝试创建的资源已经存在"),
    RESOURCE_EXHAUSTED(429,"资源配额或者达到限制速率"),
    CANCELLED(499,"请求被客户取消"),
    INTERNAL(500,"内部服务器错误。 通常是服务器错误"),
    NOT_IMPLEMENTED(501,"API方法未由服务器实现"),
    UNAVAILABLE(503,"服务不可用，一般是服务器宕机所致"),
    DEADLINE_EXCEEDED(504,"请求超期，如果重复发生，请考虑减少请求的复杂性")
    ;

    private int code;
    private String desc;

    HttpCode(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
