<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Thanh toán</title>
        <link href="assets/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body class="bg-light">
        <%@ include file="../includes/header.jspf" %>

        <div class="container mt-5">
            <h2 class="mb-4">Xác nhận đơn hàng</h2>

            <c:if test="${empty sessionScope.user}">
                <div class="alert alert-warning">
                    Vui lòng <a href="MainController?txtAction=login">đăng nhập</a> để thanh toán!
                </div>
            </c:if>

            <c:if test="${not empty sessionScope.user}">
                <form action="MainController" method="post">
                    <input type="hidden" name="txtAction" value="checkout">

                    <!-- DANH SÁCH SẢN PHẨM -->
                    <table class="table table-bordered">
                        <thead>
                            <tr><th>Sản phẩm</th><th>Size</th><th>Màu</th><th>SL</th><th>Giá</th><th>Tổng</th></tr>
                        </thead>
                        <tbody>
                            <c:set var="total" value="0"/>
                            <c:forEach var="item" items="${sessionScope.cart}">
                                <tr>
                                    <td>${item.productName}</td>
                                    <td>${item.size}</td>
                                    <td>${item.color}</td>
                                    <td>${item.quantity}</td>
                                    <td><fmt:formatNumber value="${item.price}" pattern="#,##0"/>₫</td>
                                    <td><fmt:formatNumber value="${item.price * item.quantity}" pattern="#,##0"/>₫</td>
                                    <c:set var="total" value="${total + (item.price * item.quantity)}"/>
                                </tr>
                            </c:forEach>
                        </tbody>
                        <tfoot>
                            <tr class="table-success">
                                <th colspan="5" class="text-end">Tổng cộng:</th>
                                <th><fmt:formatNumber value="${total}" pattern="#,##0"/>₫</th>
                            </tr>
                        </tfoot>
                    </table>

                    <!-- CHỌN ĐỊA CHỈ -->
                    <div class="mb-3">
                        <label class="form-label"><strong>Địa chỉ giao hàng</strong></label>
                        <select name="addressID" class="form-select" required>
                            <c:forEach var="addr" items="${requestScope.addresses}">
                                <option value="${addr.addressID}">${addr.recipientName} - ${addr.street}, ${addr.ward}, ${addr.district}, ${addr.city}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <!-- PHƯƠNG THỨC THANH TOÁN -->
                    <div class="mb-3">
                        <label class="form-label"><strong>Phương thức thanh toán</strong></label>
                        <div class="form-check">
                            <input type="radio" name="paymentMethod" value="COD" class="form-check-input" checked>
                            <label class="form-check-label">Thanh toán khi nhận hàng (COD)</label>
                        </div>
                    </div>

                    <button type="submit" class="btn btn-success btn-lg">XÁC NHẬN ĐẶT HÀNG</button>
                    <a href="MainController?txtAction=viewCart" class="btn btn-secondary btn-lg">Quay lại giỏ</a>
                </form>
            </c:if>
        </div>

        <%@ include file="../includes/footer.jspf" %>
    </body>
</html>