package cn.com.imaginary.ms.apigateway.core;

import cn.com.imaginary.ms.apigateway.model.ReturnData;
import cn.com.imaginary.ms.apigateway.util.DateUtil;
import cn.com.imaginary.ms.apigateway.util.ReturnDataUtil;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import net.minidev.json.JSONObject;
import org.apache.commons.lang3.StringUtils;

/**
 * @author : Imaginary
 * @version : V1.0
 * @date : 2018/11/2 17:08
 */
public class JwtHelper {
    private static final String TIMESTAMP = "sta";
    private static final String EXPIRE = "exp";
    private static final int DEFAULT_TIMEOUT = 5;

    private static final byte[] secret = "d8ec89ccd7394606972c1263d308cfb3!".getBytes();

    public static String creatToken(com.alibaba.fastjson.JSONObject playload) {
        return creatToken(playload, null);
    }

    public static String creatToken(com.alibaba.fastjson.JSONObject playload, Integer timeout) {
        try {
            if (playload == null) {
                playload = new com.alibaba.fastjson.JSONObject();
            }
            long currentTimeMillis = System.currentTimeMillis();
            playload.put(TIMESTAMP, currentTimeMillis);
            playload.put(EXPIRE, DateUtil.getDateByHours(timeout == null ? DEFAULT_TIMEOUT : timeout).getTime());
            JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);
            Payload payload = new Payload(new JSONObject(playload));
            JWSObject jwsObject = new JWSObject(jwsHeader, payload);
            JWSSigner jwsSigner = new MACSigner(secret);
            jwsObject.sign(jwsSigner);
            return jwsObject.serialize();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ReturnData valid(String token) {
        try {
            if (StringUtils.isEmpty(token)) {
                return ReturnDataUtil.unAuth();
            }

            JWSObject jwsObject = JWSObject.parse(token);
            Payload payload = jwsObject.getPayload();
            JWSVerifier jwsVerifier = new MACVerifier(secret);
            ReturnData returnData;
            if (jwsObject.verify(jwsVerifier)) {
                returnData = new ReturnData();
                JSONObject jsonObject = payload.toJSONObject();
                returnData.setData(jsonObject);
                if (jsonObject.containsKey(EXPIRE)) {
                    Long expTime = Long.valueOf(jsonObject.get(EXPIRE).toString());
                    Long nowTime = System.currentTimeMillis();
                    if (nowTime > expTime) {
                        returnData = ReturnDataUtil.unAuth();
                    }
                }
            } else {
                returnData = ReturnDataUtil.unAuth();
            }
            return returnData;
        } catch (Exception e) {
            return ReturnDataUtil.systemError();
        }
    }

}
