// cart.js - AJAX + Toast + Badge Update
const toastEl = document.getElementById('cartToast');
const toast = new bootstrap.Toast(toastEl);

function showToast(message) {
    document.getElementById('toastMessage').textContent = message;
    toast.show();
}

function updateQuantity(variantID, qty) {
    if (qty < 1) qty = 1;
    fetch('MainController', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: `txtAction=updateCart&variantID=${variantID}&quantity=${qty}`
    })
    .then(() => {
        updateCartDisplay();
        showToast('Đã cập nhật số lượng!');
    })
    .catch(() => showToast('Lỗi cập nhật!'));
}

function removeItem(variantID) {
    if (!confirm('Xóa sản phẩm khỏi giỏ hàng?')) return;

    fetch('MainController', {
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: `txtAction=removeFromCart&variantID=${variantID}`
    })
    .then(() => {
        updateCartDisplay();
        showToast('Đã xóa sản phẩm!');
    });
}

function updateCartDisplay() {
    fetch('MainController?txtAction=viewCart')
        .then(r => r.text())
        .then(html => {
            const parser = new DOMParser();
            const doc = parser.parseFromString(html, 'text/html');

            // Cập nhật badge trên header
            const newBadge = doc.querySelector('.badge');
            const oldBadge = document.querySelector('.badge');
            if (oldBadge) {
                if (newBadge) {
                    oldBadge.textContent = newBadge.textContent;
                    oldBadge.style.display = 'block';
                } else {
                    oldBadge.style.display = 'none';
                }
            }

            // Cập nhật tổng tiền
            const newTotal = doc.querySelector('#totalAmount').innerHTML || '0₫';
            const totalEl = document.querySelector('#totalAmount');
            if (totalEl) totalEl.innerHTML = newTotal;

            // Mobile total
            const mobileTotal = doc.querySelector('#mobileTotal').innerHTML || '';
            const mobileEl = document.querySelector('#mobileTotal');
            if (mobileEl) mobileEl.innerHTML = mobileTotal;
        });
}