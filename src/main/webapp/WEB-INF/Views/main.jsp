<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List,Application.Product" %>
<!DOCTYPE html>
<html lang="sv">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Produkter | Webbshop</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/shop.css">
</head>
<body>
<%@ include file="header.jsp" %>
<main class="page-content">
    <div class="page-heading">
        <div><p class="eyebrow">Butiken</p><h1>Våra produkter</h1></div>
        <p>Hitta något du gillar.</p>
    </div>
    <% List<Product> products = (List<Product>) request.getAttribute("products"); %>
    <% if (products != null && products.isEmpty()) { %>
        <div class="empty-state"><h2>Inga produkter ännu</h2><p>Kom tillbaka lite senare.</p></div>
    <% } %>
    <div class="product-grid">
    <% if (products != null) { for (Product product : products) { %>
        <article class="product-card">
            <div class="product-topline">
                <span class="category"><%= product.getCategory() == null ? "" : product.getCategory().replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;") %></span>
            </div>
            <h2><%= product.getName() == null ? "" : product.getName().replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;") %></h2>
            <div class="product-bottom">
                <p class="price"><%= product.getCost() %> kr</p>
            </div>
        </article>
    <% } } %>
    </div>
</main>
</body>
</html>
