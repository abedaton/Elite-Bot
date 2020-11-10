package com.chronoxx.elitebot.command;

import com.chronoxx.elitebot.MongoDatabaseManager;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import org.bson.Document;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class NextBirthday implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final MongoDatabaseManager mongoDatabaseManager = new MongoDatabaseManager("EliteServer", "Birthdays");
        MongoCollection<Document> collection = mongoDatabaseManager.getCollection();
        FindIterable<Document> documents = collection.find();

        Map<Date, String> dates = new HashMap<>();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MMMM/yyyy", Locale.US);
        for (Document document : documents ){
            try {
                dates.put(formatter.parse(document.getString("Birthday")), document.getString("Name"));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        final long now = System.currentTimeMillis();
        Date mini = Collections.min(dates.keySet(), (d1, d2) -> {
            long diff1 = Math.abs(d1.getTime() - now);
            long diff2 = Math.abs(d2.getTime() - now);
            return Long.compare(diff1, diff2);
        });

        List<Member> members = new ArrayList<>();
        for (Map.Entry<Date, String> entry : dates.entrySet()) {
            for (Member member : ctx.getGuild().getMembers()){
                if (member.getUser().getName().equals(entry.getValue()) && entry.getKey().equals(mini)){
                    members.add(member);
                }
            }
        }
        TextChannel textChannel = ctx.getChannel();
        if (members.isEmpty()){
            textChannel.sendMessage("The person is not on this server !").queue();
        } else {
            StringBuilder string = new StringBuilder();
            string.append("The next birthday is: " );
            final Calendar futureCalendar = Calendar.getInstance();
            futureCalendar.setTime(mini);
            final DateFormatSymbols dfs = new DateFormatSymbols(Locale.US);
            final String[] months = dfs.getMonths();
            for (Member member : members){
                string
                        .append("\n- ")
                        .append(member.getUser().getName())
                        .append(" the ")
                        .append(futureCalendar.get(Calendar.DAY_OF_MONTH))
                        .append("/")
                        .append(months[futureCalendar.get(Calendar.MONTH)])
                        .append("/")
                        .append(futureCalendar.get(Calendar.YEAR));
            }
            ctx.getChannel().sendMessage(string.toString()).queue();
        }
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
