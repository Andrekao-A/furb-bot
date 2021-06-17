import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.util.TimeZone;

@SpringBootApplication
@ComponentScan(basePackages = {

        "cronapp.framework.authentication.normal",
        "cronapp.framework.authentication.security",
        "cronapp.framework.rest",
        "cronapp.framework.scheduler",
        "auth.permission",
        "api.rest.events",
        "api.rest.webservices",
        "reports",
        "cronapi",
        "cronapi.chatbot",
        "blockly",
        "app", "diagram.hygia"
})
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public class SpringBootMain extends SpringBootServletInitializer {

    static {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }


    public static void main(String[] args) throws Exception {
        CronappApplication.run(args);
    }
}
