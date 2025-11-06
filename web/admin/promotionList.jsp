<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Danh sách khuyến mãi</title>
    <style>
        body { font-family: Arial, sans-serif; background: #f8fafc; margin: 0; padding: 24px; }
        h1 { margin-bottom: 16px; }
        .btn { display: inline-block; padding: 8px 16px; background: #2563eb; color: #fff; text-decoration: none; border-radius: 4px; font-weight: 600; }
        .btn-danger { background: #dc2626; }
        table { width: 100%; border-collapse: collapse; margin-top: 16px; background: #fff; box-shadow: 0 1px 3px rgba(0,0,0,0.1); }
        th, td { padding: 12px; border-bottom: 1px solid #e2e8f0; text-align: left; }
        th { background: #e2e8f0; }
        .badge { display: inline-block; padding: 4px 8px; border-radius: 999px; font-size: 12px; }
        .badge-active { background: #dcfce7; color: #15803d; }
        .badge-inactive { background: #fee2e2; color: #b91c1c; }
        .alert { padding: 12px 16px; border-radius: 4px; margin-bottom: 16px; }
        .alert-success { background: #e6ffed; color: #14532d; border: 1px solid #86efac; }
        .alert-error { background: #fee2e2; color: #7f1d1d; border: 1px solid #fca5a5; }
        .actions { display: flex; gap: 8px; }
        form { margin: 0; }
    </style>
</head>
<body>
    <h1>Danh sách khuyến mãi</h1>
    <a class="btn" href="PromotionController?txtAction=callAddPromotion">Thêm khuyến mãi</a>

    <c:if test="${not empty SUCCESS_MESSAGE}">
        <div class="alert alert-success">${SUCCESS_MESSAGE}</div>
    </c:if>
    <c:if test="${not empty ERROR_MESSAGE}">
        <div class="alert alert-error">${ERROR_MESSAGE}</div>
    </c:if>

    <c:if test="${empty PROMOTION_LIST}">
        <p>Chưa có khuyến mãi nào.</p>
    </c:if>

    <c:if test="${not empty PROMOTION_LIST}">
        <table>
            <thead>
                <tr>
                    <th>Mã</th>
                    <th>Tên khuyến mãi</th>
                    <th>Mô tả</th>
                    <th>Giảm (%)</th>
                    <th>Bắt đầu</th>
                    <th>Kết thúc</th>
                    <th>Trạng thái</th>
                    <th>Hành động</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${PROMOTION_LIST}" var="promo">
                    <tr>
                        <td>${promo.discountID}</td>
                        <td>${promo.discountName}</td>
                        <td>${promo.description}</td>
                        <td>${promo.discountPercent}</td>
                        <td><c:out value="${promo.startDate}"/></td>
                        <td><c:out value="${promo.endDate}"/></td>
                        <td>
                            <span class="badge ${promo.isActive ? 'badge-active' : 'badge-inactive'}">
                                <c:choose>
                                    <c:when test="${promo.isActive}">Đang áp dụng</c:when>
                                    <c:otherwise>Ngừng</c:otherwise>
                                </c:choose>
                            </span>
                        </td>
                        <td class="actions">
                            <a class="btn" href="PromotionController?txtAction=callUpdatePromotion&discountID=${promo.discountID}">Sửa</a>
                            <form method="post" action="PromotionController" onsubmit="return confirm('Xóa khuyến mãi này?');">
                                <input type="hidden" name="txtAction" value="deletePromotion">
                                <input type="hidden" name="discountID" value="${promo.discountID}">
                                <button type="submit" class="btn btn-danger">Xóa</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </c:if>
</body>
</html>
