<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!-- Khai báo đây là một trang JSP với mã hóa UTF-8 để hiển thị tiếng Việt đúng -->

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8" />
        <!-- Thiết lập bộ ký tự để trình duyệt hiểu tiếng Việt -->

        <title>Đăng nhập</title>
        <!-- Tiêu đề hiển thị trên tab trình duyệt -->

        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <!-- Giúp giao diện hiển thị phù hợp trên cả điện thoại và máy tính -->
    </head>

    <body>
        <!-- Toàn bộ phần nội dung trang đăng nhập -->

        <!-- Nếu người dùng đã đăng nhập (user không rỗng) thì chuyển hướng sang loginSuccess.jsp -->
        <c:if test="${not empty user}">
            <c:redirect url="home.jsp" />
        </c:if>

        <!-- Tiêu đề chính của form đăng nhập -->
        <h1>LOGIN FORM</h1>

        <!-- Nếu có thông báo lỗi từ server (biến msg), hiển thị ra màu đỏ -->
        <c:if test="${not empty msg}">
            <div>
                <span style="color:red">${msg}</span>
            </div>
        </c:if>

        <!-- Bắt đầu form gửi dữ liệu đăng nhập về MainController -->
        <form action="MainController" method="post">
            <!-- Truyền hidden input để Controller biết hành động là "login" -->
            <input type="hidden" name="txtAction" value="login" />

            <!-- Trường nhập Username -->
            <div>
                <label for="username">Username</label><br />
                <input
                    id="username"
                    type="text"
                    name="txtUsername"
                    value="${username}"
                    placeholder="Nhập tên đăng nhập"
                    required
                    />
                <!-- required: bắt buộc nhập -->
            </div>

            <br />

            <!-- Trường nhập Password -->
            <div>
                <label for="password">Password</label><br />
                <input
                    id="password"
                    type="password"
                    name="txtPassword"
                    placeholder="Nhập mật khẩu"
                    required
                    />
            </div>

            <br />

            <!-- Hai nút: Đăng nhập và Reset form -->
            <div>
                <button type="submit">Login</button>
                <button type="reset">Reset</button>
                <p>Bạn chưa có tài khoản? <a href="register.jsp">Đăng ký ngay</a></p>
            </div>
        </form>
    </body>
</html>
