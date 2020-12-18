package rz.dingtalk;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Demo {

    private static final Logger log = LoggerFactory.getLogger(Demo.class);

    public static void main(String[] args) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date now = new Date();

        Date target = sdf.parse("2020-09-12 12:47:00");

        log.info("now={}, target={}, after={}", sdf.format(now), sdf.format(target), now.after(target));
    }

}

