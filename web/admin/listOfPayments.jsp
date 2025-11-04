<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>List of Payments</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">

<div class="container mt-5">
    <div class="card shadow">
        <div class="card-header bg-success text-white d-flex justify-content-between align-items-center">
            <h4 class="mb-0">List of Payments</h4>
            <button class="btn btn-light btn-sm" id="refreshPaymentBtn">🔄 Refresh</button>
        </div>
        <div class="card-body">
            <table class="table table-bordered table-hover text-center align-middle">
                <thead class="table-dark">
                    <tr>
                        <th>Payment ID</th>
                        <th>Order ID</th>
                        <th>Payment Date</th>
                        <th>Amount</th>
                        <th>Method</th>
                    </tr>
                </thead>
                <tbody id="paymentTableBody">
                    <c:forEach var="p" items="${paymentList}">
                        <tr>
                            <td>${p.paymentID}</td>
                            <td>${p.orderID}</td>
                            <td>${p.paymentDate}</td>
                            <td>${p.amount}</td>
                            <td>${p.paymentMethod}</td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty paymentList}">
                        <tr>
                            <td colspan="5" class="text-muted">No payments found</td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
    </div>
</div>

<!-- Bootstrap + JS riêng -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="js/payment.js"></script> 
</body>
</html>
