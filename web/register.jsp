<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8" />
        <title>Register</title>
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <!-- Link to CSS -->
        <link rel="stylesheet" href="assets/css/register.css">
    </head>

    <body>
        <c:if test="${not empty user}">
            <c:redirect url="home.jsp" />
        </c:if>

        <div class="container">
            <h1>REGISTER FORM</h1>

            <c:if test="${not empty msg}">
                <div class="message">
                    <span>${msg}</span>
                </div>
            </c:if>

            <form action="MainController" method="post">
                <input type="hidden" name="txtAction" value="registerUser" />

                <label for="username">Username</label><br />
                <input id="username" type="text" name="txtNewUsername" placeholder="Enter your username" required /><br />

                <label for="email">Email</label><br />
                <input id="email" type="email" name="txtEmail" placeholder="Enter your email" required /><br />

                <label for="password">Password</label><br />
                <input id="password" type="password" name="txtNewPassword" placeholder="Enter your password" required /><br />

                <label for="confirmPassword">Confirm Password</label><br />
                <input id="confirmPassword" type="password" name="txtConfirmPassword" placeholder="Re-enter your password" required /><br />

                <label for="fullname">Full Name</label><br />
                <input id="fullname" type="text" name="txtFullName" placeholder="Enter your full name" /><br />

                <label for="phone">Phone Number</label><br />
                <input id="phone" type="text" name="txtPhoneNumber" placeholder="Enter your phone number" /><br />

                <div class="buttons">
                    <button type="submit">Register</button>
                    <button type="reset">Reset</button>
                </div>
            </form>

            <p class="bottom-text">
                Already have an account? <a href="login.jsp">Login here</a>
            </p>
        </div>
    </body>
</html>
