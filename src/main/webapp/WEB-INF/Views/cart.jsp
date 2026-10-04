<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<%@ page import="Application.Entities.Cart,Application.Entities.CartProduct,Application.Entities.Product" %>
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
    <% if (request.getAttribute("error") != null) { %><p class="notice error" role="alert"><%= (request.getAttribute("error") == null ? "" : request.getAttribute("error").toString().replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;").replace("'", "&#39;")) %></p><% } %>
    <% Cart cart = (Cart) request.getAttribute("cart"); %>
    <% if (cart.getProducts().isEmpty()) { %>
    <div class="empty-state">
        <h2>Här är det tomt just nu</h2>
        <p>Lägg till något från våra produkter.</p>
        <a class="button-link" href="${pageContext.request.contextPath}/products">Se produkter</a>
    </div>
    <% } else { %>
    <div class="product-grid">
        <% for (CartProduct item : cart.getProducts()) {
            Product product = item.getProduct();
            int quantity = item.getAmountInCart(); %>
        <article class="product-card">
            <h2><%= product.getName().replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;") %></h2>
            <p>Antal: <%= quantity %></p>
            <p class="price"><%= String.format(java.util.Locale.forLanguageTag("sv-SE"), "%.2f", item.getProductCost()) %> kr</p>
        </article>
        <% } %>
    </div>
    <p class="cart-total">Totalt: <strong><%= String.format(java.util.Locale.forLanguageTag("sv-SE"), "%.2f", request.getAttribute("cartTotal")) %> kr</strong></p>
    <div class="form-actions" id="checkout">
        <p>Best&auml;llning &auml;r inte tillg&auml;nglig &auml;n.</p>
        <form method="post" action="${pageContext.request.contextPath}/cart">
            <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}">
            <button class="text-button" name="action" value="clear">Töm varukorgen</button>
        </form>
    </div>
    <% } %>
</main>
</body>
</html>
