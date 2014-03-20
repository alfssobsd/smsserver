<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="row" style="margin-bottom: 10px;">
    <a class="btn btn-default" href="/admin/channel/add"><span class="glyphicon glyphicon-plus"></span>Create</a>
</div>
<div class="row">
    <table class="table table-bordered">
        <thead>
        <tr>
            <th>Name</th>
            <th>Queue</th>
            <th>Action</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="channel" items="${channelList}">
            <tr>
                <td>

                    <a href='<c:url value="/admin/channel/${channel.channelId}/edit"/>'>
                        <c:out value="${channel.name}" />
                    </a>
                </td>
                <td><c:out value="${channel.queue}" /></td>
                <td><a class="btn btn-danger" href="<c:url value="/admin/channel/${channel.channelId}/delete"/>">Delete</a></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>