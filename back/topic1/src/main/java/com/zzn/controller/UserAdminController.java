package com.zzn.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.zzn.config.RabbitMqConfig;
import com.zzn.pojo.Result;
import com.zzn.pojo.user.admin.UserAdmin;
import com.zzn.pojo.user.admin.UserAdminWithDict;
import com.zzn.pojo.user.UserUpdatePassword;
import com.zzn.service.UserAdminService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

/**
 * @author zzn
 * @create 2022-12-16 15:25
 */

@RestController
@RequestMapping("/userAdmin")
public class UserAdminController {
    @Autowired
    private UserAdminService userAdminService;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @GetMapping("/login/{account}/{password}")
    public Result<String> login(@PathVariable String account, @PathVariable String password, HttpServletRequest request) {

        String jwt = userAdminService.login(account, password, request);

        Result<String> result;
        if (jwt == null) {
            result = new Result<>(HttpStatus.UNAUTHORIZED.value(), "账号或密码错误！", null);
        } else {
            result = new Result<>(HttpStatus.OK.value(), null, jwt);
        }

        return result;
    }

    /*
     * 根据aId查询用户信息
     * */
    @GetMapping("{id}")
    public Result<UserAdminWithDict> getById(@PathVariable("id") String aid) {
        Result<UserAdminWithDict> result;
        UserAdminWithDict user = userAdminService.getOneByAid(aid);
        if (user == null) {
            result = new Result<>(HttpStatus.UNAUTHORIZED.value(), "无此用户！", null);
        } else {
            result = new Result<>(HttpStatus.OK.value(), null, user);
        }

        return result;
    }

    /*
     * 根据aId和password 修改password
     * */
    @PutMapping("/updatePassword")
    public Result<Boolean> updatePasswordByAid(@RequestBody UserUpdatePassword user) {
        Result<Boolean> result;
        //根据id和oldPassword查询用户

        LambdaQueryWrapper<UserAdmin> wrapper = new LambdaQueryWrapper<>();
        String aid = user.getId();
        wrapper.eq(UserAdmin::getAId, aid);
        wrapper.eq(UserAdmin::getPassword, user.getPass());

        UserAdmin userDb = userAdminService.getOne(wrapper);

        if (userDb == null) {
            result = new Result<>(HttpStatus.FORBIDDEN.value(), "旧密码错误,请重新输入！", false);
        } else {
            //验证通过，修改密码
            Boolean updatePassword = userAdminService.updatePassword(aid, user.getNewPass());

            if (updatePassword) {
                result = new Result<>(HttpStatus.OK.value(), "密码已修改！", true);
                //发送消息 同步redis的用户信息
                rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_NAME, RabbitMqConfig.ROUTING_NAME_ADMIN_UPDATE + "." + aid, aid);
            } else {
                result = new Result<>(HttpStatus.OK.value(), "密码修改失败！", false);
            }

        }


        return result;
    }

    /*
     * 根据aId修改用户信息
     * */
    @PutMapping("/updateMsg")
    public Result<Boolean> updateMsgByAid(@RequestBody HashMap<String, String> userMap) {

        String aid = userMap.get("id");

        boolean update = userAdminService.updateMsg(userMap);

        Result<Boolean> result;
        if (update) {
            result = new Result<>(HttpStatus.OK.value(), "信息已修改！", true);
            //发送消息 同步redis的用户信息
            rabbitTemplate.convertAndSend(RabbitMqConfig.EXCHANGE_NAME, RabbitMqConfig.ROUTING_NAME_ADMIN_UPDATE + "." + aid, aid);
        } else {
            result = new Result<>(HttpStatus.OK.value(), "信息修改失败！", false);
        }

        return result;

    }
}
