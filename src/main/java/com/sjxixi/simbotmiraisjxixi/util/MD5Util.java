package com.sjxixi.simbotmiraisjxixi.util;

import org.springframework.util.DigestUtils;

public class MD5Util {
    private static final String slat = "sjxixi is  a good man";

    // MD5 实现类
    public static String MD5(String str) {
        String base = slat + str;
        return DigestUtils.md5DigestAsHex(base.getBytes());
    }
}
