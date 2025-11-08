document.getElementById('productImageFile').addEventListener('change', function() {
    const file = this.files[0];
    if (!file) return;

    const reader = new FileReader();
    reader.onload = function(e) {
        const base64String = e.target.result; // chứa cả header data:image/png;base64,...
        document.getElementById('productImage').value = base64String;

        // Hiển thị xem trước
        const imgPreview = document.getElementById('productPreview');
        imgPreview.src = base64String;
        imgPreview.style.display = 'block';
    };
    reader.readAsDataURL(file); // đọc file dưới dạng Base64
});