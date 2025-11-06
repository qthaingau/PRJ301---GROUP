// =============== HEADER.JS ===============
// Script điều khiển dropdown, giỏ hàng và hiệu ứng nhỏ

document.addEventListener("DOMContentLoaded", function () {
    // Hover dropdown mượt mà (chỉ áp dụng cho màn hình lớn)
    const dropdowns = document.querySelectorAll(".nav-item.dropdown");
    dropdowns.forEach(drop => {
        drop.addEventListener("mouseenter", function () {
            const menu = this.querySelector(".dropdown-menu");
            if (menu) {
                menu.classList.add("show");
                menu.style.opacity = "1";
                menu.style.transition = "opacity 0.3s ease-in-out";
            }
        });
        drop.addEventListener("mouseleave", function () {
            const menu = this.querySelector(".dropdown-menu");
            if (menu) {
                menu.classList.remove("show");
                menu.style.opacity = "0";
            }
        });
    });

    // Hiệu ứng badge giỏ hàng nhảy nhẹ khi thay đổi
    const cartBadge = document.querySelector(".badge");
    if (cartBadge) {
        cartBadge.classList.add("bounce");
        setTimeout(() => cartBadge.classList.remove("bounce"), 600);
    }

    // Nút "Đăng xuất" xác nhận
    const logoutBtn = document.querySelector(".dropdown-item.text-danger");
    if (logoutBtn) {
        logoutBtn.addEventListener("click", function (e) {
            const confirmLogout = confirm("Bạn có chắc chắn muốn đăng xuất không?");
            if (!confirmLogout) e.preventDefault();
        });
    }

    // Logo hover hiệu ứng sáng nhẹ
    const logo = document.querySelector(".navbar-brand img");
    if (logo) {
        logo.addEventListener("mouseenter", () => logo.style.filter = "brightness(1.2)");
        logo.addEventListener("mouseleave", () => logo.style.filter = "brightness(1)");
    }
});
