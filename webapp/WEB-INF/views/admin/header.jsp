<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:import url="../header.jsp" />

<h1>Administration</h1>

<table id="admin">
	<tr>
		<td id="admin-nav">
			<a href="<c:url value="/admin/oses" />">Operating Systems</a><br />
			<a href="<c:url value="/admin/buildHosts" />">Build Hosts</a><br />
			<a href="<c:url value="/admin/users" />">Users</a><br />
		</td>
		<td id="admin-content">