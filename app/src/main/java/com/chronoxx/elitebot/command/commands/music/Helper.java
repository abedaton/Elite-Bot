package com.chronoxx.elitebot.command.commands.music;

import org.apache.commons.validator.routines.UrlValidator;

import java.util.concurrent.TimeUnit;

public class Helper {
    public static boolean isUrl(String url){
        UrlValidator urlValidator = new UrlValidator();
        return urlValidator.isValid(url);
    }

    public static String formatTime(long timeInMillis) {
        final long hours = timeInMillis / TimeUnit.HOURS.toMillis(1);
        final long minutes = timeInMillis / TimeUnit.MINUTES.toMillis(1);
        final long seconds = timeInMillis % TimeUnit.MINUTES.toMillis(1) / TimeUnit.SECONDS.toMillis(1);

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }


}
