<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Product List</title>

        <!-- CSS theme đen – đỏ – trắng -->
        <link rel="stylesheet" href="assets/css/listOfProducts.css">
    </head>
    <body class="product-list-body">

        <!-- Nút quay về trang Home -->
        <div class="text-start mt-3 ms-4">
            <a href="MainController?txtAction=viewProducts" class="btn btn-outline-light">
                ← Back to Home
            </a>
        </div>

        <div class="container product-list-wrapper">
            <!-- Tiêu đề -->
            <h3 class="product-list-title">Product List</h3>

            <!-- Nút thêm sản phẩm -->
            <div class="mb-4 text-end">
                <a href="MainController?txtAction=callSaveProduct&update=false" class="btn btn-success fw-bold">
                    + Add Product
                </a>
            </div>

            <!-- Danh sách sản phẩm -->
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
                                    <th>Image</th>
                                    <th>ID</th>
                                    <th>Name</th>
                                    <th>Description</th>
                                    <th>Category</th>
                                    <th>Brand</th>
                                    <th>Status</th>
                                    <th>Action</th>
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
                                    
                                    <c:url var="statusUrl" value="MainController">
                                        <c:param name="txtAction" value="toggleProductStatus"/>
                                        <c:param name="isActive" value="${p.isActive}"/>
                                    </c:url>


                                    <tr>
                                        <td>
                                            <img src="${p.productImage}" alt="${p.productName}" 
                                                 style="width: 55px; border-radius: 5px; border: 1px solid #ff3b30;">
                                        </td>
                                        <td><a href="${detailUrl}" class="product-link">${p.productID}</a></td>
                                        <td><a href="${detailUrl}" class="product-link">${p.productName}</a></td>
                                        <td><a href="${detailUrl}" class="product-link">${p.description}</a></td>
                                        <td><a href="${categoryUrl}" class="product-link">${p.categoryID}</a></td>
                                        <td><a href="${brandUrl}" class="product-link">${p.brandID}</a></td>
                                        <td><a href="${statusUrl}" class="product-link">${p.isActive}</a></td>


                                        <td>
                                            <div class="d-flex justify-content-center gap-2">
                                                <a href="MainController?txtAction=callSaveProduct&update=true&productID=${p.productID}"
                                                   class="btn btn-warning btn-sm fw-bold text-dark">Update</a>
                                                <a href="MainController?txtAction=deleteProductWithVariant&productID=${p.productID}"
                                                   class="btn btn-danger btn-sm fw-bold"
                                                   onclick="return confirm('Are you sure you want to delete this product?');">Delete</a>
                                            </div>
                                        </td>
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
