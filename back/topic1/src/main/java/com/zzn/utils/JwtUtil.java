package com.zzn.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;
import java.util.Map;

/**
 * @author zzn
 * @create 2022-12-16 15:59
 */
public class JwtUtil {
    private static final String TOKEN = "topic@v1.0-jwt";
    public static final int EXPIRES = 120;
    /**
     * 生成token
     * @param map  //传入payload
     * @return 返回token
     */
    public static String getToken(Map<String,String> map){
        JWTCreator.Builder builder = JWT.create();
        map.forEach(builder::withClaim);

        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.MINUTE,EXPIRES);
        builder.withExpiresAt(instance.getTime());

        return builder.sign(Algorithm.HMAC256(TOKEN));
    }
    /**
     * 生成token
     * @param map  //传入payload
     * @return 返回token
     */
    public static String getToken(Map<String,String> map,Integer expires){
        JWTCreator.Builder builder = JWT.create();
        map.forEach(builder::withClaim);

        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.MINUTE,expires);
        builder.withExpiresAt(instance.getTime());

        return builder.sign(Algorithm.HMAC256(TOKEN));
    }
    /**
     * 验证token
     * @param token
     * @return
     */
    public static void verify(String token){
        JWT.require(Algorithm.HMAC256(TOKEN)).build().verify(token);
    }
    /**
     * 获取token中payload
     * @param token
     * @return
     */
    public static DecodedJWT getDecodedJWT(String token){
        return JWT.require(Algorithm.HMAC256(TOKEN)).build().verify(token);
    }
}
