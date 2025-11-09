<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Category List</title>

        <!-- Dùng layout admin -->
        <link rel="stylesheet" href="assets/css/adminTable.css">
    </head>
    <!-- dùng chung class body với brandList -->
    <body class="brand-list-body">

        <!-- Khung chính ở giữa -->
        <div class="container brand-list-wrapper">
            <h3 class="brand-list-title">Category List</h3>

            <!-- Search bar -->
            <form action="MainController" method="post" class="product-search-form mb-3">
                <input type="hidden" name="txtAction" value="filterCategory"/>

                <div class="row g-2 align-items-center justify-content-center">
                    <div class="col-md-6">
                        <div class="input-group">
                            <span class="input-group-text">Search</span>
                            <input type="text"
                                   class="form-control"
                                   name="keyword"
                                   value="${keyword}"
                                   placeholder="Enter category name..."/>
                        </div>
                    </div>

                    <div class="col-auto">
                        <button type="submit" class="btn btn-apply">Apply</button>
                    </div>

                    <!-- Chỉ admin mới có thể thêm -->
                    <c:if test="${not empty user and user.role eq 'admin'}">
                        <div class="col-auto">
                            <a href="MainController?txtAction=callCategoryForm&update=false"
                               class="btn btn-add">
                                Add New Category
                            </a>
                        </div>
                    </c:if>
                </div>
            </form>

            <!-- Hiển thị danh sách -->
            <c:choose>
                <c:when test="${empty listCategories}">
                    <div class="alert alert-warning text-center product-empty-alert">
                        <h5 class="mb-1">No categories found</h5>
                        <p class="mb-0">Try another keyword or add a new category.</p>
                    </div>
                </c:when>

                <c:otherwise>
                    <div class="table-responsive product-table-wrapper">
                        <table class="table table-hover align-middle product-table">
                            <thead>
                                <tr>
                                    <th>Category ID</th>
                                    <th>Category Name</th>
                                    <th>Sport Type</th>
                                    <c:if test="${not empty user and user.role eq 'admin'}">
                                        <th>Status</th>
                                        <th>Actions</th>
                                    </c:if>
                                </tr>
                            </thead>

                            <tbody>
                                <c:forEach var="c" items="${listCategories}">
                                    <!-- USER: chỉ thấy isActive = true -->
                                    <c:if test="${(empty user or user.role ne 'admin') and c.isActive}">
                                        <tr>
                                            <td>${c.categoryID}</td>
                                            <td>${c.categoryName}</td>
                                            <td>${c.sportType}</td>
                                        </tr>
                                    </c:if>

                                    <!-- ADMIN: thấy tất cả -->
                                    <c:if test="${not empty user and user.role eq 'admin'}">
                                        <tr>
                                            <td>${c.categoryID}</td>
                                            <td>${c.categoryName}</td>
                                            <td>${c.sportType}</td>
                                            <td>
                                                <c:choose>
                                                    <c:when test="${c.isActive}">
                                                        <span class="status-badge status-active">Active</span>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <span class="status-badge status-inactive">Inactive</span>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td>
                                                <!-- Update -->
                                                <a href="MainController?txtAction=callCategoryForm&update=true&categoryID=${c.categoryID}"
                                                   class="btn btn-update">
                                                    Update
                                                </a>

                                                <!-- Delete -->
                                                <a href="MainController?txtAction=deleteCategory&categoryID=${c.categoryID}"
                                                   class="btn btn-delete"
                                                   onclick="return confirm('Are you sure you want to deactivate this category?');">
                                                    Delete
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
