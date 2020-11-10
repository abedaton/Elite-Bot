package com.chronoxx.elitebot.command.commands.role;

import com.chronoxx.elitebot.command.CommandContext;
import com.chronoxx.elitebot.command.ICommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;

public class RoleAddCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final User author = ctx.getAuthor();
        if(!author.isBot()){
            final Member member = ctx.getGuild().getMember(author);
            final String value = String.join(" ", ctx.getArgs());
            System.out.println(value);
            //member.getRoles().add()
        }
    }

    @Override
    public String getName() {
        return "Role";
    }

    @Override
    public String getHelp() {
        return "Usage : Role add <role>";
    }
}
