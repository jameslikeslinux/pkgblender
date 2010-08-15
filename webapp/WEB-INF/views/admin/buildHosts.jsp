<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:import url="header.jsp">
	<c:param name="title">Administration - Build Hosts</c:param>
</c:import>

<h2>Build Hosts</h2>

<h3>Add New</h3>

<form:form modelAttribute="newBuildHostForm">
	<table>
		<tr>
			<td class="form-labels">
				<label for="hostname">Hostname:</label>
			</td>
			<td><form:input path="hostname" /></td>
			<td class="status">
				<spring:bind path="newBuildHostForm.hostname">
					<c:if test="${status.error}">
						<img src="<c:url value="/images/invalid.png" />" />
						<form:errors path="hostname" />
					</c:if>
				</spring:bind>			
			</td>
		</tr>
				
		<tr>
			<td class="form-labels">
				<label for="os">OS:</label>
			</td>
			<td>
				<form:select path="os">
					<form:options items="${newBuildHostForm.oses}" />
				</form:select>
			</td>
			<td class="status"></td>
		</tr>

		<tr>
			<td class="form-labels">
				<label for="architecture">Architecture:</label>
			</td>
			<td><form:radiobuttons path="architecture" items="${newBuildHostForm.architectures}" delimiter="<br />" /></td>
			<td class="status"></td>
		</tr>
		
		<tr>
			<td colspan="2" id="form-submit" ><br /><input type="submit" value="Add" /></td>
		</tr>
	</table>
</form:form>

<c:import url="footer.jsp" />