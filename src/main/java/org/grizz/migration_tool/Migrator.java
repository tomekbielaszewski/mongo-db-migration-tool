package org.grizz.migration_tool;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Service;

@Service
public class Migrator implements Converter<DBObject, DBObject> {

    public String getDBName() {
        return "keeper";
    }

    String getCollectionName() {
        return "entries";
    }

    DBObject getQuery() {
        return new BasicDBObject();
    }

    @Override
    public DBObject convert(DBObject dbObject) {

        Object value = dbObject.get("value");
        dbObject.put("value", new BasicDBObject("count", value));

        return dbObject;
    }
}
