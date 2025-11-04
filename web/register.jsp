<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8" />
        <title>Đăng ký tài khoản</title>
        <meta name="viewport" content="width=device-width, initial-scale=1" />
    </head>

    <body>
        <!-- Nếu đã đăng nhập, tự động chuyển về dashboard -->
        <c:if test="${not empty user}">
            <c:redirect url="dashboard.jsp" />
        </c:if>

        <h1>REGISTER FORM</h1>

        <!-- Hiển thị thông báo lỗi hoặc thành công -->
        <c:if test="${not empty msg}">
            <div>
                <span style="color:red">${msg}</span>
            </div>
        </c:if>

        <!-- Form đăng ký -->
        <form action="MainController" method="post">
    <input type="hidden" name="txtAction" value="registerUser" />

    <label for="username">Username</label><br />
    <input id="username" type="text" name="txtNewUsername" placeholder="Nhập tên đăng nhập" required /><br /><br />

    <label for="email">Email</label><br />
    <input id="email" type="email" name="txtEmail" placeholder="Nhập email" required /><br /><br />

    <label for="password">Password</label><br />
    <input id="password" type="password" name="txtNewPassword" placeholder="Nhập mật khẩu" required /><br /><br />

    <label for="confirmPassword">Confirm Password</label><br />
    <input id="confirmPassword" type="password" name="txtConfirmPassword" placeholder="Nhập lại mật khẩu" required /><br /><br />

    <label for="fullname">Họ và tên</label><br />
    <input id="fullname" type="text" name="txtFullName" placeholder="Nhập họ tên" /><br /><br />

    <label for="phone">Số điện thoại</label><br />
    <input id="phone" type="text" name="txtPhoneNumber" placeholder="Nhập số điện thoại" /><br /><br />

    <button type="submit">Register</button>
    <button type="reset">Reset</button>
</form>


        <br />
        <!-- Link quay lại trang đăng nhập -->
        <p>Bạn đã có tài khoản? <a href="login.jsp">Đăng nhập tại đây</a></p>
    </body>
</html>
