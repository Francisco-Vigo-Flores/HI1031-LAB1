<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="sv">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:out value="${registering ? 'Registrera' : 'Logga in'}"/></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/login.css">
    <script src="${pageContext.request.contextPath}/assets/js/login.js" defer></script>
</head>
<body>
<main class="login-page">
    <div class="login-frame">
        <section class="login-card" aria-labelledby="login-heading">
            <p class="brand">Webbshop<span>.</span></p>
            <h1 id="login-heading"><c:out value="${registering ? 'Skapa konto' : 'Logga in'}"/></h1>
            <p class="intro"><c:out value="${registering ? 'Skapa ett kundkonto.' : 'Logga in till butiken.'}"/></p>
            <c:if test="${not empty error}">
                <p class="error" role="alert"><c:out value="${error}"/></p>
            </c:if>
            <c:if test="${param.registered == 'true'}">
                <p class="intro" role="status">Kontot är skapat. Du kan nu logga in.</p>
            </c:if>
            <form action="${pageContext.request.contextPath}${registering ? '/register' : '/login'}" method="post">
                <input type="hidden" name="csrfToken" value="${csrfToken}">
                <c:if test="${registering}">
                    <label for="name">Namn</label>
                    <input id="name" name="name" autocomplete="name" maxlength="50"
                           value="<c:out value='${param.name}'/>" required>
                </c:if>
                <label for="username">Användarnamn</label>
                <input id="username" name="username" type="text" autocomplete="username"
                       maxlength="50" value="<c:out value='${param.username}'/>" required>
                <label for="password">Lösenord</label>
                <input id="password" name="password" type="password"
                       autocomplete="${registering ? 'new-password' : 'current-password'}" minlength="1"
                       maxlength="4" required>
                <label class="password-option" for="show-password">
                    <input id="show-password" type="checkbox" aria-controls="password">
                    <span>Visa lösenord</span>
                </label>
                <c:if test="${registering}">
                    <p class="intro">Lösenordet ska innehålla 1–4 tecken.</p>
                </c:if>
                <button type="submit"><c:out value="${registering ? 'Skapa konto' : 'Logga in'}"/> <span aria-hidden="true">→</span></button>
            </form>
            <p class="form-link"><a href="${pageContext.request.contextPath}${registering ? '/login' : '/register'}"><c:out value="${registering ? 'Logga in' : 'Skapa konto'}"/></a></p>
        </section>
    </div>
</main>
</body>
</html>