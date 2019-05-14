package cn.com.imaginary.ms.apigateway.core.whiteblacklist;

import cn.com.imaginary.ms.apigateway.common.constants.StringConsts;
import cn.com.imaginary.ms.apigateway.model.ReturnData;
import cn.com.imaginary.ms.apigateway.util.DateUtil;
import cn.com.imaginary.ms.apigateway.util.ReturnDataUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static cn.com.imaginary.ms.apigateway.common.constants.CacheNameConsts.BLACKLIST_IP_KEY;


/**
 * @author : Imaginary
 * @version : V1.0
 * @date : 2018/11/16 17:39
 */
@Slf4j
@Service
public class IpBlacklistResolver {
    @Autowired
    private StringRedisTemplate redisTemplate;

    public Mono<ReturnData> getIpList() {
        List result = new ArrayList<>();

        redisTemplate.keys(BLACKLIST_IP_KEY + StringConsts.COLON + StringConsts.ASTERISK).stream().
                collect(Collectors.toMap(key -> key.substring(key.indexOf(StringConsts.COLON)+1, key.length()),
                        key -> redisTemplate.opsForValue().get(key)))
        .forEach((k,v)->{
            JSONObject ipObj = new JSONObject();
            ipObj.put(k,v);
            result.add(ipObj);
        });
        return Mono.just(ReturnDataUtil.getSuccussReturn(result));
    }

    public Mono<ReturnData> addIp(String ip, String remark) {
        redisTemplate.opsForValue().set(BLACKLIST_IP_KEY + StringConsts.COLON + ip, DateUtil.format() + StringConsts.COMMA + remark, 1, TimeUnit.DAYS);
        return Mono.just(ReturnDataUtil.getSuccussReturn());
    }

    public Mono<ReturnData> delIp(String ip) {
        redisTemplate.delete(BLACKLIST_IP_KEY + StringConsts.COLON + ip);
        return Mono.just(ReturnDataUtil.getSuccussReturn());
    }
}
