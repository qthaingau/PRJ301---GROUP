<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Change Password</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/changePassword.css">
</head>
<body>
    <!-- Nếu chưa đăng nhập thì quay về login -->
    <c:if test="${empty sessionScope.user}">
        <c:redirect url="${pageContext.request.contextPath}/login.jsp" />
    </c:if>

    <div class="container">
        <h1>Change Password</h1>

        <!-- Gửi về MainController, không còn ChangePasswordController -->
        <form action="${pageContext.request.contextPath}/MainController" method="post">
            <!-- Xác định action để MainController biết -->
            <input type="hidden" name="txtAction" value="changePassword">

            <div class="form-group">
                <label>Current Password:</label>
                <input type="password" name="currentPassword" required>
            </div>

            <div class="form-group">
                <label>New Password:</label>
                <input type="password" name="newPassword" required>
            </div>

            <div class="form-group">
                <label>Confirm New Password:</label>
                <input type="password" name="confirmPassword" required>
            </div>

            <button type="submit">Update Password</button>

            <!-- Hiển thị thông báo -->
            <c:if test="${not empty msg}">
                <p class="message">${msg}</p>
            </c:if>
        </form>

        <!-- Đường dẫn quay lại Profile (đúng folder customer) -->
        <a href="${pageContext.request.contextPath}/customer/profile.jsp" class="back">← Back to Profile</a>
    </div>
</body>
</html>
