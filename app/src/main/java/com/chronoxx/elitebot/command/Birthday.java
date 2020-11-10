package com.chronoxx.elitebot.command;

import com.chronoxx.elitebot.Config;
import com.chronoxx.elitebot.MongoDatabaseManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;


public class Birthday implements ICommand {
    final MongoDatabaseManager mongoDatabaseManager = new MongoDatabaseManager("EliteServer", "Birthdays");
    TextChannel channel;
    @Override
    public void handle(CommandContext ctx) {
        channel = ctx.getChannel();
        if (ctx.getArgs().isEmpty()){
            channel.sendMessage("Please specify an action! See `" + Config.get("prefix") + "help birthday` for more information!").queue();
            return;
        }


        ArrayList<Member> members = new ArrayList<>(ctx.getMessage().getMentionedMembers());
        ArrayList<String> newArgs = new ArrayList<>(ctx.getArgs());
        if (members.isEmpty()){
            members.add(0, ctx.getMember());
            newArgs.add(1, String.valueOf(ctx.getMember()));
        }

        Member member = members.get(0);
        final String action = newArgs.get(0);

        switch (action){
            case "add":
                this.add(newArgs, ctx, member);
                break;
            case "get":
                this.get(ctx, member);
                break;
            case "modify":
                if (newArgs.size() != 3){
                    channel.sendMessage("Please provide a date !").queue();
                } else {
                    channel.sendMessage("This is not yet implemented :sob:").queue();
                }
                break;
            default:
                channel.sendMessage("Unknown command " + action + "!").queue();
                break;
        }
    }





    private void add(ArrayList<String> newArgs, CommandContext ctx, Member member) {
        if (newArgs.size() != 3){
            channel.sendMessage("Please provide a date !").queue();
        } else {
            if (rightFormat(newArgs.get(2))) {
                if (mongoDatabaseManager.addBirthday(ctx.getGuild().getId(), member.getEffectiveName(), newArgs.get(2))){
                    channel.sendMessage("The user `" + member.getUser().getName() + "` has been added to the database!").queue();
                } else {
                    channel.sendMessage("This user is already in the database !").queue();
                }
            } else {
                channel.sendMessage("Invalid date format ! Please use this format: " + Config.get("date_format")).queue();
            }
        }
    }


    private void get(CommandContext ctx, Member member){
        String date = mongoDatabaseManager.getUser(ctx.getGuild().getId(), member.getEffectiveName());
        if (date != null){
            String newDate = convertDateToBigMonths(date);
            if (newDate != null){
                channel.sendMessage("The birthday of " + member.getEffectiveName() + " is the: " + newDate).queue();
            } else {
                channel.sendMessage("Error in parsing..").queue();
            }
        } else {
            channel.sendMessage("This user is not yet in the database, please `add` it first!").queue();
        }
    }


    private boolean rightFormat(String date) {
        return date.matches("([0-9]{2})/([0-9]{2})/([0-9]{4})");
    }

    private String convertDateToBigMonths(String oldDate){
        Locale locale = new Locale("en", "US");
        SimpleDateFormat oldFormat = new SimpleDateFormat(Config.get("INPUT_DATE_FORMAT"), locale);
        SimpleDateFormat newFormat = new SimpleDateFormat(Config.get("PRINT_DATE_FORMAT"), locale);
        try {
            return newFormat.format(oldFormat.parse(oldDate));
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }



    @Override
    public String getName() {
        return "birthday";
    }

    @Override
    public String getHelp() {
        return "Manage the birthday of people!\n" +
                "Usage: `" + Config.get("prefix") + "birthday add|modify @<user> <date>`\n" +
                "Or: `" + Config.get("prefix") + "birthday get @<user>`";
    }
}
