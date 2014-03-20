package net.alfss.smsserver.http;

import org.eclipse.jetty.server.AbstractNCSARequestLog;
import org.eclipse.jetty.server.RequestLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * User: alfss
 * Date: 25.10.13
 * Time: 18:28
 */
public class WebServerRequestLog  extends AbstractNCSARequestLog implements RequestLog {

    final Logger logger = (Logger) LoggerFactory.getLogger(WebServerRequestLog.class);

    public WebServerRequestLog() {
    }

    @Override
    protected boolean isEnabled() {
        return logger != null;
    }

    @Override
    public void write(String requestEntry) throws IOException {
        logger.info(requestEntry);
    }
}
