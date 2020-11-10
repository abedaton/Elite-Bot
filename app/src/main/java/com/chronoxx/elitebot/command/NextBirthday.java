package com.chronoxx.elitebot.command;

import com.chronoxx.elitebot.Config;
import com.chronoxx.elitebot.MongoDatabaseManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.text.DateFormatSymbols;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.*;

public class NextBirthday implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final MongoDatabaseManager mongoDatabaseManager = new MongoDatabaseManager("EliteServer", "Birthdays");
        final TextChannel channel = ctx.getChannel();

        HashMap<Member, String> map = mongoDatabaseManager.getAllBirthdayOfGuild(ctx.getGuild().getId(), ctx);
        if (map.isEmpty()){
            channel.sendMessage("There is no birthday in this server !").queue();
            return;
        }


        SimpleDateFormat formatter = new SimpleDateFormat(Config.get("INPUT_DATE_FORMAT"), new Locale("en", "US"));
        NavigableSet<String> stringDates = new TreeSet<>(map.values());
        NavigableSet<Date> dates = new TreeSet<>();
        for (String date : stringDates){
            try {
                dates.add(formatter.parse(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        Date now = new Date();
        Date earliest = dates.lower(now);
        if (earliest == null){
            channel.sendMessage("There is no birthday in this server !").queue();
            return;
        }

        String earliestString = formatter.format(earliest);
        String[] earliestStringSplit = earliestString.split("/");
        Map<Member, String> members = new HashMap<>();

        for (Map.Entry<Member, String> entry : map.entrySet()){
            String[] dateParts = entry.getValue().split("/");
            if (dateParts[0].equals(earliestStringSplit[0]) && dateParts[1].equals(earliestStringSplit[1])){
                members.put(entry.getKey(), dateParts[2]);
            }
        }



        StringBuilder string = new StringBuilder();
        boolean solo = false;
        final Calendar futureCalendar = Calendar.getInstance();
        futureCalendar.setTime(earliest);
        final DateFormatSymbols dfs = new DateFormatSymbols(Locale.US);
        final String[] months = dfs.getMonths();
        if (members.size() == 1){
            solo = true;
            string.append("The next birthday is " );
        }else {
            string.append("The next birthdays are the ").append(futureCalendar.get(Calendar.DAY_OF_MONTH))
                    .append(" of ")
                    .append(months[futureCalendar.get(Calendar.MONTH)]);
        }
        if (solo){
            string.append(members.keySet().iterator().next().getUser().getName())
                    .append(" the ")
                    .append(futureCalendar.get(Calendar.DAY_OF_MONTH))
                    .append(" of ")
                    .append(months[futureCalendar.get(Calendar.MONTH)])
                    .append(", he will be/she ")
                    .append(Period.between( LocalDate.of(Integer.parseInt(members.values().iterator().next()), Integer.parseInt(earliestStringSplit[1]), Integer.parseInt(earliestStringSplit[0])),
                                    now.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()).getYears())
                    .append(" years old !");
        } else {
            for (Map.Entry<Member, String> entry : members.entrySet()) {
                string
                        .append("\n- ")
                        .append(entry.getKey().getUser().getName())
                        .append(", he will be/she ")
                        .append(Period.between(LocalDate.of(Integer.parseInt(entry.getValue()), Integer.parseInt(earliestStringSplit[1]), Integer.parseInt(earliestStringSplit[0])),
                                        now.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()).getYears())
                        .append(" years old !");
            }
        }
        ctx.getChannel().sendMessage(string.toString()).queue();

    }

    @Override
    public String getName() {
        return "nextbirthday";
    }

    @Override
    public String getHelp() {
        return "Return the next next birthday !";
    }
}
