<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Product List</title>

        <!-- CSS theme đen – đỏ – trắng -->
        <link rel="stylesheet" href="assets/css/productList.css">
        <!-- Nếu dùng Bootstrap thì nhớ đã link ở layout ngoài hoặc thêm ở đây -->
        <!-- <link rel="stylesheet" href="assets/css/bootstrap.min.css"> -->
    </head>
    <body class="product-list-body">

        <div class="container px-4 px-lg-5 mt-5 product-list-wrapper">
            <div class="text-center mb-4">
                <h1 class="fw-bolder text-white">HTV STYLES</h1>
                <p class="lead fw-normal text-muted mb-0">SHOES WITH YOU</p>
            </div>

            <c:choose>
                <c:when test="${empty listProducts}">
                    <div class="alert alert-warning text-center product-empty-alert">
                        <h5 class="mb-1">No products found</h5>
                        <p class="mb-0">Please enter another keyword to find products.</p>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="row gx-4 gx-lg-5 row-cols-2 row-cols-md-3 row-cols-xl-4 justify-content-center">
                        <c:forEach var="p" items="${listProducts}">
                            <c:url var="detailUrl" value="MainController">
                                <c:param name="txtAction" value="viewProductDetail"/>
                                <c:param name="productID" value="${p.productID}"/>
                            </c:url>

                            <div class="col mb-5">
                                <div class="card h-100">
                                    <img class="card-img-top" src="${p.productImage}" alt="${p.productName}"/>
                                    <div class="card-body p-4">
                                        <div class="text-center">
                                            <h5 class="fw-bolder">
                                                <a href="${detailUrl}" class="product-link">${p.productName}</a>
                                            </h5>
                                            <p class="text-muted">${p.description}</p>
                                        </div>
                                    </div>
                                    <div class="card-footer p-4 pt-0 border-top-0 bg-transparent">
                                        <div class="text-center">
                                            <a class="btn btn-outline-dark mt-auto" href="${detailUrl}">View options</a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>

    </body>

</html>
