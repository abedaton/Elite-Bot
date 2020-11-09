package com.chronoxx.elitebot.command.commands;

import com.chronoxx.elitebot.Config;
import com.chronoxx.elitebot.command.CommandContext;
import com.chronoxx.elitebot.command.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.awt.*;
import java.util.List;

public class UserInfoCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        if (ctx.getArgs().isEmpty()){
            channel.sendMessage("Usage: `" + Config.get("prefix") + "userinfo <user>`").queue();
            return;
        }

        if (ctx.getMessage().getMentionedMembers().isEmpty()){
            channel.sendMessage("Please tag the member!").queue();
            return;
        }
        List<Member> members = ctx.getEvent().getGuild().getMembers();
        if (members.isEmpty()){
            channel.sendMessage("User " + ctx.getArgs().get(0) + " not found!").queue();
            return;
        }
        Member member = ctx.getMessage().getMentionedMembers().get(0);
        User user = member.getUser();
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(user.getName() + "'s Info:");
        String avatarUrl = user.getAvatarUrl();
        embed.setThumbnail(avatarUrl);
        embed.setColor(Color.GREEN);
        embed.addField("Name", user.getName(), true);
        embed.addField("Online Status: ", member.getOnlineStatus().toString(), true);
        if (member.getTimeBoosted() != null) {
            embed.addField("Boosted since: ", member.getTimeBoosted().getDayOfMonth() + ":" +
                    member.getTimeBoosted().getMonth() + ":" + member.getTimeBoosted().getYear(), true);
        }
        embed.addField("Server joined: ", member.getTimeJoined().getDayOfMonth() + ":" +
                member.getTimeJoined().getMonth() + ":" +
                       member.getTimeJoined().getYear(), false);
        embed.addField("Account Creation: ", member.getTimeCreated().getDayOfMonth() + ":" +
                member.getTimeCreated().getMonth() + ":" +
                member.getTimeCreated().getYear(), true);

        ctx.getChannel().sendMessage(embed.build()).queue();
    }

    @Override
    public String getName() {
        return "userinfo";
    }

    @Override
    public String getHelp() {
        return "Displays the information of a user." +
                "Usage: `" + Config.get("prefix") + "userinfo <user>`";
    }

    @Override
    public List<String> getAliases() {
        return List.of("info", "user");
    }
}
