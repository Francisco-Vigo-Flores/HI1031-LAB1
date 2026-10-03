<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="sv">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Webbshop Log in</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/login.css">
    <script src="${pageContext.request.contextPath}/assets/js/login.js" defer></script>
</head>
<body>
<main class="login-page">
    <div class="login-frame">
        <section class="login-card" aria-labelledby="login-heading">
            <p class="brand">Webbshop<span>.</span></p>
            <h1 id="login-heading">Logga in</h1>
            <p class="intro">Välkommen tillbaka. Dina produkter väntar.</p>
            <% if (request.getAttribute("error") != null) { %>
                <p class="error" role="alert">${requestScope.error}</p>
            <% } %>
            <form action="${pageContext.request.contextPath}/login" method="post">
                <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
                <label for="username">Användarnamn</label>
                <input id="username" name="username" type="text" autocomplete="username"
                       maxlength="50" required>
                <label for="password">Lösenord</label>
                <input id="password" name="password" type="password" autocomplete="current-password" maxlength="256" required>
                <label class="password-option" for="show-password">
                    <input id="show-password" type="checkbox" aria-controls="password">
                    <span>Visa lösenord</span>
                </label>
                <button type="submit">Logga in <span aria-hidden="true">→</span></button>
            </form>
        </section>
    </div>
</main>
</body>
</html>
