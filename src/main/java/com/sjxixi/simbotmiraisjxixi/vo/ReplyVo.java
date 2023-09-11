package com.sjxixi.simbotmiraisjxixi.vo;

import com.sjxixi.simbotmiraisjxixi.entity.AtReply;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReplyVo {
    AtReply atReply;
    List<AtReply> atReplyList;
}
