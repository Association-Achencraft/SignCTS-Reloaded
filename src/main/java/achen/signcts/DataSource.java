package achen.signcts;

import io.lettuce.core.*;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

public class DataSource {
    //récup data depuis Redis

    private static RedisClient redisClient;
    private static StatefulRedisConnection<String, String> connection;

    public static void connect() {
        redisClient = RedisClient.create(RedisURI.create("redis://lucas-lett.fr:25617"));
        connection = redisClient.connect();
        System.out.println("Connected to Redis");
    }

    public static void disconnect()
    {
        connection.close();
        redisClient.shutdown();
    }

    public static boolean add(String idsae) {

        RedisCommands<String, String> syncCommands = connection.sync();

        String val = syncCommands.get(idsae);
        if(val == "nil")
        {
            System.out.println("yapa");
        }
        else
        {
            System.out.println(val);
        }

        syncCommands.set(idsae, "bar");

        val = syncCommands.get(idsae);
        if(val == "nil")
        {
            System.out.println("yapa");
        }
        else
        {
            System.out.println(val);
        }

        return true;
    }
}
