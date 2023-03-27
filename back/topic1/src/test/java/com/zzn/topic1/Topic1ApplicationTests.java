package com.zzn.topic1;

import com.zzn.mapper.UserAdminMapper;
import com.zzn.mapper.UserStudentMapper;
import com.zzn.mapper.UserTutorMapper;
import com.zzn.pojo.user.student.UserStudentWithDict;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class Topic1ApplicationTests {
    @Resource
    private UserAdminMapper userAdminMapper;

    @Resource
    private UserTutorMapper userTutorMapper;

    @Resource
    UserStudentMapper userStudentMapper;

    @Test
    void userAdmin() {
        UserStudentWithDict one = userStudentMapper.getOneBySidAndPassword("111", "111");

        System.out.println("one = " + one);
    }



}
