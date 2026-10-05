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
    <p id="purchase-status" role="status" aria-live="polite"></p>
    <div class="product-grid">
        <c:forEach var="product" items="${products}">
            <article class="product-card">
                <div class="product-topline">
                    <span class="category"><c:out value="${product.category}"/></span>
                </div>
                <h2><c:out value="${product.name}"/></h2>
                <p><c:out value="${product.desc}"/></p>
                <p>Antal i lager: <strong>${product.quantity}</strong></p>
                <div class="product-bottom">
                    <p class="price">${product.cost} kr</p>
                    <c:choose>
                        <c:when test="${product.quantity <= 0}">
                            <span class="category">Slut i lager</span>
                        </c:when>
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
<script>
    document.querySelectorAll('.product-card form').forEach(form => {
        form.addEventListener('submit', async event => {
            event.preventDefault();
            const button = form.querySelector('button');
            if (button.disabled) return;
            const card = form.closest('.product-card');
            const status = document.getElementById('purchase-status');
            status.textContent = '';
            button.disabled = true;
            button.textContent = 'Lägger till…';
            card.classList.remove('purchase-added');
            try {
                const response = await fetch(form.action, {
                    method: 'POST',
                    headers: { 'X-Requested-With': 'fetch' },
                    body: new URLSearchParams(new FormData(form))
                });
                if (response.redirected) {
                    window.location.assign(response.url);
                    return;
                }
                if (response.status === 409) {
                    status.textContent = 'Det finns inte fler av varan i lager att lägga till.';
                    return;
                }
                if (!response.ok) throw new Error('Purchase failed');
                document.getElementById('cart-count').textContent = response.headers.get('X-Cart-Count');
                card.classList.add('purchase-added');
                button.textContent = 'Tillagd ✓';
                status.textContent = card.querySelector('h2').textContent + ' har lagts i varukorgen.';
            } catch (error) {
                console.error('Could not add product to cart', error);
            } finally {
                setTimeout(() => {
                    card.classList.remove('purchase-added');
                    button.textContent = 'Lägg i varukorg';
                    button.disabled = false;
                }, 1200);
            }
        });
    });
</script>
</body>
</html>
