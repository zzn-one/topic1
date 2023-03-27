package com.zzn.pojo.user;

import lombok.Data;

/**
 * @author zzn
 * @create 2022-12-17 2:10
 */
@Data
public class UserUpdatePassword {
    private String id;
    private String name;
    private String pass;
    private String checkPass;
    private String newPass;
}
