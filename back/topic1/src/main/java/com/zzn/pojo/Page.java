package com.zzn.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author zzn
 * @create 2022-12-27 16:40
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Page <T>{
    private Long total;
    private List<T> records;
}
