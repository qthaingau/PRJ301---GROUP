<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Brand Management</title>

        <%-- CSS chung, em chỉnh path theo project của em --%>
        <link rel="stylesheet" href="assets/css/productList.css">
        <%-- nếu có CSS riêng cho brand thì include thêm ở đây --%>
    </head>
    <body class="product-list-body">

        <div class="container product-list-wrapper">
            <h3 class="product-list-title">Brand List</h3>

            <!-- Thông báo chung -->
            <c:if test="${not empty message}">
                <p style="color:green">${message}</p>
            </c:if>
            <c:if test="${not empty error}">
                <p style="color:red">${error}</p>
            </c:if>

            <!-- NÚT ADD CHỈ HIỆN KHI LÀ ADMIN -->
            <c:if test="${user.role eq 'admin'}">
                <div class="mb-3">
                    <form action="MainController" method="get" style="display:inline;">
                        <input type="hidden" name="txtAction" value="callAddBrandForm"/>
                        <button type="submit" class="btn btn-apply">
                            Add New Brand
                        </button>
                    </form>
                </div>
            </c:if>

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
                                    <th>Status</th>

                                    <!-- Cột Action chỉ cho ADMIN -->
                                    <c:if test="${user.role eq 'admin'}">
                                        <th>Action</th>
                                    </c:if>
                                </tr>
                            </thead>

                            <tbody>
                                <c:forEach var="b" items="${brandList}">
                                    <tr>
                                        <td>${b.brandID}</td>
                                        <td>${b.brandName}</td>
                                        <td>${b.origin}</td>

                                        <!-- Status hiển thị Active / Inactive -->
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

                                        <!-- ACTION CHỈ KHI ADMIN -->
                                        <c:if test="${user.role eq 'admin'}">
                                            <td>
                                                <!-- UPDATE -->
                                                <a href="MainController?txtAction=callUpdateBrand&brandID=${b.brandID}"
                                                   class="btn btn-sm btn-primary">
                                                    Update
                                                </a>

                                                <!-- DELETE (soft delete hoặc hard delete tuỳ em xử lý trong DAO) -->
                                                <a href="MainController?txtAction=deleteBrand&brandID=${b.brandID}"
                                                   class="btn btn-sm btn-danger"
                                                   onclick="return confirm('Are you sure you want to delete this brand?');">
                                                    Delete
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

    </body>
</html>
