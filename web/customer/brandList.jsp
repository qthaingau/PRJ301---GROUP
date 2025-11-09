<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Brand List</title>

        <!-- CSS layout admin -->
        <link rel="stylesheet" href="assets/css/adminTable.css">
    </head>
    <body class="brand-list-body">

        <!-- Back to Home -->
        <div class="text-start">
            <a href="MainController?txtAction=viewProducts" class="btn btn-back">
                ← Back to Home
            </a>
        </div>

        <!-- Khung chính -->
        <div class="container brand-list-wrapper">

            <h3 class="brand-list-title">Brand List</h3>

            <!-- Messages -->
            <c:if test="${not empty message}">
                <div class="alert alert-success text-center">
                    ${message}
                </div>
            </c:if>
            <c:if test="${not empty error}">
                <div class="alert alert-danger text-center">
                    ${error}
                </div>
            </c:if>

            <!-- Search bar + Add -->
            <form action="MainController" method="post" class="product-search-form mb-3">
                <input type="hidden" name="txtAction" value="filterBrand"/>

                <div class="row g-2 align-items-center justify-content-center">
                    <!-- Ô search -->
                    <div class="col-md-6">
                        <div class="input-group">
                            <span class="input-group-text">Search</span>
                            <input type="text"
                                   class="form-control"
                                   name="keyword"
                                   value="${keyword}"
                                   placeholder="Enter brand id / name / origin..."/>
                        </div>
                    </div>

                    <!-- Nút Apply -->
                    <div class="col-auto">
                        <button type="submit" class="btn btn-apply">Apply</button>
                    </div>

                    <!-- Chỉ admin mới có thể thêm Brand -->
                    <c:if test="${not empty user and user.role eq 'admin'}">
                        <div class="col-auto">
                            <a href="MainController?txtAction=callBrandForm&update=false" class="btn btn-add">
                                Add New Brand
                            </a>
                        </div>
                    </c:if>
                </div>
            </form>

            <!-- DANH SÁCH BRAND -->
            <c:choose>
                <c:when test="${empty brandList}">
                    <div class="alert alert-warning text-center product-empty-alert">
                        <h5 class="mb-1">No brands found</h5>
                        <p class="mb-0">There is currently no brand in the system.</p>
                    </div>
                </c:when>

                <c:otherwise>
                    <div class="table-responsive product-table-wrapper">
                        <table class="table table-hover align-middle product-table">
                            <thead>
                                <tr>
                                    <th>Brand ID</th>
                                    <th>Brand Name</th>
                                    <th>Origin</th>

                                    <c:if test="${not empty user and user.role eq 'admin'}">
                                        <th>Status</th>
                                        <th>Actions</th>
                                    </c:if>
                                </tr>
                            </thead>

                            <tbody>
                                <c:forEach var="b" items="${brandList}">
                                    <c:if test="${(not empty user and user.role eq 'admin') or b.isActive}">
                                        <tr>
                                            <td>${b.brandID}</td>
                                            <td>${b.brandName}</td>
                                            <td>${b.origin}</td>

                                            <c:if test="${not empty user and user.role eq 'admin'}">
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${b.isActive}">
                                                            <span class="status-badge status-active">Active</span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="status-badge status-inactive">Inactive</span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>

                                                <td>
                                                    <a href="MainController?txtAction=callBrandForm&brandID=${b.brandID}&update=true"
                                                       class="btn btn-update">
                                                        Update
                                                    </a>
                                                    <a href="MainController?txtAction=deleteBrand&brandID=${b.brandID}"
                                                       class="btn btn-delete"
                                                       onclick="return confirm('Are you sure you want to deactivate this brand?');">
                                                        Delete
                                                    </a>
                                                </td>
                                            </c:if>
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
