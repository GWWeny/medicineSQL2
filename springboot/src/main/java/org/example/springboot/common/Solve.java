package org.example.springboot.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Solve {
    //无法改变
    public static final String CODE_SUCCESS = "200";
    public static final String CODE_AUTH_ERROR = "401";
    public static final String CODE_SYS_ERROR = "404";

    private String code;
    private String msg;
    private Object data;


    public static Solve success() {
        return new Solve(CODE_SUCCESS, "请求成功", null);
    }

    public static Solve success(Object data) {
        return new Solve(CODE_SUCCESS, "请求成功", data);
    }

    public static Solve error(String msg) {
        return new Solve(CODE_SYS_ERROR, msg, null);
    }

    public static Solve error(String code, String msg) {
        return new Solve(code, msg, null);
    }

    public static Solve error() {
        return new Solve(CODE_SYS_ERROR, "系统错误", null);
    }

    public static Solve failure(String s) {

        return new Solve(CODE_AUTH_ERROR, s, null);
    }
}
