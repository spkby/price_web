<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<footer>
    <div class="container-fluid">
        © <a href="http://<c:out value='${applicationScope.urlDomain}'/>" target="_blank"><c:out
            value="${applicationScope.urlDomain}"/></a> |
        <small>ver <c:out value="${applicationScope.ver}"/></small>
    </div>
</footer>

</body>
</html>
