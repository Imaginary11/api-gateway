package cn.com.imaginary.ms.apigateway.api;

import cn.com.imaginary.ms.apigateway.core.whiteblacklist.ApiWhitelistResolver;
import cn.com.imaginary.ms.apigateway.model.ReturnData;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author : Imaginary
 * @version : V1.0
 * @date : 2019/5/14 15:11
 */
@RestController
@RequestMapping("/gateway/whitelist/api")
public class ApiWhitelistApi {
    @Autowired
    private ApiWhitelistResolver apiWhitelistResolver;

    @PostMapping
    public ReturnData addApi(@RequestBody JSONObject data) {
        return apiWhitelistResolver.addApi(data.getString("api"));
    }

    @PutMapping
    public boolean validate(@RequestBody JSONObject data) {
        return apiWhitelistResolver.isWhitelistApi(data.getString("api"));
    }

    @DeleteMapping
    public ReturnData deleApi(@RequestBody JSONObject data){
        return apiWhitelistResolver.delApi(data.getString("api"));
    }

    @GetMapping
    public ReturnData getApis(){
        return apiWhitelistResolver.getWhitelistApis();
    }
}
