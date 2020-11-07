package com.chronoxx.elitebot.command.commands.music;

import com.chronoxx.elitebot.command.CommandContext;
import com.chronoxx.elitebot.command.ICommand;
import com.chronoxx.elitebot.lavaplayer.GuildMusicManager;
import com.chronoxx.elitebot.lavaplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;


public class QueueCommand implements ICommand {

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
        final BlockingQueue<AudioTrack> queue = musicManager.scheduler.queue;
        final StringBuilder builder = new StringBuilder();
        boolean isPlaying = false;
        if (musicManager.audioPlayer.getPlayingTrack() != null){
            final AudioTrackInfo info = musicManager.audioPlayer.getPlayingTrack().getInfo();
            builder.append("Currently playing song: `")
                    .append(info.title)
                    .append("` by `")
                    .append(info.author)
                    .append("` [`")
                    .append(Helper.formatTime(musicManager.audioPlayer.getPlayingTrack().getPosition()))
                    .append("`]\n");
            isPlaying = true;
        }

        if (queue.isEmpty()){
            if (!isPlaying) {
                channel.sendMessage("The queue is currently empty").queue();
            } else {
                builder.append("There is no other song in the queue!");
                String string = builder.toString();
                channel.sendMessage(string).queue();
            }
            return;
        }

        final int trackCount = Math.min(queue.size(), 20);
        final List<AudioTrack> trackList = new ArrayList<>(queue);
        builder.append("**Current Queue:**\n");

        for (int i = 0; i < trackCount; i++){
            final AudioTrack track = trackList.get(i);
            final AudioTrackInfo info = track.getInfo();

            builder.append("#")
                    .append((i + 1))
                    .append(" `")
                    .append(info.title)
                    .append("` by `")
                    .append(info.author)
                    .append("` [`")
                    .append(Helper.formatTime(track.getDuration()))
                    .append("`]\n");
        }

        if (trackList.size() > trackCount){
            builder.append("And `")
                    .append((trackList.size() - trackCount))
                    .append("` more...");
        }

        channel.sendMessage(builder.toString()).queue();
    }

    @Override
    public String getName() {
        return "queue";
    }

    @Override
    public String getHelp() {
        return "Shows the queued song";
    }
}
