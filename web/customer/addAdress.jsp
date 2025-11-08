<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Thêm địa chỉ mới</title>
    <style>
        body { font-family: Arial; margin: 40px; background: #f8f9fa; }
        .form-container { max-width: 500px; margin: auto; background: white; padding: 25px; border-radius: 10px; box-shadow: 0 0 10px rgba(0,0,0,0.1); }
        input, button { width: 100%; padding: 12px; margin: 8px 0; border: 1px solid #ddd; border-radius: 5px; font-size: 16px; }
        button { background: #007bff; color: white; border: none; cursor: pointer; font-weight: bold; }
        button:hover { background: #0056b3; }
        button.cancel { background: #6c757d; }
        button.cancel:hover { background: #5a6268; }
        label { display: block; margin: 10px 0 5px; font-weight: bold; }
        .checkbox { width: auto; display: inline; }
    </style>
</head>
<body>
<div class="form-container">
    <h2 style="text-align:center; color:#007bff;">Thêm địa chỉ giao hàng</h2>
    <form action="MainController" method="post">
        <input type="hidden" name="txtAction" value="addAddress">
        <input type="hidden" name="returnUrl" value="${param.returnUrl}">

        <label>Họ tên người nhận *</label>
        <input type="text" name="recipientName" placeholder="Nguyễn Văn A" required>

        <label>Số điện thoại *</label>
        <input type="text" name="phoneNumber" placeholder="0901234567" pattern="[0-9]{10,11}" required>

        <label>Đường, số nhà *</label>
        <input type="text" name="street" placeholder="123 Đường ABC" required>

        <label>Phường/Xã *</label>
        <input type="text" name="ward" placeholder="Phường 1" required>

        <label>Quận/Huyện *</label>
        <input type="text" name="district" placeholder="Quận 1" required>

        <label>Tỉnh/Thành phố *</label>
        <input type="text" name="city" placeholder="TP. Hồ Chí Minh" required>

        <label>
            <input type="checkbox" name="isDefault" class="checkbox"> 
            Đặt làm địa chỉ mặc định
        </label>

        <button type="submit">Lưu & Quay lại</button>
        <button type="button" class="cancel" onclick="history.back()">Hủy</button>
    </form>
</div>
</body>
</html>