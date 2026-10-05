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
        <p class="notice success" role="status">Ändringarna har sparats.</p>
    </c:if>
    <c:if test="${not empty error}">
        <p class="notice error" role="alert"><c:out value="${error}"/></p>
    </c:if>
    <c:if test="${products == null}">
        <p class="notice error">Lagersaldot kunde inte hämtas.</p>
    </c:if>
    <div class="page-heading">
            <a class="button-link" href="${pageContext.request.contextPath}/inventory#product">Ny vara</a>
            <a class="button-link" href="${pageContext.request.contextPath}/inventory#categories">Skapa / redigera kategorier</a>
    </div>
    <div class="product-grid">
        <c:forEach var="stockProduct" items="${products}">
            <article class="product-card">
                <h2><c:out value="${stockProduct.name}"/></h2>
                <p class="category"><c:out value="${stockProduct.category}"/></p>
                <a href="${pageContext.request.contextPath}/inventory?productId=${stockProduct.id}#product">Redigera vara</a>
                <form class="form-actions" method="post" action="${pageContext.request.contextPath}/inventory">
                    <input type="hidden" name="csrfToken" value="${csrfToken}">
                    <input type="hidden" name="productId" value="${stockProduct.id}">
                    <label for="quantity-${stockProduct.id}">Antal i lager</label>
                    <input class="stock-input" id="quantity-${stockProduct.id}" name="quantity" type="number" min="0" max="2147483647" step="1" value="${stockProduct.quantity}" required>
                    <button type="submit">Spara</button>
                </form>
            </article>
        </c:forEach>
    </div>
    <c:if test="${categories == null}"><p class="notice error" role="alert">Kategorierna kunde inte hämtas.</p></c:if>
    <c:set var="retryProduct" value="${not empty error and param.action == 'product'}"/>
    <section class="profile-card" id="product">
        <h2>${not empty product or (retryProduct and param.id != '0') ? 'Redigera vara' : 'Ny vara'}</h2>
        <form class="catalog-form" method="post" action="${pageContext.request.contextPath}/inventory">
            <input type="hidden" name="action" value="product">
            <input type="hidden" name="id" value="<c:out value='${retryProduct ? param.id : (empty product ? 0 : product.id)}'/>">
            <label for="product-name">Namn</label>
            <input id="product-name" name="name" maxlength="50" value="<c:out value='${retryProduct ? param.name : product.name}'/>" required>
            <label for="product-cost">Pris (kr)</label>
            <input id="product-cost" name="cost" type="number" min="0" step="0.01" value="<c:out value='${retryProduct ? param.cost : product.cost}'/>" required>
            <label for="product-category">Kategori</label>
            <input id="product-category" name="category" list="category-options" maxlength="50" value="<c:out value='${retryProduct ? param.category : product.category}'/>" required>
            <datalist id="category-options">
                <c:forEach var="category" items="${categories}">
                    <option value="<c:out value='${category}'/>"></option>
                </c:forEach>
            </datalist>
            <p>Välj en befintlig kategori eller skriv en ny. Kategorin sparas tillsammans med varan.</p>
            <label for="product-desc">Beskrivning</label>
            <textarea id="product-desc" name="desc" maxlength="100" rows="3" required><c:out value="${retryProduct ? param.desc : product.desc}"/></textarea>
            <label for="product-quantity">Antal i lager</label>
            <input id="product-quantity" name="quantity" type="number" min="0" max="2147483647" step="1" value="<c:out value='${retryProduct ? param.quantity : (empty product ? 0 : product.quantity)}'/>" required>
            <button type="submit">Spara vara</button>
        </form>
    </section>
    <section class="profile-management" id="categories">
        <div class="page-heading"><h2>Kategorier</h2></div>
        <div class="product-grid">
            <article class="product-card">
                <h2>Ny kategori</h2>
                <p>Skapa en vara med det nya kategorinamnet. Byter du namn på en kategori här uppdateras alla dess varor.</p>
                <a class="button-link" href="${pageContext.request.contextPath}/inventory#product-category">Ny vara / kategori</a>
            </article>
            <c:forEach var="category" items="${categories}" varStatus="row">
                <article class="product-card">
                    <form class="catalog-form" method="post" action="${pageContext.request.contextPath}/inventory">
                        <input type="hidden" name="action" value="category">
                        <input type="hidden" name="oldName" value="<c:out value='${category}'/>">
                        <label for="category-${row.index}">Kategorinamn</label>
                        <input id="category-${row.index}" name="name" maxlength="50" value="<c:out value='${category}'/>" required>
                        <button type="submit">Spara kategori</button>
                    </form>
                </article>
            </c:forEach>
        </div>
    </section>
</main>
</body>
</html>
