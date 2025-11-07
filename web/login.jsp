<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8" />
        <title>Login</title>
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <link rel="stylesheet" href="assets/css/login.css">
    </head>

    <body>
        <div class="login-container">
            <!-- Redirect if user already logged in -->
            <c:if test="${not empty user}">
                <c:redirect url="home.jsp" />
            </c:if>

            <!-- Main title -->
            <h1>LOGIN FORM</h1>

            <!-- Show error message if exists -->
            <c:if test="${not empty msg}">
                <span>${msg}</span>
            </c:if>

            <!-- Login form -->
            <form action="MainController" method="post">
                <input type="hidden" name="txtAction" value="login" />

                <label for="username">Username</label>
                <input
                    id="username"
                    type="text"
                    name="txtUsername"
                    value="${username}"
                    placeholder="Enter your username"
                    required
                />
                <br>
                <label for="password">Password</label>
                <input
                    id="password"
                    type="password"
                    name="txtPassword"
                    placeholder="Enter your password"
                    required
                />

                <div class="buttons">
                    <button type="submit">Login</button>
                    <button type="reset">Reset</button>
                </div>

                <p>Don't have an account? <a href="register.jsp">Register</a></p>
            </form>
        </div>
    </body>
</html>
