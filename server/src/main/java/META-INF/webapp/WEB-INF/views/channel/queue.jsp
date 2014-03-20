<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

${channel.channelName}
${channel.channelQueue}

<div class="row">
    <table class="table table-bordered">
        <thead>
        <tr>
            <th>phone</th>
            <th>priority</th>
            <th>createTime</th>
            <th>text</th>
            <th>referenceNumber</th>
            <th>startPart</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="messageChannel" items="${channelMessageList}">
            <tr>
                <td><c:out value="${messageChannel.message.destinationAddress}" /></td>
                <td><c:out value="${messageChannel.message.priority}" /></td>
                <td><c:out value="${messageChannel.message.createTime}" /></td>
                <td><c:out value="${messageChannel.message.messageText}" /></td>
                <td><c:out value="${messageChannel.message.referenceNumber}" /></td>
                <td><c:out value="${messageChannel.message.startPart}" /></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
