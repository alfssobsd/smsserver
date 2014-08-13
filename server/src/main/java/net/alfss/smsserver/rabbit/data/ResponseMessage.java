package net.alfss.smsserver.rabbit.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * User: alfss
 * Date: 28.06.14
 * Time: 2:30
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseMessage {

    @JsonIgnore
    private Pattern deliverMessageId = Pattern.compile("^id:(.*) sub:");
    @JsonIgnore
    private Pattern deliverStat = Pattern.compile("stat:([A-Z]+)");
    @JsonIgnore
    private Pattern deliverError = Pattern.compile("err:([0-9]+)");

    private Integer sequenceNumber;
    private String deliverMessage;

    public ResponseMessage() {}

    public ResponseMessage(String deliverMessage, Integer sequenceNumber) {
        this.deliverMessage = deliverMessage;
        this.sequenceNumber = sequenceNumber;
    }

    @JsonProperty("deliver_message")
    public String getDeliverMessage() {
        return deliverMessage;
    }

    @JsonProperty("deliver_message")
    public void setDeliverMessage(String deliverMessage) {
        this.deliverMessage = deliverMessage;
    }


    @JsonProperty("sequence_number")
    void setSequenceNumber(Integer sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    @JsonProperty("sequence_number")
    public Integer getSequenceNumber() {
        return sequenceNumber;
    }

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch (IOException e) {
            return null;
        }
    }

    @JsonIgnore
    public String getDeliverMessageId() {
        Matcher matcher = deliverMessageId.matcher(this.deliverMessage);
        if (matcher.find()) {
            return matcher.group(1);
        }

        return null;
    }


    @JsonIgnore
    public int getDeliverErrorCode() {
        Matcher matcher = deliverError.matcher(this.deliverMessage);
        if (matcher.find()) {
            return Integer.parseInt(matcher.group(1));
        } else {
            return 0;
        }
    }

    //Вернется статус UNDELIV и код ошибки, подробнее здесь http://smsc.ru/api/smpp/#errs
    //251	Превышен лимит на один номер.	Превышен суточный лимит сообщений на один номер. Лимит устанавливается Клиентом в личном кабинете в пункте "Настройки". Также такая ошибка возможна при отправке более 50 сообщений одному абоненту, которые были отправлены с перерывом между сообщениями менее 30 секунд.
    @JsonIgnore
    public int getDeliverStatus() {
        Matcher matcher = deliverStat.matcher(this.deliverMessage);
        if (matcher.find()) {
            switch (matcher.group(1)) {
                case "DELIVRD":
                    return 0;
                case "EXPIRED":
                    return 1;
                case "UNDELIV":
                    return 2;
                case "REJECTD":
                    return 3;
            }
        }

        return 3;
    }


}
