package com.chronoxx.elitebot;

import com.chronoxx.elitebot.command.CommandContext;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import net.dv8tion.jda.api.entities.Member;
import org.bson.Document;
import org.json.JSONObject;

import java.util.HashMap;


public class MongoDatabaseManager {
    private final MongoCollection<Document> collection;

    public MongoDatabaseManager(String database, String acollection) {
        final String mongoUri = Config.get("MongoDB");
        MongoClientURI clientURI = new MongoClientURI(mongoUri);
        MongoClient mongoClient = new MongoClient(clientURI);

        MongoDatabase mongoDatabase = mongoClient.getDatabase(database);
        collection = mongoDatabase.getCollection(acollection);
    }


    public String getUser(String guildId, String memberName) {
        try (MongoCursor<Document> cursor = collection.find().iterator()) {
            while (cursor.hasNext()) {
                JSONObject json = new JSONObject(cursor.next().toJson());
                if (json.has(guildId)) {
                    if (json.getJSONObject(guildId).getString("Name").equals(memberName)){
                        return json.getJSONObject(guildId).getString("Birthday");
                    }
                }
            }
            return null;
        }
    }

    private boolean isUserInDb(String guildId, String username){
        try (MongoCursor<Document> cursor = collection.find().iterator()) {
            while (cursor.hasNext()) {
                JSONObject json = new JSONObject(cursor.next().toJson());
                if (json.has(guildId)) {
                    if (json.getJSONObject(guildId).getString("Name").equals(username)){
                        return true;
                    }
                }
            }
            return false;
        }
    }


    public boolean addBirthday(String guildId, String username, String date){
        if (isUserInDb(guildId, username)){
            return false;
        }
        Document doc = new Document(guildId, new Document("Name", username).append("Birthday", date));
        collection.insertOne(doc);
        return true;
    }


    public HashMap<Member, String> getAllBirthdayOfGuild(String guildId, CommandContext ctx){
        HashMap<Member, String> map = new HashMap<>();
        try (MongoCursor<Document> cursor = collection.find().iterator()) {
            while (cursor.hasNext()) {
                JSONObject json = new JSONObject(cursor.next().toJson());
                if (json.has(guildId)) {
                    String username = json.getJSONObject(guildId).getString("Name");
                    Member member = ctx.getGuild().getMembersByEffectiveName(username, false).get(0);
                    String date = json.getJSONObject(guildId).getString("Birthday");
                    map.put(member, date);
                }
            }
        }
        return map;
    }

}
