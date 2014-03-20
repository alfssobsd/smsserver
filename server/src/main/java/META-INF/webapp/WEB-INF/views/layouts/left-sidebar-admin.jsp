<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<div class="col-sm-3 col-md-2 sidebar">
    <ul class="nav nav-sidebar">
        <li class="<tiles:insertAttribute name="left-sidebar-admin-channels-active" />"><a href="/admin/channel">Channels</a></li>
        <li class="<tiles:insertAttribute name="left-sidebar-admin-users-active" />"><a href="/admin/user">Users</a></li>
    </ul>
</div>