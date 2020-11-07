package com.chronoxx.elitebot.command.commands.music;

import com.chronoxx.elitebot.command.CommandContext;
import com.chronoxx.elitebot.command.ICommand;
import com.chronoxx.elitebot.lavaplayer.GuildMusicManager;
import com.chronoxx.elitebot.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class StopCommand implements ICommand {
    @SuppressWarnings("ConstantConditions")
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final Member selfMember = ctx.getSelfMember();
        final GuildVoiceState selfVoiceState = selfMember.getVoiceState();

        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();
        if (!memberVoiceState.inVoiceChannel()){
            channel.sendMessage("You need to be in a voice channel for this to work!").queue();
            return;
        }

        if (!selfVoiceState.inVoiceChannel()){
            channel.sendMessage("I need to be in a voice channel for this to work!").queue();
            return;
        }

        if (!memberVoiceState.getChannel().equals(selfVoiceState.getChannel())){
            channel.sendMessage("You need to be in the same voice channel as me for this to work!").queue();
            return;
        }


        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());

        musicManager.scheduler.player.stopTrack();
        musicManager.scheduler.queue.clear();

        channel.sendMessage("The player has been stopped and the queue has been cleared!").queue();
    }

    @Override
    public String getName() {
        return "stop";
    }

    @Override
    public String getHelp() {
        return "Stop the current song and clear the queue";
    }

    @Override
    public List<String> getAliases() {
        return List.of("clear", "reset");
    }
}
