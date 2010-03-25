<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:import url="header.jsp">
	<c:param name="title">Login</c:param>
</c:import>

<h1>Login</h1>

<c:if test="${!empty param.error}">
	<div id="form-errors">
		<img src="/blender/images/invalid.png" />
		<c:choose>
			<c:when test="${!empty reCaptchaHtml}">
				<fmt:message key="invalid.user.pass.captcha" />
			</c:when>
			<c:otherwise>
				<fmt:message key="invalid.user.pass" />
			</c:otherwise>
		</c:choose>
	</div>
</c:if>

<form action="/blender/checkCredentials" method="post">
	<table>
		<tr>
			<td class="form-labels">
				<label for="username">Username:</label>
			</td>
			<td><input type="text" name="username" id="username" /></td>
		</tr>
		<tr>
			<td class="form-labels">
				<label for="password">Password:</label>
			</td>
			<td><input type="password" name="password" id="password" /></td>
		</tr>
		<tr>
			<td></td>
			<td>
				<input type="checkbox" name="rememberMe" id="rememberMe" value="true" />
				<label for="rememberMe">Remember Me</label>
			</td>
		</tr>
		<c:if test="${!empty reCaptchaHtml}">
			<tr><td><br /></td></tr>
			<tr>
				<td colspan="2"><div id="recaptcha-html">${reCaptchaHtml}</div></td>
			</tr>
		</c:if>
		<tr>
			<td colspan="2" id="form-submit" ><br /><input type="submit" value="Login" /></td>
		</tr>
	</table>
</form>

<c:import url="footer.jsp" />