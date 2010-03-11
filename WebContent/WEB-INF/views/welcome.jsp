<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<html>
<head>
<title>Welcome</title>
</head>
<body>
<h2><fmt:message key="welcome.title" /></h2>
Today is <c:out value="${today}" />.<br /><br />
People:<br />
<c:forEach items="${people}" var="person">${person.firstName} ${person.lastName} <a href="delete/${person.id}">Delete</a><br /></c:forEach>
<form:form modelAttribute="person" method="post">
<form:input path="firstName" />
<form:input path="lastName" />
<input type="submit" value="Add" />
</form:form>
</body>
</html>