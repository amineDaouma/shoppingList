package io.pvardanega.shoppinglist.config;

import static io.pvardanega.shoppinglist.config.MongoClient.*;
import static org.apache.commons.lang3.StringUtils.isBlank;
import java.net.UnknownHostException;
import org.jongo.Jongo;
import org.junit.rules.ExternalResource;
import com.mongodb.*;

public class MongoDataInjector extends ExternalResource {

    private static Jongo jongo;
    private final String collectionName;
    private final Object[] data;

    public MongoDataInjector(String collectionName, Object ... data) throws UnknownHostException {
        this.collectionName = collectionName;
        this.data = data;

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

    @Override
    protected void before() throws Throwable {
        if (data.length > 0) {
            jongo.getCollection(collectionName).insert(data);
        }
    }

    @Override
    protected void after() {
        jongo.getCollection(collectionName).drop();
    }
}
