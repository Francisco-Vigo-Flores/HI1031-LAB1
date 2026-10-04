<%@ page pageEncoding="UTF-8" %>
<%@ page import="Presentation.Web" %>
<%
    String currentPage = (String) request.getAttribute("jakarta.servlet.forward.servlet_path");
    if (currentPage == null) currentPage = request.getServletPath();
    java.util.List<?> headerCart = (java.util.List<?>) session.getAttribute("cart");
    int cartCount = headerCart == null ? 0 : headerCart.size();
%>
<header class="site-header">
    <a class="brand" href="${pageContext.request.contextPath}/products"><span>Webbshop .</span></a>
    <nav aria-label="Huvudmeny">
        <a class="nav-button" <%= "/products".equals(currentPage) ? "aria-current=\"page\"" : "" %> href="${pageContext.request.contextPath}/products">Produkter</a>
        <a class="nav-button order-button" href="${pageContext.request.contextPath}/cart#checkout">Beställ</a>
        <% if (Web.user(request) != null) { %>
        <a class="nav-button" <%= "/orders".equals(currentPage) ? "aria-current=\"page\"" : "" %> href="${pageContext.request.contextPath}/orders">Ordrar</a>
        <% } %>
        <% if (Web.staff(request)) { %>
        <a class="nav-button" <%= "/inventory".equals(currentPage) ? "aria-current=\"page\"" : "" %> href="${pageContext.request.contextPath}/inventory">Lager</a>
        <% } %>
    </nav>
    <nav class="account" aria-label="Konto och varukorg">
        <a class="nav-button cart-button" <%= "/cart".equals(currentPage) ? "aria-current=\"page\"" : "" %> href="${pageContext.request.contextPath}/cart">
            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M3 3h2l2.4 12h11.2l2-8H6"/><circle cx="9" cy="20" r="1"/><circle cx="18" cy="20" r="1"/></svg>
            Varukorg <span class="cart-count" id="cart-count" aria-live="polite" aria-atomic="true"><%= cartCount %></span>
        </a>
        <% if (Web.user(request) == null) { %>
        <a class="button-link" href="${pageContext.request.contextPath}/login">Logga in / Registrera</a>
        <% } else { %>
        <a class="nav-button profile-link" <%= "/profile".equals(currentPage) ? "aria-current=\"page\"" : "" %> href="${pageContext.request.contextPath}/profile">Min profil</a>
        <form method="post" action="${pageContext.request.contextPath}/logout">
            <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
            <button class="nav-button logout-button" type="submit">Logga ut</button>
        </form>
        <% } %>
    </nav>
</header>
