<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="col-sm-3 col-md-2 sidebar">
    <h3><c:out value="${channel.name}" /></h3>
    <ul class="nav nav-sidebar">
        <li class="active"><a href="/chennel/<c:out value="${channel.channelId}" />/message/list/page/0">Message</a></li>
    </ul>
</div>