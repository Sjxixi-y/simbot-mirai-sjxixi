package com.sjxixi.simbotmiraisjxixi.listener;


import com.sjxixi.simbotmiraisjxixi.entity.AtReply;
import com.sjxixi.simbotmiraisjxixi.entity.Field;
import com.sjxixi.simbotmiraisjxixi.entity.Users;
import com.sjxixi.simbotmiraisjxixi.service.AtReplyService;
import com.sjxixi.simbotmiraisjxixi.service.FieldService;
import com.sjxixi.simbotmiraisjxixi.service.UsersService;
import com.sjxixi.simbotmiraisjxixi.util.MenuUtil;
import jakarta.annotation.Resource;

import love.forte.simboot.annotation.Filter;
import love.forte.simboot.annotation.Listener;
import love.forte.simbot.ID;
import love.forte.simbot.component.mirai.event.MiraiMemberJoinRequestEvent;
import love.forte.simbot.component.mirai.event.RequestMemberInfo;
import love.forte.simbot.definition.Group;
import love.forte.simbot.definition.GroupMember;
import love.forte.simbot.definition.Member;
import love.forte.simbot.definition.MemberInfo;
import love.forte.simbot.event.GroupMemberDecreaseEvent;
import love.forte.simbot.event.GroupMemberIncreaseEvent;
import love.forte.simbot.event.GroupMessageEvent;
import love.forte.simbot.message.Messages;
import love.forte.simbot.message.MessagesBuilder;
import love.forte.simbot.utils.item.Items;
import net.mamoe.mirai.Mirai;
import net.mamoe.mirai.data.UserProfile;
import net.mamoe.mirai.message.data.Face;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;

import static com.sjxixi.simbotmiraisjxixi.util.NumberUtil.LEVEL_ONE;


/**
 * 群相关事件
 */

@Component
public class GroupListener {
    @Resource
    UsersService usersService;
    @Resource
    FieldService fieldService;
    @Resource
    AtReplyService atReplyService;
    // 日志对象
    private static final Logger logger = LoggerFactory.getLogger(GroupListener.class);

    /**
     * 重要消息，用于对数据进行判断
     */
    private final Map<String, String> fieldMap = new HashMap<>();

    /**
     * 获取关键字段
     *
     * @return 字段 map
     */
    public Map<String, String> getMessages() {
        if (fieldMap.size() < 1) {
            List<Field> list = fieldService.getFieldSimBot();
            list.forEach(field -> fieldMap.put(field.getName(), field.getValue()));
        }
        return fieldMap;
    }

    /**
     * 刷新集合
     */
    public void updateMessages() {
        // 清空
        fieldMap.clear();
        //
        List<Field> list1 = fieldService.getFieldSimBot();
        list1.forEach(field -> fieldMap.put(field.getName(), field.getValue()));
    }

    /**
     * 加群事件监听
     *
     * @param event 事件对象
     */
    @Listener
    @Filter(targets = @Filter.Targets(groups = {"827638861", "747467483"}))
    public void groupJoinRequestEvent(MiraiMemberJoinRequestEvent event) {

        RequestMemberInfo user = event.getRequester();

        logger.info("[入群申请] 用户：" + user.getUsername() + " -- QQ号: " + user.getId());

        // 判断是否黑名单
        Users data = usersService.getUserByCodeSimBot(user.getId().toString());
        if (data != null) {
            if (data.getStatus()) {
                logger.info("[黑名单] 发现成员" + user.getId() + " 是黑名单成员。已将其拒绝。");
                event.rejectAsync("发现为黑名单成员");
                return;
            }
        }
        // 等级判断
        UserProfile profile = Mirai.getInstance().queryProfile(event.getOriginalEvent().getBot(), user.getId().toInt());
        int level = profile.getQLevel();

        logger.info("[入群申请] 用户等级 + " + level);

        if (level < Integer.parseInt(getMessages().get("入群等级"))) {
            logger.info("[入群申请] 用户：" + user.getUsername() + " QQ: " + user.getId() + " 等级不满足指定要求");
            event.rejectAsync("等级不满足" + getMessages().get("入群等级"));
            return;
        }

        // 获取附带消息
        String message = event.getMessage();
        logger.info("[入群申请] 消息文本" + message);
        // 切割
        String[] strings = message.split("答案：");
        int length = strings.length;

        logger.info("[入群申请] 切割消息如下: " + strings[length - 1]);

        if (!getMessages().get("入群问题").equals(strings[length - 1])) {
            logger.info("[入群申请] 问题错误");
            event.rejectAsync("入群问题有误");
            return;
        }
        logger.info("[入群申请] 问题正确");
        event.acceptAsync();
    }

    /**
     * 成员新增
     *
     * @param event 事件对象
     */
    @Listener
    @Filter(targets = @Filter.Targets(groups = {"827638861", "747467483"}))
    public void groupMemberIncreaseEvent(GroupMemberIncreaseEvent event) {
        // 新人
        GroupMember after = event.getAfter();
        // 群
        Group group = event.getGroup();

        logger.info("[群员新增] 成员: " + after.getUsername() + " QQ: " + after.getId() + " 加入群聊");

        // 存储
        // 构建成员对象
        Users user = Users.builder()
                .code(after.getId().toString())
                .name(after.getUsername())
                .permission(2)
                .regTime(new Date(after.getJoinTime().getMillisecond()))
                .build();

        logger.info("[创建对象] User: " + user + " 准备新增数据库");

        usersService.addUserSimBot(user);
        // 欢迎
        Messages build = new MessagesBuilder()
                .at(after.getId())
                .text("\n欢迎新用户进群,有什么问题可以@我哦")
                .build();

        logger.info("[消息发送] " + build);

        group.sendBlocking(build);
    }

    /**
     * 退群事件
     *
     * @param event 事件对象
     */
    @Listener
    @Filter(targets = @Filter.Targets(groups = {"827638861", "747467483"}))
    public void groupMemberDecreaseEvent(GroupMemberDecreaseEvent event) {
        GroupMember member = event.getMember();
        ID id = member.getId();
        // 日志
        logger.info("[退出群聊] 用户：" + member.getUsername() + " -- QQ号: " + id);
        logger.info("[准备数据] QQ: " + id + " 准备执行查询。");

        Integer code = usersService.deleteUserByCodeSimBot(id.toString());

        Group group = event.getGroup();
        // 无法删除
        if (code != 200) {

            logger.info("[无法删除] 用户：" + member.getUsername() + " -- QQ号: " + id);

            Messages messages = new MessagesBuilder()
                    .at(group.getOwner().getId())
                    .text("\n发现成员 " + id + " 无法清除\n请手动清除后在后台进行手动黑名单添加")
                    .build();
            group.sendBlocking(messages);

            return;
        }

        MemberInfo operator = event.getOperator();
        // 判断是否是被踢出的
        assert operator != null;
        if (operator.getId().equals(member.getId())) {
            logger.info("[被踢出] 用户：" + member.getUsername() + " -- QQ号: " + id);
            return;
        }
        logger.info("[主动退群] 用户：" + member.getUsername() + " -- QQ号: " + id);
    }

    /**
     * 菜单列表
     *
     * @param event 事件对象
     */
    @Listener
    @Filter(value = "菜单")
    public void automaticResponse(GroupMessageEvent event) {
        Group group = event.getGroup();
        Member author = event.getAuthor();

        logger.info("[菜单] 用户: " + author.getUsername() + " QQ: " + author.getId() + " 呼唤了菜单");

        List<AtReply> list = atReplyService.getReplyListSimBot(1);

        logger.info("[菜单] 菜单如下：" + list);

        group.sendBlocking(MenuUtil.getMenu(list));
    }

    /**
     * 群员相关指令
     *
     * @param event 事件对象
     */
    @Listener
    @Filter(value = "! .+")
    public void response(GroupMessageEvent event) {
        // 获取事件发起对象
        Member author = event.getAuthor();

        // 获取群对象
        Group group = event.getGroup();

        logger.info("[群员指令] 用户: " + author.getUsername() + " QQ: " + author.getId() + " 指令为: " + event.getMessageContent().getPlainText());

        String text = event.getMessageContent().getPlainText();

        logger.info("[群员指令] 获取指令: " + text);

        // 以空格作为分割
        String[] s = text.split(" ");

        logger.info("[群员指令] 指令集合为: " + Arrays.toString(s));

        // 指令判断
        if (s.length < 2 || s[1] == null || "".equals(s[1])) {
            return;
        }


        switch (s[1]) {
            case "help" -> {
                List<AtReply> list = atReplyService.getReplyListSimBot(s[2]);

                // 判断消息列表
                if (list == null || list.size() < 1) {
                    Messages build = new MessagesBuilder()
                            .at(author.getId())
                            .text("没有更详细的内容啦").build();
                    group.sendBlocking(build);
                    return;
                }

                Messages message = MenuUtil.getMenu(list);

                group.sendAsync(message);
            }

            case "server" -> {

            }

            default -> {
                Messages build = new MessagesBuilder()
                        .at(author.getId())
                        .text("\nSjxixi管理的青楼必须井井有条").build();

                group.sendBlocking(build);
            }
        }
    }

    /**
     * sjxixi相关指令
     *
     * @param event 事件对象
     */
    @Listener
    @Filter(value = "!.+", targets = @Filter.Targets(authors = {"2680031723", "1544117104"}))
    public void getSjxixi(GroupMessageEvent event) {

        Member author = event.getAuthor();

        // 获取群对象
        Group group = event.getGroup();

        logger.info("[管理指令] " + author.getUsername() + " QQ: " + author.getId() + " 指令为: " + event.getMessageContent().getPlainText());

        // 获取指令
        String text = event.getMessageContent().getPlainText();

        logger.info("[管理指令] 获取指令: " + text);

        // 以空格作为分割
        String[] s = text.split(" ");

        logger.info("[管理指令] 指令集合为: " + Arrays.toString(s));

        // 指令判断
        if (s.length < 2 || s[1] == null || "".equals(s[1])) {
            return;
        }


        switch (s[1]) {
            /*
                刷新成员列表
             */
            case "s" -> {
                Items<GroupMember> members = group.getMembers();
                Stream<? extends GroupMember> stream = members.asStream();
                stream.forEach(member -> {
                    Users user;
                    // 构建成员对象
                    Users.UsersBuilder builder = Users.builder()
                            .code(member.getId().toString())
                            .name(member.getUsername())
                            .regTime(new Date(member.getJoinTime().getMillisecond()));
                    // 进行判断
                    if (member.isOwner()) {
                        user = builder.permission(0)
                                .build();
                    } else if (member.isAdmin()) {
                        user = builder.permission(1)
                                .build();
                    } else {
                        user = builder.permission(2)
                                .build();
                    }
                    usersService.addUserSimBot(user);
                });
                Messages messages = new MessagesBuilder()
                        .at(author.getId())
                        .text("\n已刷新群，请进入后台进行查询，并查看日志")
                        .build();
                group.sendBlocking(messages);
            }
            /*
                查询黑名单
             */
            case "h" -> {
                Items<GroupMember> members = group.getMembers();
                Stream<? extends GroupMember> stream = members.asStream();
                MessagesBuilder messages = new MessagesBuilder()
                        .at(author.getId())
                        .text("下面是黑名单成员，请及时清理:\n");
                stream.forEach(member -> {
                    String code = member.getId().toString();
                    Users data = usersService.getUserByCodeSimBot(code);
                    if (data == null) {
                        group.sendBlocking(messages.text("发现成员未添加入数据库中，请先使用 [! s] 指令刷新群成员").build());
                        return;
                    }
                    if (data.getStatus()) {
                        messages.at(member.getId()).text("\n");
                    }
                });
                group.sendBlocking(messages.build());
            }
            /*
                通过 QQ 查询用户
             */
            case "get" -> {
                Users user = usersService.getUserByCodeSimBot(s[2]);
                if (user == null) {
                    Messages build = new MessagesBuilder()
                            .at(author.getId())
                            .text("\n不存在此用户")
                            .build();
                    group.sendBlocking(build);
                    return;
                }

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

                String format = simpleDateFormat.format(user.getRegTime());
                Messages build = new MessagesBuilder()
                        .at(author.getId())
                        .text("查询结果如下：\n")
                        .text("QQ: " + user.getCode())
                        .text("\n昵称: " + user.getName())
                        .text("\n入群时间：" + format)
                        .text("\n黑名单: " + ((user.getStatus()) ? "是" : "不是"))
                        .build();
                group.sendBlocking(build);
            }
            /*
                刷新集合
             */
            case "break" -> {
                updateMessages();
                Messages build = new MessagesBuilder()
                        .at(author.getId())
                        .text("\n刷新成功")
                        .build();
                group.sendBlocking(build);
            }
        }
    }
}