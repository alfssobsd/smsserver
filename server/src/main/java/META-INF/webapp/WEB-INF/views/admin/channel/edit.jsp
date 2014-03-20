<jsp:useBean id="channel" scope="request" type="net.alfss.smsserver.database.entity.Channel"/>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<h3>Edit Channel ${channel.name}</h3>
<div class="row">
    <form action="" class="form-horizontal">
        <div class="form-group">
            <label class="col-sm-2 control-label">Name</label>
            <div class="col-sm-5">
                <input name="name" class="form-control" id="channelName" placeholder="Name" value="${channel.name}">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">Queue</label>
            <div class="col-sm-5">
                <input name="queue" class="form-control" id="channelQueue" placeholder="Queue" value="${channel.queue}">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">SMPP Host</label>
            <div class="col-sm-5">
                <input name="smppHost" class="form-control" id="channelSmppHost" placeholder="SMPP Host" value="${channel.smppHost}">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">SMPP Port</label>
            <div class="col-sm-5">
                <input name="smppPort" class="form-control" id="channelSmppPort" placeholder="SMPP Port" value="${channel.smppPort}">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">SMPP User</label>
            <div class="col-sm-5">
                <input name="smppUserName" class="form-control" id="channelSmppUser" placeholder="SMPP User" value="${channel.smppUserName}">
            </div>
        </div>
        <div class="form-group">
            <label class="col-sm-2 control-label">SMPP Password</label>
            <div class="col-sm-5">
                <input name="smppPassword" class="form-control" id="channelSmppPassword" placeholder="SMPP Password" value="${channel.smppPassword}">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label">SMPP Source Addr</label>
            <div class="col-sm-5">
                <input name="smppSourceAddr" class="form-control" id="channelSmppSourceAddr" placeholder="SMPP Source Addr" value="${channel.smppSourceAddr}">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label">SMPP Source Ton</label>
            <div class="col-sm-5">
                <input name="smppSourceTon" class="form-control" id="channelSmppSourceTon" placeholder="SMPP Source Ton" value="${channel.smppSourceTon}">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label">SMPP Source Npi</label>
            <div class="col-sm-5">
                <input name="smppSourceNpi" class="form-control" id="channelSmppSourceNpi" placeholder="SMPP Source Npi" value="${channel.smppSourceNpi}">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label">SMPP Destination Ton</label>
            <div class="col-sm-5">
                <input name="smppDestTon" class="form-control" id="channelSmppDestTon" placeholder="SMPP Destination Ton" value="${channel.smppDestTon}">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label">SMPP Destination Npi</label>
            <div class="col-sm-5">
                <input name="smppDestNpi" class="form-control" id="channelSmppDestNpi" placeholder="SMPP Destination Npi" value="${channel.smppDestNpi}">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label">SMPP Max Message</label>
            <div class="col-sm-5">
                <input name="smppMaxMessage" class="form-control" id="channelSmppMaxMessage" placeholder="SMPP Max Message" value="${channel.smppMaxMessage}">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label">SMPP send message per second</label>
            <div class="col-sm-5">
                <input name="smppSendMessagePerSecond" class="form-control" id="channelSmppSendMessagePerSecond" placeholder="60" value="${channel.smppSendMessagePerSecond}">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label">SMPP reconnect timeout</label>
            <div class="col-sm-5">
                <input name="smppReconnectTimeOut" class="form-control" id="smppReconnectTimeOut" placeholder="60" value="${channel.smppReconnectTimeOut}">
            </div>
        </div>

        <div class="form-group">
            <label class="col-sm-2 control-label">SMPP Enquire link interval</label>
            <div class="col-sm-5">
                <input name="smppEnquireLinkInterval" class="form-control" id="smppEnquireLinkInterval" placeholder="60" value="${channel.smppEnquireLinkInterval}">
            </div>
        </div>

        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-5">
                <div class="checkbox">
                    <label>
                        <input type="checkbox" value="${channel.payload}"> SMPP Enable Payload
                    </label>
                </div>
            </div>
        </div>

        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-5">
                <div class="checkbox">
                    <label>
                        <input type="checkbox" value="${channel.fake}"> SMPP is fake
                    </label>
                </div>
            </div>
        </div>

        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-5">
                <div class="checkbox">
                    <label>
                        <input type="checkbox" value="${channel.enable}"> SMPP is enable
                    </label>
                </div>
            </div>
        </div>

        <div class="form-group">
            <div class="col-sm-offset-2 col-sm-5">
                <button type="submit" class="btn btn-primary">Update</button>
            </div>
        </div>

    </form>
</div>