// validation.js

document.addEventListener('DOMContentLoaded', function() {
    // Giả định biến existingVariantIDs đã được khai báo và gán giá trị trong tệp JSP.
    // Xem Bước 2 để biết cách gán.
    
    const variantIDInput = document.querySelector('input[name="txtVariantID"]');
    
    // Đảm bảo element tồn tại và không ở chế độ chỉ đọc (readonly)
    if (variantIDInput && !variantIDInput.readOnly) {
        
        // Thêm sự kiện kiểm tra mỗi khi giá trị thay đổi
        variantIDInput.addEventListener('input', function() {
            const enteredID = variantIDInput.value.trim().toUpperCase();
            let isDuplicate = false;
            
            // Lặp qua mảng ID đã có (biến toàn cục được tạo ở Bước 2)
            // Cần đảm bảo existingVariantIDs đã được load thành công.
            if (typeof existingVariantIDs !== 'undefined') {
                 isDuplicate = existingVariantIDs.includes(enteredID);
            }

            // Thực hiện validation
            if (isDuplicate) {
                // Nếu trùng, thiết lập thông báo lỗi
                variantIDInput.setCustomValidity('Variant ID already exist!');
                // (Bạn có thể thêm CSS class hoặc style để highlight lỗi ở đây nếu cần)
                // variantIDInput.classList.add('is-invalid');
            } else {
                // Nếu không trùng, xóa thông báo lỗi
                variantIDInput.setCustomValidity('');
                // (Tắt highlight nếu có)
                // variantIDInput.classList.remove('is-invalid');
            }
        });
    }
});