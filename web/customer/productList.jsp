<%-- 
    Document   : ListOfProducts
    Created on : Nov 3, 2025, 7:41:43 AM
    Author     : TEST
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Product List</title>

        <!-- nếu bạn đã link ở layout chung thì có thể bỏ 2 dòng này -->
        <link rel="stylesheet" href="assets/css/bootstrap.min.css">
        <link rel="stylesheet" href="assets/css/product.css">
    </head>
    <body>

        <div class="container product-list-wrapper">
            <h3 class="product-list-title">Product List</h3>

            <!-- Search + filter -->
            <form action="MainController" method="post" class="product-search-form mb-3">
                <input type="hidden" name="txtAction" value="searchProduct"/>

                <div class="row g-2 align-items-center">
                    <div class="col-md-6">
                        <div class="input-group">
                            <span class="input-group-text bg-white">Search</span>
                            <input type="text"
                                   class="form-control"
                                   name="keyword"
                                   value="${keyword}"/>
                        </div>
                    </div>
                    <div class="col-auto">
                        <button type="submit" class="btn btn-primary">
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
                                    <th>Product ID</th>
                                    <th>Product name</th>
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
                                    <tr>
                                        <td>${p.productID}</td>
                                        <td>${p.productName}</td>
                                        <td>${p.description}</td>
                                        <td>${p.categoryID}</td>
                                        <td>${p.brandID}</td>
                                        <td>${p.createdAt}</td>
                                        <td>${p.isActive}</td>

                                        <c:if test="${user.role eq 'Admin'}">
                                            <td class="text-center">
                                                <a href="MainController?txtAction=callUpdateProduct&productID=${p.productID}"
                                                   class="btn btn-outline-primary btn-sm product-view-btn">
                                                    Update
                                                </a>
                                            </td>
                                        </c:if>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>

        <!-- nếu dùng Bootstrap JS -->
        <script src="assets/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
