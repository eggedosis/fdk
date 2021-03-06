package no.dcat.gdoc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by dask on 19.12.2016.
 */
@SpringBootApplication
@EnableScheduling
public class Application {
    public static void main(String[] args) {

        SpringApplication.run(Application.class, args);
    }
}
