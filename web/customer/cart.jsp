<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Giỏ hàng - HTV Sport</title>
    <link rel="stylesheet" href="assets/css/footer.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css" rel="stylesheet">

    <style>
        body {
            background-color: #f8f9fa;
        }
        .cart-item-img {
            width: 80px;
            height: 80px;
            object-fit: cover;
            border-radius: 8px;
        }
        .quantity-input {
            width: 80px;
            text-align: center;
        }
        .empty-cart-icon {
            font-size: 5rem;
        }
        .table-desktop {
            display: block;
        }
        .card-mobile {
            display: none;
        }
        
        @media (max-width: 768px) {
            .table-desktop {
                display: none;
            }
            .card-mobile {
                display: block;
            }
        }
        
        .total-row {
            background-color: #e8f5e9;
            font-weight: bold;
        }
    </style>
</head>
<body>

    <%@ include file="../includes/header.jspf" %>

    <div class="container my-5">
        <div class="row">
            <div class="col-lg-10 mx-auto">

                <h2 class="mb-4 text-center">
                    <i class="bi bi-cart3"></i> Giỏ hàng của bạn
                </h2>

                <!-- EMPTY CART -->
                <c:if test="${empty cartItems and empty sessionScope.cart}">
                    <div class="text-center py-5">
                        <i class="bi bi-cart-x empty-cart-icon text-muted"></i>
                        <p class="lead mt-3">Giỏ hàng trống</p>
                        <a href="MainController?txtAction=viewProducts" class="btn btn-warning btn-lg">
                            <i class="bi bi-shop"></i> Mua sắm ngay
                        </a>
                    </div>
                </c:if>

                <!-- CART ITEMS -->
                <c:set var="displayCart" value="${not empty cartItems ? cartItems : sessionScope.cart}" />
                
                <c:if test="${not empty displayCart}">
                    <!-- DESKTOP: TABLE -->
                    <div class="table-responsive table-desktop">
                        <table class="table table-hover align-middle bg-white rounded shadow-sm">
                            <thead class="table-dark">
                                <tr>
                                    <th style="width: 100px;">Hình ảnh</th>
                                    <th>Tên sản phẩm</th>
                                    <th style="width: 100px;">Màu sắc</th>
                                    <th style="width: 80px;">Size</th>
                                    <th style="width: 120px;">Giá</th>
                                    <th style="width: 100px;">Số lượng</th>
                                    <th style="width: 120px;">Tổng tiền</th>
                                    <th style="width: 80px;">Xóa</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:set var="total" value="0" />
                                <c:forEach var="item" items="${displayCart}">
                                    <c:set var="itemTotal" value="${item.price * item.quantity}" />
                                    <c:set var="total" value="${total + itemTotal}" />
                                    
                                    <tr>
                                        <td>
                                            <img src="${not empty item.imageUrl ? item.imageUrl : 'assets/img/no-image.png'}" 
                                                 class="cart-item-img" 
                                                 alt="${item.productName}"
                                                 onerror="this.src='assets/img/no-image.png'">
                                        </td>
                                        <td>
                                            <strong>${item.productName}</strong>
                                        </td>
                                        <td>
                                            <span class="badge bg-info text-dark">${item.color}</span>
                                        </td>
                                        <td>
                                            <span class="badge bg-secondary">${item.size}</span>
                                        </td>
                                        <td>
                                            <fmt:formatNumber value="${item.price}" type="number" maxFractionDigits="0"/>₫
                                        </td>
                                        <td>
                                            <form action="MainController" method="post" style="display: inline;">
                                                <input type="hidden" name="txtAction" value="updateCart">
                                                <input type="hidden" name="variantID" value="${item.variantID}">
                                                <input type="number" 
                                                       class="form-control form-control-sm quantity-input" 
                                                       name="quantity"
                                                       value="${item.quantity}" 
                                                       min="1" 
                                                       max="99"
                                                       onchange="this.form.submit()">
                                            </form>
                                        </td>
                                        <td class="fw-bold text-danger">
                                            <fmt:formatNumber value="${itemTotal}" type="number" maxFractionDigits="0"/>₫
                                        </td>
                                        <td>
                                            <form action="MainController" method="post" style="display: inline;">
                                                <input type="hidden" name="txtAction" value="removeFromCart">
                                                <input type="hidden" name="variantID" value="${item.variantID}">
                                                <button class="btn btn-sm btn-outline-danger" 
                                                        type="submit"
                                                        onclick="return confirm('Bạn có chắc muốn xóa sản phẩm này?')">
                                                    <i class="bi bi-trash"></i>
                                                </button>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                            <tfoot>
                                <tr class="total-row">
                                    <th colspan="6" class="text-end">Tổng cộng:</th>
                                    <th class="text-danger">
                                        <fmt:formatNumber value="${total}" type="number" maxFractionDigits="0"/>₫
                                    </th>
                                    <th></th>
                                </tr>
                            </tfoot>
                        </table>
                    </div>

                    <!-- MOBILE: CARD -->
                    <div class="card-mobile">
                        <c:set var="total" value="0" />
                        <c:forEach var="item" items="${displayCart}">
                            <c:set var="itemTotal" value="${item.price * item.quantity}" />
                            <c:set var="total" value="${total + itemTotal}" />
                            
                            <div class="card mb-3">
                                <div class="row g-0">
                                    <div class="col-4">
                                        <img src="${not empty item.imageUrl ? item.imageUrl : 'assets/img/no-image.png'}" 
                                             class="img-fluid rounded-start h-100" 
                                             alt="${item.productName}"
                                             style="object-fit: cover;"
                                             onerror="this.src='assets/img/no-image.png'">
                                    </div>
                                    <div class="col-8">
                                        <div class="card-body py-2">
                                            <h6 class="card-title mb-1">${item.productName}</h6>
                                            <p class="card-text small mb-2">
                                                <span class="badge bg-secondary">Size: ${item.size}</span>
                                                <span class="badge bg-info text-dark">Màu: ${item.color}</span>
                                            </p>
                                            <p class="card-text">
                                                <small>Giá: <fmt:formatNumber value="${item.price}" type="number" maxFractionDigits="0"/>₫</small>
                                            </p>
                                            <div class="d-flex justify-content-between align-items-center">
                                                <span class="text-danger fw-bold">
                                                    <fmt:formatNumber value="${itemTotal}" type="number" maxFractionDigits="0"/>₫
                                                </span>
                                                <div class="d-flex align-items-center gap-2">
                                                    <form action="MainController" method="post" style="display: inline;">
                                                        <input type="hidden" name="txtAction" value="updateCart">
                                                        <input type="hidden" name="variantID" value="${item.variantID}">
                                                        <input type="number" 
                                                               class="form-control form-control-sm quantity-input"
                                                               name="quantity"
                                                               value="${item.quantity}" 
                                                               min="1" 
                                                               max="99"
                                                               onchange="this.form.submit()"
                                                               style="width: 60px;">
                                                    </form>
                                                    <form action="MainController" method="post" style="display: inline;">
                                                        <input type="hidden" name="txtAction" value="removeFromCart">
                                                        <input type="hidden" name="variantID" value="${item.variantID}">
                                                        <button class="btn btn-sm btn-outline-danger"
                                                                type="submit"
                                                                onclick="return confirm('Bạn có chắc muốn xóa?')">
                                                            <i class="bi bi-trash"></i>
                                                        </button>
                                                    </form>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                        
                        <!-- Mobile Total -->
                        <div class="card mb-3 total-row">
                            <div class="card-body">
                                <div class="d-flex justify-content-between align-items-center">
                                    <h5 class="mb-0">Tổng cộng:</h5>
                                    <h5 class="mb-0 text-danger">
                                        <fmt:formatNumber value="${total}" type="number" maxFractionDigits="0"/>₫
                                    </h5>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- BUTTONS -->
                    <div class="d-flex flex-column flex-md-row gap-3 mt-4">
                        <a href="MainController?txtAction=viewProducts" class="btn btn-outline-secondary flex-fill">
                            <i class="bi bi-arrow-left"></i> Tiếp tục mua
                        </a>
                        <a href="checkout.jsp" class="btn btn-success flex-fill">
                            <i class="bi bi-credit-card"></i> Thanh toán
                        </a>
                    </div>
                </c:if>
            </div>
        </div>
    </div>

    <%@ include file="../includes/footer.jspf" %>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>