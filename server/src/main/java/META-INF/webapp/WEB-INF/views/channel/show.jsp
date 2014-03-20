<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<div class="row">
    channelId: <c:out value="${channel.channelId}"/> <br>
    name: <c:out value="${channel.name}"/> <br>
    queue: <c:out value="${channel.queue}"/> <br>
    <c:out value="${channel.smppHost}"/> <br>
    <c:out value="${channel.smppPort}"/> <br>
    <c:out value="${channel.smppUserName}"/> <br>
    <c:out value="${channel.smppPassword}"/> <br>
    <c:out value="${channel.smppSourceAddr}"/> <br>
    <c:out value="${channel.smppSourceTon}"/> <br>
    <c:out value="${channel.smppSourceNpi}"/> <br>
    <c:out value="${channel.smppDestTon}"/> <br>
    <c:out value="${channel.smppDestNpi}"/> <br>
    <c:out value="${channel.smppMaxSplitMessage}"/> <br>
    <c:out value="${channel.smppMaxMessagePerSecond}"/> <br>
    <c:out value="${channel.smppReconnectTimeOut}"/> <br>
    <c:out value="${channel.smppEnquireLinkInterval}"/> <br>
    <c:out value="${channel.payload}"/> <br>
    <c:out value="${channel.fake}"/> <br>
    <c:out value="${channel.enable}"/> <br>



    ---- <br>
    <c:forEach var="connection" items="${channelConnectionList}">
        <c:out value="${connection.channeConnectionlId}" /> <c:out value="${connection.smppSystemType}"/>  <br>
    </c:forEach>

</div>
