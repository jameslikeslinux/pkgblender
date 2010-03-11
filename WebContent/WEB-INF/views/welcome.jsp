<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<html>
<head>
<title>Welcome</title>
</head>
<body>
<h2><fmt:message key="welcome.title" /></h2>
Today is <c:out value="${today}" />.
</body>
</html>