package com.chronoxx.elitebot.command.commands.embed;

import com.chronoxx.elitebot.Config;
import com.chronoxx.elitebot.command.CommandContext;
import com.chronoxx.elitebot.command.ICommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CreateEmbed implements ICommand {
    private final List<Member> doing = new ArrayList<>();
    @Override
    public void handle(CommandContext ctx) {
        final Member sender = ctx.getMember();
        final TextChannel channel = ctx.getChannel();
        if (!sender.hasPermission(Permission.MESSAGE_MANAGE)){
            channel.sendMessage("You don't have permission to create an embed!").queue(message -> message.delete().queueAfter(5, TimeUnit.SECONDS));
            return;
        }
        final List<String> args = ctx.getArgs();
        if (args.isEmpty()){
            channel.sendMessage(getHelp()).queue();
            return;
        }
        if (args.get(0).equals("create")){
            if (doing.contains(sender)){
                channel.sendMessage("You can only do one embed at a time!").queue();
                return;
            } else {
                doing.add(sender);
                ctx.getEvent().getJDA().addEventListener(new ManageEmbed(sender, channel));
            }
        }
    }

    @Override
    public String getName() {
        return "embed";
    }

    @Override
    public String getHelp() {
        return "Create an embed dynamically!\n" +
                "Usage: `" + Config.get("prefix") + "embed create`";
    }
}
