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
    <c:when test="${!empty invalidFilename}">
        <div id="form-errors">
            <p><img src="/blender/images/invalid.png" /> The file you upload must end with ${invalidFilename}.</p>
        </div>
    </c:when>
    <c:otherwise>
        <p>To create a new package, choose a completed spec file to upload.</p>
    </c:otherwise>
</c:choose>

<form action="<c:url value="/upload" />" method="post" enctype="multipart/form-data">
	<table>
		<tr>
			<td class="form-labels">
				<label for="specFile">Spec File:</label>
			</td>
			<td><input type="file" name="specFile" id="specFile" /></td>
		</tr>
		<tr>
			<td colspan="2" id="form-submit" ><br />
				<input type="submit" value="Upolad" />
			</td>
		</tr>
	</table>
</form>

<c:import url="footer.jsp" />