<%@ page pageEncoding="UTF-8" %>
<%@ page import="Application.Entities.User" %>
<%
    User currentUser = (User) request.getAttribute("currentUser");
    String currentPage = (String) request.getAttribute("currentPage");
%>
<header class="site-header">
    <a class="brand" href="${pageContext.request.contextPath}/products"><span>Webbshop .</span></a>
    <nav aria-label="Huvudmeny">
        <a class="nav-button" <%= "/products".equals(currentPage) ? "aria-current=\"page\"" : "" %> href="${pageContext.request.contextPath}/products">Produkter</a>
    </nav>
    <nav class="account" aria-label="Konto och varukorg">
        <a class="nav-button cart-button" <%= "/cart".equals(currentPage) ? "aria-current=\"page\"" : "" %> href="${pageContext.request.contextPath}/cart">
            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M3 3h2l2.4 12h11.2l2-8H6"/><circle cx="9" cy="20" r="1"/><circle cx="18" cy="20" r="1"/></svg>
            Varukorg <span class="cart-count" id="cart-count" aria-live="polite" aria-atomic="true">${requestScope.cartCount}</span>
        </a>
        <% if (currentUser == null) { %>
        <a class="button-link" href="${pageContext.request.contextPath}/login">Logga in / Registrera</a>
        <% } else { %>
        <span class="profile-link"><%= currentUser.getName() == null ? "" : currentUser.getName().replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;").replace("'", "&#39;") %></span>
        <form method="post" action="${pageContext.request.contextPath}/logout">
            <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
            <button class="nav-button logout-button" type="submit">Logga ut</button>
        </form>
        <% } %>
    </nav>
</header>
