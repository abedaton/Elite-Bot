package com.chronoxx.elitebot.command.commands.role;

import com.chronoxx.elitebot.command.CommandContext;
import com.chronoxx.elitebot.command.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class RolesCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final List<Role> listRoles = ctx.getGuild().getRoles();
        final TextChannel channel = ctx.getChannel();
        final EmbedBuilder eb = new EmbedBuilder();
        final ArrayList<String> roleToPrint = new ArrayList<String>();
        eb.setTitle("Roles");
        eb.setColor(Color.orange);
        roleToPrint.add("773217705222340631");

        for (Role role : listRoles) {
            //if (!role.getName().equals("@everyone") && roleToPrint.contains(role.getId())) {
            if (!role.getName().equals("@everyone")){
                eb.appendDescription(role.getAsMention());
                eb.appendDescription("\n");
            }
        }

        channel.sendMessage(eb.build()).queue();

    }

    @Override
    public String getName() {
        return "roles";
    }

    @Override
    public String getHelp() {
        return "Print all Roles ";
    }


}
