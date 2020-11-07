package com.chronoxx.elitebot.command.commands;

import com.chronoxx.elitebot.CommandManager;
import com.chronoxx.elitebot.Config;
import com.chronoxx.elitebot.command.CommandContext;
import com.chronoxx.elitebot.command.ICommand;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class HelpCommands implements ICommand {

    private final CommandManager manager;

    public HelpCommands(CommandManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(CommandContext ctx) {
        List<String> args = ctx.getArgs();
        TextChannel channel = ctx.getChannel();

        if (args.isEmpty()){
            StringBuilder builder = new StringBuilder();
            builder.append("List of commands:\n");

            manager.getCommands().stream().map(ICommand::getName).forEach((it) -> builder.append("`").append(Config.get("prefix")).append(it).append("`\n"));

            builder.append("You can do: `").append(Config.get("prefix")).append("help <command>` to see the help message for this command");
            channel.sendMessage(builder.toString()).queue();
            return;
        }

        String search = args.get(0);
        ICommand command = manager.getCommand(search);

        if (command == null){
            channel.sendMessage("The command " + search + " does not exists!").queue();
            return;
        }

        channel.sendMessage(command.getHelp()).queue();
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getHelp() {
        return "Shows the list of commands in the bot\n" +
                "Usage: `" + Config.get("prefix") + "help [command]`";
    }

    @Override
    public List<String> getAliases() {
        return List.of("commands", "cmds", "commandlist");
    }
}
