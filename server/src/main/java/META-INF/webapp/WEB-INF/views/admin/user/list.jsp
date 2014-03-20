<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="row" style="margin-bottom: 10px;">
    <a class="btn btn-default" href="/admin/user/add"><span class="glyphicon glyphicon-plus"></span>Create</a>
</div>
<div class="row">
    <table class="table table-bordered">
        <thead>
        <tr>
            <th>Id</th>
            <th>Name</th>
            <th>Action</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="user" items="${userList}">
            <tr>
                <td><c:out value="${user.userId}" /></td>
                <td>

                    <a href='<c:url value="/admin/user/${user.userId}/edit"/>'>
                        <c:out value="${user.login}" />
                    </a>
                </td>
                <td><a class="btn btn-danger" href="<c:url value="/admin/user/${user.userId}/delete"/>">Delete</a></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>