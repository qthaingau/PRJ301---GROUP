<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html><head><title>Địa chỉ của bạn</title>
        <style>
            .addr {
                border:1px solid #ccc;
                padding:15px;
                margin:10px 0;
                border-radius:8px;
            }
            .default {
                background:#e3f2fd;
                border-left:5px solid #1976d2;
            }
            a {
                color:blue;
                text-decoration:none;
                margin:0 5px;
            }
        </style>
    </head><body>
        <h2>Quản lý địa chỉ</h2>
        <c:if test="${not empty param.msg}">
            <p style="color:green">${param.msg}</p>
        </c:if>

        <c:forEach var="a" items="${addresses}">
            <div class="addr ${a.isDefault ? 'default' : ''}">
                <strong>${a.recipientName}</strong> - ${a.phoneNumber}<br>
                ${a.getFullAddress()}
                ${a.isDefault ? ' <span style="color:green">(Mặc định)</span>' : ''}
                <div style="margin-top:8px;">
                    <a href="MainController?txtAction=setDefaultAddress&addressID=${a.addressID}">Đặt mặc định</a> |
                    <a href="MainController?txtAction=deleteAddress&addressID=${a.addressID}"
                       onclick="return confirm('Xóa địa chỉ này?')">Xóa</a>
                </div>
            </div>
        </c:forEach>

        <a href="MainController?txtAction=showAddAddressForm&returnUrl=${returnUrl}">+ Thêm địa chỉ mới</a>
        <br><br>
        <a href="${returnUrl != null ? returnUrl : 'MainController?txtAction=viewCart'}">Quay lại</a>
    </body></html>