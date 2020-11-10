package com.example.eigenfield;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

@RestController
@SpringBootApplication
public class DemoApplication {

	@Value("${spring.redis.host}")
	public String redis_host;

	@Value("${spring.redis.pass}")
	public String redis_pass;

	@Value("${spring.redis.port}")
	public int redis_port;

	private static ApplicationContext ctx;

	public static void main(String[] args) {
		ctx = SpringApplication.run(DemoApplication.class, args);
	}

	@GetMapping("/visit")
	public long visit()
	{
        DemoApplication demo = ctx.getBean(DemoApplication.class);
        Jedis jedis = new Jedis(demo.redis_host, demo.redis_port);
        if (!demo.redis_pass.isEmpty()) {
            jedis.auth(demo.redis_pass);
        }
        jedis.connect();
        long count = jedis.incr("resumeviews");
        jedis.disconnect();
        return count;
	}
}
