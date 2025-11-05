<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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
        <!-- navbar -->
        <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
            <div class="container-fluid">
                <a class="navbar-brand" href="#">User Dashboard</a>
                <div class="d-flex">
                    <span class="navbar-text me-3">
                        Welcome, <span class="fw-bold text-warning">${user.fullName}</span>
                    </span>

                    <a class="btn btn-outline-light btn-sm" href="MainController?txtAction=logout">Logout</a>
                    <c:if test="${user.role eq 'admin'}">
                        <td class="text-center">
                            <a href="MainController?txtAction=callSaveProduct&update=false"
                               class="btn btn-outline-primary btn-sm product-view-btn">
                                Add new product
                            </a>
                        </td>
                    </c:if>
                </div>
            </div>
        </nav>       

        <!-- main content -->
        <div class="container mt-4">
            <jsp:include page="/customer/productList.jsp"/>
        </div>

        <!-- External JS -->
        <!--        <script src="assets/js/bootstrap.bundle.min.js"></script>-->
    </body>
</html>
