<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Brand List</title>

        <!-- CSS theme đen – đỏ – trắng -->
        <link rel="stylesheet" href="assets/css/productList.css">
    </head>
    <body class="product-list-body">

        <!-- ✅ Back to Home giống categoryList.jsp -->
        <a href="home.jsp" class="btn btn-secondary" style="margin-left: 10px;">
            Back to Home
        </a>

        <div class="container product-list-wrapper">
            <h3 class="product-list-title">Brand List</h3>

            <!-- Thông báo chung (dùng alert cho đồng bộ) -->
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

            <!-- Search bar -->
            <form action="MainController" method="post" class="product-search-form mb-3">
                <!-- Action cho brand -->
                <input type="hidden" name="txtAction" value="filterBrand"/>

                <div class="row g-2 align-items-center">
                    <!-- Ô search -->
                    <div class="col-md-6">
                        <div class="input-group">
                            <span class="input-group-text bg-dark text-light">Search</span>
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
                                + Add New Brand
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

                                    <!-- Chỉ admin mới thấy cột Status + Action -->
                                    <c:if test="${not empty user and user.role eq 'admin'}">
                                        <th>Status</th>
                                        <th>Actions</th>
                                        </c:if>
                                </tr>
                            </thead>

                            <tbody>
                                <c:forEach var="b" items="${brandList}">
                                    <!--
                                        Admin: thấy tất cả brand.
                                        User: chỉ thấy brand đang active (b.isActive == true).
                                    -->
                                    <c:if test="${(not empty user and user.role eq 'admin') or b.isActive}">
                                        <tr>
                                            <td>${b.brandID}</td>
                                            <td>${b.brandName}</td>
                                            <td>${b.origin}</td>

                                            <!-- STATUS + ACTION chỉ hiện cho admin -->
                                            <c:if test="${not empty user and user.role eq 'admin'}">
                                                <!-- Status -->
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${b.isActive}">
                                                            <span class="status-badge status-active">
                                                                Active
                                                            </span>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <span class="status-badge status-inactive">
                                                                Inactive
                                                            </span>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </td>

                                                <!-- Actions -->
                                                <td>
                                                    <!-- UPDATE -->
                                                    <a href="MainController?txtAction=callBrandForm&brandID=${b.brandID}&update=true"
                                                       class="btn btn-update">
                                                        Update
                                                    </a>

                                                    <!-- DELETE (soft delete: isActive = 0) -->
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
