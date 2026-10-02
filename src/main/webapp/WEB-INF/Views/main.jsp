<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List,Application.Product" %>
<!DOCTYPE html>
<html lang="sv">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Webbshop</title>
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
    <p id="purchase-status" role="status" aria-live="polite"></p>
    <div class="product-grid">
    <% if (products != null) { for (Product product : products) { %>
        <article class="product-card">
            <div class="product-topline">
                <span class="category"><%= product.getCategory() == null ? "" : product.getCategory().replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;") %></span>
            </div>
            <h2><%= product.getName() == null ? "" : product.getName().replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;") %></h2>
            <div class="product-bottom">
                <p class="price"><%= product.getCost() %> kr</p>
                <form method="post" action="${pageContext.request.contextPath}/cart">
                    <input type="hidden" name="productId" value="<%= product.getId() %>">
                    <button type="submit">Köp</button>
                </form>
            </div>
        </article>
    <% } } %>
    </div>
</main>
<script>
    document.querySelectorAll('.product-card form').forEach(form => {
        form.addEventListener('submit', async event => {
            event.preventDefault();
            const button = form.querySelector('button');
            if (button.disabled) return;
            const card = form.closest('.product-card');
            const status = document.getElementById('purchase-status');
            button.disabled = true;
            button.textContent = 'Lägger till…';
            card.classList.remove('purchase-added');
            try {
                const response = await fetch(form.action, {
                    method: 'POST',
                    headers: { 'X-Requested-With': 'fetch' },
                    body: new URLSearchParams(new FormData(form))
                });
                if (!response.ok) throw new Error('Purchase failed');
                card.classList.add('purchase-added');
                button.textContent = 'Tillagd ✓';
                status.textContent = card.querySelector('h2').textContent + ' har lagts i varukorgen.';
            } catch (error) {
                button.textContent = 'Försök igen';
                status.textContent = 'Kunde inte lägga till varan. Försök igen.';
            } finally {
                setTimeout(() => {
                    card.classList.remove('purchase-added');
                    button.textContent = 'Köp';
                    button.disabled = false;
                }, 1200);
            }
        });
    });
</script>
</body>
</html>
