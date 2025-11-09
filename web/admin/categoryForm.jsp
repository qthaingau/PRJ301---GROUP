<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>${update ? "Update Category" : "Add New Category"}</title>

        <!-- Dùng chung layout admin gradient -->
        <link rel="stylesheet" href="assets/css/adminTable.css">
    </head>

    <!-- body dùng chung với các trang admin (gradient + căn giữa) -->
    <body class="brand-list-body">

        <!-- Khung form ở giữa giống Brand Form -->
        <div class="container brand-form-wrapper">
            <h3 class="product-list-title">
                ${update ? "Update Category" : "Add New Category"}
            </h3>

            <!-- Hiển thị lỗi chung -->
            <c:if test="${not empty error}">
                <div class="alert alert-danger text-center">
                    ${error}
                </div>
            </c:if>

            <!-- Form xử lý -->
            <form action="MainController" method="post" class="category-form">
                <!-- Action -->
                <input type="hidden" name="txtAction"
                       value="${update ? 'updateCategory' : 'addCategory'}" />

                <!-- Gửi lại flag update -->
                <input type="hidden" name="update" value="${update}" />

                <!-- Category ID -->
                <div class="mb-3">
                    <label for="categoryID" class="form-label fw-bold">Category ID (C***):</label>
                    <input type="text"
                           id="categoryID"
                           name="txtCategoryID"
                           class="form-control"
                           value="${c.categoryID}"
                           pattern="[cC][0-9]{3}"
                           title="Category ID must follow format C***, e.g., C001"
                           ${update ? "readonly" : "required"} />
                    <c:if test="${not empty error_categoryID}">
                        <small class="text-danger">${error_categoryID}</small>
                    </c:if>
                </div>

                <!-- Category Name -->
                <div class="mb-3">
                    <label for="categoryName" class="form-label fw-bold">Category Name:</label>
                    <input type="text"
                           id="categoryName"
                           name="txtCategoryName"
                           class="form-control"
                           value="${c.categoryName}"
                           required />
                    <c:if test="${not empty error_categoryName}">
                        <small class="text-danger">${error_categoryName}</small>
                    </c:if>
                </div>

                <!-- Sport Type -->
                <div class="mb-3">
                    <label for="sportType" class="form-label fw-bold">Sport Type:</label>
                    <input type="text"
                           id="sportType"
                           name="txtSportType"
                           class="form-control"
                           value="${c.sportType}"
                           placeholder="e.g. Football, Running, Gym..."
                           required />
                    <c:if test="${not empty error_sportType}">
                        <small class="text-danger">${error_sportType}</small>
                    </c:if>
                </div>

                <!-- Status (chỉ hiển thị khi update và là admin) -->
                <c:if test="${update and user.role eq 'admin'}">
                    <div class="mb-3">
                        <label class="form-label fw-bold d-block">Status:</label>
                        <label class="me-3">
                            <input type="radio" name="txtIsActive" value="true"
                                   ${c.isActive ? 'checked="checked"' : ''}/> Active
                        </label>
                        <label>
                            <input type="radio" name="txtIsActive" value="false"
                                   ${not c.isActive ? 'checked="checked"' : ''}/> Inactive
                        </label>
                    </div>
                </c:if>

                <!-- Submit + Back -->
                <div class="mt-4 d-flex gap-2">
                    <button type="submit" class="btn btn-apply">
                        ${update ? "Update Category" : "Add Category"}
                    </button>

                    <a href="MainController?txtAction=viewCategoryList" class="btn btn-cancel">
                        Back to List
                    </a>
                </div>
            </form>
        </div>
    </body>
</html>
