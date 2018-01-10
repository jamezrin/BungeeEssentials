
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Map;

public class RedisConnectionTest {
    @Test
    public void test() {
        try {
            String host = "192.168.1.124";
            int port = 6379;
            int timeout = 2000;
            String password = "";
            if (!password.matches(".*\\w.*")) {
                password = null;
            }
            JedisPool pool = new JedisPool(new JedisPoolConfig(), host, port, timeout, password);
            try (Jedis jedis = pool.getResource()) {
                //Lo que sea para poner columnas
                jedis.hset("rce:lrp", "uuid", "MrJaime");

                //Mapas
                jedis.hset("rce:rs", "MrJaime", "rubiana61");
                jedis.hset("rce:rs", "rubiana61", "MrJaime");

                Map<String, String> map = jedis.hgetAll("rce:rs");
                map.forEach((key, value) -> {
                    System.out.println(String.format("Got key: %s with value: %s", key, value));
                });
                System.out.println("Successfully connected to redis!");
            }
        } catch (Exception e) {
            System.out.println("Unable to connect to redis");
        }
    }
}
