<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<fmt:setLocale value="sv_SE"/>
<!DOCTYPE html>
<html lang="sv">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Beställningar</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/shop.css">
</head>
<body>
<%@ include file="header.jsp" %>
<main class="page-content">
    <div class="page-heading">
        <div><p class="eyebrow">${isStaff ? 'Butiken' : 'Mitt konto'}</p><h1>Beställningar</h1></div>
        <a href="${pageContext.request.contextPath}/products">Till butiken</a>
    </div>
    <c:if test="${not empty param.placed}">
        <p class="notice success" role="status">Din beställning har lagts.</p>
    </c:if>
    <c:if test="${param.cartClearFailed == 'true'}">
        <p class="notice error" role="alert">Beställningen är sparad, men varukorgen kunde inte tömmas. Töm varukorgen innan du handlar igen.</p>
    </c:if>
    <c:if test="${param.completed == 'true'}">
        <p class="notice success" role="status">Beställningen har slutförts.</p>
    </c:if>
    <c:if test="${param.completed == 'false'}">
        <p class="notice error" role="alert">Beställningen kunde inte slutföras. Försök igen.</p>
    </c:if>
    <c:choose>
        <c:when test="${orders == null}">
            <p class="notice error" role="alert">Beställningarna kunde inte hämtas. Försök igen senare.</p>
        </c:when>
        <c:when test="${empty orders}">
            <div class="empty-state">
                <h2>Inga beställningar ännu</h2>
                <p>Här visas ${isStaff ? 'butikens' : 'dina'} beställningar.</p>
                <a class="button-link" href="${pageContext.request.contextPath}/products">Se produkter</a>
            </div>
        </c:when>
        <c:otherwise>
            <div class="product-grid">
                <c:forEach var="order" items="${orders}">
                    <article class="product-card">
                        <div class="product-topline"><span class="role-badge">${order.complete ? 'Slutförd' : 'Pågående'}</span></div>
                        <h2>Beställning #${order.id}</h2>
                        <c:if test="${isStaff}"><p>Kund: <c:out value="${order.user.name}"/></p></c:if>
                        <c:set var="orderTotal" value="${0}"/>
                        <c:forEach var="item" items="${order.products}">
                            <p><c:out value="${item.product.name}"/> × ${item.amountInCart}
                                — <fmt:formatNumber value="${item.productCost}" minFractionDigits="2" maxFractionDigits="2" groupingUsed="false"/> kr</p>
                            <c:set var="orderTotal" value="${orderTotal + item.productCost}"/>
                        </c:forEach>
                        <div class="product-bottom"><p class="price">Totalt: <fmt:formatNumber value="${orderTotal}" minFractionDigits="2" maxFractionDigits="2" groupingUsed="false"/> kr</p></div>
                        <c:if test="${isStaff and not order.complete}">
                            <form class="form-actions" method="post" action="${pageContext.request.contextPath}/orders">
                                <input type="hidden" name="csrfToken" value="${csrfToken}">
                                <input type="hidden" name="orderId" value="${order.id}">
                                <button type="submit">Packa / slutför beställning</button>
                            </form>
                        </c:if>
                    </article>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</main>
</body>
</html>
