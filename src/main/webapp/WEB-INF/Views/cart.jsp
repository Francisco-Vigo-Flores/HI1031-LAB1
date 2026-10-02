<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<%@ page import="java.util.List,java.util.Map,java.util.LinkedHashMap,Application.Product" %>
<html lang="sv">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Varukorg | Webbshop</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/shop.css">
</head>
<body>
<%@ include file="header.jsp" %>
<main class="page-content">
    <div class="page-heading">
        <div><p class="eyebrow">Dina val</p><h1>Din varukorg</h1></div>
        <a href="${pageContext.request.contextPath}/">Fortsätt handla</a>
    </div>
    <% List<Product> cart = (List<Product>) session.getAttribute("cart"); %>
    <% if (cart == null || cart.isEmpty()) { %>
    <div class="empty-state">
        <h2>Här är det tomt just nu</h2>
        <p>Lägg till något från våra produkter.</p>
        <a class="button-link" href="${pageContext.request.contextPath}/">Se produkter</a>
    </div>
    <% } else { %>
    <%
        Map<Integer, Product> items = new LinkedHashMap<>();
        Map<Integer, Integer> quantities = new LinkedHashMap<>();
        for (Product product : cart) {
            items.put(product.getId(), product);
            quantities.put(product.getId(), quantities.getOrDefault(product.getId(), 0) + 1);
        }
    %>
    <div class="product-grid">
        <% for (Product product : items.values()) {
            int quantity = quantities.get(product.getId()); %>
        <article class="product-card">
            <h2><%= product.getName().replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;") %></h2>
            <p>Antal: <%= quantity %></p>
            <p class="price"><%= String.format(java.util.Locale.forLanguageTag("sv-SE"), "%.2f", product.getCost() * quantity) %> kr</p>
        </article>
        <% } %>
    </div>
    <% } %>
</main>
</body>
</html>
