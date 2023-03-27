package com.zzn.mq;

import com.alibaba.fastjson.JSON;
import com.zzn.mapper.UserAdminMapper;
import com.zzn.mapper.UserStudentMapper;
import com.zzn.mapper.UserTutorMapper;
import com.zzn.pojo.user.admin.UserAdminWithDict;
import com.zzn.pojo.user.student.UserStudentWithDict;
import com.zzn.pojo.user.tutor.UserTutorWithDict;
import com.zzn.service.UserAdminService;
import com.zzn.service.UserStudentService;
import com.zzn.service.UserTutorService;
import com.zzn.service.impl.UserAdminServiceImpl;
import com.zzn.service.impl.UserStudentServiceImpl;
import com.zzn.service.impl.UserTutorServiceImpl;
import com.zzn.utils.JwtUtil;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * @author zzn
 * @create 2023-02-01 21:34
 * 数据同步
 */
@Component
public class UserMsgListener {

    @Resource
    private UserStudentMapper userStudentMapper;

    @Resource
    private UserTutorMapper userTutorMapper;

    @Resource
    private UserAdminMapper userAdminMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @RabbitListener(queues = "topic1_queue_student_update")
    public void studentUpdate(Message message) {
        String sid = new String(message.getBody());

        UserStudentWithDict userDb = userStudentMapper.getOneBySid(sid);

        if (userDb != null) {
            // 数据同步
            String jsonString = JSON.toJSONString(userDb);
            stringRedisTemplate.opsForValue().getAndDelete(UserStudentServiceImpl.KEY_PREFIX + sid);
            stringRedisTemplate.opsForValue().set(UserStudentServiceImpl.KEY_PREFIX + sid,jsonString, JwtUtil.EXPIRES, TimeUnit.MINUTES);
        }
    }

    @RabbitListener(queues = "topic1_queue_tutor_update")
    public void tutorUpdate(Message message) {
        String tid = new String(message.getBody());

        UserTutorWithDict userDb = userTutorMapper.getOneByTid(tid);

        if (userDb != null) {
            // 数据同步
            String jsonString = JSON.toJSONString(userDb);
            stringRedisTemplate.opsForValue().getAndDelete(UserTutorServiceImpl.KEY_PREFIX + tid);
            stringRedisTemplate.opsForValue().set(UserTutorServiceImpl.KEY_PREFIX + tid,jsonString, JwtUtil.EXPIRES, TimeUnit.MINUTES);
        }
    }

    @RabbitListener(queues = "topic1_queue_admin_update")
    public void adminUpdate(Message message) {
        String aid = new String(message.getBody());

        UserAdminWithDict userDb = userAdminMapper.getOneByAId(aid);

        if (userDb != null) {
            // 数据同步
            String jsonString = JSON.toJSONString(userDb);
            stringRedisTemplate.opsForValue().getAndDelete(UserAdminServiceImpl.KEY_PREFIX + aid);
            stringRedisTemplate.opsForValue().set(UserAdminServiceImpl.KEY_PREFIX + aid,jsonString, JwtUtil.EXPIRES, TimeUnit.MINUTES);
        }
    }

}



