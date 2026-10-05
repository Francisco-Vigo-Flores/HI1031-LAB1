<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<fmt:setLocale value="sv_SE"/>
<!DOCTYPE html>
<html lang="sv">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Varukorg</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/shop.css">
</head>
<body>
<%@ include file="header.jsp" %>
<main class="page-content">
    <div class="page-heading">
        <div><p class="eyebrow">Dina val</p><h1>Din varukorg</h1></div>
        <a href="${pageContext.request.contextPath}/products">Fortsätt handla</a>
    </div>
    <c:if test="${param.checkout == 'failed'}">
        <p class="notice error" role="alert">Beställningen kunde inte läggas. Kontrollera att varukorgen innehåller varor och att antalet finns i lager. Försök igen.</p>
    </c:if>
    <c:choose>
        <c:when test="${empty cartItems}">
            <div class="empty-state">
                <h2>Här är det tomt just nu</h2>
                <p>Lägg till något från våra produkter.</p>
                <a class="button-link" href="${pageContext.request.contextPath}/products">Se produkter</a>
            </div>
        </c:when>
        <c:otherwise>
            <div class="product-grid">
                <c:forEach var="item" items="${cartItems}">
                    <article class="product-card">
                        <h2><c:out value="${item.product.name}"/></h2>
                        <p><c:out value="${item.product.desc}"/></p>
                        <p>Antal i lager: <strong>${item.product.quantity}</strong></p>
                        <p>Antal: ${item.amountInCart}</p>
                        <p class="price"><fmt:formatNumber value="${item.productCost}" minFractionDigits="2" maxFractionDigits="2" groupingUsed="false"/> kr</p>
                        <form class="form-actions" method="post" action="${pageContext.request.contextPath}/cart">
                            <input type="hidden" name="csrfToken" value="${csrfToken}">
                            <input type="hidden" name="productId" value="${item.product.id}">
                            <button class="text-button" name="action" value="remove">Ta bort en</button>
                        </form>
                    </article>
                </c:forEach>
            </div>
            <p class="cart-total">Totalt: <strong><fmt:formatNumber value="${cartTotal}" minFractionDigits="2" maxFractionDigits="2" groupingUsed="false"/> kr</strong></p>
            <div class="form-actions">
                <form method="post" action="${pageContext.request.contextPath}/cart">
                    <input type="hidden" name="csrfToken" value="${csrfToken}">
                    <button type="submit" name="action" value="checkout">Beställ</button>
                </form>
                <form method="post" action="${pageContext.request.contextPath}/cart">
                    <input type="hidden" name="csrfToken" value="${csrfToken}">
                    <button class="text-button" name="action" value="clear">Töm varukorgen</button>
                </form>
            </div>
        </c:otherwise>
    </c:choose>
</main>
</body>
</html>
