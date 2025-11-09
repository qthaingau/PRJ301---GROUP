<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Variant List</title>

        <!-- Dùng lại CSS admin -->
        <link rel="stylesheet" href="assets/css/adminTable.css">
    </head>
    <body class="product-list-body">
        <div class="container variant-list-wrapper">
            <!-- Tiêu đề -->
            <h3 class="product-list-title">Variant List</h3>

            <!-- Ô search -->
            <form action="MainController" method="post" class="product-search-form mb-3">
                <input type="hidden" name="txtAction" value="filterVariant"/>

                <div class="row g-2 align-items-center justify-content-center">
                    <div class="col-md-6">
                        <div class="input-group">
                            <span class="input-group-text">Search</span>
                            <input type="text"
                                   class="form-control"
                                   name="keyword"
                                   value="${keyword}"
                                   placeholder="Search variants (ID, product, size, color)..."/>
                        </div>
                    </div>

                    <div class="col-auto">
                        <button type="submit" class="btn btn-apply">Apply</button>
                    </div>
                </div>
            </form>


            <!-- Nút thêm variant -->
            <div class="mb-4 text-end">
                <a href="MainController?txtAction=callSaveVariant&update=false" class="btn btn-success fw-bold">
                    Add Variant or product
                </a>
            </div>

            <!-- Danh sách variant -->
            <c:choose>
                <c:when test="${empty listVariants}">
                    <div class="alert alert-warning text-center product-empty-alert">
                        <h5 class="mb-1">No variants found</h5>
                        <p class="mb-0">Please enter another keyword to find variants.</p>
                    </div>
                </c:when>

                <c:otherwise>
                    <div class="table-responsive product-table-wrapper">
                        <table class="table table-hover align-middle product-table">
                            <thead>
                                <tr>
                                    <th>Image</th>
                                    <th>Variant ID</th>
                                    <th>Product ID</th>
                                    <th>Size</th>
                                    <th>Color</th>
                                    <th>Stock</th>
                                    <th>Price</th>
                                    <th>Action</th>
                                </tr>
                            </thead>

                            <tbody>
                                <c:forEach var="v" items="${listVariants}">
                                    <!-- Link xem product -->
                                    <c:url var="productUrl" value="MainController">
                                        <c:param name="txtAction" value="viewProductDetail"/>
                                        <c:param name="productID" value="${v.productID}"/>
                                    </c:url>

                                    <!-- Link toggle status -->
                                    <c:url var="statusUrl" value="MainController">
                                        <c:param name="txtAction" value="viewVariantList"/>
                                        <c:param name="variantID" value="${v.variantID}"/>
                                    </c:url>

                                    <!-- Link update / delete -->
                                    <c:url var="updateUrl" value="MainController">
                                        <c:param name="txtAction" value="callSaveVariant"/>
                                        <c:param name="update" value="true"/>
                                        <c:param name="variantID" value="${v.variantID}"/>
                                    </c:url>

                                    <c:url var="deleteUrl" value="MainController">
                                        <c:param name="txtAction" value="deleteVariant"/>
                                        <c:param name="variantID" value="${v.variantID}"/>
                                        <c:param name="productID" value="${v.productID}"/>
                                    </c:url>

                                    <tr>
                                        <td>
                                            <img src="${v.variantImage}" alt="Variant ${v.variantID}"
                                                 style="width: 55px; border-radius: 5px; border: 1px solid #ff3b30;">
                                        </td>
                                        <td>${v.variantID}</td>
                                        <td>
                                            <a href="${productUrl}" class="product-link">
                                                ${v.productID}
                                            </a>
                                        </td>
                                        <td>${v.size}</td>
                                        <td>${v.color}</td>
                                        <td>${v.stock}</td>
                                        <td>${v.price}</td>
                                        <td>
                                            <div class="d-flex justify-content-center gap-2">
                                                <a href="${updateUrl}"
                                                   class="btn btn-warning btn-sm fw-bold text-dark">
                                                    Update
                                                </a>
                                                <a href="${deleteUrl}"
                                                   class="btn btn-danger btn-sm fw-bold"
                                                   onclick="return confirm('Are you sure you want to delete this variant?');">
                                                    Delete
                                                </a>
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
