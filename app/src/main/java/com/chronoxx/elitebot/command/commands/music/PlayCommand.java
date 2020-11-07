package com.chronoxx.elitebot.command.commands.music;

import com.chronoxx.elitebot.Config;
import com.chronoxx.elitebot.command.CommandContext;
import com.chronoxx.elitebot.command.ICommand;
import com.chronoxx.elitebot.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.managers.AudioManager;

@SuppressWarnings("ConstantConditions")
public class PlayCommand implements ICommand {

    @SuppressWarnings("ConstantConditions")
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();

        if (ctx.getArgs().isEmpty()){
            channel.sendMessage("Correct usage is `" + Config.get("prefix") + "play <youtube link>`").queue();
            return;
        }

        final Member selfMember = ctx.getSelfMember();
        final GuildVoiceState selfVoiceState = selfMember.getVoiceState();

        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if (!memberVoiceState.inVoiceChannel()){
            channel.sendMessage("You need to be in a voice channel for this to work!").queue();
            return;
        }

        if (!selfVoiceState.inVoiceChannel()){
            final AudioManager audioManager = ctx.getGuild().getAudioManager();
            audioManager.openAudioConnection(memberVoiceState.getChannel());
            channel.sendMessageFormat("Connecting to " + memberVoiceState.getChannel().getName()+ "!").queue();
        } else {

            if (!memberVoiceState.getChannel().equals(selfVoiceState.getChannel())) {
                channel.sendMessage("You need to be in the same voice channel as me for this to work!").queue();
                return;
            }
        }


        String link = String.join(" ", ctx.getArgs());

        if (!Helper.isUrl(link)){
            link = "ytsearch:" + link;
        }
        PlayerManager.getInstance().loadAndPlay(channel, link);

    }

    @Override
    public String getName() {
        return "play";
    }

    @Override
    public String getHelp() {
        return "Plays a song/video from youtube\n" +
                "Usage: `" + Config.get("prefix") + "play <youtube link>`";
    }


}
