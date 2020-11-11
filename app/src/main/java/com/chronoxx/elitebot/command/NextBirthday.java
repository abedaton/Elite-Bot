package com.chronoxx.elitebot.command;

import com.chronoxx.elitebot.Config;
import com.chronoxx.elitebot.MongoDatabaseManager;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.text.DateFormatSymbols;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.MonthDay;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

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


        Collection<String> stringDates = map.values();
        List<LocalDate> dates = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        for (String date : stringDates){
            dates.add(LocalDate.parse(date, formatter));
        }
        System.out.println(dates);

        Function<LocalDate, Integer> key = (d) -> d.getMonthValue() * 100 + d.getDayOfMonth();
        Map<Integer, List<LocalDate>> lookup =  dates.stream().collect(Collectors.groupingBy(key));
        List<Integer> searchIndex = lookup.keySet().stream().sorted().collect(Collectors.toList());
        int index = Collections.binarySearch(searchIndex, key.apply(LocalDate.now()));

        if (index < 0){
            index = -index -1;
        }
        if (index >= searchIndex.size()){
            index = 0;
        }

        List<LocalDate> localDates = lookup.get(searchIndex.get(index));

        if (localDates.isEmpty()){
            channel.sendMessage("There is no birthday in this server !").queue();
            return;
        }

        String earliestString = formatter.format(localDates.get(0));
        String[] earliestStringSplit = earliestString.split("/");
        Map<Member, String> members = new HashMap<>();

        for (Map.Entry<Member, String> entry : map.entrySet()){
            String[] dateParts = entry.getValue().split("/");
            if (dateParts[0].equals(earliestStringSplit[0]) && dateParts[1].equals(earliestStringSplit[1])){
                members.put(entry.getKey(), dateParts[2]);
            }
        }


        Date now = new Date();
        StringBuilder string = new StringBuilder();
        boolean solo = false;
        final Calendar futureCalendar = Calendar.getInstance();
        futureCalendar.setTime(java.sql.Date.valueOf(localDates.get(0)));
        final DateFormatSymbols dfs = new DateFormatSymbols(Locale.US);
        final String[] months = dfs.getMonths();
        if (members.size() == 1){
            solo = true;
            string.append("The next birthday is: " );
        }else {
            string.append("The next birthdays are: ").append(futureCalendar.get(Calendar.DAY_OF_MONTH))
                    .append(" of ")
                    .append(months[futureCalendar.get(Calendar.MONTH)]);
        }
        if (solo){
            string.append(members.keySet().iterator().next().getUser().getName())
                    .append(", the ")
                    .append(futureCalendar.get(Calendar.DAY_OF_MONTH))
                    .append(" of ")
                    .append(months[futureCalendar.get(Calendar.MONTH)])
                    .append(", they will be ")
                    .append(Period.between( LocalDate.of(Integer.parseInt(members.values().iterator().next()), Integer.parseInt(earliestStringSplit[1]), Integer.parseInt(earliestStringSplit[0])),
                                    now.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()).getYears() + 1)
                    .append(" years old !");
        } else {
            for (Map.Entry<Member, String> entry : members.entrySet()) {
                string
                        .append("\n- ")
                        .append(entry.getKey().getUser().getName())
                        .append(", they will be ")
                        .append(Period.between(LocalDate.of(Integer.parseInt(entry.getValue()), Integer.parseInt(earliestStringSplit[1]), Integer.parseInt(earliestStringSplit[0])),
                                        now.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()).getYears() + 1)
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
