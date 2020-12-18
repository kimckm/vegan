package rz.dingtalk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import rz.dingtalk.manager.DingTalkManager;

@SpringBootApplication
public class DingTalkApplication {

    private static final Logger log = LoggerFactory.getLogger(DingTalkApplication.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DingTalkApplication.class);
        DingTalkManager dingTalkManager = context.getBean(DingTalkManager.class);

        String userId = dingTalkManager.fetchUserId("15820268447");
        log.info("userId={}", userId);
    }

}
