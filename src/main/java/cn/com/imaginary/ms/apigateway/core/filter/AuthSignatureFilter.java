package cn.com.imaginary.ms.apigateway.core.filter;

import cn.com.imaginary.ms.apigateway.common.constants.StringConsts;
import cn.com.imaginary.ms.apigateway.core.JwtHelper;
import cn.com.imaginary.ms.apigateway.core.whiteblacklist.ApiWhitelistResolver;
import cn.com.imaginary.ms.apigateway.model.ReturnData;
import cn.com.imaginary.ms.apigateway.util.ReturnDataUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author : Imaginary
 * @version : V1.0
 * @date : 2018/11/13 15:33
 */
@Slf4j
@Component
public class AuthSignatureFilter implements GlobalFilter, Ordered {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private ApiWhitelistResolver apiWhitelistResolver;

    private static final String EXPIRE = "expire";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String uri = exchange.getRequest().getURI().getPath();
        log.info("get request uri {}", uri);
        if (!apiWhitelistResolver.isWhitelistApi(uri)) {
            String token = exchange.getRequest().getHeaders().getFirst(StringConsts.X_AUTH_TOKEN);
            ReturnData tokenData = JwtHelper.valid(token);
            if (tokenData == null || !ReturnDataUtil.isSuccess(tokenData) || redisTemplate.hasKey(EXPIRE + StringConsts.COLON + token)) {
                exchange.getResponse().setStatusCode(HttpStatus.OK);
                exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON_UTF8);
                return exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(getUnAuthString().getBytes())));
            }
        }
        return chain.filter(exchange);
    }

    private String getUnAuthString() {
        return JSONObject.toJSONString(ReturnDataUtil.unAuth());
    }

    @Override
    public int getOrder() {
        return -200;
    }
}
