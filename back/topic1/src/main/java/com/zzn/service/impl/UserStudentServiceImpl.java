package com.zzn.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zzn.mapper.UserStudentMapper;
import com.zzn.pojo.Page;
import com.zzn.pojo.user.student.UserStudent;
import com.zzn.pojo.user.student.UserStudentWithDict;
import com.zzn.service.UserStudentService;
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
 * @description 针对表【user_student】的数据库操作Service实现
 * @createDate 2022-12-27 11:56:13
 */
@Service
public class UserStudentServiceImpl extends ServiceImpl<UserStudentMapper, UserStudent>
        implements UserStudentService {

    @Resource
    private UserStudentMapper userStudentMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public static final String KEY_PREFIX = "user:student:";

    @Override
    public String login(String account, String password, HttpServletRequest request) {
        //查询数据库 根据 account password
        UserStudentWithDict userStudent = userStudentMapper.getOneBySidAndPassword(account, password);

        if (userStudent == null) {
            return null;
        } else {
            //登录成功，生成jwt发送到前端，用户信息存储到redis
            //1.jwt
            //设置jwt的payload
            Map<String, String> payloadMap = new HashMap<>();
            payloadMap.put("id", userStudent.getSId());
            payloadMap.put("name", userStudent.getSName());
            payloadMap.put("userType", userStudent.getUserType().toString());
            payloadMap.put("college", userStudent.getCollege());
            payloadMap.put("major", userStudent.getMajor());
            payloadMap.put("year", userStudent.getGraduationYear());
            //生成jwt
            String token = JwtUtil.getToken(payloadMap);

            //2.redis
            String userJson = JSON.toJSONString(userStudent);
            stringRedisTemplate.opsForValue().set(KEY_PREFIX + userStudent.getSId(), userJson, JwtUtil.EXPIRES, TimeUnit.MINUTES);

            //3.修改login_time和login_ip
            LambdaUpdateWrapper<UserStudent> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(UserStudent::getId, userStudent.getId());
            updateWrapper.set(UserStudent::getLoginTime, new Date());
            updateWrapper.set(UserStudent::getLoginIp, request.getRemoteAddr());
            userStudentMapper.update(null, updateWrapper);

            //返回jwt
            return token;
        }
    }

    @Override
    public UserStudentWithDict getOneBySid(String sId) {
        UserStudentWithDict userStudent;
        //先查redis
        String userJson = stringRedisTemplate.opsForValue().get(KEY_PREFIX + sId);
        if (userJson != null) {
            userStudent = JSON.parseObject(userJson, UserStudentWithDict.class);
        } else {
            userStudent = userStudentMapper.getOneBySid(sId);
        }
        return userStudent;
    }

    @Override
    public Page<UserStudentWithDict> pageByCondition(Integer currentPage, Integer pageSize, String college, String sId, String sName, String major) {
        Integer start = (currentPage - 1) * pageSize;
        //分页数据
        List<UserStudentWithDict> list = userStudentMapper.pageByCondition(start, pageSize, college, sId, sName, major);
        //分页条数
        LambdaQueryWrapper<UserStudent> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserStudent::getCollege, college);
        queryWrapper.likeRight(sId != null && !sId.equals(""), UserStudent::getSId, sId);
        queryWrapper.like(sName != null && !sName.equals(""), UserStudent::getSName, sName);
        queryWrapper.eq(major != null && !major.equals(""), UserStudent::getMajor, major);

        Long total = userStudentMapper.selectCount(queryWrapper);

        return new Page<>(total, list);
    }

    @Override
    public List<UserStudentWithDict> listByCollege(String college) {
        return userStudentMapper.listByCollege(college);
    }

    @Override
    public List<String> getYearListByCollege(String college) {
        return userStudentMapper.getYearListByCollege(college);
    }

    @Override
    public List<UserStudentWithDict> exportMsgByCondition(String college, String major, String year, Integer number) {
        return userStudentMapper.exportMsgByCondition(college, major, year, number);
    }


}




