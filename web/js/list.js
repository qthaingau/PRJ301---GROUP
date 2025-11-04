// list.js - dùng cho listOfOrder.jsp và listOfPayment.jsp

// Tính năng tìm kiếm nhanh theo nội dung trong bảng
function searchTable(inputId, tableId) {
    const input = document.getElementById(inputId);
    const filter = input.value.toLowerCase();
    const rows = document.querySelectorAll(`#${tableId} tbody tr`);

    rows.forEach(row => {
        const text = row.innerText.toLowerCase();
        row.style.display = text.includes(filter) ? '' : 'none';
    });
}

// Khi người dùng nhập vào ô tìm kiếm, tự động lọc
document.addEventListener('DOMContentLoaded', () => {
    const searchInputs = document.querySelectorAll('.search-input');
    searchInputs.forEach(input => {
        input.addEventListener('keyup', () => {
            const tableId = input.dataset.table;
            searchTable(input.id, tableId);
        });
    });
});
