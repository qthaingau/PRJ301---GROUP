<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Product List</title>
    <!-- Bootstrap Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css" rel="stylesheet">
    <!-- Custom CSS -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/productList.css">
</head>
<body class="product-list-body">
<div class="container product-list-wrapper">
    <h3 class="product-list-title">Danh sách sản phẩm</h3>

    <!-- Search + Filter -->
    <form action="MainController" method="post" class="product-search-form mb-3">
        <input type="hidden" name="txtAction" value="filterProduct"/>
        <div class="row g-2 align-items-center">
            <div class="col-md-6">
                <div class="input-group">
                    <span class="input-group-text bg-dark text-light">Tìm kiếm</span>
                    <input type="text" class="form-control" name="keyword" value="${keyword}" placeholder="Nhập tên sản phẩm..."/>
                </div>
            </div>
            <div class="col-auto">
                <button type="submit" class="btn btn-apply">Áp dụng</button>
            </div>
        </div>
    </form>

    <!-- Danh sách sản phẩm -->
    <c:choose>
        <c:when test="${empty listProducts}">
            <div class="alert alert-warning text-center product-empty-alert">
                <h5 class="mb-1">Không tìm thấy sản phẩm</h5>
                <p class="mb-0">Vui lòng thử từ khóa khác.</p>
            </div>
        </c:when>
        <c:otherwise>
            <div class="table-responsive product-table-wrapper">
                <table class="table table-hover align-middle product-table">
                    <thead class="table-dark">
                        <tr>
                            <th>ID</th>
                            <th>Tên sản phẩm</th>
                            <th>Mô tả</th>
                            <th>Loại</th>
                            <th>Thương hiệu</th>
                            <!-- Admin: Status -->
                            <c:if test="${user.role eq 'admin'}">
                                <th>Trạng thái</th>
                            </c:if>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="p" items="${listProducts}">
                            <c:url var="detailUrl" value="MainController">
                                <c:param name="txtAction" value="viewProductDetail"/>
                                <c:param name="productID" value="${p.productID}"/>
                            </c:url>
                            <c:url var="brandUrl" value="MainController">
                                <c:param name="txtAction" value="viewBrandList"/>
                                <c:param name="brandID" value="${p.brandID}"/>
                            </c:url>
                            <c:url var="categoryUrl" value="MainController">
                                <c:param name="txtAction" value="viewCategoryList"/>
                                <c:param name="categoryID" value="${p.categoryID}"/>
                            </c:url>

                            <!-- ADMIN: Xem tất cả -->
                            <c:if test="${user.role eq 'admin'}">
                                <tr>
                                    <td><a href="${detailUrl}" class="product-link">${p.productID}</a></td>
                                    <td><a href="${detailUrl}" class="product-link">${p.productName}</a></td>
                                    <td><a href="${detailUrl}" class="product-link">${p.description}</a></td>
                                    <td><a href="${categoryUrl}" class="product-link">${p.categoryID}</a></td>
                                    <td><a href="${brandUrl}" class="product-link">${p.brandID}</a></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${p.isActive}">
                                                <a href="MainController?txtAction=toggleProductStatus&productID=${p.productID}"
                                                   class="status-badge status-active">Active</a>
                                            </c:when>
                                            <c:otherwise>
                                                <a href="MainController?txtAction=toggleProductStatus&productID=${p.productID}"
                                                   class="status-badge status-inactive">Inactive</a>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:if>

                            <!-- CUSTOMER: Chỉ sản phẩm active -->
                            <c:if test="${user.role ne 'admin' and p.isActive}">
                                <tr>
                                    <td><a href="${detailUrl}" class="product-link">${p.productID}</a></td>
                                    <td><a href="${detailUrl}" class="product-link">${p.productName}</a></td>
                                    <td><a href="${detailUrl}" class="product-link">${p.description}</a></td>
                                    <td><a href="${categoryUrl}" class="product-link">${p.categoryID}</a></td>
                                    <td><a href="${brandUrl}" class="product-link">${p.brandID}</a></td>
                                </tr>
                            </c:if>
                        </c:forEach>
                    </tbody>
                </table>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>


<!-- Custom JS -->
<script src="${pageContext.request.contextPath}/assets/js/productList.js"></script>
</body>
</html>