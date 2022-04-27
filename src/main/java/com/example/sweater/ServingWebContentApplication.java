package com.example.sweater;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import java.util.HashMap;

@SpringBootApplication
public class ServingWebContentApplication {
    public static void main(String[] args) {
        HashMap<String, Object> props = new HashMap<>();
        props.put("server.port", 9000);
        new SpringApplicationBuilder()
                .sources(ServingWebContentApplication.class)
                .properties(props)
                .run(args);
      //SpringApplication.run(ServingWebContentApplication.class, args);
    }
}
