<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:import url="header.jsp">
	<c:param name="title">Create Account</c:param>
	<c:param name="scripts">/blender/scripts/jquery.validate.js /blender/messages.js /blender/scripts/register.js</c:param>
</c:import>

<spring:hasBindErrors name="registrationForm">
	<c:set var="imgInvalid"><img src="/blender/images/invalid.png" /></c:set>
	<c:set var="okImg"><img src="/blender/images/ok.png" /></c:set>
</spring:hasBindErrors>

<h1>Create Account</h1>
<form:form modelAttribute="registrationForm">
	<table>
		<tr>
			<td class="form-labels">
				<label for="username">Username:</label><br />
				<span class="hint">(Unix-style username)</span>
			</td>
			<td><form:input path="username" /></td>
			<td class="status">
				<spring:bind path="registrationForm.username">
					<c:choose>
						<c:when test="${status.error}">
							${imgInvalid}
							<form:errors path="username" />
						</c:when>
						<c:otherwise>${okImg}</c:otherwise>
					</c:choose>
				</spring:bind>			
			</td>
		</tr>
		<tr>
			<td class="form-labels">
				<label for="password">Password:</label><br />
				<span class="hint">(≥ 8 characters, 1 a-z, 1 A-Z, 1 0-9)</span>
			</td>
			<td><form:password path="password" showPassword="true" /></td>
			<td class="status">
				<spring:bind path="registrationForm.password">
					<c:choose>
						<c:when test="${status.error}">
							${imgInvalid}
							<form:errors path="password" />
						</c:when>
						<c:otherwise>${okImg}</c:otherwise>
					</c:choose>
				</spring:bind>
			</td>
		</tr>
		<tr>
			<td class="form-labels">
				<label for="passwordConfirmation">Confirm Password:</label><br />
			</td>
			<td><form:password path="passwordConfirmation" showPassword="true" /></td>
			<td class="status">
				<spring:bind path="registrationForm.passwordConfirmation">
					<c:choose>
						<c:when test="${status.error}">
							${imgInvalid}
							<form:errors path="passwordConfirmation" />
						</c:when>
						<c:otherwise>${okImg}</c:otherwise>
					</c:choose>
				</spring:bind>
			</td>
		</tr>
		<tr>
			<td class="form-labels">
				<label for="name">Full Name:</label><br />
				<span class="hint">(How it will be displayed on this site)</span>
			</td>
			<td><form:input path="name" /></td>
			<td class="status">
				<spring:bind path="registrationForm.name">
					<c:choose>
						<c:when test="${status.error}">
							${imgInvalid}
							<form:errors path="name" />
						</c:when>
						<c:otherwise>${okImg}</c:otherwise>
					</c:choose>
				</spring:bind>
			</td>
		</tr>
		<tr>
			<td class="form-labels">
				<label for="email">E-mail Address:</label><br />
			</td>
			<td><form:input path="email" /></td>
			<td class="status">
				<spring:bind path="registrationForm.email">
					<c:choose>
						<c:when test="${status.error}">
							${imgInvalid}
							<form:errors path="email" />
						</c:when>
						<c:otherwise>${okImg}</c:otherwise>
					</c:choose>
				</spring:bind>
			</td>
		</tr>
		<tr><td><br /></td></tr>
		<tr>
			<td colspan="2"><div id="recaptcha-html">${reCaptchaHtml}</div></td>
			<td id="recaptcha-status" class="status">
				<spring:bind path="registrationForm.reCaptcha">
					<c:if test="${status.error}">
						${imgInvalid}
						<form:errors path="reCaptcha" />
					</c:if>
				</spring:bind>
			</td>
		</tr>
		<tr>
			<td colspan="2" id="form-submit" ><br /><input type="submit" value="Register" /></td>
		</tr>
	</table>
</form:form>

<c:import url="footer.jsp" />