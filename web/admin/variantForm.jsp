<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${update ? "Update Variant" : "Add New Variant"}</title>

    <!-- Dùng chung layout admin gradient -->
    <link rel="stylesheet" href="assets/css/adminTable.css">
</head>
<body class="brand-list-body">

<div class="container brand-form-wrapper">
    <h3 class="product-list-title">
        ${update ? "Update Variant" : "Add New Variant"}
    </h3>

    <!-- General error -->
    <c:if test="${not empty error}">
        <div class="alert alert-danger text-center">
            ${error}
        </div>
    </c:if>

    <form action="MainController" method="post" enctype="multipart/form-data" class="category-form">
        <input type="hidden" name="txtAction"
               value="${update ? 'updateVariant' : 'addVariant'}" />
        <input type="hidden" name="update" value="${update}" />

        <!-- ================= VARIANT ================= -->
        <h4 class="mb-3 mt-4">Variant</h4>

        <!-- Variant ID -->
        <div class="mb-3">
            <label class="form-label">Variant ID (PV***):</label>
            <input type="text"
                   name="txtVariantID"
                   class="form-control"
                   value="${v.variantID}"
                   pattern="[Pp][Vv][0-9]{3}"
                   title="e.g., PV001"
                   ${update ? 'readonly' : 'required'} />
            <c:if test="${not empty error_variantID}">
                <small class="text-danger">${error_variantID}</small>
            </c:if>
        </div>

        <!-- Existing Variant IDs -->
        <div class="mb-3">
            <label class="form-label">Existing Variant IDs:</label><br/>
            <c:forEach var="vItem" items="${variantList}" varStatus="loop">
                <span class="badge bg-secondary">${vItem.variantID}</span>
                <c:if test="${!loop.last}"> </c:if>
            </c:forEach>
            <c:if test="${empty variantList}">
                <em class="text-muted">(None)</em>
            </c:if>
        </div>

        <!-- ================= PRODUCT ================= -->
        <h4 class="mb-3">Product</h4>

        <!-- Product ID -->
        <div class="mb-3">
            <label class="form-label">Product ID (P***):</label>
            <input type="text"
                   name="txtProductID"
                   class="form-control"
                   value="${p.productID}"
                   pattern="[Pp][0-9]{3}"
                   title="e.g., P001"
                   ${update ? 'readonly' : 'required'} />
            <c:if test="${not empty error_productID}">
                <small class="text-danger">${error_productID}</small>
            </c:if>
        </div>

        <!-- Size -->
        <div class="mb-3">
            <label class="form-label">Size:</label>
            <input type="text"
                   name="txtSize"
                   class="form-control"
                   value="${v.size}"
                   required />
            <c:if test="${not empty error_size}">
                <small class="text-danger">${error_size}</small>
            </c:if>
        </div>

        <!-- Color -->
        <div class="mb-3">
            <label class="form-label">Color:</label>
            <input type="text"
                   name="txtColor"
                   class="form-control"
                   value="${v.color}"
                   required />
            <c:if test="${not empty error_color}">
                <small class="text-danger">${error_color}</small>
            </c:if>
        </div>

        <!-- Stock -->
        <div class="mb-3">
            <label class="form-label">Stock:</label>
            <input type="number"
                   name="txtStock"
                   class="form-control"
                   value="${v.stock}"
                   min="0"
                   required />
            <c:if test="${not empty error_stock}">
                <small class="text-danger">${error_stock}</small>
            </c:if>
        </div>

        <!-- Price -->
        <div class="mb-3">
            <label class="form-label">Price:</label>
            <input type="number"
                   name="txtPrice"
                   class="form-control"
                   value="${v.price}"
                   step="0.01"
                   min="0"
                   required />
            <c:if test="${not empty error_price}">
                <small class="text-danger">${error_price}</small>
            </c:if>
        </div>

        <!-- Variant Image -->
        <div class="mb-3">
            <label class="form-label">Variant Image</label>
            <input type="file" id="variantImageFile" accept="image/*" class="form-control" />
            <input type="hidden" name="txtVariantImage" id="variantImage" value="${v.variantImage}" />
            <div class="mt-2">
                <img id="variantPreview"
                     src="${not empty v.variantImage ? v.variantImage : ''}"
                     alt="Variant Preview"
                     class="preview"
                     style="max-width:150px; max-height:150px; object-fit:cover;
                            border-radius:6px; border:1px solid #ddd;
                            display:${not empty v.variantImage ? 'block' : 'none'};">
            </div>
        </div>

        <!-- Submit + Back -->
        <div class="mt-4 d-flex gap-2">
            <button type="submit" class="btn btn-apply">
                ${update ? "Update Variant" : "Add Variant"}
            </button>

            <a href="MainController?txtAction=viewVariantList" class="btn btn-cancel">
                Back to List
            </a>
        </div>
    </form>
</div>

<!-- ====================== JAVASCRIPT ====================== -->
<script>
    // --- Variant Image Preview ---
    const variantImageInput = document.getElementById('variantImageFile');
    if (variantImageInput) {
        variantImageInput.addEventListener('change', function () {
            const file = this.files[0];
            if (!file) return;
            const reader = new FileReader();
            reader.onload = function (e) {
                const base64 = e.target.result;
                const hidden = document.getElementById('variantImage');
                const preview = document.getElementById('variantPreview');
                if (hidden) hidden.value = base64;
                if (preview) {
                    preview.src = base64;
                    preview.style.display = 'block';
                }
            };
            reader.readAsDataURL(file);
        });
    }

    // --- Variant ID Duplication Check ---
    const existingVariantIDs = [
        <c:forEach var="vItem" items="${variantList}" varStatus="loop">
            "${fn:toUpperCase(vItem.variantID)}"<c:if test="${!loop.last}">,</c:if>
        </c:forEach>
    ];

    document.addEventListener('DOMContentLoaded', function () {
        const input = document.querySelector('input[name="txtVariantID"]');
        if (input && !input.readOnly) {
            input.addEventListener('input', function () {
                const val = this.value.trim().toUpperCase();
                if (existingVariantIDs.includes(val)) {
                    this.setCustomValidity('Variant ID already exists!');
                    this.style.border = '2px solid red';
                } else {
                    this.setCustomValidity('');
                    this.style.border = '';
                }
            });
        }
    });
</script>

</body>
</html>
