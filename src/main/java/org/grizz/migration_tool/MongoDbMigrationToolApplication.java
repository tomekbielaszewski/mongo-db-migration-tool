package org.grizz.migration_tool;

import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class MongoDbMigrationToolApplication {
    private MongoOperations mongoOperations;

    @Autowired
    private Migrator migrator;

    public static void main(String[] args) {
        SpringApplication.run(MongoDbMigrationToolApplication.class, args);
    }

    @PostConstruct
    public void run() {
        mongoOperations = new MongoTemplate(new SimpleMongoDbFactory(new MongoClient(), migrator.getDBName()));
        migrate(migrator.getCollectionName(), migrator.getQuery(), migrator);
    }

    private void migrate(String collectionName, DBObject query, Converter<DBObject, DBObject> converter) {
        DBCollection collection = mongoOperations.getCollection(collectionName);
        DBCursor dbCursor = collection.find(query);
        List<DBObject> convertedObjects = new ArrayList<>();

        while (dbCursor.hasNext()) {
            DBObject object = dbCursor.next();
            convertedObjects.add(converter.convert(object));
        }

//        System.out.println(convertedObjects.size());
        collection.drop();
        collection.insert(convertedObjects);
    }
}
