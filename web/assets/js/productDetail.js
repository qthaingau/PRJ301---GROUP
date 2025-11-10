function addToCart(event, form) {
    event.preventDefault();
    
    const variantSelect = form.querySelector('select[name="variantID"]');
    const quantityInput = form.querySelector('input[name="quantity"]');
    const variantID = variantSelect.value;
    const quantity = quantityInput.value;
    
    if (!variantID) {
        showAlert('Vui lòng chọn size!', 'warning');
        variantSelect.focus();
        return;
    }
    
    // Disable button during request
    const submitBtn = form.querySelector('button[type="submit"]');
    const originalText = submitBtn.innerHTML;
    submitBtn.innerHTML = '<i class="bi bi-arrow-repeat spin"></i> Đang thêm...';
    submitBtn.disabled = true;
    
    // Gửi AJAX request
    fetch('MainController?txtAction=addToCart', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded',
        },
        body: `variantID=${encodeURIComponent(variantID)}&quantity=${encodeURIComponent(quantity)}`
    })
    .then(response => response.text())
    .then(result => {
        console.log('Add to cart result:', result);
        
        if (result === 'OK') {
            showAlert('✅ Đã thêm vào giỏ hàng!', 'success');
            // Có thể update cart counter ở header nếu có
            updateCartCounter();
        } else if (result === 'OUT_OF_STOCK') {
            showAlert('❌ Sản phẩm đã hết hàng!', 'error');
        } else if (result === 'LOGIN_REQUIRED') {
            showAlert('🔐 Vui lòng đăng nhập để thêm vào giỏ!', 'warning');
            setTimeout(() => {
                window.location.href = 'login.jsp';
            }, 1500);
        } else {
            showAlert('❌ Lỗi khi thêm vào giỏ!', 'error');
        }
    })
    .catch(error => {
        console.error('Error:', error);
        showAlert('❌ Lỗi kết nối!', 'error');
    })
    .finally(() => {
        // Restore button
        submitBtn.innerHTML = originalText;
        submitBtn.disabled = false;
    });
}

function buyNow(button) {
    const form = button.closest('form');
    const variantSelect = form.querySelector('select[name="variantID"]');
    const quantityInput = form.querySelector('input[name="quantity"]');
    const variantID = variantSelect.value;
    const quantity = quantityInput.value;
    
    if (!variantID) {
        showAlert('Vui lòng chọn size!', 'warning');
        variantSelect.focus();
        return;
    }
    
    // First add to cart, then redirect
    addToCart(new Event('submit'), form);
    
    // Redirect to cart after short delay
    setTimeout(() => {
        window.location.href = 'MainController?txtAction=viewCart';
    }, 1000);
}

function showAlert(message, type) {
    const alertDiv = document.getElementById('alertMessage');
    alertDiv.innerHTML = `
        <div class="alert alert-${type === 'error' ? 'danger' : type} alert-dismissible fade show">
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    `;
    
    // Auto hide after 3 seconds
    setTimeout(() => {
        const bsAlert = new bootstrap.Alert(alertDiv.querySelector('.alert'));
        bsAlert.close();
    }, 3000);
}

function updateCartCounter() {
    // Update cart counter in header if exists
    const cartCounter = document.querySelector('.cart-count');
    if (cartCounter) {
        const currentCount = parseInt(cartCounter.textContent) || 0;
        cartCounter.textContent = currentCount + 1;
    }
}