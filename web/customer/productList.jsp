<%-- 
    Document   : ListOfProducts
    Created on : Nov 3, 2025, 7:41:43 AM
    Author     : TEST
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Product List</title>

        <!-- Bootstrap có thể đã được load ở layout ngoài -->
        <!-- <link rel="stylesheet" href="assets/css/bootstrap.min.css"> -->

        <!-- CSS theme đen – đỏ – trắng -->
        <link rel="stylesheet" href="assets/css/productList.css">
    </head>
    <body class="product-list-body">

        <div class="container product-list-wrapper">
            <h3 class="product-list-title">Product List</h3>

            <!-- Search + filter -->
            <form action="MainController" method="post" class="product-search-form mb-3">
                <input type="hidden" name="txtAction" value="filterProduct"/>

                <div class="row g-2 align-items-center">
                    <div class="col-md-6">
                        <div class="input-group">
                            <span class="input-group-text bg-dark text-light">Search</span>
                            <input type="text"
                                   class="form-control"
                                   name="keyword"
                                   value="${keyword}"/>
                        </div>
                    </div>
                    <div class="col-auto">
                        <button type="submit" class="btn btn-apply">
                            Apply
                        </button>
                    </div>
                </div>
            </form>

            <!-- viewProducts -->
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
                                    <c:if test="${user.role eq 'admin' or 'staff'}">
                                        <th>Product ID</th>
                                        </c:if>
                                    <th>Product Image</th>
                                    <th>Product Name</th>
                                    <th>Description</th>
                                    <th>Category ID</th>
                                    <th>Brand ID</th>

                                    <!-- Status + Action chỉ cho ADMIN -->
                                    <c:if test="${user.role eq 'admin'}">
                                        <th>Status</th>
                                        </c:if>
                                </tr>
                            </thead>

                            <tbody>
                                <c:forEach var="p" items="${listProducts}">
                                    <c:url var="detailUrl" value="MainController">
                                        <c:param name="txtAction" value="viewProductDetail"/>
                                        <c:param name="productID" value="${p.productID}"/>
                                        <c:param name="productName" value="${p.productName}"/>
                                    </c:url>

                                    <!-- URL tới Brand form -->
                                    <c:url var="brandUrl" value="MainController">
                                        <c:param name="txtAction" value="viewBrandList"/>
                                        <c:param name="brandID" value="${p.brandID}"/>
                                    </c:url>

                                    <!-- URL tới Category form -->
                                    <c:url var="categoryUrl" value="MainController">
                                        <c:param name="txtAction" value="viewCategoryList"/>
                                        <c:param name="categoryID" value="${p.categoryID}"/>
                                    </c:url>

                                    <!-- ADMIN: thấy tất cả -->
                                    <c:if test="${user.role eq 'admin'}">
                                        <tr>
                                            <td><img src="${p.productImage}" style="width: 50px"/></td>

                                            <td>
                                                <a href="${detailUrl}" class="product-link">
                                                    ${p.productID}
                                                </a>
                                            </td>
                                            <td>
                                                <a href="${detailUrl}" class="product-link">
                                                    ${p.productName}
                                                </a>
                                            </td>
                                            <td>
                                                <a href="${detailUrl}" class="product-link">
                                                    ${p.description}
                                                </a>
                                            </td>

                                            <!-- Category ID clickable -->
                                            <td>
                                                <a href="${categoryUrl}" class="product-link">
                                                    ${p.categoryID}
                                                </a>
                                            </td>

                                            <!-- Brand ID clickable -->
                                            <td>
                                                <a href="${brandUrl}" class="product-link">
                                                    ${p.brandID}
                                                </a>
                                            </td>

                                            <td>
                                                <c:choose>
                                                    <c:when test="${p.isActive}">
                                                        <a href="MainController?txtAction=toggleProductStatus&productID=${p.productID}"
                                                           class="status-badge status-active">
                                                            Active
                                                        </a>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <a href="MainController?txtAction=toggleProductStatus&productID=${p.productID}"
                                                           class="status-badge status-inactive">
                                                            Inactive
                                                        </a>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                        </tr>
                                    </c:if>

                                    <!-- USER: chỉ hiện isActive = true, không có Status/Action -->
                                    <c:if test="${user.role ne ('admin' or 'staff') and p.isActive}">
                                        <tr>
                                            <td><img src="${p.productImage}" style="width: 50px"/></td>
                                            <td>
                                                <a href="${detailUrl}" class="product-link">
                                                    ${p.productName}
                                                </a>
                                            </td>
                                            <td>
                                                <a href="${detailUrl}" class="product-link">
                                                    ${p.description}
                                                </a>
                                            </td>

                                            <!-- Category ID clickable -->
                                            <td>
                                                <a href="${categoryUrl}" class="product-link">
                                                    ${p.categoryID}
                                                </a>
                                            </td>

                                            <!-- Brand ID clickable -->
                                            <td>
                                                <a href="${brandUrl}" class="product-link">
                                                    ${p.brandID}
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
        </div>

    </body>
</html>