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
    <c:if test="${param.updated == 'true'}">
        <p class="notice success" role="status">Lagersaldot har sparats.</p>
    </c:if>
    <c:if test="${not empty error}">
        <p class="notice error" role="alert"><c:out value="${error}"/></p>
    </c:if>
    <c:if test="${products == null}">
        <p class="notice error">Lagersaldot kunde inte hämtas.</p>
    </c:if>
    <div class="product-grid">
        <c:forEach var="product" items="${products}">
            <article class="product-card">
                <h2><c:out value="${product.name}"/></h2>
                <form class="form-actions" method="post" action="${pageContext.request.contextPath}/inventory">
                    <input type="hidden" name="csrfToken" value="${csrfToken}">
                    <input type="hidden" name="productId" value="${product.id}">
                    <label for="quantity-${product.id}">Antal i lager</label>
                    <input class="stock-input" id="quantity-${product.id}" name="quantity" type="number" min="0" max="2147483647" step="1" value="${product.quantity}" required>
                    <button type="submit">Spara</button>
                </form>
            </article>
        </c:forEach>
    </div>
</main>
</body>
</html>
