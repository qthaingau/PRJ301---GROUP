<%-- 
    Document   : error
    Created on : Nov 3, 2025, 8:07:05 AM
    Author     : TEST
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <c:if test="${not empty msg}">
            <p>${msg}</p>
        </c:if>
    </body>
</html>
