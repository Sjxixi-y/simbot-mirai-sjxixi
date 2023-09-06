package com.sjxixi.simbotmiraisjxixi.util;


import com.sjxixi.simbotmiraisjxixi.vo.PagingVo;

public class PagingVoUtil {
    public static PagingVo getPagingVo(PagingVo paging) {
        if ("".equals(paging.getValueOne())) {
            paging.setValueOne(null);
        }

        if ("".equals(paging.getValueTow())) {
            paging.setValueTow(null);
        }

        return paging;
    }
}
