<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="models.OrderDTO"%>
<%@page import="java.util.ArrayList"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>List of Orders</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="css/list.css">
</head>
<body>
    <div class="container mt-4">
        <h2>📦 Danh sách đơn hàng</h2>

        <input type="text" id="searchOrder" class="form-control search-input mb-3"
               placeholder="🔍 Tìm kiếm đơn hàng..." data-table="orderTable">

        <table id="orderTable" class="table table-hover table-bordered">
            <thead>
                <tr>
                    <th>Order ID</th>
                    <th>Customer ID</th>
                    <th>Order Date</th>
                    <th>Total Amount</th>
                    <th>Status</th>
                </tr>
            </thead>
            <tbody>
                <%
                    ArrayList<OrderDTO> list = (ArrayList<OrderDTO>) request.getAttribute("orderList");
                    if (list != null && !list.isEmpty()) {
                        for (OrderDTO o : list) {
                %>
                    <tr>
                        <td><%= o.getOrderID() %></td>
                        <td><%= o.getUserName() %></td>
                        <td><%= o.getOrderDate() %></td>
                        <td><%= o.getTotalAmount() %></td>
                        <td><%= o.getStatus() %></td>
                    </tr>
                <%
                        }
                    } else {
                %>
                    <tr><td colspan="5" class="text-center">Không có đơn hàng nào</td></tr>
                <%
                    }
                %>
            </tbody>
        </table>
    </div>

    <script src="js/list.js"></script>
</body>
</html>
