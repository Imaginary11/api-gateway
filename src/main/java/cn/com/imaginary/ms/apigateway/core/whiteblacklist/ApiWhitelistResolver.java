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
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static cn.com.imaginary.ms.apigateway.common.constants.CacheNameConsts.*;
import static cn.com.imaginary.ms.apigateway.common.constants.StringConsts.*;

/**
 * @author : Imaginary
 * @version : V1.0
 * @date : 2019/5/14 13:26
 */
@Slf4j
@Service
public class ApiWhitelistResolver {

    private static final int SERVICE_LENGTH = 3;

    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * /nc/*
     * /nc/sms/*
     * /nc/sms/deliver
     * /nc/sms/{id}
     * /nc/sms/{id}/info
     */
    public boolean isWhitelistApi(String uri) {
        if (StringUtils.isEmpty(uri) || StringUtils.isEmpty(SPRIT)) {
            return false;
        }
        List<String> patternUrl = redisTemplate.opsForHash().keys(WHITELIST_API_PATTERN_KEY).stream().map(Object::toString).collect(Collectors.toList());


        return uri.contains(DOT) || uri.contains(HIPHEN)
                || redisTemplate.opsForHash().hasKey(WHITELIST_API_KEY, uri)
                || redisTemplate.opsForHash().hasKey(WHITELIST_SERVICE_KEY, uri.split(SPRIT)[1])
                || patternUrl.stream().filter(x -> {
            if ((x.endsWith(ASTERISK) || x.endsWith(PATH_VARIANLE))) {
                String pattern = x.replace(ASTERISK, StringConsts.BLANK).replace(PATH_VARIANLE, BLANK);
                if (uri.startsWith(pattern)) {
                    return true;
                }
            } else if (x.contains(PATH_VARIANLE)) {
                String pattern = x.replace(PATH_VARIANLE, ".*");
                pattern = UPER + pattern + DOLLAR;
                return Pattern.compile(pattern).matcher(uri).matches();
            }
            return false;
        }).count() > 0;
    }

    public ReturnData addApi(String api) {
        String time = DateUtil.format();
        if (StringUtils.isEmpty(api) || StringUtils.isEmpty(SPRIT)) {
            return ReturnDataUtil.invalidArgument();
        }
        if (api.contains(StringConsts.ASTERISK)) {
            String[] apiArr = api.split(StringConsts.SPRIT);
            if (apiArr.length == SERVICE_LENGTH) {
                redisTemplate.opsForHash().put(WHITELIST_SERVICE_KEY, apiArr[1], time);
            } else {
                redisTemplate.opsForHash().put(WHITELIST_API_PATTERN_KEY, api, time);
            }
        } else if (api.contains(PATH_VARIANLE)) {
            redisTemplate.opsForHash().put(WHITELIST_API_PATTERN_KEY, api, time);
        } else {
            redisTemplate.opsForHash().put(WHITELIST_API_KEY, api, time);
        }
        return ReturnDataUtil.getSuccussReturn();
    }

    public ReturnData delApi(String api) {
        if (StringUtils.isEmpty(api) || StringUtils.isEmpty(SPRIT)) {
            return ReturnDataUtil.invalidArgument();
        }
        if (api.contains(StringConsts.ASTERISK)) {
            String[] apiArr = api.split(StringConsts.SPRIT);
            if (apiArr.length == SERVICE_LENGTH) {
                redisTemplate.opsForHash().delete(WHITELIST_SERVICE_KEY, apiArr[1]);
            } else {
                redisTemplate.opsForHash().delete(WHITELIST_API_PATTERN_KEY, api);
            }
        } else if (api.contains(PATH_VARIANLE)) {
            redisTemplate.opsForHash().delete(WHITELIST_API_PATTERN_KEY, api);
        } else {
            redisTemplate.opsForHash().delete(WHITELIST_API_KEY, api);
        }
        return ReturnDataUtil.getSuccussReturn();
    }

    public ReturnData getWhitelistApis() {
        JSONObject data = new JSONObject();

        Arrays.asList(WHITELIST_API_KEY, WHITELIST_SERVICE_KEY, WHITELIST_API_PATTERN_KEY).forEach(p -> {
            data.put(p, redisTemplate.opsForHash().keys(p).stream().map(x -> x.toString().contains(SPRIT) ? x : SPRIT + x + SPRIT + ASTERISK).collect(Collectors.toList()));
        });
        return ReturnDataUtil.getSuccussReturn(data);
    }


}
