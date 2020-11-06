package com.chronoxx.elitebot;


import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;

public class Main {

    private Main(){
        try {
            JDABuilder.createDefault(Config.get("token"))
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
