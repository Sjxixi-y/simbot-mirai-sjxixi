package com.sjxixi.simbotmiraisjxixi.util;


import com.sjxixi.simbotmiraisjxixi.vo.PagingVo;

public class PagingVoUtil {
    public static PagingVo getPagingVo(PagingVo paging) {
        if ("".equals(paging.getName())) {
            paging.setName(null);
        }

        if ("".equals(paging.getCode())) {
            paging.setCode(null);
        }

        return paging;
    }
}
