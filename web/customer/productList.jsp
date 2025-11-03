<%-- 
    Document   : ListOfProducts
    Created on : Nov 3, 2025, 7:41:43 AM
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
        <c:choose>
            <c:when test="${empty listProducts}">
                <div>
                    <h5>No products found</h5>
                    <p>Please enter another keyword to find products.</p>
                </div>
            </c:when>
        </c:choose>
        
        <c:otherwise>
            <table>
            <thead>
                <tr>
                    <th>Product ID</th>
                    <th>Product name</th>
                    <th>Description</th>
                    <th>Category ID</th>
                    <th>Brand ID</th>
                    <th>Created At</th>
                    <th>Status</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var ="p" items="${listProducts}">
                    <tr>
                        <td>${p.productID}</td>
                        <td>${p.productName}</td>
                        <td>${p.description}</td>
                        <td>${p.categoryID}</td>
                        <td>${p.brandID}</td>
                        <td>${p.createdAt}</td>
                        <td>${p.isActive}</td>
                    </tr>
                    <c:if test="${user.role eq 'Admin'}">
                    <td>
                        <div>
                            <a href="MainController!txtAction=callUpdateProduct&productID=${p.productID}">Update</a>
                        </div>
                    </td>
                    </c:if>
                </c:forEach>
            </tbody>
            </table>
        </c:otherwise>
        
    </body>
</html>
