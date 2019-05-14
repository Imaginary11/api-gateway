package cn.com.imaginary.ms.apigateway.core.filter;

import cn.com.imaginary.ms.apigateway.common.constants.StringConsts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ipresolver.RemoteAddressResolver;
import org.springframework.cloud.gateway.support.ipresolver.XForwardedRemoteAddressResolver;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.InetSocketAddress;

import static cn.com.imaginary.ms.apigateway.common.constants.CacheNameConsts.BLACKLIST_IP_KEY;

/**
 * @author : Imaginary
 * @version : V1.0
 * @date : 2018/11/16 16:58
 */
@Slf4j
@Component
public class IpBlackListFilter implements GlobalFilter, Ordered {

    @Autowired
    private StringRedisTemplate redisTemplate;


    private final RemoteAddressResolver remoteAddressResolver = XForwardedRemoteAddressResolver
            .maxTrustedIndex(1);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        try {
            InetSocketAddress remoteAddress = remoteAddressResolver.resolve(exchange);
            String clientIp = remoteAddress.getHostName();
            if (redisTemplate.hasKey(BLACKLIST_IP_KEY + StringConsts.COLON + clientIp)) {
                log.info("intercept invalid request from forbidden ip {}", clientIp);
                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                return Mono.empty();
            }
        } catch (Exception e) {
            log.error("IpBlackListFilter error", e);
        }
        return chain.filter(exchange);

    }


    @Override
    public int getOrder() {
        return -201;
    }
}
