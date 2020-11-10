package com.chronoxx.elitebot.command;

import com.chronoxx.elitebot.Config;
import com.chronoxx.elitebot.MongoDatabaseManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
                if (newArgs.size() != 3){
                    channel.sendMessage("Please provide a date !").queue();
                } else {

                    addUser(member, newArgs.get(2));
                }
                break;
            case "get":
                getUser(member);
                break;
            case "modify":
                if (newArgs.size() != 3){
                    channel.sendMessage("Please provide a date !").queue();
                } else {
                    modifyUser(member, newArgs.get(2));
                }
                break;
            default:
                channel.sendMessage("Unknown command " + action + "!").queue();
                break;
        }
    }

    private void modifyUser(Member member, String s) {
        channel.sendMessage("This is not yet implemented :sob:").queue();
    }

    private void getUser(Member member) {
        final String date = mongoDatabaseManager.getField("Birthday", member.getUser().getName());
        if (date != null){
            channel.sendMessage("The birthday of " + member.getUser().getName() + " is the: " + date).queue();
        } else {
            channel.sendMessage("This user is not yet in the database, please `add` it first!").queue();
        }
    }

    private void addUser(Member member, String birthday) {
        if (memberExists(member)){
            channel.sendMessage("This user is already in the database !").queue();
        } else {
            Date date = formatBirthday(birthday);
            if (date != null) {
                final Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                if (calendar.get(Calendar.YEAR) < 1900){
                    channel.sendMessage("Invalid date format ! Please use this format: " + Config.get("date_format")).queue();
                    return;
                }
                final DateFormatSymbols dfs = new DateFormatSymbols(Locale.US);
                final String[] months = dfs.getMonths();
                mongoDatabaseManager.addField("Name", member.getUser().getName(), "Birthday",
                        calendar.get(Calendar.DAY_OF_MONTH) + "/" +
                        months[calendar.get(Calendar.MONTH)] + "/" + calendar.get(Calendar.YEAR));
                channel.sendMessage("The user `" + member.getUser().getName() + "` has been added to the database!").queue();
            }
        }
    }

    private Date formatBirthday(String birthday) {
        SimpleDateFormat formatter = new SimpleDateFormat(Config.get("date_format"), Locale.US);
        formatter.setLenient(false);
        try {
            return formatter.parse(birthday);
        } catch (ParseException e) {
            channel.sendMessage("Invalid date format ! Please use this format: " + Config.get("date_format")).queue();
            return null;
        }
    }

    private boolean memberExists(Member member) {
        return mongoDatabaseManager.exists("Name", member.getUser().getName());
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
