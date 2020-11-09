package com.chronoxx.elitebot.command.commands.embed;

import com.chronoxx.elitebot.Config;
import com.chronoxx.elitebot.command.commands.music.Helper;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageEmbedEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.beryx.awt.color.ColorFactory;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class ManageEmbed extends ListenerAdapter {
    private final TextChannel channel;
    private final Member sender;
    private EmbedBuilder embedBuilder;
    public ManageEmbed(Member member, TextChannel channel){
        sender = member;
        this.channel = channel;
        embedBuilder = new EmbedBuilder();
        channel.sendMessage("""
                You've entered the build mode! You can now customize your embed !
                Usage: just write `set color|thumbnail|title|description|footer|image <data>`
                Or: `addField <name> <value> <inline>`
                When you are finish, you can test by sending `test` or publish and exit by sending `publish`
                If you want to cancel, write `cancel`""").queue();
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot() || event.isWebhookMessage()){
            return;
        }

        if (event.getChannel().getIdLong() != channel.getIdLong()){
            return;
        }

        if (event.getMessage().getContentRaw().contains("create")){
            return;
        }


        String[] split = event.getMessage().getContentRaw()
                .replaceFirst("(?i)" + Pattern.quote(Config.get("prefix")), "")
                .split("\\s+");

        switch (split[0]) {
            case "test" -> {
                if (!embedBuilder.isEmpty()) {
                    channel.sendMessage(embedBuilder.build()).queue(message -> message.delete().queueAfter(10, TimeUnit.SECONDS));
                } else {
                    channel.sendMessage("You cannot send an empty embed!").queue();
                }
            }
            case "publish" -> {
                if (!embedBuilder.isEmpty()) {
                    channel.sendMessage(embedBuilder.build()).queue();
                    event.getJDA().removeEventListener(this);
                } else {
                    channel.sendMessage("You cannot send an empty embed!").queue();
                }
            }

            case "cancel" -> {
                channel.sendMessage("Embed successfully canceled!").queue(message -> message.delete().queueAfter(5, TimeUnit.SECONDS));
                event.getJDA().removeEventListener(this);
            }
            case "set" -> {
                if (split.length < 2){
                    channel.sendMessage("Please provide argument : `set color|thumbnail|title|description|footer|image <data>`").queue();
                    return;
                }
                switch (split[1]){
                    case "color" -> {
                        if (split.length < 3){
                            channel.sendMessage("Please provide a color !").queue();
                            return;
                        }
                        Color color = ColorFactory.valueOf(split[2]);
                        if (color != null){
                            embedBuilder.setColor(color);
                            channel.sendMessage("Color set!").queue();
                        } else {
                            channel.sendMessage("Invalid color!").queue();
                        }
                    }

                    case "title" -> {
                        if (split.length < 3){
                            channel.sendMessage("Please provide a title !").queue();
                            return;
                        }
                        StringBuilder title = new StringBuilder();
                        for (String arg : Arrays.asList(split).subList(1, split.length)){
                            title.append(arg).append(" ");
                        }
                        embedBuilder.setTitle(title.toString());
                        channel.sendMessage("Title set!").queue();
                    }

                    case "description" -> {
                        if (split.length < 3){
                            channel.sendMessage("Please provide a description !").queue();
                            return;
                        }
                        StringBuilder description = new StringBuilder();
                        for (String arg : split){
                            description.append(arg);
                        }
                        embedBuilder.setDescription(description);
                        channel.sendMessage("Description set!").queue();
                    }

                    case "thumbnail" -> {
                        if (split.length < 3){
                            channel.sendMessage("Please provide an url ").queue();
                            return;
                        }
                        if (Helper.isUrl(split[2])) {
                            embedBuilder = embedBuilder.setThumbnail(split[2]);
                            channel.sendMessage("Thumbnail set!").queue();
                        } else {
                            channel.sendMessage("Please provide a valid url!").queue();
                        }
                    }
                }
            }

            case "addField" -> {
                String[] name = event.getMessage().getContentRaw()
                        .replaceFirst("addField", "")
                        .split("#");

                if (name.length < 3){
                    channel.sendMessage("Not enough arguments: `addField <name> # <content> # <inline>`").queue();
                    return;
                }
                embedBuilder.addField(name[0], name[1], Boolean.parseBoolean(name[2]));
                channel.sendMessage("Field set!").queue();
            }


            default -> {
                channel.sendMessage("""
                Usage: just write `set color|thumbnail|title|description|footer|image <data>`
                Or: `addField <name> # <content> # <inline>`
                When you are finish, you can test by sending `test` or publish and exit by sending `publish`
                If you want to cancel, write `cancel´""").queue();
            }
        }

    }
}
