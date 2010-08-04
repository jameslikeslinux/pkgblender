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

<p>The following additional files are required:</p>

<form:form modelAttribute="uploadForm" enctype="multipart/form-data">
	<form:hidden path="numFiles" />
	<table>
		<tr>
			<td class="form-labels">
				<label for="files[0]">File 0:</label>
			</td>
			<td><input type="file" name="files[0]" id="files[0]" /></td>
			<td class="status">
				<spring:bind path="uploadForm.files[0]">
					<c:choose>
						<c:when test="${status.error}">
							${imgInvalid}
							<form:errors path="files[0]" />
						</c:when>
						<c:otherwise>${okImg}</c:otherwise>
					</c:choose>
				</spring:bind>
			</td>
		</tr>

		<tr>
			<td class="form-labels">
				<label for="files[1]">File 1:</label>
			</td>
			<td><input type="file" name="files[1]" id="files[1]" /></td>
			<td class="status">
				<spring:bind path="uploadForm.files[1]">
					<c:choose>
						<c:when test="${status.error}">
							${imgInvalid}
							<form:errors path="files[1]" />
						</c:when>
						<c:otherwise>${okImg}</c:otherwise>
					</c:choose>
				</spring:bind>
			</td>
		</tr>
		
		<tr>
			<td colspan="2" id="form-submit" ><br />
				<input type="submit" value="Upload" />
			</td>
		</tr>
	</table>
</form:form>

<c:import url="footer.jsp" />