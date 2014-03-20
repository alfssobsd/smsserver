<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%>

<div class="navbar navbar-inverse navbar-fixed-top" role="navigation">
    <div class="container-fluid">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-collapse">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="/">SMSServer</a>
        </div>
        <div class="navbar-collapse collapse">
            <ul class="nav navbar-nav">
                <li class="<tiles:insertAttribute name="top-menu-channel-active" />">
                    <a href='/chennel/list'>
                        Channels
                    </a>
                </li>
                <li class="<tiles:insertAttribute name="top-menu-admin-active" />">
                    <a href='/admin/channel'>
                        Admin
                    </a>
                </li>
                <li class="<tiles:insertAttribute name="top-menu-send-active" />">
                    <a href="#">Send</a>
                </li>
                <li><a href="#">Help</a></li>
            </ul>
        </div>
    </div>
</div>