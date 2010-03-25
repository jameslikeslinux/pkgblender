<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<title>${param.title}</title>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<link href="<c:url value="/styles/master.css" />" rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="/blender/scripts/jquery-1.4.2.js"></script>
		<c:forTokens items="${param.scripts}" delims=" " var="script">
			<script type="text/javascript" src="${script}"></script>
		</c:forTokens>
	</head>
	
	<body>
		<div id="container">
			<div id="header">
				<div id="pre-toolbar">
					<div id="logo"><a href="#"><img class="logo" src="<c:url value="/images/logo.png" />" /></a></div>
					<div id="search"><form><input type="text" /> <input type="submit" value="Search" /></form></div>
				</div>
				<div id="toolbar">
					<div id="logo-descenders"><img class="logo" src="<c:url value="/images/logo-descenders.png" />" /></div>
					<span id="toolbar-left-links">
						<a href="#">Dashboard</a>
						<a href="#">Packages</a>
						<a href="#">Builds</a>
					</span>
					<span id="toolbar-right-links">
						<sec:authorize access="isAnonymous()">
							<a href="<c:url value="/login" />">Login</a>
							<a href="<c:url value="/register" />">Create Account</a>
						</sec:authorize>
						<sec:authorize access="isAuthenticated()">
							<a href="<c:url value="/logout" />">Logout</a>
							<a href="<c:url value="/accountSettings" />">Account Settings</a>
						</sec:authorize>
					</span>
				</div>
			</div>
			
			<div id="content">