<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ include file="includes/header.jspf" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>HOME</title>

        <!-- External CSS -->
        <link rel="stylesheet" href="assets/css/style.css" />
    </head>
    <body class="bg-light">

        <!-- CHỈ auto-submit nếu chưa có productList -->
        <c:if test="${empty listProducts}">
            <form action="MainController" method="post" id="viewProductsForm">
                <input type="hidden" name="txtAction" value="viewProducts"/>
            </form>

            <script src="assets/js/autoRedirect.js"></script>
        </c:if>     
        <!-- main content -->
        <div class="container mt-4">
            <c:choose>
                <c:when test="${user.role eq 'admin' or user.role eq 'staff'}">
                    <jsp:include page="admin/listOfProducts.jsp" />
                </c:when>
                <c:otherwise>
                    <jsp:include page="customer/productList.jsp" />
                </c:otherwise>
            </c:choose>
        </div>

        <!-- External JS -->
        <!--        <script src="assets/js/bootstrap.bundle.min.js"></script>-->
    </body>
</html>
