package com.yourcompany.yourapp.util;

import java.util.HashMap;

// 通用接口返回封装类，用于统一前后端数据格式
public class R extends HashMap<String, Object> {

    // 成功返回（无业务数据）
    public static R ok() {
        R r = new R();
        r.put("code", 0);
        r.put("msg", "success");
        return r;
    }

    // 成功返回（携带业务数据）
    public static R ok(Object data) {
        R r = ok();
        r.put("data", data);
        return r;
    }

    // 失败返回（携带错误信息）
    public static R error(String msg) {
        R r = new R();
        r.put("code", -1);
        r.put("msg", msg);
        return r;
    }

    // 链式设置返回数据
    public R data(Object data) {
        this.put("data", data);
        return this;
    }

    // 链式设置提示信息
    public R msg(String msg) {
        this.put("msg", msg);
        return this;
    }
}
