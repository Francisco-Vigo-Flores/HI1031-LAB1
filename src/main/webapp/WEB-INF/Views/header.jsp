<%@ page pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="currentUser" value="${sessionScope.user}"/>
<c:set var="loggedIn" value="${not empty currentUser}"/>
<c:set var="isAdmin" value="${currentUser.type == 'Admin'}"/>
<c:set var="isStaff" value="${isAdmin or currentUser.type == 'InventoryManager'}"/>
<header class="site-header">
    <a class="brand" href="${pageContext.request.contextPath}/products"><span>Webbshop .</span></a>
    <nav aria-label="Huvudmeny">
        <a class="nav-button" aria-current="${currentPage == '/products' ? 'page' : 'false'}" href="${pageContext.request.contextPath}/products">Produkter</a>
        <c:if test="${loggedIn}">
            <a class="nav-button" aria-current="${currentPage == '/orders' ? 'page' : 'false'}" href="${pageContext.request.contextPath}/orders">Beställningar</a>
        </c:if>
        <c:if test="${isStaff}">
            <a class="nav-button" aria-current="${currentPage == '/inventory' ? 'page' : 'false'}" href="${pageContext.request.contextPath}/inventory">Lager</a>
        </c:if>
    </nav>
    <nav class="account" aria-label="Konto och varukorg">
        <a class="nav-button cart-button" aria-current="${currentPage == '/cart' ? 'page' : 'false'}" href="${pageContext.request.contextPath}/cart">
            <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="1.7" stroke-linecap="round" stroke-linejoin="round" aria-hidden="true"><path d="M3 3h2l2.4 12h11.2l2-8H6"/><circle cx="9" cy="20" r="1"/><circle cx="18" cy="20" r="1"/></svg>
            Varukorg <span class="cart-count" id="cart-count" aria-live="polite" aria-atomic="true">${cartCount}</span>
        </a>
        <c:choose>
            <c:when test="${loggedIn}">
                <a class="nav-button profile-link" aria-current="${currentPage == '/profile' ? 'page' : 'false'}" href="${pageContext.request.contextPath}/profile">Min profil</a>
                <form method="post" action="${pageContext.request.contextPath}/logout">
                    <input type="hidden" name="csrfToken" value="${csrfToken}">
                    <button class="nav-button logout-button" type="submit">Logga ut</button>
                </form>
            </c:when>
            <c:otherwise>
                <a class="button-link" href="${pageContext.request.contextPath}/login">Logga in / Registrera</a>
            </c:otherwise>
        </c:choose>
    </nav>
</header>
