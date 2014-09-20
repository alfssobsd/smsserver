package net.alfss.smsserver.sms;

import com.google.common.util.concurrent.RateLimiter;
import net.alfss.smsserver.config.GlobalConfig;
import net.alfss.smsserver.database.entity.Channel;
import net.alfss.smsserver.sms.exceptions.SmsServerException;
import net.alfss.smsserver.sms.pool.SmsServerConnectPool;
import net.alfss.smsserver.sms.prototype.AsyncSmsServerChild;
import org.smpp.TimeoutException;
import org.smpp.WrongSessionStateException;
import org.smpp.pdu.EnquireLink;
import org.smpp.pdu.PDUException;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * User: alfss
 * Date: 25.11.13
 * Time: 16:02
 */
public class SmsServerEnquireLink extends AsyncSmsServerChild {

    public SmsServerEnquireLink(GlobalConfig config,
                                Channel channel,
                                SmsServerConnectPool connectPool,
                                int numberConnection,
                                RateLimiter rateLimiter,
                                AtomicInteger seqNumber) {
        super(config, channel, connectPool, numberConnection, rateLimiter, seqNumber);
    }


    @Override
    public void run() {
        setRunning(true);
        errorMessage("start (channel = " + channel.getName() + ")");
        do {
            try {
                sleep(channel.getSmppEnquireLinkInterval() * 1000);
                enquireLinkRequest();
            } catch (InterruptedException e) {
                debugMessage("Interrupted");
                setRunning(false);
            } catch (SmsServerException e) {
                debugMessage("error Invalidated object", e);
            } catch (NullPointerException e) {
                debugMessage("NullPointerException", e);
            } catch (Exception e) {
                debugMessage("WTF Exception!!! " + channel.getName() + " ", e);
            }
        } while (isRunning());

        errorMessage("stop (channel = " + channel.getName() + ")");
    }

    private void enquireLinkRequest() {
        rateLimiter.acquire();
        EnquireLink request = new EnquireLink();
        request.setSequenceNumber(seqNumber.incrementAndGet());
        try {
            debugMessage("enquireLinkRequest (channel = " + channel.getName() + ")");
            session = connectPool.getResource();
            session.enquireLink(request);
            connectPool.returnResource(session);
        } catch (TimeoutException | PDUException | IOException | WrongSessionStateException  e) {
            debugMessage("", e);
            if (session != null) connectPool.returnBrokenResource(session);
        } catch (SmsServerException e) {
            errorMessage("Error connect channel " + channel.getName(), e);
            if (session != null) connectPool.returnBrokenResource(session);
        }
    }
}
