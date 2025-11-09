<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>HOME</title>

        <!-- CSS base (Bootstrap + style chung) -->
        <link rel="stylesheet" href="assets/css/style.css" />
        <!-- ✅ CSS riêng cho trang product list (gradient + card) -->
        <link rel="stylesheet" href="assets/css/productList.css" />
    </head>

    <!-- ✅ dùng class này để ăn gradient trong productList.css -->
    <body class="product-list-body">

        <%@ include file="includes/header.jspf" %>

        <!-- CHỈ auto-submit nếu chưa có productList -->
        <c:if test="${empty listProducts}">
            <form action="MainController" method="post" id="viewProductsForm">
                <input type="hidden" name="txtAction" value="viewProducts"/>
            </form>
            <script src="assets/js/autoRedirect.js"></script>
        </c:if>

        <!-- main content -->
        <!-- ✅ thêm class product-page-container để mình style gradient cho khung -->
        <div class="container mt-4 product-page-container">
            <jsp:include page="customer/productList.jsp" />
        </div>

        <!-- External JS (nếu cần) -->
        <!-- <script src="assets/js/bootstrap.bundle.min.js"></script> -->
    </body>
</html>
