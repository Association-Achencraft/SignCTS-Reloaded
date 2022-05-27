package achen.signcts;

import io.lettuce.core.*;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

public class DataSource {

    private static RedisClient redisClient;
    private static StatefulRedisConnection<String, String> connection;

    public static void connect() {
        String loginpath = Signcts.config.get("redis-password")+"@"+Signcts.config.get("redis-server")+":"+Signcts.config.get("redis-port");
        redisClient = RedisClient.create(RedisURI.create("redis://"+loginpath));
        connection = redisClient.connect();
        Signcts.instance.getLogger().info("Connected to Redis");
    }

    public static void disconnect()
    {
        connection.close();
        redisClient.shutdown();
    }

    public static void add(String idsae) {
        RedisCommands<String, String> syncCommands = connection.sync();
        if(syncCommands.get(idsae) == null)
        {
            syncCommands.set(idsae, "vide");
        }
    }

    public static void remove(String idsae) {
        //si encore des panneaux ont cet IDSAE, on garde
        for (MySign s : Signcts.signs) {
            if(s.idsae.equalsIgnoreCase(idsae))  {
                return;
            }
        }
        //sinon on supprime
        RedisCommands<String, String> syncCommands = connection.sync();
        syncCommands.del(idsae);

    }

    public static String get(String idsae) {
        RedisCommands<String, String> syncCommands = connection.sync();
        String val = syncCommands.get(idsae);
        if(val == "" || val == null)
        {
            return "NO DATA";
        }
        return val;
    }
}
