<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:import url="header.jsp">
	<c:param name="title">Create New Package</c:param>
</c:import>

<h1>Create New Package</h1>

<form:form modelAttribute="uploadForm" enctype="multipart/form-data">
	<spring:hasBindErrors name="uploadForm">
		<c:set var="hasBindErrors" value="true" />
	
		<c:choose>
			<c:when test="${not empty errors.globalErrors}">
				<p>There was a problem processing the spec file that you uploaded.  Please correct the following error and try again.</p>
				<p class="spec-error"><img src="<c:url value="/images/invalid.png" />" /> <form:errors /></p>
			</c:when>
			
			<c:otherwise>
				<p>Please correct the errors below and upload again.</p>
			</c:otherwise>
		</c:choose>
	</spring:hasBindErrors>
	
	<c:if test="${not hasBindErrors}">
		<c:choose>
			<c:when test="${empty uploadForm.packageName}">
				<p>To create a new package, upload a completed spec file.  pkgblender will prompt you for any additional files required by your spec file.</p>
			</c:when>
			
			<c:otherwise>
				<p>The spec file that you uploaded requires the following additional files:</p>
			</c:otherwise>
		</c:choose>
	</c:if>

	<form:hidden path="numFiles" />
	<form:hidden path="packageName" />
	
	<table>
		<c:forEach var="i" begin="0" end="${uploadForm.numFiles - 1}" step="1">
			<tr>
				<td class="form-labels">
					<label for="files[${i}]">${uploadForm.fileNames[i]}:</label>
					<form:hidden path="fileNames[${i}]" />
					<form:hidden path="fileTypes[${i}]" />
				</td>
				<td><input type="file" name="files[${i}]" id="files[${i}]" /></td>
				<td class="status">
					<spring:bind path="uploadForm.files[${i}]">
						<c:if test="${status.error}">
							<img src="<c:url value="/images/invalid.png" />" />
							<form:errors path="files[${i}]" />
						</c:if>
					</spring:bind>
				</td>
			</tr>
		</c:forEach>
		
		<tr>
			<td colspan="2" id="form-submit" ><br />
				<input type="submit" value="Upload" />
			</td>
		</tr>
	</table>
</form:form>

<c:import url="footer.jsp" />