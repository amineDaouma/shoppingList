package io.pvardanega.shoppinglist.config;

import static org.apache.commons.lang3.StringUtils.isBlank;
import java.net.UnknownHostException;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import com.google.inject.Singleton;
import com.mongodb.DB;
import com.mongodb.MongoClientURI;

@Singleton
public class MongoClient {

    public static final String MONGO_URL = "MONGO_URL";
    private final Jongo jongo;

    public MongoClient() throws UnknownHostException {
        String mongohqUrl = isBlank(System.getenv().get(MONGO_URL))
                ? System.getProperty(MONGO_URL)
                : System.getenv().get(MONGO_URL);
        DB db;
        if (mongohqUrl == null) {
            throw new IllegalStateException("Paramter '" + MONGO_URL + "' is missing");
        }
        else {
            MongoClientURI mongoClientURI = new MongoClientURI(mongohqUrl);
            db = new com.mongodb.MongoClient(mongoClientURI).getDB(mongoClientURI.getDatabase());
        }
        jongo = new Jongo(db);
    }

    public MongoCollection getCollection(String collectionName) {
        return jongo.getCollection(collectionName);
    }
}
