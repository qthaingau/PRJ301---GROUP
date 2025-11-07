
</html><%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>User Profile</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/profile.css">

</head>
<body>
    <c:if test="${empty sessionScope.user}">
        <c:redirect url="login.jsp" />
    </c:if>

    <div class="profile-container">
        <h1>User Profile</h1>

        <div class="info">
            <p><strong>User ID:</strong> ${user.userID}</p>
            <p><strong>Username:</strong> ${user.username}</p>
            <p><strong>Email:</strong> ${user.email}</p>
            <p><strong>Full Name:</strong> ${user.fullName}</p>
            <p><strong>Phone Number:</strong> ${user.phoneNumber}</p>
            <p><strong>Role:</strong> ${user.role}</p>
            <p><strong>Created At:</strong> ${user.createdAt}</p>
        </div>

        <div class="buttons">
            <a href="${pageContext.request.contextPath}/customer/changePassword.jsp" class="btn change">Change Password</a>

            <a href="../home.jsp" class="btn back">Back to Home</a>

        </div>
    </div>
</body>
</html>

