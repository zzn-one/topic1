package com.zzn.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzn.pojo.Page;
import com.zzn.pojo.user.tutor.TutorStudent;
import com.zzn.pojo.user.tutor.UserTutor;
import com.zzn.pojo.user.tutor.UserTutorWithDict;
import com.zzn.pojo.user.tutor.UserTutorWithExcel;
import com.zzn.service.UserTutorService;
import com.zzn.mapper.UserTutorMapper;
import com.zzn.utils.JwtUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author zzz06
 * @description 针对表【user_tutor】的数据库操作Service实现
 * @createDate 2022-12-27 11:56:13
 */
@Service
public class UserTutorServiceImpl extends ServiceImpl<UserTutorMapper, UserTutor>
        implements UserTutorService {
    @Resource
    private UserTutorMapper userTutorMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    public static final String KEY_PREFIX = "user:tutor:";

    @Override
    public String login(String account, String password, HttpServletRequest request) {
        //查询用户 根据account password
        UserTutorWithDict userTutor = userTutorMapper.getOneByTidAndPassword(account, password);

        if (userTutor == null) {
            return null;
        } else {
            //登录成功，生成jwt发送到前端，用户信息存储到redis
            //1.jwt
            //设置jwt的payload
            Map<String, String> payloadMap = new HashMap<>();
            payloadMap.put("id", userTutor.getTId());
            payloadMap.put("name", userTutor.getTutorName());
            payloadMap.put("userType", userTutor.getUserType().toString());
            payloadMap.put("college", userTutor.getCollege());
            payloadMap.put("major", userTutor.getMajor());
            //生成jwt
            String token = JwtUtil.getToken(payloadMap);

            //2.redis
            String userJson = JSON.toJSONString(userTutor);
            stringRedisTemplate.opsForValue().set(KEY_PREFIX + userTutor.getTId(), userJson, JwtUtil.EXPIRES, TimeUnit.MINUTES);

            //3.修改login_time和login_ip
            LambdaUpdateWrapper<UserTutor> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(UserTutor::getId, userTutor.getId());
            updateWrapper.set(UserTutor::getLoginTime, new Date());
            updateWrapper.set(UserTutor::getLoginIp, request.getRemoteAddr());

            userTutorMapper.update(null, updateWrapper);

            //返回jwt
            return token;
        }

    }

    @Override
    public UserTutorWithDict getOneByTid(String tId) {
        UserTutorWithDict userTutor;
        //先查redis
        String userJson = stringRedisTemplate.opsForValue().get(KEY_PREFIX + tId);
        if (userJson != null) {
            userTutor = JSON.parseObject(userJson, UserTutorWithDict.class);
        } else {
            userTutor = userTutorMapper.getOneByTid(tId);
        }
        return userTutor;
    }


    @Override
    public Page<UserTutorWithDict> getByConditionAndPage(Integer currentPage, Integer pageSize, String college, String tId, String tutorName, String major) {

        Integer start = (currentPage - 1) * pageSize;
        //分页数据
        List<UserTutorWithDict> list = userTutorMapper.pageByCondition(start, pageSize, college, tId, tutorName, major);
        //分页条数
        LambdaQueryWrapper<UserTutor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserTutor::getCollege, college);
        queryWrapper.likeRight(tId != null && !tId.equals(""), UserTutor::getTId, tId);
        queryWrapper.like(tutorName != null && !tutorName.equals(""), UserTutor::getTutorName, tutorName);
        queryWrapper.eq(major != null && !major.equals(""), UserTutor::getMajor, major);

        Long total = userTutorMapper.selectCount(queryWrapper);

        return new Page<>(total, list);
    }

    @Override
    public List<UserTutorWithDict> listByCollege(String college) {
        return userTutorMapper.listByCollege(college);
    }

    @Override
    public List<UserTutorWithDict> exportMsgByCondition(String college, String major, Integer number) {
        return userTutorMapper.exportMsgByCondition(college, major, number);
    }

    @Override
    public List<TutorStudent> getStudentByTid(String tid) {
        return userTutorMapper.getStudentByTid(tid);
    }
}




