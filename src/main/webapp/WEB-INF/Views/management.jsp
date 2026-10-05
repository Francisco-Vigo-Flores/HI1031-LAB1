<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="sv">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Min profil</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/shop.css">
</head>
<body>
<%@ include file="header.jsp" %>
<main class="page-content">
    <div class="page-heading">
        <div><p class="eyebrow">Mitt konto</p><h1>Min profil</h1></div>
        <a href="${pageContext.request.contextPath}/products">Till butiken</a>
    </div>
    <section class="profile-card" aria-labelledby="profile-name">
        <div class="profile-identity">
            <span class="profile-avatar" aria-hidden="true"><c:out value="${empty profileUser.name ? '' : profileUser.name.substring(0, 1)}"/></span>
            <div>
                <h2 id="profile-name"><c:out value="${profileUser.name}"/></h2>
                <span class="role-badge"><c:out value="${profileUser.type}"/></span>
            </div>
        </div>
        <dl class="profile-details">
            <div><dt>Användarnamn</dt><dd><c:out value="${profileUser.username}"/></dd></div>
            <div><dt>Namn</dt><dd><c:out value="${profileUser.name}"/></dd></div>
            <div><dt>Kontotyp</dt><dd><c:out value="${profileUser.type}"/></dd></div>
        </dl>
        <div class="form-actions">
            <c:if test="${isStaff}">
                <a class="button-link" href="${pageContext.request.contextPath}/inventory">Visa lager</a>
            </c:if>
            <c:if test="${isAdmin}">
                <a class="button-link" href="#users">Visa användare</a>
            </c:if>
        </div>
    </section>
    <c:if test="${isAdmin}">
        <section class="profile-management" id="users" aria-labelledby="users-heading">
            <div class="page-heading">
                <div>
                    <p class="eyebrow">Administration</p>
                    <h2 id="users-heading">Visa användare</h2>
                    <p>Här ser du butikens konton. Redigering är inte tillgänglig just nu.</p>
                </div>
                <a class="button-link" href="${pageContext.request.contextPath}/register">Skapa kundkonto</a>
            </div>
            <div class="product-grid">
                <c:choose>
                    <c:when test="${users == null}">
                        <p class="notice error" role="alert">Användarna kunde inte hämtas. Försök igen senare.</p>
                    </c:when>
                    <c:when test="${empty users}">
                        <div class="empty-state"><h3>Inga andra användare ännu</h3><p>Skapa ett konto för att komma igång.</p></div>
                    </c:when>
                    <c:otherwise>
                        <c:forEach var="account" items="${users}">
                            <article class="product-card">
                                <h3><c:out value="${account.username}"/></h3>
                                <p class="category"><c:out value="${account.type}"/></p>
                                <p><c:out value="${account.name}"/></p>
                            </article>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </div>
        </section>
    </c:if>
</main>
</body>
</html>
