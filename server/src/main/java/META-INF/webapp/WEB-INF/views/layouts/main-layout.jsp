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

    <spring:url value="/c/dashboard.css" var="dashboard"/>
    <link href="${dashboard}" rel="stylesheet"/>
</head>
<body>
<tiles:insertAttribute name="top-menu" />
<div class="container-fluid">
    <div class="row">
        <%--left sidebar --%>
        <tiles:insertAttribute name="left-sidebar" />
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
           <%--<h1 class="page-header">Dashboard</h1>--%>
            <%--<div class="row placeholders">--%>
                <%--<div class="col-xs-6 col-sm-3 placeholder">--%>
                    <%--<img data-src="holder.js/200x200/auto/sky" class="img-responsive" alt="200x200" src="data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIyMDAiIGhlaWdodD0iMjAwIj48cmVjdCB3aWR0aD0iMjAwIiBoZWlnaHQ9IjIwMCIgZmlsbD0iIzBEOEZEQiI+PC9yZWN0Pjx0ZXh0IHRleHQtYW5jaG9yPSJtaWRkbGUiIHg9IjEwMCIgeT0iMTAwIiBzdHlsZT0iZmlsbDojZmZmO2ZvbnQtd2VpZ2h0OmJvbGQ7Zm9udC1zaXplOjEzcHg7Zm9udC1mYW1pbHk6QXJpYWwsSGVsdmV0aWNhLHNhbnMtc2VyaWY7ZG9taW5hbnQtYmFzZWxpbmU6Y2VudHJhbCI+MjAweDIwMDwvdGV4dD48L3N2Zz4=">--%>
                    <%--<h4>Label</h4>--%>
                    <%--<span class="text-muted">Something else</span>--%>
                <%--</div>--%>
                <%--<div class="col-xs-6 col-sm-3 placeholder">--%>
                    <%--<img data-src="holder.js/200x200/auto/vine" class="img-responsive" alt="200x200" src="data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIyMDAiIGhlaWdodD0iMjAwIj48cmVjdCB3aWR0aD0iMjAwIiBoZWlnaHQ9IjIwMCIgZmlsbD0iIzM5REJBQyI+PC9yZWN0Pjx0ZXh0IHRleHQtYW5jaG9yPSJtaWRkbGUiIHg9IjEwMCIgeT0iMTAwIiBzdHlsZT0iZmlsbDojMUUyOTJDO2ZvbnQtd2VpZ2h0OmJvbGQ7Zm9udC1zaXplOjEzcHg7Zm9udC1mYW1pbHk6QXJpYWwsSGVsdmV0aWNhLHNhbnMtc2VyaWY7ZG9taW5hbnQtYmFzZWxpbmU6Y2VudHJhbCI+MjAweDIwMDwvdGV4dD48L3N2Zz4=">--%>
                    <%--<h4>Label</h4>--%>
                    <%--<span class="text-muted">Something else</span>--%>
                <%--</div>--%>
                <%--<div class="col-xs-6 col-sm-3 placeholder">--%>
                    <%--<img data-src="holder.js/200x200/auto/sky" class="img-responsive" alt="200x200" src="data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIyMDAiIGhlaWdodD0iMjAwIj48cmVjdCB3aWR0aD0iMjAwIiBoZWlnaHQ9IjIwMCIgZmlsbD0iIzBEOEZEQiI+PC9yZWN0Pjx0ZXh0IHRleHQtYW5jaG9yPSJtaWRkbGUiIHg9IjEwMCIgeT0iMTAwIiBzdHlsZT0iZmlsbDojZmZmO2ZvbnQtd2VpZ2h0OmJvbGQ7Zm9udC1zaXplOjEzcHg7Zm9udC1mYW1pbHk6QXJpYWwsSGVsdmV0aWNhLHNhbnMtc2VyaWY7ZG9taW5hbnQtYmFzZWxpbmU6Y2VudHJhbCI+MjAweDIwMDwvdGV4dD48L3N2Zz4=">--%>
                    <%--<h4>Label</h4>--%>
                    <%--<span class="text-muted">Something else</span>--%>
                <%--</div>--%>
                <%--<div class="col-xs-6 col-sm-3 placeholder">--%>
                    <%--<img data-src="holder.js/200x200/auto/vine" class="img-responsive" alt="200x200" src="data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIyMDAiIGhlaWdodD0iMjAwIj48cmVjdCB3aWR0aD0iMjAwIiBoZWlnaHQ9IjIwMCIgZmlsbD0iIzM5REJBQyI+PC9yZWN0Pjx0ZXh0IHRleHQtYW5jaG9yPSJtaWRkbGUiIHg9IjEwMCIgeT0iMTAwIiBzdHlsZT0iZmlsbDojMUUyOTJDO2ZvbnQtd2VpZ2h0OmJvbGQ7Zm9udC1zaXplOjEzcHg7Zm9udC1mYW1pbHk6QXJpYWwsSGVsdmV0aWNhLHNhbnMtc2VyaWY7ZG9taW5hbnQtYmFzZWxpbmU6Y2VudHJhbCI+MjAweDIwMDwvdGV4dD48L3N2Zz4=">--%>
                    <%--<h4>Label</h4>--%>
                    <%--<span class="text-muted">Something else</span>--%>
                <%--</div>--%>
            <%--</div>--%>

            <tiles:insertAttribute name="body-content" />
            <%--<h2 class="sub-header">Section title</h2>--%>
            <%--<div class="table-responsive">--%>
                <%--<tiles:insertAttribute name="body-content" />--%>
            <%--</div>--%>

        </div>

    </div>
</div>
</body>
</html>