<%-- 
    Document   : productDetail
    Created on : Nov 3, 2025, 8:09:57 AM
    Author     : TEST
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>PRODUCT ${productDetail.productID} DETAIL</title>

        <%-- nếu đã link bootstrap + css chung ở layout khác thì bỏ 3 dòng dưới --%>
        <link rel="stylesheet" href="assets/css/bootstrap.min.css">
        <link rel="stylesheet" href="assets/css/productDetail.css">
    </head>
    <body class="product-detail-body">   

        <!-- Nút trở lại -->
        <div class="back-btn-wrapper">
            <a href="MainController?txtAction=viewProducts" class="back-btn">
                Back to Products
            </a>
        </div>

        <div class="product-page container">
            <h1 class="page-title">Product Detail</h1>

            <div class="product-layout">
                <!-- ==== LEFT: PRODUCT DETAIL CARD ==== -->
                <section class="product-detail-card">
                    <c:choose>
                        <c:when test="${not empty productDetail}">
                            <h2 class="product-name">
                                ${productName}
                            </h2>

                            <div class="product-detail-grid">
                                <p class="detail-item">
                                    <span class="label">Product ID:</span>
                                    <span class="value">${productDetail.productID}</span>
                                </p>

                                <p class="detail-item">
                                    <span class="label">Variant ID:</span>
                                    <span class="value">${productDetail.variantID}</span>
                                </p>

                                <p class="detail-item">
                                    <span class="label">Size:</span>
                                    <span class="value">${productDetail.size}</span>
                                </p>

                                <p class="detail-item">
                                    <span class="label">Color:</span>
                                    <span class="value">${productDetail.color}</span>
                                </p>

                                <p class="detail-item">
                                    <span class="label">Stock:</span>
                                    <span class="value">${productDetail.stock}</span>
                                </p>

                                <p class="detail-item">
                                    <span class="label">Price:</span>
                                    <span class="value">${productDetail.price}</span>
                                </p>

                                <p class="detail-item">
                                    <span class="label">Sales Count:</span>
                                    <span class="value">${productDetail.salesCount}</span>
                                </p>
                            </div>
                        </c:when>

                        <c:otherwise>
                            <p class="product-not-found">Product not found.</p>
                        </c:otherwise>
                    </c:choose>
                </section>

                <!-- ==== RIGHT: OTHER PRODUCTS LIST ==== -->
                <section class="product-list-section">
                    <h2 class="section-title">Other Products</h2>

                    <c:choose>
                        <c:when test="${empty listProducts}">
                            <div class="alert alert-warning text-center product-empty-alert">
                                <h5 class="mb-1">No products found</h5>
                                <p class="mb-0">Please enter another keyword to find products.</p>
                            </div>
                        </c:when>

                        <c:otherwise>
                            <div class="table-responsive product-table-wrapper">
                                <table class="table table-hover align-middle product-table">
                                    <thead>
                                        <tr>
                                            <th>Product ID</th>
                                            <th>Product Name</th>
                                            <th>Description</th>
                                            <th>Category ID</th>
                                            <th>Brand ID</th>
                                            <th>Created At</th>
                                            <th>Status</th>
                                                <c:if test="${user.role eq 'Admin'}">
                                                <th class="text-center">Action</th>
                                                </c:if>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="p" items="${listProducts}">
                                            <c:if test="${p.productID ne productID}">
                                                <tr>
                                                    <td>${p.productID}</td>
                                                    <td>${p.productName}</td>
                                                    <td>${p.description}</td>
                                                    <td>${p.categoryID}</td>
                                                    <td>${p.brandID}</td>
                                                    <td>${p.createdAt}</td>
                                                    <td>${p.isActive}</td>

                                                    <td>
                                                        <a href="MainController?txtAction=viewProductDetail&productID=${p.productID}&productName=${p.productName}">
                                                            View Detail
                                                        </a>
                                                    </td>
                                                </tr>
                                            </c:if>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </section>
            </div>
        </div>
    </body>
</html>
