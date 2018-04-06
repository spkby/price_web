<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="header.jsp" %>
<div class="jumbotron">
    <form action="<c:url value='/login'/>" method="post" class="needs-validation" novalidate>
        <div class="form-group">
            <input type="text" class="form-control" name="login" placeholder="Login" required>
            <div class="invalid-feedback">
                Please enter login.
            </div>
        </div>
        <div class="form-group">
            <input type="password" class="form-control" name="password" placeholder="Password" required>
            <div class="invalid-feedback">
                Please enter password.
            </div>
        </div>
        <input type="submit" class="btn btn-primary" value="Login">
    </form>
    <c:if test="${error}">
        <div class="alert alert-danger" role="alert">
            Invalid login or password
        </div>
    </c:if>
</div>
<script>
    // Example starter JavaScript for disabling form submissions if there are invalid fields
    (function() {
        'use strict';
        window.addEventListener('load', function() {
            // Fetch all the forms we want to apply custom Bootstrap validation styles to
            var forms = document.getElementsByClassName('needs-validation');
            // Loop over them and prevent submission
            var validation = Array.prototype.filter.call(forms, function(form) {
                form.addEventListener('submit', function(event) {
                    if (form.checkValidity() === false) {
                        event.preventDefault();
                        event.stopPropagation();
                    }
                    form.classList.add('was-validated');
                }, false);
            });
        }, false);
    })();
</script>
<%@ include file="footer.jsp" %>