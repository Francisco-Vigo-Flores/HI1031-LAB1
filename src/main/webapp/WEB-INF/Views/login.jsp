<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="Presentation.Web" %>
<% boolean registering = Boolean.TRUE.equals(request.getAttribute("registering")); %>
<!DOCTYPE html>
<html lang="sv">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><%= registering ? "Registrera" : "Logga in" %></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/login.css">
    <script src="${pageContext.request.contextPath}/assets/js/login.js" defer></script>
</head>
<body>
<main class="login-page">
    <div class="login-frame">
        <section class="login-card" aria-labelledby="login-heading">
            <p class="brand">Webbshop<span>.</span></p>
            <h1 id="login-heading"><%= registering ? "Skapa konto" : "Logga in" %></h1>
            <p class="intro"><%= registering ? (Web.admin(request) ? "Skapa ett konto och välj behörighet." : "Skapa ett kundkonto för att beställa.") : "Välkommen tillbaka. Dina produkter väntar." %></p>
            <% if (request.getAttribute("error") != null) { %>
                <p class="error" role="alert"><%= Web.escape(request.getAttribute("error")) %></p>
            <% } %>
            <% if (request.getParameter("registered") != null) { %>
                <p class="intro" role="status">Kontot är skapat. Logga in för att beställa.</p>
            <% } %>
            <form action="${pageContext.request.contextPath}<%= registering ? "/register" : "/login" %>" method="post">
                <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
                <% if (registering) { %>
                <label for="name">Namn</label>
                <input id="name" name="name" autocomplete="name" maxlength="50" value="<%= Web.escape(request.getParameter("name")) %>" required>
                <% } %>
                <label for="username">Användarnamn</label>
                <input id="username" name="username" type="text" autocomplete="username"
                       maxlength="50" value="<%= Web.escape(request.getParameter("username")) %>" required>
                <label for="password">Lösenord</label>
                <input id="password" name="password" type="password" autocomplete="<%= registering ? "new-password" : "current-password" %>" <%= registering ? "minlength=\"8\"" : "" %> maxlength="256" required>
                <label class="password-option" for="show-password">
                    <input id="show-password" type="checkbox" aria-controls="password">
                    <span>Visa lösenord</span>
                </label>
                <% if (registering) { %><p class="intro">Lösenordet ska innehålla minst 8 tecken.</p><% } %>
                <% if (registering && Web.admin(request)) { %>
                <label for="type">Behörighet</label>
                <select id="type" name="type"><option value="Customer">Kund</option><option value="Admin">Administratör</option><option value="InventoryManager">Lagerarbetare</option></select>
                <% } %>
                <button type="submit"><%= registering ? "Skapa konto" : "Logga in" %> <span aria-hidden="true">→</span></button>
            </form>
            <p class="form-link"><a href="${pageContext.request.contextPath}<%= registering ? (Web.admin(request) ? "/profile#users" : "/login") : "/register" %>"><%= registering ? (Web.admin(request) ? "Tillbaka till min profil" : "Har du ett konto? Logga in") : "Skapa konto" %></a></p>
        </section>
    </div>
</main>
</body>
</html>
