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
                grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
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

            .size-select, .quantity-input {
                margin: 10px 0;
                font-size: 0.9em;
            }
            
            .quantity-input {
                width: 100%;
                text-align: center;
            }
            
            .quantity-label {
                font-size: 0.85em;
                color: #666;
                text-align: left;
                margin-bottom: 5px;
                font-weight: 500;
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
            
            .alert {
                padding: 12px 20px;
                margin: 15px 0;
                border-radius: 6px;
                display: none;
            }
            .alert-success {
                background: #d4edda;
                color: #155724;
                border: 1px solid #c3e6cb;
            }
            .alert-danger {
                background: #f8d7da;
                color: #721c24;
                border: 1px solid #f5c6cb;
            }
        </style>
    </head>
    <body class="bg-light">

        <div class="container">
            <a href="home.jsp" class="back-btn">← Quay lại danh sách</a>
            
            <!-- Alert messages -->
            <div id="alertMessage" class="alert"></div>
            
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
                                <c:set var="maxStock" value="0" />
                                <c:forEach var="v" items="${group}">
                                    <c:if test="${v.stock > 0}">
                                        <c:set var="inStock" value="true" />
                                        <c:if test="${v.stock > maxStock}">
                                            <c:set var="maxStock" value="${v.stock}" />
                                        </c:if>
                                    </c:if>
                                </c:forEach>
                                <c:if test="${!inStock}"><div class="out-of-stock">Hết hàng</div></c:if>

                                <!-- ẢNH -->
                                <div class="variant-img-wrapper">
                                    <img src="${not empty first.variantImage ? first.variantImage : 'assets/img/no-image.png'}"
                                         alt="${first.color}"
                                         class="variant-img"
                                         onerror="this.src='assets/img/no-image.png'">
                                </div>

                                <!-- GIÁ -->
                                <div>
                                    <span class="old-price">₫<fmt:formatNumber value="${first.price * 1.3}" type="number" maxFractionDigits="0"/></span>
                                    <span class="new-price">₫<fmt:formatNumber value="${first.price}" type="number" maxFractionDigits="0"/></span>
                                </div>

                                <!-- TÊN + MÀU -->
                                <div class="product-title">${product.productName}</div>
                                <div class="color-code">${first.color}</div>

                                <!-- FORM CHỌN SIZE & QUANTITY -->
                                <form class="add-to-cart-form" onsubmit="return handleAddToCart(event, this)">
                                    <!-- SIZE SELECT -->
                                    <select name="size" class="form-select size-select" required onchange="updateVariantInfo(this)">
                                        <option value="">Chọn size</option>
                                        <c:forEach var="v" items="${group}">
                                            <c:choose>
                                                <c:when test="${v.stock > 0}">
                                                    <option value="${v.size}" 
                                                            data-variant-id="${v.variantID}"
                                                            data-stock="${v.stock}">
                                                        ${v.size} (Còn ${v.stock})
                                                    </option>
                                                </c:when>
                                                <c:otherwise>
                                                    <option value="${v.size}" disabled>${v.size} (Hết)</option>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:forEach>
                                    </select>
                                    
                                    <!-- QUANTITY INPUT -->
                                    <div class="quantity-label">Số lượng:</div>
                                    <input type="number" 
                                           name="quantity" 
                                           class="form-control quantity-input" 
                                           value="1" 
                                           min="1" 
                                           max="${maxStock > 0 ? maxStock : 1}"
                                           required>
                                    
                                    <input type="hidden" name="variantID" value="">
                                    <input type="hidden" name="maxStock" value="${maxStock}">

                                    <button type="submit" class="btn btn-cart">
                                        <i class="bi bi-cart-plus"></i> Thêm vào giỏ
                                    </button>
                                    <button type="button" class="btn btn-buy" onclick="buyNow(this.form)">
                                        <i class="bi bi-lightning-fill"></i> Mua ngay
                                    </button>
                                </form>
                            </div>
                        </c:forEach>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>

        <script>
            function updateVariantInfo(select) {
                const option = select.selectedOptions[0];
                const variantID = option.getAttribute('data-variant-id');
                const stock = parseInt(option.getAttribute('data-stock')) || 1;
                
                const form = select.closest('form');
                const quantityInput = form.querySelector('input[name="quantity"]');
                const variantIDInput = form.querySelector('input[name="variantID"]');
                
                // Update hidden inputs
                variantIDInput.value = variantID || '';
                form.querySelector('input[name="maxStock"]').value = stock;
                
                // Update quantity constraints
                quantityInput.max = stock;
                if (parseInt(quantityInput.value) > stock) {
                    quantityInput.value = stock;
                }
                
                console.log('Updated variant:', variantID, 'Stock:', stock);
            }

            function showAlert(message, type) {
                const alertDiv = document.getElementById('alertMessage');
                alertDiv.textContent = message;
                alertDiv.className = 'alert alert-' + type;
                alertDiv.style.display = 'block';
                
                // Auto hide after 4 seconds
                setTimeout(() => {
                    alertDiv.style.display = 'none';
                }, 4000);
                
                // Scroll to top to show alert
                window.scrollTo({ top: 0, behavior: 'smooth' });
            }

            function handleAddToCart(event, form) {
                event.preventDefault();
                
                const variantID = form.querySelector('input[name="variantID"]').value;
                const quantity = parseInt(form.querySelector('input[name="quantity"]').value);
                const maxStock = parseInt(form.querySelector('input[name="maxStock"]').value);
                
                // Validation
                if (!variantID) {
                    showAlert('Vui lòng chọn size còn hàng!', 'danger');
                    return false;
                }
                
                if (quantity < 1) {
                    showAlert('Số lượng phải lớn hơn 0!', 'danger');
                    return false;
                }
                
                if (quantity > maxStock) {
                    showAlert('Số lượng vượt quá tồn kho (còn ' + maxStock + ')!', 'danger');
                    return false;
                }
                
                // Disable button to prevent double submission
                const submitBtn = form.querySelector('button[type="submit"]');
                const originalHTML = submitBtn.innerHTML;
                submitBtn.disabled = true;
                submitBtn.innerHTML = '<span class="spinner-border spinner-border-sm"></span> Đang thêm...';
                
                console.log('Sending to cart:', { variantID, quantity });
                
                // Send AJAX request
                fetch('MainController', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded',
                    },
                    body: 'txtAction=addToCart&variantID=' + encodeURIComponent(variantID) + 
                          '&quantity=' + encodeURIComponent(quantity)
                })
                .then(response => {
                    console.log('Response status:', response.status);
                    return response.text();
                })
                .then(data => {
                    console.log('Response data:', data);
                    submitBtn.disabled = false;
                    submitBtn.innerHTML = originalHTML;
                    
                    const trimmedData = data.trim();
                    
                    if (trimmedData === 'OK') {
                        showAlert('Đã thêm ' + quantity + ' sản phẩm vào giỏ hàng!', 'success');
                        // Reset quantity to 1
                        form.querySelector('input[name="quantity"]').value = 1;
                    } else if (trimmedData === 'OUT_OF_STOCK') {
                        showAlert('Sản phẩm đã hết hàng hoặc không đủ số lượng!', 'danger');
                    } else if (trimmedData === 'LOGIN_REQUIRED') {
                        showAlert('Vui lòng đăng nhập để thêm vào giỏ hàng!', 'danger');
                        setTimeout(() => {
                            window.location.href = 'login.jsp';
                        }, 1500);
                    } else {
                        console.error('Unexpected response:', trimmedData);
                        showAlert('Có lỗi xảy ra: ' + trimmedData, 'danger');
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    submitBtn.disabled = false;
                    submitBtn.innerHTML = originalHTML;
                    showAlert('Có lỗi xảy ra, vui lòng thử lại!', 'danger');
                });
                
                return false;
            }
            
            function buyNow(form) {
                const variantID = form.querySelector('input[name="variantID"]').value;
                const quantity = parseInt(form.querySelector('input[name="quantity"]').value);
                const maxStock = parseInt(form.querySelector('input[name="maxStock"]').value);
                
                if (!variantID) {
                    showAlert('Vui lòng chọn size còn hàng!', 'danger');
                    return;
                }
                
                if (quantity < 1 || quantity > maxStock) {
                    showAlert('Số lượng không hợp lệ!', 'danger');
                    return;
                }
                
                console.log('Buy now:', { variantID, quantity });
                
                // First add to cart, then redirect to cart page
                fetch('MainController', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded',
                    },
                    body: 'txtAction=addToCart&variantID=' + encodeURIComponent(variantID) + 
                          '&quantity=' + encodeURIComponent(quantity)
                })
                .then(response => response.text())
                .then(data => {
                    console.log('Buy now response:', data);
                    if (data.trim() === 'OK') {
                        window.location.href = 'MainController?txtAction=viewCart';
                    } else if (data.trim() === 'LOGIN_REQUIRED') {
                        showAlert('Vui lòng đăng nhập!', 'danger');
                        setTimeout(() => {
                            window.location.href = 'login.jsp';
                        }, 1500);
                    } else {
                        showAlert('Có lỗi xảy ra: ' + data.trim(), 'danger');
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    showAlert('Có lỗi xảy ra!', 'danger');
                });
            }
        </script>
        
        <!-- Bootstrap Icons -->
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.0/font/bootstrap-icons.css">
        <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

    </body>
</html>