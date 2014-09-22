package net.alfss.smsserver.http.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.alfss.smsserver.Master;
import net.alfss.smsserver.database.dao.impl.ChannelDAOImpl;
import net.alfss.smsserver.database.entity.Channel;
import net.alfss.smsserver.http.dao.impl.GlobalConfigDAOImpl;
import net.alfss.smsserver.http.domain.ChannelControlCommand;
import net.alfss.smsserver.http.domain.Status;
import net.alfss.smsserver.http.services.ChannelControl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

/**
 * User: alfss
 * Date: 20.09.14
 * Time: 2:38
 */
@Service
public class ChannelControlImpl implements ChannelControl {
    final Logger logger = (Logger) LoggerFactory.getLogger(ChannelControlImpl.class);

    @Autowired
    private GlobalConfigDAOImpl globalConfigDAO;
    private final ChannelDAOImpl channelDAO;

    public ChannelControlImpl () {
        channelDAO = new ChannelDAOImpl();
    }

    @Override
    public String startChannel(String json, Map<String, String> header) {

        if (!enableAccess(header)) {
            return Status.ACCESS_ERROR;
        }

        ObjectMapper mapper = new ObjectMapper();
        ChannelControlCommand command = null;
        try {
            command = mapper.readValue(json, ChannelControlCommand.class);
        } catch (IOException | NullPointerException e) {
            return Status.PARSE_ERROR;
        }

        Channel channel = setEnableChannel(command.getChannelId());

        if (channel == null ) {
            return Status.DATABSE_ERROR;
        }

        try {
            Master.startSmsServer(channel);
        } catch (Exception e ) {
            setDisableChannel(command.getChannelId());
            return Status.INTERNAL_ERROR;
        }

        return Status.OK;
    }

    @Override
    public String stopChannel(String json, Map<String, String> header) {

        if (!enableAccess(header)) {
            return Status.ACCESS_ERROR;
        }

        ObjectMapper mapper = new ObjectMapper();
        ChannelControlCommand command = null;
        try {
            command = mapper.readValue(json, ChannelControlCommand.class);
        } catch (IOException | NullPointerException e) {
            return Status.PARSE_ERROR;
        }

        Channel channel = setDisableChannel(command.getChannelId());

        if (channel == null ) {
            return Status.DATABSE_ERROR;
        }

        try {
            Master.stopSmsServer(channel);
        } catch (Exception e ) {
            setEnableChannel(command.getChannelId());
            return Status.INTERNAL_ERROR;
        }

        return Status.OK;
    }

    private Channel setEnableChannel(int channelId) {
        Channel channel = null;
        try {
            channel = channelDAO.get(channelId);
        } catch ( RuntimeException e) {
            return null;
        }

        try {
            channelDAO.enable(channel);
        } catch ( RuntimeException e) {
            logger.debug("channel not set enable");
            return null;
        }

        return channel;
    }

    private Channel setDisableChannel(int channelId) {
        Channel channel = null;
        try {
            channel = channelDAO.get(channelId);
        } catch ( RuntimeException e) {
            return null;
        }

        try {
            channelDAO.disable(channel);
        } catch ( RuntimeException e) {
            logger.debug("channel not set disable");
            return null;
        }

        return channel;
    }

    private boolean enableAccess(Map<String, String> header) {
//        logger.debug(header.toString());
        String token = header.get("X-Token");
//        logger.debug(token);
        return token != null && token.equals(globalConfigDAO.getJettyHttpToken());
    }
}
