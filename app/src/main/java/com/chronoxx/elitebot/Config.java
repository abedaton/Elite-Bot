package com.chronoxx.elitebot;

import io.github.cdimascio.dotenv.Dotenv;

// So we don't call load everytime
public class Config {

    private static final Dotenv dotenv = Dotenv.load();

    public static String get(String key){
        return dotenv.get(key.toUpperCase());
    }
}
