package com.sjxixi.simbotmiraisjxixi;

import love.forte.simboot.spring.autoconfigure.EnableSimbot;
import net.mamoe.mirai.utils.BotConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import xyz.cssxsh.mirai.tool.FixProtocolVersion;

@SpringBootApplication
@EnableSimbot
public class Main {
    public static void main(String[] args) {
        FixProtocolVersion.fetch(BotConfiguration.MiraiProtocol.ANDROID_PHONE, "8.9.63");
        SpringApplication.run(Main.class, args);
    }
}