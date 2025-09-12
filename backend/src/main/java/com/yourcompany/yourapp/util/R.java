package com.yourcompany.yourapp.util;

import java.util.HashMap;

public class R extends HashMap<String, Object> {
    public static R ok() { 
        R r = new R(); 
        r.put("code", 0); 
        r.put("msg", "success"); 
        return r; 
    }
    public static R ok(Object data) {
        R r = ok();
        r.put("data", data);
        return r;
    }
    public static R error(String msg) {
        R r = new R();
        r.put("code", -1);
        r.put("msg", msg);
        return r;
    }
    public R data(Object data) { this.put("data", data); return this; }
    public R msg(String msg) { this.put("msg", msg); return this; }
}
