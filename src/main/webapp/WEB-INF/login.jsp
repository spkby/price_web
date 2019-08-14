<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ include file="header.jsp" %>
<div class="jumbotron">
    <form action="j_security_check" method="post" class="needs-validation" novalidate>
        <div class="form-group">
            <input type="text" class="form-control" name="j_username" placeholder="Имя пользователя" required>
            <div class="invalid-feedback">
                Пожалуйста введите <b>Имя пользователя</b>.
            </div>
        </div>
        <div class="form-group">
            <input type="password" class="form-control" name="j_password" placeholder="Пароль" required>
            <div class="invalid-feedback">
                Пожалуйста введите <b>Пароль</b>.
            </div>
        </div>
        <input type="submit" class="btn btn-primary" value="Войти">
    </form>
</div>
<script>
    // Example starter JavaScript for disabling form submissions if there are invalid fields
    (function () {
        'use strict';
        window.addEventListener('load', function () {
            // Fetch all the forms we want to apply custom Bootstrap validation styles to
            let forms = document.getElementsByClassName('needs-validation');
            // Loop over them and prevent submission
            let validation = Array.prototype.filter.call(forms, function (form) {
                form.addEventListener('submit', function (event) {
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