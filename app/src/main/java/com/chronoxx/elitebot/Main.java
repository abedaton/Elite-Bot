package com.chronoxx.elitebot;


import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;
import java.util.EnumSet;

public class Main {

    private Main(){

        WebUtils.setUserAgent("Mozilla/5.0 Chronoxx Elite Bot#1416 / chronoxx#7474");

        try {
            JDABuilder.createDefault(Config.get("token"),
                    GatewayIntent.GUILD_MEMBERS,
                    GatewayIntent.GUILD_MESSAGES,
                    GatewayIntent.GUILD_VOICE_STATES)
                    .disableCache(EnumSet.of(
                            CacheFlag.CLIENT_STATUS,
                            CacheFlag.ACTIVITY,
                            CacheFlag.EMOTE
                    ))
                    .enableCache(CacheFlag.VOICE_STATE)
                    .addEventListeners(new Listener())
                    .setActivity(Activity.watching(" the Elite Team"))
                    .build();
        } catch (LoginException e) {
            System.out.println("Error while launching the bot...");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Main();
    }
}
