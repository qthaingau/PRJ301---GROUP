<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Brand Form</title>

        <!-- Giao diện chung (đen - đỏ - trắng) -->
        <link rel="stylesheet" href="assets/css/productList.css">
    </head>

    <body class="product-list-body">
        <div class="container product-list-wrapper">
            <h3 class="product-list-title text-center mb-4">
                ${b != null ? "Update Brand" : "Add New Brand"}
            </h3>

            <!-- Hiển thị lỗi chung -->
            <c:if test="${not empty error}">
                <div class="alert alert-danger text-center">
                    ${error}
                </div>
            </c:if>

            <!-- FORM SAVE BRAND -->
            <form action="MainController" method="post" class="category-form">
                <input type="hidden" name="txtAction" value="saveBrand"/>

                <!-- Brand ID -->
                <div class="mb-3">
                    <label for="brandID" class="form-label fw-bold">Brand ID (B***):</label>
                    <input type="text"
                           id="brandID"
                           name="txtBrandID"
                           class="form-control"
                           value="${b.brandID}"
                           pattern="[bB][0-9]{3}"
                           title="Brand ID must follow format B***, e.g., B001"
                           ${b != null ? "readonly" : "required"} />
                    <c:if test="${not empty error_brandID}">
                        <small class="text-danger">${error_brandID}</small>
                    </c:if>
                </div>

                <!-- Brand Name -->
                <div class="mb-3">
                    <label for="brandName" class="form-label fw-bold">Brand Name:</label>
                    <input type="text"
                           id="brandName"
                           name="txtBrandName"
                           class="form-control"
                           value="${b.brandName}"
                           required />
                    <c:if test="${not empty error_brandName}">
                        <small class="text-danger">${error_brandName}</small>
                    </c:if>
                </div>

                <!-- Origin -->
                <div class="mb-3">
                    <label for="origin" class="form-label fw-bold">Origin:</label>
                    <input type="text"
                           id="origin"
                           name="txtOrigin"
                           class="form-control"
                           value="${b.origin}"
                           placeholder="e.g. USA, Germany, Japan..."
                           required />
                    <c:if test="${not empty error_origin}">
                        <small class="text-danger">${error_origin}</small>
                    </c:if>
                </div>

                <!-- Status (chỉ hiện với Admin khi đang Update) -->
                <c:if test="${not empty user and user.role eq 'admin' and b != null}">
                    <div class="mb-3">
                        <label class="form-label fw-bold d-block">Status:</label>
                        <label class="me-3">
                            <input type="radio" name="txtIsActive" value="true"
                                   ${b.isActive ? "checked" : ""}/> Active
                        </label>
                        <label>
                            <input type="radio" name="txtIsActive" value="false"
                                   ${!b.isActive ? "checked" : ""}/> Inactive
                        </label>
                    </div>
                </c:if>

                <!-- Nút Submit & Navigation -->
                <div class="mt-4 d-flex gap-2">
                    <button type="submit" class="btn btn-apply">
                        ${b != null ? "Update Brand" : "Add Brand"}
                    </button>

                    <a href="MainController?txtAction=viewBrandList" class="btn btn-cancel">
                        Back to List
                    </a>

                    <a href="home.jsp" class="btn btn-secondary">
                        Back to Home
                    </a>
                </div>
            </form>
        </div>
    </body>
</html>
