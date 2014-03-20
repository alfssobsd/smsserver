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
                    <a href='<c:url value="/chennel/${channel.channelId}/message/list/page/0"/>'>
                        <c:out value="${channel.name}" />
                    </a>
                </td>
                <td><c:out value="${channel.queue}" /></td>
                <td><c:out value="${channel.channelId}" /></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
