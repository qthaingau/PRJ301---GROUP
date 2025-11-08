// assets/js/productList.js
const toastEl = document.getElementById('cartToast');
const toast = new bootstrap.Toast(toastEl);
const toastMessage = document.getElementById('toastMessage');

function showToast(msg) {
    toastMessage.textContent = msg;
    toast.show();
}

function addToCartFirst(productID, quantity = 1) {
    fetch('MainController', {  // ĐÚNG: GỌI QUA MAIN
        method: 'POST',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
        body: `txtAction=addToCartFirst&productID=${productID}&quantity=${quantity}`
    })
    .then(r => r.text())
    .then(res => {
        const result = res.trim();
        if (result === 'OK') {
            showToast('Đã thêm vào giỏ hàng!');
            updateCartBadge();
        } else if (result === 'OUT_OF_STOCK') {
            showToast('Sản phẩm tạm hết hàng!');
        } else {
            showToast('Lỗi thêm vào giỏ: ' + result); // Debug
        }
    })
    .catch(err => {
        console.error(err);
        showToast('Lỗi kết nối!');
    });
}

function buyNow(productID) {
    addToCartFirst(productID, 1);
    setTimeout(() => {
        window.location.href = 'checkout.jsp';
    }, 800);
}

function updateCartBadge() {
    fetch('CartController?txtAction=viewCart')
        .then(r => r.text())
        .then(html => {
            const parser = new DOMParser();
            const doc = parser.parseFromString(html, 'text/html');
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
        });
}
$(document).ready(function() {
    let page = 1;

    // Detect scroll event and load more products when the user scrolls to the bottom
    $(window).on('scroll', function() {
        if ($(window).scrollTop() + $(window).height() == $(document).height()) {
            page++;  // Increment page number
            loadMoreProducts(page);
        }
    });

    // Function to load more products dynamically
    function loadMoreProducts(page) {
        $.ajax({
            url: 'MainController',
            type: 'GET',
            data: {
                txtAction: 'loadMoreProducts',
                page: page
            },
            success: function(response) {
                // Append new products to the product grid
                $('#product-grid').append(response);
            },
            error: function() {
                console.log("Error loading more products.");
            }
        });
    }

    // Optional: Button to load more products manually
    $('#load-more button').click(function() {
        page++;  // Increment page number
        loadMoreProducts(page);
    });
});
