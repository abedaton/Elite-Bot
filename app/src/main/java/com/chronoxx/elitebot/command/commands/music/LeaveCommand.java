package com.chronoxx.elitebot.command.commands.music;

import com.chronoxx.elitebot.command.CommandContext;
import com.chronoxx.elitebot.command.ICommand;
import com.chronoxx.elitebot.lavaplayer.GuildMusicManager;
import com.chronoxx.elitebot.lavaplayer.PlayerManager;

import net.dv8tion.jda.api.managers.AudioManager;

public class LeaveCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        StopCommand stopCommand = new StopCommand();
        stopCommand.handle(ctx);
        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());

        musicManager.scheduler.player.stopTrack();
        musicManager.scheduler.queue.clear();
        final AudioManager audioManager = ctx.getGuild().getAudioManager();
        audioManager.closeAudioConnection();
    }

    @Override
    public String getName() {
        return "leave";
    }

    @Override
    public String getHelp() {
        return "Leave the voice channel and clear all queue.";
    }
}
