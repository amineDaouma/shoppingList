package io.pvardanega.shoppinglist.config;

import static io.pvardanega.shoppinglist.config.MongoClient.MONGO_URL;
import static org.assertj.core.api.Assertions.assertThat;
import java.net.UnknownHostException;
import org.junit.Ignore;
import org.junit.Test;

public class MongoClientTest {

    @Ignore("Fails when no MongoDB server is started locally")
    @Test public void
    should_create_client_from_mongo_url() throws UnknownHostException {
        System.setProperty(MONGO_URL, "mongodb://user:password@localhost:27017/db");

        MongoClient client = new MongoClient();

        assertThat(client).isNotNull();
    }

    @Test(expected = IllegalStateException.class) public void
    should_fail_to_create_mongo_client_when_property_MONGOHQ_URL_is_not_provided() throws UnknownHostException {
        new MongoClient();
    }
}
