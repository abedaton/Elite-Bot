package com.chronoxx.elitebot.command.commands;

import com.chronoxx.elitebot.command.CommandContext;
import com.chronoxx.elitebot.command.ICommand;
import net.dv8tion.jda.api.JDA;

public class PingCommand implements ICommand {

    @Override
    public void handle(CommandContext ctx) {
        JDA jda = ctx.getJDA();

        jda.getRestPing().queue((ping) -> ctx.getChannel().sendMessageFormat("The bot ping is: %sms\nWS ping: %sms", ping, jda.getGatewayPing()).queue());
    }

    @Override
    public String getName() {
        return "ping";
    }
}
