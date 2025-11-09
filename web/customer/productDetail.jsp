<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.Map, java.util.List, models.ProductVariantDTO" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>${product.productName}</title>
        <link rel="stylesheet" href="assets/css/bootstrap.min.css">
        <style>
            .back-btn {
                display: inline-block;
                margin: 20px 0;
                padding: 10px 20px;
                background: #333;
                color: white;
                border-radius: 8px;
                text-decoration: none;
                font-weight: 500;
            }
            .back-btn:hover {
                background: #555;
            }

            .product-grid {
                display: grid;
                grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
                gap: 30px;
                margin-top: 30px;
            }
            .variant-card {
                background: #fff;
                border-radius: 12px;
                box-shadow: 0 4px 15px rgba(0,0,0,0.08);
                transition: 0.3s;
                text-align: center;
                padding: 15px;
                position: relative;
            }
            .variant-card:hover {
                transform: translateY(-10px);
                box-shadow: 0 10px 25px rgba(0,0,0,0.15);
            }

            .variant-img-wrapper {
                height: 200px;
                display: flex;
                align-items: center;
                justify-content: center;
                background: #f8f9fa;
                border-radius: 12px;
                overflow: hidden;
                margin-bottom: 15px;
            }
            .variant-img {
                max-width: 100%;
                max-height: 100%;
                object-fit: contain;
            }

            .old-price {
                text-decoration: line-through;
                color: #999;
                font-size: 0.9em;
            }
            .new-price {
                color: #e74c3c;
                font-weight: bold;
                font-size: 1.3em;
            }
            .product-title {
                font-size: 0.95em;
                font-weight: bold;
                color: #333;
                margin: 10px 0 5px;
                line-height: 1.3;
            }
            .color-code {
                font-size: 0.85em;
                color: #555;
                margin-bottom: 10px;
            }

            .size-select {
                margin: 10px 0;
                font-size: 0.9em;
            }
            .btn-cart, .btn-buy {
                width: 100%;
                margin: 5px 0;
                font-size: 0.9em;
                padding: 8px;
                border-radius: 6px;
            }
            .btn-cart {
                background: #f8f9fa;
                border: 1px solid #007bff;
                color: #007bff;
            }
            .btn-cart:hover {
                background: #007bff;
                color: white;
            }
            .btn-buy {
                background: #dc3545;
                color: white;
                border: none;
            }
            .btn-buy:hover {
                background: #c82333;
            }

            .out-of-stock {
                position: absolute;
                top: 10px;
                right: 10px;
                background: rgba(220,53,69,0.9);
                color: white;
                padding: 4px 8px;
                border-radius: 4px;
                font-size: 0.8em;
                font-weight: bold;
            }
        </style>
    </head>
    <body class="bg-light">

        <div class="container">
            <a href="MainController?txtAction=viewProducts" class="back-btn">Quay lại danh sách</a>
            <h1 class="text-center mb-4 fw-bold">${product.productName}</h1>
            <p class="text-center text-muted mb-5">${product.description}</p>

            <c:choose>
                <c:when test="${empty groupedVariants}">
                    <div class="text-center py-5">
                        <div class="alert alert-light border">
                            <h5>Sản phẩm hiện tại không có sẵn</h5>
                            <p class="text-muted">Vui lòng quay lại sau!</p>
                        </div>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="product-grid">
                        <!-- LẶP QUA MAP -->
                        <c:forEach var="entry" items="${groupedVariants}">
                            <c:set var="group" value="${entry.value}" />
                            <c:set var="first" value="${group[0]}" />

                            <div class="variant-card">
                                <!-- Hết hàng? -->
                                <c:set var="inStock" value="false" />
                                <c:forEach var="v" items="${group}">
                                    <c:if test="${v.stock > 0}"><c:set var="inStock" value="true" /></c:if>
                                </c:forEach>
                                <c:if test="${!inStock}"><div class="out-of-stock">Hết hàng</div></c:if>

                                    <!-- ẢNH -->
                                    <div class="variant-img-wrapper">
                                        <img src="${not empty first.variantImage ? first.variantImage : 'assets/img/no-image.png'}"
                                         alt="${first.color}"
                                         class="variant-img">
                                </div>

                                <!-- GIÁ -->
                                <div>
                                    <span class="old-price">₫${first.price * 1.3}</span>
                                    <span class="new-price">₫${first.price}</span>
                                </div>

                                <!-- TÊN + MÀU -->
                                <div class="product-title">${product.productName}</div>
                                <div class="color-code">${first.color}</div>

                                <!-- FORM CHỌN SIZE -->
                                <form action="MainController" method="post" onsubmit="return validateSize(this)">
                                    <input type="hidden" name="txtAction" value="addToCart">
                                    <input type="hidden" name="quantity" value="1">

                                    <select name="size" class="form-select size-select" required onchange="updateVariantID(this)">
                                        <option value="">Chọn size</option>
                                        <c:forEach var="v" items="${group}">
                                            <c:choose>
                                                <c:when test="${v.stock > 0}">
                                                    <option value="${v.size}" data-variant-id="${v.variantID}">${v.size} (Còn ${v.stock})</option>
                                                </c:when>
                                                <c:otherwise>
                                                    <option value="${v.size}" disabled>${v.size} (Hết)</option>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                    </select>
                                    <input type="hidden" name="variantID" value="">

                                    <button type="submit" name="action" value="add" class="btn btn-cart">Thêm vào giỏ</button>
                                    <button type="submit" name="action" value="buy" class="btn btn-buy">Mua ngay</button>
                                </form>
                            </div>
                        </c:forEach>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>

        <script>
            function updateVariantID(select) {
                const variantID = select.selectedOptions[0].getAttribute('data-variant-id');
                select.closest('form').querySelector('input[name="variantID"]').value = variantID || '';
            }

            function validateSize(form) {
                const variantID = form.querySelector('input[name="variantID"]').value;
                if (!variantID) {
                    alert("Vui lòng chọn size còn hàng!");
                    return false;
                }
                return true;
            }
        </script>

    </body>
</html>