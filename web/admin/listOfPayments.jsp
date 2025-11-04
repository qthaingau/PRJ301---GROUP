<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="models.PaymentDTO"%>
<%@page import="java.util.ArrayList"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>List of Payments</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="css/list.css">
</head>
<body>
    <div class="container mt-4">
        <h2>💰 Danh sách thanh toán</h2>

        <input type="text" id="searchPayment" class="form-control search-input mb-3"
               placeholder="🔍 Tìm kiếm thanh toán..." data-table="paymentTable">

        <table id="paymentTable" class="table table-hover table-bordered">
            <thead>
                <tr>
                    <th>Payment ID</th>
                    <th>Order ID</th>
                    <th>Amount</th>
                    <th>Payment Date</th>
                    <th>Method</th>
                    <th>Status</th>
                </tr>
            </thead>
            <tbody>
                <%
                    ArrayList<PaymentDTO> list = (ArrayList<PaymentDTO>) request.getAttribute("paymentList");
                    if (list != null && !list.isEmpty()) {
                        for (PaymentDTO p : list) {
                %>
                    <tr>
                        <td><%= p.getPaymentID() %></td>
                        <td><%= p.getOrderID() %></td>
                        <td><%= p.getAmount() %></td>
                        <td><%= p.getPaymentDate() %></td>
                        <td><%= p.getPaymentMethod() %></td>
                        <td><%= p.getPaymentStatus() %></td>
                    </tr>
                <%
                        }
                    } else {
                %>
                    <tr><td colspan="6" class="text-center">Không có thanh toán nào</td></tr>
                <%
                    }
                %>
            </tbody>
        </table>
    </div>

    <script src="js/list.js"></script>
</body>
</html>
