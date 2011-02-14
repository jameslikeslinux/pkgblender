<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<c:import url="header.jsp">
	<c:param name="title">Package - ${pkg.name}</c:param>
</c:import>

<h1>Package '${pkg.name}'</h1>

<table id="package">
	<tr>
		<td>
			<div class="package-section">
				<h3>Maintainer</h3>
				<div class="section-content">${pkg.maintainer.name}</div>
			</div>
			<br />
			<div class="package-section">
				<h3>Files</h3>
				<div class="section-content">
					<c:forEach var="file" items="${files}">
						<a href="<c:url value="${pkg.name}/files/${file.name}" />">${file.name}</a><br />
					</c:forEach>
				</div>
			</div>
			<br />
			<div class="package-section">
				<h3>Operating Systems</h3>
				<div class="section-content">
					<c:choose>
						<c:when test="${canModifyPackage}">
							<form:form modelAttribute="osesForm" action="${pkg.name}/branches/${branch.name}/changeOses">
								<div class="package-form">
									<form:checkboxes items="${osesForm.availableOses}" path="oses" />
									<div class="package-form-submit"><input type="submit" value="Change" /></div>
								</div>
							</form:form>
						</c:when>
						<c:otherwise>
							<c:if test="${empty branch.oses}">
								None
							</c:if>
							<c:forEach var="os" items="${branch.oses}">
								${os.name}<br />
							</c:forEach>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
			<br />
			<div class="package-section">
				<h3>Dependencies</h3>
				<div class="section-content">
					<c:if test="${empty dependencies}">
						None
					</c:if>
					<c:forEach var="dependency" items="${dependencies}">
						<c:choose>
							<c:when test="${dependency.status == OK}">
								<img src="<c:url value="/images/ok.png" />" /> <a href="<c:url value="/packages/${dependency.realName}" />">${dependency.name}</a><br />
							</c:when>
							<c:otherwise>
								<img src="<c:url value="/images/invalid.png" />" /> ${dependency.name}<br />
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</div>
			</div>
		</td>
		
		<td>
			<h3>Comments</h3>
			<div class="section-content">
				<sec:authorize access="not isAuthenticated()">
					<c:if test="${empty comments}">
						None
					</c:if>
				</sec:authorize>
				<sec:authorize access="isAuthenticated()">
					<c:forEach var="comment" items="${pkg.comments}">
						${comment.comment}<br />
					</c:forEach>
					<form action="${pkg.name}/addComment" method="post">
						<textarea name="comment" style="width: 100%" rows="4"></textarea>
						<div class="package-form-submit"><input type="submit" value="Post" /></div>
					</form>
				</sec:authorize>
			</div>
		</td>
	</tr>
</table>

<c:import url="footer.jsp" />