<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>List of Orders</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">

<div class="container mt-5">
    <div class="card shadow">
        <div class="card-header bg-primary text-white d-flex justify-content-between align-items-center">
            <h4 class="mb-0">List of Orders</h4>
            <button class="btn btn-light btn-sm" id="refreshOrderBtn">🔄 Refresh</button>
        </div>
        <div class="card-body">
            <table class="table table-bordered table-hover text-center align-middle">
                <thead class="table-dark">
                    <tr>
                        <th>Order ID</th>
                        <th>Customer ID</th>
                        <th>Order Date</th>
                        <th>Total Amount</th>
                    </tr>
                </thead>
                <tbody id="orderTableBody">
                    <c:forEach var="o" items="${orderList}">
                        <tr>
                            <td>${o.orderID}</td>
                            <td>${o.customerID}</td>
                            <td>${o.orderDate}</td>
                            <td>${o.totalAmount}</td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty orderList}">
                        <tr>
                            <td colspan="4" class="text-muted">No orders found</td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>
</div>

<!-- Bootstrap + JS riêng -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="js/order.js"></script> 
</body>
</html>
