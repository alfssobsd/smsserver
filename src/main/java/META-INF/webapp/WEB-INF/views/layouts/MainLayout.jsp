<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<!DOCTYPE html>

<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <title><tiles:insertAttribute name="title-content" /><</title>
    <spring:url value="/c/bootstrap.css" var="bootstrapCss"/>
    <link href="${bootstrapCss}" rel="stylesheet"/>

    <spring:url value="/c/jumbotron-narrow.css" var="jumbotronNarRow"/>
    <link href="${jumbotronNarRow}" rel="stylesheet"/>
</head>
<body>
<div class="container">
    <div class="header">
        <ul class="nav nav-pills pull-right">
            <li>
                <a href='<c:url value="/channels"/>'>
                    Channels
                </a>
            <li><a href="#">Send SMS</a></li>
            <li><a href="#">Admin</a></li>
        </ul>
        <h3 class="text-muted">SMSServer</h3>
    </div>
    <div><tiles:insertAttribute name="body-content" /></div>
    <div class="footer">
        <p>&copy; SmSServer </p>
    </div>
</div> <!-- /container -->

</body>
</html>