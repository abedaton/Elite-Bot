package com.chronoxx.elitebot;


import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.util.EnumSet;


public class Main {

    private Main(){
        ((LoggerContext) LoggerFactory.getILoggerFactory()).getLogger("org.mongodb.driver").setLevel(Level.WARN);
        final Logger LOGGER = LoggerFactory.getLogger(Listener.class);

        LOGGER.info("Database Connected");

        WebUtils.setUserAgent("Mozilla/5.0 Chronoxx Elite Bot#1416 / chronoxx#7474");
        String testPrefix = Config.get("token_test");
        if (testPrefix == null){
            testPrefix = Config.get("token");
        }
        try {
            JDABuilder.createDefault(testPrefix,
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
                    .setActivity(Activity.playing(" .help"))
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
