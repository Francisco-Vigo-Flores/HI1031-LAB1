<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
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
        <a href="${pageContext.request.contextPath}/products">Fortsätt handla</a>
    </div>
    <div class="empty-state">
        <h2>Här är det tomt just nu</h2>
        <p>Lägg till något från våra produkter.</p>
        <a class="button-link" href="${pageContext.request.contextPath}/products">Se produkter</a>
    </div>
</main>
</body>
</html>
