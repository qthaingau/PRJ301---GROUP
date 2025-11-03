<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta http-equiv="refresh" content="2;url=home.jsp">
        <title>Login Success</title>
    </head>
    <body>
        <span class="navbar-text me-3">
            Welcome, <span class="fw-bold text-warning">${user.fullName}</span>
        </span>
    </body>
</html>
