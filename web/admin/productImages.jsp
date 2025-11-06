<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Quản lý ảnh sản phẩm</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 0; padding: 24px; background: #f6f7fb; }
        h1 { margin-bottom: 16px; }
        .alert { padding: 12px 16px; border-radius: 4px; margin-bottom: 16px; }
        .alert-success { background: #e6ffed; color: #14532d; border: 1px solid #86efac; }
        .alert-error { background: #fee2e2; color: #7f1d1d; border: 1px solid #fca5a5; }
        form { background: #fff; padding: 16px; border-radius: 8px; box-shadow: 0 1px 3px rgba(0,0,0,0.1); margin-bottom: 24px; }
        label { display: block; margin-bottom: 4px; font-weight: 600; }
        input[type="text"], input[type="url"], input[type="hidden"] { width: 100%; padding: 8px; border: 1px solid #d1d5db; border-radius: 4px; margin-bottom: 12px; }
        .checkbox { display: flex; align-items: center; gap: 8px; margin-bottom: 12px; }
        .btn { display: inline-block; padding: 8px 16px; border: none; border-radius: 4px; cursor: pointer; font-weight: 600; }
        .btn-primary { background: #2563eb; color: #fff; }
        .btn-danger { background: #dc2626; color: #fff; }
        .btn-secondary { background: #6b7280; color: #fff; }
        table { width: 100%; border-collapse: collapse; background: #fff; box-shadow: 0 1px 3px rgba(0,0,0,0.1); }
        th, td { padding: 12px; border-bottom: 1px solid #e5e7eb; text-align: left; }
        th { background: #f3f4f6; }
        img { max-width: 120px; height: auto; border-radius: 4px; }
        .actions { display: flex; gap: 8px; }
    </style>
</head>
<body>
    <h1>Quản lý ảnh sản phẩm</h1>
    <p>Sản phẩm: <strong>${PRODUCT_ID}</strong></p>

    <c:if test="${not empty SUCCESS_MESSAGE}">
        <div class="alert alert-success">${SUCCESS_MESSAGE}</div>
    </c:if>
    <c:if test="${not empty ERROR_MESSAGE}">
        <div class="alert alert-error">${ERROR_MESSAGE}</div>
    </c:if>

    <form action="ProductImageController" method="post">
        <input type="hidden" name="txtAction" value="addImage">
        <input type="hidden" name="productID" value="${PRODUCT_ID}">
        <label for="imageUrl">URL ảnh</label>
        <input type="url" id="imageUrl" name="imageUrl" placeholder="https://example.com/image.jpg" required>
        <div class="checkbox">
            <input type="checkbox" id="isMain" name="isMain" value="true">
            <label for="isMain" style="margin: 0; font-weight: normal;">Đặt làm ảnh chính</label>
        </div>
        <button class="btn btn-primary" type="submit">Thêm ảnh</button>
    </form>

    <c:if test="${not empty IMAGES_LIST}">
        <table>
            <thead>
                <tr>
                    <th>#</th>
                    <th>Ảnh</th>
                    <th>Đường dẫn</th>
                    <th>Ảnh chính</th>
                    <th>Hành động</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${IMAGES_LIST}" var="img" varStatus="loop">
                    <tr>
                        <td>${loop.index + 1}</td>
                        <td>
                            <c:choose>
                                <c:when test="${not empty img.imageUrl}">
                                    <img src="${img.imageUrl}" alt="Ảnh sản phẩm">
                                </c:when>
                                <c:otherwise>
                                    Không có ảnh
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td>${img.imageUrl}</td>
                        <td><c:if test="${img.isMain}">✓</c:if></td>
                        <td class="actions">
                            <form action="ProductImageController" method="post">
                                <input type="hidden" name="txtAction" value="setMainImage">
                                <input type="hidden" name="productID" value="${PRODUCT_ID}">
                                <input type="hidden" name="imageID" value="${img.imageID}">
                                <button type="submit" class="btn btn-secondary">Đặt ảnh chính</button>
                            </form>
                            <form action="ProductImageController" method="post">
                                <input type="hidden" name="txtAction" value="deleteImage">
                                <input type="hidden" name="productID" value="${PRODUCT_ID}">
                                <input type="hidden" name="imageID" value="${img.imageID}">
                                <button type="submit" class="btn btn-danger" onclick="return confirm('Xóa ảnh này?');">Xóa</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </c:if>

    <c:if test="${empty IMAGES_LIST}">
        <p>Sản phẩm này chưa có ảnh nào.</p>
    </c:if>

    <form action="ProductImageController" method="post" style="margin-top: 24px;">
        <input type="hidden" name="txtAction" value="deleteAllImages">
        <input type="hidden" name="productID" value="${PRODUCT_ID}">
        <button type="submit" class="btn btn-danger" onclick="return confirm('Xóa tất cả ảnh của sản phẩm?');">Xóa tất cả ảnh</button>
    </form>
</body>
</html>
