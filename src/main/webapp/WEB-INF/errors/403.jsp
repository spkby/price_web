<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core_1_1" %>
<html>
<head>
    <title>Login Error</title>
</head>
<body>
<c:url var="url" value="/"/>
<h2>Invalid user name or password.</h2>

<p>Please enter a user name or password that is authorized to access this
    application. Click here to <a href="javascript:history.back()">Try Again</a></p>
</body>
</html>