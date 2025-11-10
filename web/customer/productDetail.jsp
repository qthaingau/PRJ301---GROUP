<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.Map, java.util.List, models.ProductVariantDTO" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>${product.productName}</title>
        <link rel="stylesheet" href="assets/css/productDetail.css">
        <link rel="stylesheet" href="assets/css/header.css">
        <script src="assets/js/header.js"></script>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
    </head>
    <body>
        
        <div class="container">
            <a href="MainController?txtAction=viewProducts" class="back-btn">
                ← Quay lại danh sách
            </a>
            <div id="alertMessage" class="alert"></div>
            <h1 class="product-title-main">${product.productName}</h1>
            <p class="product-desc">${product.description}</p>
            <c:choose>
                <c:when test="${empty groupedVariants}">
                    <div class="no-stock">
                        <h5>Sản phẩm hiện tại không có sẵn</h5>
                        <p>Vui lòng quay lại sau!</p>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="variant-grid">
                        <c:forEach var="entry" items="${groupedVariants}">
                            <c:set var="group" value="${entry.value}" />
                            <c:set var="first" value="${group[0]}" />
                            <div class="variant-card">
                                <c:set var="inStock" value="false" />
                                <c:set var="maxStock" value="0" />
                                <c:forEach var="v" items="${group}">
                                    <c:if test="${v.stock > 0}">
                                        <c:set var="inStock" value="true" />
                                        <c:if test="${v.stock > maxStock}">
                                            <c:set var="maxStock" value="${v.stock}" />
                                        </c:if>
                                    </c:if>
                                </c:forEach>
                                <div class="variant-img-wrapper">
                                    <img src="${not empty first.variantImage ? first.variantImage : 'assets/img/no-image.png'}"
                                         alt="${first.color}" class="variant-img"
                                         onerror="this.src='assets/img/no-image.png'">
                                    <c:if test="${!inStock}">
                                        <div class="out-of-stock">Hết hàng</div>
                                    </c:if>
                                </div>
<div class="price-section">
                                    <span class="new-price">
                                        $<fmt:formatNumber value="${first.price}" type="number" maxFractionDigits="0"/>
                                    </span>
                                </div>
                                <div class="product-subtitle">${product.productName}</div>
                                <div class="color-name">${first.color}</div>
                                <form class="add-to-cart-form" onsubmit="addToCart(event, this)">
                                    <select name="variantID" class="form-select size-select" required>
                                        <option value="">Chọn size</option>
                                        <c:forEach var="v" items="${group}">
                                            <option value="${v.variantID}"
                                                    ${v.stock == 0 ? 'disabled' : ''}>
                                                ${v.size}
                                                <c:choose>
                                                    <c:when test="${v.stock > 0}">(Còn ${v.stock})</c:when>
                                                    <c:otherwise>(Hết)</c:otherwise>
                                                </c:choose>
                                            </option>
                                        </c:forEach>
                                    </select>
                                    <label class="quantity-label">Số lượng:</label>
                                    <input type="number" name="quantity" class="form-control quantity-input"
                                           value="1" min="1" max="${maxStock > 0 ? maxStock : 1}" required>
                                    <input type="hidden" name="productID" value="${product.productID}">

                                    <button type="submit" class="btn-cart">
                                        <i class="bi bi-cart-plus"></i> Thêm vào giỏ
                                    </button>
                                    <button type="button" class="btn-buy" onclick="buyNow(this)">
                                        <i class="bi bi-lightning-fill"></i> Mua ngay
                                    </button>
                                </form>
                            </div>
                        </c:forEach>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
        <!-- Loại bỏ script JS không cần thiết nữa -->
    </body>
</html>