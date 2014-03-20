<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="pageNextNumber" value="${pageNumber+1}" />
<c:set var="pagePrevNumber" value="${pageNumber-1}" />

<ul class="pager">
    <c:choose>
        <c:when test="${pageNumber!=0}">
            <li class="previous"><a href="<c:url value="/chennel/${channel.channelId}/message/list/page/${pagePrevNumber}"/>"><span class="glyphicon glyphicon-arrow-left"></span> Next messages</a></li>
        </c:when>
        <c:otherwise>
            <li class="previous disabled"><a href=""><span class="glyphicon glyphicon-arrow-left"></span> Next messages</a></li>
        </c:otherwise>
    </c:choose>
    <li class="next"><a href="<c:url value="/chennel/${channel.channelId}/message/list/page/${pageNextNumber}"/>">Previous messages <span class="glyphicon glyphicon-arrow-right"></span></a></li>
</ul>
<div class="row">
    <table class="table table-bordered">
        <thead>
        <tr>
            <th>id</th>
            <th>from</th>
            <th>to</th>
            <th>createTime</th>
            <th>updateTime</th>
            <th>messageData</th>
            <th>messageStatus</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="message" items="${messageList}">
            <tr>
                <td><c:out value="${message.messageId}" /></td>
                <td><c:out value="${message.from}" /></td>
                <td><c:out value="${message.to}" /></td>
                <td><c:out value="${message.createTime}" /></td>
                <td><c:out value="${message.updateTime}" /></td>
                <td><c:out value="${message.getMessageDataString()}" /></td>
                <td>
                    <c:choose>
                        <c:when test="${message.messageStatus.name == 'SUCCESS'}">
                            <span class="label label-success"><c:out value="${message.messageStatus.name}"/></span>
                        </c:when>
                        <c:when test="${message.messageStatus.name == 'FAIL'}">
                            <span class="label label-danger"><c:out value="${message.messageStatus.name}"/></span>
                        </c:when>
                        <c:otherwise>
                            <span class="label label-info"> <c:out value="${message.messageStatus.name}"/></span>
                        </c:otherwise>
                    </c:choose>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>


<ul class="pager">
    <c:choose>
        <c:when test="${pageNumber!=0}">
            <li class="previous"><a href="<c:url value="/chennel/${channel.channelId}/message/list/page/${pagePrevNumber}"/>"><span class="glyphicon glyphicon-arrow-left"></span> Next messages</a></li>
        </c:when>
        <c:otherwise>
            <li class="previous disabled"><a href=""><span class="glyphicon glyphicon-arrow-left"></span> Next messages</a></li>
        </c:otherwise>
    </c:choose>
    <li class="next"><a href="<c:url value="/chennel/${channel.channelId}/message/list/page/${pageNextNumber}"/>">Previous messages <span class="glyphicon glyphicon-arrow-right"></span></a></li>
</ul>