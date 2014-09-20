package net.alfss.smsserver.http.services.impl;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.alfss.smsserver.Master;
import net.alfss.smsserver.config.GlobalConfig;
import net.alfss.smsserver.config.SharedConfig;
import net.alfss.smsserver.database.dao.impl.ChannelDAOImpl;
import net.alfss.smsserver.database.entity.Channel;
import net.alfss.smsserver.http.dao.impl.GlobalConfigDAOImpl;
import net.alfss.smsserver.http.domain.ChannelControlCommand;
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

    @Override
    public boolean startChannel(String json, Map<String, String> header) {

        if (!enableAccess(header)) {
            return false;
        }
        logger.debug("access");

        try {
            ObjectMapper mapper = new ObjectMapper();
            Channel channel = null;
            ChannelControlCommand command = mapper.readValue(json, ChannelControlCommand.class);
            logger.debug("mapping");
            ChannelDAOImpl channelDAO = new ChannelDAOImpl();
            channel = channelDAO.get(command.getChannelId());
            Master.startSmsServer(channel);
        } catch (JsonMappingException e) {
            logger.debug("mapping" + e.toString());
            return false;
        } catch (JsonParseException e) {
            logger.debug("parse" + e.toString());
            return false;
        } catch (IOException e) {
            return false;
        } catch (NullPointerException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean stopChannel(String json, Map<String, String> header) {

        if (!enableAccess(header)) {
            return false;
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            Channel channel = null;
            ChannelControlCommand command = mapper.readValue(json, ChannelControlCommand.class);
            ChannelDAOImpl channelDAO = new ChannelDAOImpl();
            channel = channelDAO.get(command.getChannelId());
            Master.stopSmsServer(channel);
        } catch (JsonMappingException e) {
            return false;
        } catch (JsonParseException e) {
            return false;
        } catch (IOException e) {
            return false;
        } catch (NullPointerException e) {
            return false;
        }
        return false;
    }

    private boolean enableAccess(Map<String, String> header) {
        String token = header.get("X-TOKEN");
        return token != null && token.equals(globalConfigDAO.getJettyHttpToken());
    }
}
