<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:import url="header.jsp">
	<c:param name="title">Administration - Operating Systems</c:param>
</c:import>

<h2>Operating Systems</h2>

<h3>Add New</h3>

<form:form modelAttribute="newOsForm">
	<table>
		<tr>
			<td class="form-labels">
				<label for="name">Name:</label><br />
				<span class="hint">(A descriptive name)</span>
			</td>
			<td><form:input path="name" /></td>
			<td class="status">
				<spring:bind path="newOsForm.name">
					<c:if test="${status.error}">
						<img src="<c:url value="/images/invalid.png" />" />
						<form:errors path="name" />
					</c:if>
				</spring:bind>			
			</td>
		</tr>
		
		<tr>
			<td class="form-labels">
				<label for="slug">Slug:</label><br />
				<span class="hint">(For use in URLs)</span>
			</td>
			<td><form:input path="slug" /></td>
			<td class="status">
				<spring:bind path="newOsForm.slug">
					<c:if test="${status.error}">
						<img src="<c:url value="/images/invalid.png" />" />
						<form:errors path="slug" />
					</c:if>
				</spring:bind>			
			</td>
		</tr>
		
		<tr>
			<td class="form-labels">
				<label for="publisher">Publisher:</label><br />
				<span class="hint">(Source of OS's packages)</span>
			</td>
			<td><form:input path="publisher" /></td>
			<td class="status">
				<spring:bind path="newOsForm.publisher">
					<c:if test="${status.error}">
						<img src="<c:url value="/images/invalid.png" />" />
						<form:errors path="publisher" />
					</c:if>
				</spring:bind>			
			</td>
		</tr>
		
		<tr>
			<td class="form-labels">
				<label for="branch">Branch:</label><br />
				<span class="hint">(In the package FMRIs, such as '0.134')</span>
			</td>
			<td><form:input path="branch" /></td>
			<td class="status">
				<spring:bind path="newOsForm.branch">
					<c:if test="${status.error}">
						<img src="<c:url value="/images/invalid.png" />" />
						<form:errors path="branch" />
					</c:if>
				</spring:bind>			
			</td>
		</tr>
		
		<tr>
			<td colspan="2" id="form-submit" ><br /><input type="submit" value="Create" /></td>
		</tr>
	</table>
</form:form>

<c:import url="footer.jsp" />