package com.example.eigenfield;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;

import java.time.LocalDateTime;
import java.util.Map;

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

    @GetMapping(path={"/visit", "/visit/{ipaddr}"})
    public long visit(@PathVariable(required = false, name = "ipaddr") String ipaddr,
                      @RequestParam(required = false) Map<String, String> qparams)
    {
        qparams.forEach((a, b) -> {
            System.out.println(String.format("%s -> %s",a,b));
        });

        DemoApplication demo = ctx.getBean(DemoApplication.class);
        Jedis jedis = new Jedis(demo.redis_host, demo.redis_port);
        if (!demo.redis_pass.isEmpty()) {
            jedis.auth(demo.redis_pass);
        }
        jedis.connect();
        long count = jedis.incr("resumeviews");

        if (ipaddr != null) {
            String ldt = LocalDateTime.now().toString();
            jedis.lpush(ipaddr, ldt);
        }

        IPAddressInterceptor interceptor = ctx.getBean(IPAddressInterceptor.class);
        if (interceptor != null && interceptor.ipaddr != null && !interceptor.ipaddr.isEmpty()) {
            jedis.lpush("remote", interceptor.ipaddr);
        } else {

        }

        jedis.disconnect();
        return count;
    }
}
