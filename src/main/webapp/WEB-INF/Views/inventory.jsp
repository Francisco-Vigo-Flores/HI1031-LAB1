<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="sv">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Lager</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/shop.css">
</head>
<body>
<%@ include file="header.jsp" %>
<main class="page-content">
    <div class="page-heading"><h1>Lager</h1><a href="${pageContext.request.contextPath}/products">Se produkter</a></div>
    <p class="notice">Här visas aktuellt lagersaldo. Redigering är inte tillgänglig just nu.</p>
    <c:if test="${products == null}">
        <p class="notice error">Lagersaldot kunde inte hämtas.</p>
    </c:if>
    <div class="product-grid">
        <c:forEach var="product" items="${products}">
            <article class="product-card">
                <h2><c:out value="${product.name}"/></h2>
                <p>Antal i lager: <strong>${product.quantity}</strong></p>
            </article>
        </c:forEach>
    </div>
</main>
</body>
</html>
