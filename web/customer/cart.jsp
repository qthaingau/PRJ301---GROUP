<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Giỏ hàng - HTV Sport</title>

    <!-- Bootstrap 5 + Icons -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css" rel="stylesheet">

    <!-- Custom CSS -->
    <link href="${pageContext.request.contextPath}/assets/css/cart.css" rel="stylesheet">
</head>
<body class="bg-light">

    <!-- HEADER -->
    <%@ include file="../includes/header.jspf" %>

    <!-- MAIN CONTENT -->
    <div class="container my-5">
        <div class="row">
            <div class="col-lg-8 mx-auto">

                <h2 class="mb-4 text-center text-md-start">
                    <i class="bi bi-cart3"></i> Giỏ hàng của bạn
                </h2>

                <!-- EMPTY CART -->
                <c:if test="${empty cartItems}">
                    <div class="text-center py-5">
                        <i class="bi bi-cart-x empty-cart-icon text-muted"></i>
                        <p class="lead mt-3">Giỏ hàng trống</p>
                        <a href="MainController?txtAction=viewProducts" class="btn btn-warning btn-lg">
                            <i class="bi bi-shop"></i> Mua sắm ngay
                        </a>
                    </div>
                </c:if>

                <!-- CART ITEMS -->
                <c:if test="${not empty cartItems}">
                    <!-- DESKTOP: TABLE -->
                    <div class="table-responsive table-desktop">
                        <table class="table table-hover align-middle bg-white rounded shadow-sm">
                            <thead class="table-dark">
                                <tr>
                                    <th>Hình ảnh</th>
                                    <th>Sản phẩm</th>
                                    <th>Giá</th>
                                    <th>Số lượng</th>
                                    <th>Thành tiền</th>
                                    <th></th>
                                </tr>
                            </thead>
                            <tbody id="cartTableBody">
                                <c:forEach var="item" items="${cartItems}">
                                    <tr data-variant-id="${item.variantID}">
                                        <td>
                                            <img src="${not empty item.imageUrl ? item.imageUrl : '${pageContext.request.contextPath}/assets/img/no-image.png'}"
                                                 class="cart-item-img" alt="${item.productName}">
                                        </td>
                                        <td>
                                            <strong>${item.productName}</strong><br>
                                            <small class="text-muted">Size: ${item.size} | Màu: ${item.color}</small>
                                        </td>
                                        <td><fmt:formatNumber value="${item.price}" type="currency" currencySymbol="₫"/></td>
                                        <td>
                                            <input type="number" class="form-control quantity-input" 
                                                   value="${item.quantity}" min="1" max="99"
                                                   onchange="updateQuantity('${item.variantID}', this.value)">
                                        </td>
                                        <td class="fw-bold text-danger">
                                            <fmt:formatNumber value="${item.total}" type="currency" currencySymbol="₫"/>
                                        </td>
                                        <td>
                                            <button class="btn btn-sm btn-outline-danger" 
                                                    onclick="removeItem('${item.variantID}')">
                                                <i class="bi bi-trash"></i>
                                            </button>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                            <tfoot>
                                <tr class="table-success">
                                    <th colspan="4" class="text-end">Tổng cộng:</th>
                                    <th id="totalAmount" class="text-danger">
                                        <fmt:formatNumber value="${
                                            cartItems.stream().map(i -> i.total).sum()
                                        }" type="currency" currencySymbol="₫"/>
                                    </th>
                                    <th></th>
                                </tr>
                            </tfoot>
                        </table>
                    </div>

                    <!-- MOBILE: CARD -->
                    <div class="card-mobile">
                        <c:forEach var="item" items="${cartItems}">
                            <div class="card mb-3 cart-card" data-variant-id="${item.variantID}">
                                <div class="row g-0">
                                    <div class="col-4">
                                        <img src="${not empty item.imageUrl ? item.imageUrl : '${pageContext.request.contextPath}/assets/img/no-image.png'}"
                                             class="img-fluid rounded-start cart-item-img" alt="${item.productName}">
                                    </div>
                                    <div class="col-8">
                                        <div class="card-body py-2">
                                            <h6 class="card-title mb-1">${item.productName}</h6>
                                            <p class="card-text small text-muted mb-2">
                                                Size: ${item.size} | Màu: ${item.color}
                                            </p>
                                            <div class="d-flex justify-content-between align-items-center">
                                                <span class="text-danger fw-bold">
                                                    <fmt:formatNumber value="${item.total}" type="currency" currencySymbol="₫"/>
                                                </span>
                                                <div class="d-flex align-items-center gap-2">
                                                    <input type="number" class="form-control form-control-sm quantity-input"
                                                           value="${item.quantity}" min="1" max="99"
                                                           onchange="updateQuantity('${item.variantID}', this.value)">
                                                    <button class="btn btn-sm btn-outline-danger"
                                                            onclick="removeItem('${item.variantID}')">
                                                        <i class="bi bi-trash"></i>
                                                    </button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>

                    <!-- BUTTONS -->
                    <div class="d-flex flex-column flex-md-row gap-3 mt-4">
                        <a href="MainController?txtAction=viewProducts" class="btn btn-outline-secondary flex-fill">
                            <i class="bi bi-arrow-left"></i> Tiếp tục mua
                        </a>
                        <a href="checkout.jsp" class="btn btn-success flex-fill">
                            <i class="bi bi-credit-card"></i> Thanh toán 
                            <span id="mobileTotal" class="d-md-none">
                                (<fmt:formatNumber value="${
                                    cartItems.stream().map(i -> i.total).sum()
                                }" type="currency" currencySymbol="₫"/>)
                            </span>
                        </a>
                    </div>
                </c:if>
            </div>
        </div>
    </div>

    <!-- TOAST -->
    <div class="toast-container">
        <div id="cartToast" class="toast align-items-center text-bg-success border-0" role="alert">
            <div class="d-flex">
                <div class="toast-body">
                    <i class="bi bi-check-circle"></i> <span id="toastMessage"></span>
                </div>
                <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast"></button>
            </div>
        </div>
    </div>

    <!-- FOOTER -->
    <%@ include file="../includes/footer.jspf" %>

    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>

    <!-- Custom JS -->
    <script src="${pageContext.request.contextPath}/assets/js/cart.js"></script>
</body>
</html>