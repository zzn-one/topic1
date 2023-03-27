package com.zzn.utils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author zzn
 * @create 2022-11-09 17:55
 */
public class WebUtils {

    public static void printJson(HttpServletResponse response, String... msgs) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter writer = response.getWriter();
        try {
            for (String msg : msgs) {
                writer.println(msg);
            }
        } finally {
            writer.close();
        }
    }
}
