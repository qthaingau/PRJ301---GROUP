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

        <%-- nếu đã link bootstrap + css chung ở layout khác thì bỏ 2 dòng dưới --%>
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
                <!-- ==== LEFT: PRODUCT + VARIANT DETAIL CARD ==== -->
                <section class="product-detail-card">
                    <c:choose>
                        <c:when test="${not empty productDetail}">
                            <!-- TÊN SẢN PHẨM -->
                            <h2 class="product-name">${product.productName}</h2>

                            <!-- THÔNG TIN CHUNG CỦA PRODUCT -->
                            <div class="product-info mb-3 p-3 border rounded">
                                <p>
                                    <span class="label">Product ID:</span>
                                    <span class="value">${product.productID}</span>
                                </p>

                                <!-- CATEGORY: click để sang categoryList (filterCategory) -->
                                <p>
                                    <span class="label">Category ID:</span>
                                    <span class="value">
                                        <a class="product-link"
                                           href="MainController?txtAction=filterCategory&keyword=${product.categoryID}">
                                            ${product.categoryID}
                                        </a>
                                    </span>
                                </p>

                                <!-- BRAND: click để sang brandList (filterBrand) -->
                                <p>
                                    <span class="label">Brand ID:</span>
                                    <span class="value">
                                        <a class="product-link"
                                           href="MainController?txtAction=filterBrand&keyword=${product.brandID}">
                                            ${product.brandID}
                                        </a>
                                    </span>
                                </p>

                                <p>
                                    <span class="label">Description:</span>
                                    <span class="value">${product.description}</span>
                                </p>

                                <!-- Status chỉ cho admin -->
                                <c:if test="${isAdmin}">
                                    <p>
                                        <span class="label">Status:</span>
                                        <span class="value">
                                            <c:choose>
                                                <c:when test="${product.isActive}">
                                                    Active
                                                </c:when>
                                                <c:otherwise>
                                                    Inactive
                                                </c:otherwise>
                                            </c:choose>
                                        </span>
                                    </p>
                                </c:if>
                            </div>

                            <!-- NẾU KHÔNG CÓ VARIANT -->
                            <c:if test="${empty variants}">
                                <p class="text-muted">This product is currently out of stock.</p>
                            </c:if>

                            <!-- DANH SÁCH VARIANT -->
                            <c:forEach var="v" items="${variants}">
                                <!-- Admin thấy hết, user chỉ thấy variant có stock > 0 -->
                                <c:if test="${isAdmin or v.stock > 0}">
                                    <div class="product-detail-card mb-3 p-3 border rounded">
                                        <p>
                                            <span class="label">Variant ID:</span>
                                            <span class="value">${v.variantID}</span>
                                        </p>
                                        <p>
                                            <span class="label">Size:</span>
                                            <span class="value">${v.size}</span>
                                        </p>
                                        <p>
                                            <span class="label">Color:</span>
                                            <span class="value">${v.color}</span>
                                        </p>
                                        <p>
                                            <span class="label">Price:</span>
                                            <span class="value">${v.price}</span>
                                        </p>


                                        <p>
                                            <span class="label">Stock:</span>
                                            <span class="value">${v.stock}</span>
                                        </p>
                                        <p>
                                            <span class="label">Sales Count:</span>
                                            <span class="value">${v.salesCount}</span>
                                        </p>
                                        <!-- Chỉ admin mới thấy stock, sales và nút chỉnh sửa -->
                                        <c:if test="${isAdmin}">
                                            <div class="mt-2 d-flex gap-2">
                                                <!-- UPDATE VARIANT -->
                                                <a href="MainController?txtAction=callSaveProduct&productID=${v.productID}&variantID=${v.variantID}&update=true"
                                                   class="btn btn-primary btn-sm">
                                                    Update Variant
                                                </a>

                                                <!-- DELETE VARIANT -->
                                                <a href="MainController?txtAction=deleteProductWithVariant&productID=${v.productID}&variantID=${v.variantID}"
                                                   class="btn btn-danger btn-sm"
                                                   onclick="return confirm('Are you sure you want to delete this variant?');">
                                                    Delete Variant
                                                </a>
                                            </div>
                                        </c:if>
                                    </div>
                                </c:if>
                            </c:forEach>
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
                                            <c:if test="${isAdmin}">
                                                <th>Product ID</th>
                                                </c:if>
                                            <th>Product Name</th>
                                            <th>Description</th>
                                            <th>Category ID</th>
                                            <th>Brand ID</th>
                                            <!-- Chỉ hiện cột Status cho Admin -->
                                            <c:if test="${isAdmin}">
                                                <th>Status</th>
                                                </c:if>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="p" items="${listProducts}">
                                            <!-- Chỉ hiển thị nếu khác sản phẩm đang xem -->
                                            <c:if test="${p.productID ne productID}">

                                                <!-- Trường hợp ADMIN: hiển thị tất cả -->
                                                <c:if test="${isAdmin}">
                                                    <tr>
                                                        <td>${p.productID}</td>
                                                        <td class="text-center">
                                                            <a href="MainController?txtAction=viewProductDetail&productID=${p.productID}&productName=${p.productName}">
                                                                ${p.productName}
                                                            </a>
                                                        </td>

                                                        <td>${p.description}</td>
                                                        <td>${p.categoryID}</td>
                                                        <td>${p.brandID}</td>
                                                        <td>
                                                            <c:choose>
                                                                <c:when test="${p.isActive}">
                                                                    Active
                                                                </c:when>
                                                                <c:otherwise>
                                                                    Inactive
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </td>


                                                    </tr>
                                                </c:if>

                                                <!-- Trường hợp USER: chỉ hiển thị khi isActive = true -->
                                                <c:if test="${not isAdmin and p.isActive}">
                                                    <tr>
                                                        <td class="text-center">
                                                            <a href="MainController?txtAction=viewProductDetail&productID=${p.productID}&productName=${p.productName}">
                                                                ${p.productName}
                                                            </a>
                                                        </td>

                                                        <td>${p.description}</td>
                                                        <td>${p.categoryID}</td>
                                                        <td>${p.brandID}</td>

                                                    </tr>
                                                </c:if>

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
