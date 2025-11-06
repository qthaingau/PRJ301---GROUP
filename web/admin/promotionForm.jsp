<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>${FORM_TITLE}</title>
    <style>
        body { font-family: Arial, sans-serif; background: #f8fafc; margin: 0; padding: 24px; }
        form { background: #fff; padding: 24px; border-radius: 8px; max-width: 640px; margin: 0 auto; box-shadow: 0 1px 3px rgba(0,0,0,0.1); }
        h1 { text-align: center; margin-bottom: 16px; }
        label { display: block; margin-bottom: 6px; font-weight: 600; }
        input[type="text"], input[type="number"], input[type="date"], textarea { width: 100%; padding: 10px; border: 1px solid #cbd5f5; border-radius: 4px; margin-bottom: 16px; }
        textarea { min-height: 100px; resize: vertical; }
        .checkbox { display: flex; align-items: center; gap: 8px; margin-bottom: 16px; }
        .btn { display: inline-block; padding: 10px 18px; border: none; border-radius: 4px; font-weight: 600; cursor: pointer; }
        .btn-primary { background: #2563eb; color: #fff; }
        .btn-secondary { background: #6b7280; color: #fff; }
        .actions { display: flex; justify-content: flex-end; gap: 12px; }
        .alert { padding: 12px 16px; border-radius: 4px; margin-bottom: 16px; }
        .alert-error { background: #fee2e2; color: #7f1d1d; border: 1px solid #fca5a5; }
    </style>
</head>
<body>
    <form action="PromotionController" method="post">
        <h1>${FORM_TITLE}</h1>

        <c:if test="${not empty ERROR_MESSAGE}">
            <div class="alert alert-error">${ERROR_MESSAGE}</div>
        </c:if>

        <input type="hidden" name="txtAction" value="${FORM_ACTION}">

        <label for="discountID">Mã khuyến mãi</label>
        <input type="text" id="discountID" name="discountID" value="${PROMOTION.discountID}" ${FORM_ACTION == 'updatePromotion' ? 'readonly' : ''} required>

        <label for="discountName">Tên khuyến mãi</label>
        <input type="text" id="discountName" name="discountName" value="${PROMOTION.discountName}" required>

        <label for="description">Mô tả</label>
        <textarea id="description" name="description">${PROMOTION.description}</textarea>

        <label for="discountPercent">Giảm giá (%)</label>
        <input type="number" step="0.01" min="0" id="discountPercent" name="discountPercent" value="${PROMOTION.discountPercent}" required>

        <label for="startDate">Ngày bắt đầu</label>
        <input type="date" id="startDate" name="startDate" value="${PROMOTION.startDate}">

        <label for="endDate">Ngày kết thúc</label>
        <input type="date" id="endDate" name="endDate" value="${PROMOTION.endDate}">

        <div class="checkbox">
            <input type="checkbox" id="isActive" name="isActive" value="true" ${PROMOTION.isActive ? 'checked' : ''}>
            <label for="isActive" style="margin: 0; font-weight: normal;">Đang áp dụng</label>
        </div>

        <div class="actions">
            <a class="btn btn-secondary" href="PromotionController?txtAction=viewPromotions">Hủy</a>
            <button type="submit" class="btn btn-primary">Lưu</button>
        </div>
    </form>
</body>
</html>
