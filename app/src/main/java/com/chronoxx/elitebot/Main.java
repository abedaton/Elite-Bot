package com.chronoxx.elitebot;


import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Accumulators;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import me.duncte123.botcommons.web.WebUtils;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.LoginException;
import java.util.Arrays;
import java.util.EnumSet;


public class Main {

    private Main(){
        ((LoggerContext) LoggerFactory.getILoggerFactory()).getLogger("org.mongodb.driver").setLevel(Level.WARN);
        final Logger LOGGER = LoggerFactory.getLogger(Listener.class);


        LOGGER.info("Database Connected");



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
