package net.alfss.smsserver.sms.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * User: alfss
 * Date: 28.10.13
 * Time: 18:04
 */
public class SuccessSend {

    final Logger logger = (Logger) LoggerFactory.getLogger(SuccessSend.class);

    public void writeLog(String string) {
        logger.info(string);
    }
}
