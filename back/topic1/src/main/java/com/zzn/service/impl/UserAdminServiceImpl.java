package com.zzn.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzn.pojo.user.admin.UserAdmin;
import com.zzn.pojo.user.admin.UserAdminWithDict;
import com.zzn.service.UserAdminService;
import com.zzn.mapper.UserAdminMapper;
import com.zzn.utils.JwtUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author zzz06
 * @description 针对表【user_admin】的数据库操作Service实现
 * @createDate 2022-12-27 11:56:13
 */
@Service
public class UserAdminServiceImpl extends ServiceImpl<UserAdminMapper, UserAdmin>
        implements UserAdminService {
    @Resource
    private UserAdminMapper userAdminMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public static final String KEY_PREFIX = "user:admin:";

    @Override
    public UserAdminWithDict getOneByAid(String aid) {
        UserAdminWithDict user;
        //先去redis查
        String userJson = stringRedisTemplate.opsForValue().get(KEY_PREFIX + aid);
        if (userJson != null) {
            user = JSON.parseObject(userJson, UserAdminWithDict.class);
        } else {
            //redis 无，查mysql
            user = userAdminMapper.getOneByAId(aid);
        }
        return user;
    }

    @Override
    public String login(String aid, String password, HttpServletRequest request) {
        //查询是否有aid password对应用户
        UserAdminWithDict userDb = userAdminMapper.getOneByAIdAndPassword(aid, password);
        if (userDb == null) {
            //没有用户
            return null;
        } else {
            //登录成功，生成jwt发送到前端，用户信息存储到redis
            //1.jwt
            //设置jwt的payload
            Map<String, String> payloadMap = new HashMap<>();
            payloadMap.put("id", userDb.getAId());
            payloadMap.put("name", userDb.getAName());
            payloadMap.put("userType", userDb.getUserType().toString());
            payloadMap.put("college", userDb.getCollege());
            //生成jwt
            String token = JwtUtil.getToken(payloadMap);


            //2.redis
            String userJson = JSON.toJSONString(userDb);

            stringRedisTemplate.opsForValue().set(KEY_PREFIX + userDb.getAId(), userJson, JwtUtil.EXPIRES, TimeUnit.MINUTES);

            //3.修改login_time和login_ip

            LambdaUpdateWrapper<UserAdmin> updateWrapper = new LambdaUpdateWrapper<>();

            updateWrapper.eq(UserAdmin::getId, userDb.getId());

            updateWrapper.set(UserAdmin::getLoginTime, new Date());
            updateWrapper.set(UserAdmin::getLoginIp, request.getRemoteAddr());

            userAdminMapper.update(null, updateWrapper);

            //返回jwt
            return token;
        }

    }

    @Override
    public Boolean updatePassword(String aid, String newPass) {

        LambdaUpdateWrapper<UserAdmin> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(UserAdmin::getAId, aid);

        updateWrapper.set(UserAdmin::getPassword, newPass);

        int update = userAdminMapper.update(null, updateWrapper);

        return update > 0;

    }

    @Override
    public Boolean updateMsg(HashMap<String, String> userMap) {

        String aId = userMap.get("id");
        String phone = userMap.get("phone");
        String email = userMap.get("email");

        LambdaUpdateWrapper<UserAdmin> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(UserAdmin::getAId, aId);
        updateWrapper.set(UserAdmin::getPhone, phone);
        updateWrapper.set(UserAdmin::getEmail, email);

        int update = userAdminMapper.update(null, updateWrapper);

        return update > 0;
    }
}




