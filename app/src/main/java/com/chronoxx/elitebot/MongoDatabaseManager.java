package com.chronoxx.elitebot;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongoDatabaseManager {
    private final MongoDatabase mongoDatabase;
    private final MongoCollection<Document> collection;
    public MongoDatabaseManager(String database, String acollection){
        final String mongoUri = Config.get("MongoDB");
        MongoClientURI clientURI = new MongoClientURI(mongoUri);
        MongoClient mongoClient = new MongoClient(clientURI);

        mongoDatabase = mongoClient.getDatabase(database);
        collection = mongoDatabase.getCollection(acollection);
    }

    public void addField(String key1, String value1, String key2, String value2){
        Document document = new Document(key1, value1);
        document.append(key2, value2);
        collection.insertOne(document);
    }


    public String getField(String key1, String value1){
        FindIterable<Document> result = collection.find(new BasicDBObject("Name", value1));
        if (result.first() != null){
            return result.first().get(key1).toString();
        } else {
            return null;
        }
    }

    public boolean exists(String key1, String value1){
        BasicDBObject where  = new BasicDBObject();
        where.put(key1, value1);
        FindIterable<Document> findIterable = collection.find(where);
        return findIterable.first() != null;
    }

    public MongoCollection<Document> getCollection(){
        return collection;
    }
}
