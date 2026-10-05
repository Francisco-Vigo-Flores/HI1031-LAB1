<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="sv">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Webbshop</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/shop.css">
</head>
<body>
<%@ include file="header.jsp" %>
<main class="page-content">
    <div class="page-heading">
        <div><p class="eyebrow">Butiken</p><h1>Våra produkter</h1></div>
        <p>Hitta något du gillar.</p>
    </div>
    <c:choose>
        <c:when test="${products == null}">
            <p class="notice error" role="alert">Produkterna kunde inte hämtas. Försök igen senare.</p>
        </c:when>
        <c:when test="${empty products}">
            <div class="empty-state"><h2>Inga produkter ännu</h2><p>Kom tillbaka lite senare.</p></div>
        </c:when>
    </c:choose>
    <div class="product-grid">
        <c:forEach var="product" items="${products}">
            <article class="product-card">
                <div class="product-topline">
                    <span class="category"><c:out value="${product.category}"/></span>
                </div>
                <h2><c:out value="${product.name}"/></h2>
                <div class="product-bottom">
                    <p class="price">${product.cost} kr</p>
                    <c:choose>
                        <c:when test="${loggedIn}">
                            <form method="post" action="${pageContext.request.contextPath}/cart">
                                <input type="hidden" name="csrfToken" value="${csrfToken}">
                                <input type="hidden" name="productId" value="${product.id}">
                                <button type="submit">Lägg i varukorg</button>
                            </form>
                        </c:when>
                        <c:otherwise>
                            <a class="button-link" href="${pageContext.request.contextPath}/login">Logga in för att handla</a>
                        </c:otherwise>
                    </c:choose>
                </div>
            </article>
        </c:forEach>
    </div>
</main>
</body>
</html>
