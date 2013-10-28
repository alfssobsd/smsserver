<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="row">
    <table class="table table-bordered">
        <thead>
        <tr>
            <th>Name</th>
            <th>Queue</th>
            <th>QueueSize</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="channel" items="${channelList}">
            <tr>
                <td>
                    <a href='<c:url value="/channel/${channel.channelName}/queue"/>'>
                        <c:out value="${channel.channelName}" />
                    </a>
                </td>
                <td><c:out value="${channel.channelQueue}" /></td>
                <td><c:out value="${channel.channelQueueSize}" /></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
