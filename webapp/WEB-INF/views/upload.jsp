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

<c:choose>
	<c:when test="${!empty packageExists}">
		<div id="form-errors">
			<p><img src="/blender/images/invalid.png" /> A package already exists for the spec file you tried to upload.</p>
			<p>To create a new package, choose a completed spec file to upload.</p>
		</div>
	</c:when>
	<c:when test="${!empty formatError}">
		<div id="form-errors">
			<p><img src="/blender/images/invalid.png" /> The file you upload must end with ${formatError}.</p>
		</div>
	</c:when>
	<c:when test="${!empty specError}">
		<div id="form-errors">
			<p><img src="/blender/images/invalid.png" /> There is an error in the spec file you uploaded.  The error is:</p>
			<p class="error">${specError}</p>
			<p>Please fix the error and try uploading it again.</p>
		</div>
	</c:when>
	<c:when test="${!empty requiredFile}">
		<p>The spec file you uploaded requires the following file:</p>
	</c:when>
	<c:otherwise>
		<p>To create a new package, choose a completed spec file to upload.</p>
	</c:otherwise>
</c:choose>

<c:choose>
	<c:when test="${!empty requiredFile}">
		<form action="<c:url value="/upload" />" method="post" enctype="multipart/form-data">
			<table>
				<tr>
					<td class="form-labels">
						<label for="file">${requiredFile}</label>
					</td>
					<td><input type="file" name="file" id="file" /></td>
				</tr>
				<tr>
					<td colspan="2" id="form-submit" ><br />
						<input type="hidden" name="requiredFile" value="${requiredFile}" />
						<input type="hidden" name="packageName" value="${packageName}" />
						<input type="submit" value="Upolad" />
					</td>
				</tr>
			</table>
		</form>
	</c:when>
	
	<c:otherwise>
		<form action="<c:url value="/upload" />" method="post" enctype="multipart/form-data">
			<table>
				<tr>
					<td class="form-labels">
						<label for="specfile">Spec File:</label>
					</td>
					<td><input type="file" name="specfile" id="specfile" /></td>
				</tr>
				<tr>
					<td colspan="2" id="form-submit" ><br /><input type="submit" value="Upolad" /></td>
				</tr>
			</table>
		</form>
	</c:otherwise>
</c:choose>

<c:import url="footer.jsp" />