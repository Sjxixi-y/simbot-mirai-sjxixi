package com.sjxixi.simbotmiraisjxixi.util;

import com.sjxixi.simbotmiraisjxixi.entity.AtReply;
import love.forte.simbot.ID;
import love.forte.simbot.Identifies;
import love.forte.simbot.message.Messages;
import love.forte.simbot.message.MessagesBuilder;

import java.util.List;

public class MenuUtil {

    // 玫瑰
    private static final ID MEI_GUI = Identifies.ID(63);

    public static Messages getMenu(List<AtReply> list) {
        // 获取最长字段
        int max = -1;

        // 获取最长字符串
        for (AtReply item : list) {
            int length = item.getValue().length();

            if (length > max) {
                max = length;
            }
        }

        MessagesBuilder builder = new MessagesBuilder();
        // 拼装菜单
        // 头部
        getMenuHeader(builder);
        // 身体
        getMenuBody(list, builder);
        // 尾部
        getMenuTail(builder);

        return builder.build();
    }

    // 获取菜单头部
    public static void getMenuHeader(MessagesBuilder builder) {
        builder.face(MEI_GUI);

        for (int i = 0; i < 6; i++) {
            builder.text("=");
        }

        builder.text("=Sjxixi=");

        for (int i = 0; i < 6; i++) {
            builder.text("=");
        }

        builder.face(MEI_GUI);
    }

    public static void getMenuBody(List<AtReply> list, MessagesBuilder builder) {

        for (AtReply item : list) {

            builder.text("\n").text(" -- " + item.getValue());
        }
    }

    public static void getMenuTail(MessagesBuilder builder) {
        builder.text("\n提示: \n通过 ! help <标题名称> 进行详细查询");
    }
}
