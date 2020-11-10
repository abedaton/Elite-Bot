package com.chronoxx.elitebot;

import com.chronoxx.elitebot.command.Birthday;
import com.chronoxx.elitebot.command.CommandContext;
import com.chronoxx.elitebot.command.ICommand;
import com.chronoxx.elitebot.command.NextBirthday;
import com.chronoxx.elitebot.command.commands.*;
import com.chronoxx.elitebot.command.commands.embed.CreateEmbed;
import com.chronoxx.elitebot.command.commands.music.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class CommandManager {

    private final List<ICommand> commands = new ArrayList<>();

    public CommandManager(){
        addCommand(new PingCommand());
        addCommand(new HelpCommands(this));
        addCommand(new HasteCommand());
        addCommand(new JoinCommand());
        addCommand(new PlayCommand());
        addCommand(new StopCommand());
        addCommand(new SkipCommand());
        addCommand(new NowPlayingCommand());
        addCommand(new QueueCommand());
        addCommand(new LeaveCommand());
        addCommand(new UserInfoCommand());
        addCommand(new Birthday());
        addCommand(new NextBirthday());
        addCommand(new CreateEmbed());
    }

    private void addCommand(ICommand cmd){
        boolean alreadyHave = this.commands.stream().anyMatch((it) -> it.getName().equalsIgnoreCase(cmd.getName()));

        if (alreadyHave){
            throw new IllegalArgumentException("A command with this name is already present");
        }

        commands.add(cmd);
    }


    public List<ICommand> getCommands(){
        return commands;
    }


    @Nullable
    public ICommand getCommand(String search){
        String searchLower = search.toLowerCase();

        for (ICommand cmd : this.commands) {
            if (cmd.getName().equals(searchLower) || cmd.getAliases().contains(searchLower)){
                return cmd;
            }
        }
        return null;
    }

    void handle(GuildMessageReceivedEvent event){
        String[] split = event.getMessage().getContentRaw()
                .replaceFirst("(?i)" + Pattern.quote(Config.get("prefix")), "")
                .split("\\s+");

        String invoke = split[0].toLowerCase();
        ICommand cmd = this.getCommand(invoke);

        if (cmd != null){
            event.getChannel().sendTyping().queue();
            List<String> args = Arrays.asList(split).subList(1, split.length);

            CommandContext ctx = new CommandContext(event, args);

            cmd.handle(ctx);
        } else{
            // Command not found
        }
    }
}
