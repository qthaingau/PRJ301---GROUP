<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>${update ? "Update Product" : "Add New Product"}</title>
        <style>
            .form-container {
                max-width: 600px;
                margin: auto;
                padding: 20px;
            }
            .mb-3 {
                margin-bottom: 1rem;
            }
            .form-label {
                font-weight: bold;
            }
            img.preview {
                max-width: 150px;
                max-height: 150px;
                object-fit: cover;
                border: 1px solid #ddd;
                border-radius: 5px;
                margin-top: 10px;
            }
            .error {
                color: red;
                font-size: 0.9em;
            }
        </style>
    </head>
    <body>
        <div class="form-container">
            <h2>${update ? "Update Product" : "Add New Product"}</h2>

            <!-- General error -->
            <c:if test="${not empty error}">
                <p class="error">${error}</p>
            </c:if>

            <form action="MainController" method="post" enctype="multipart/form-data">
                <input type="hidden" name="txtAction"
                       value="${update ? 'updateProductWithVariant' : 'addProductWithVariant'}"/>
                <input type="hidden" name="update" value="${update}" />

                <!-- ================= PRODUCT ================= -->
                <h3>Product</h3>

                <!-- Product ID -->
                <div class="mb-3">
                    <label>Product ID (P***):</label>
                    <input type="text" name="txtProductID" value="${p.productID}"
                           pattern="[Pp][0-9]{3}" title="e.g., P001"
                           ${update ? 'readonly' : 'required'} />
                    <c:if test="${not empty error_productID}">
                        <span class="error">${error_productID}</span>
                    </c:if>
                </div>

                <!-- Product Name -->
                <div class="mb-3">
                    <label>Product Name:</label>
                    <input type="text" name="txtProductName" value="${p.productName}" required />
                    <c:if test="${not empty error_productName}">
                        <span class="error">${error_productName}</span>
                    </c:if>
                </div>

                <!-- Description -->
                <div class="mb-3">
                    <label>Description:</label>
                    <textarea name="txtDescription" required>${p.description}</textarea>
                    <c:if test="${not empty error_description}">
                        <span class="error">${error_description}</span>
                    </c:if>
                </div>

                <!-- Category -->
                <div class="mb-3">
                    <label>Category:</label>
                    <select name="txtCategoryID" required>
                        <option value="">-- Select Category --</option>
                        <c:forEach var="c" items="${categoryList}">
                            <option value="${c.categoryID}" ${p.categoryID eq c.categoryID ? 'selected' : ''}>
                                ${c.categoryID} - ${c.categoryName}
                            </option>
                        </c:forEach>
                    </select>
                    <c:if test="${not empty error_categoryID}">
                        <span class="error">${error_categoryID}</span>
                    </c:if>
                </div>

                <!-- Brand -->
                <div class="mb-3">
                    <label>Brand:</label>
                    <select name="txtBrandID" required>
                        <option value="">-- Select Brand --</option>
                        <c:forEach var="b" items="${brandList}">
                            <option value="${b.brandID}" ${p.brandID eq b.brandID ? 'selected' : ''}>
                                ${b.brandID} - ${b.brandName} <c:if test="${not empty b.origin}">(${b.origin})</c:if>
                                </option>
                        </c:forEach>
                    </select>
                    <c:if test="${not empty error_brandID}">
                        <span class="error">${error_brandID}</span>
                    </c:if>
                </div>

                <!-- Product Image -->
                <div class="mb-3">
                    <label class="form-label">Product Image</label>
                    <input type="file" id="productImageFile" accept="image/*" class="form-control" />
                    <input type="hidden" name="txtProductImage" id="productImage" value="${p.productImage}" />
                    <div class="mt-2">
                        <img id="productPreview"
                             src="${not empty p.productImage ? p.productImage : ''}"
                             alt="Product Preview"
                             class="preview"
                             style="display: ${not empty p.productImage ? 'block' : 'none'};">
                    </div>
                </div>

                <!-- ================= VARIANT ================= -->
                <h3>Variant</h3>

                <!-- Variant ID -->
                <div class="mb-3">
                    <label>Variant ID (V***):</label>
                    <input type="text" name="txtVariantID" value="${v.variantID}"
                           pattern="(?i)[Vv][0-9]{3}" title="e.g., V001"
                           ${update ? 'readonly' : 'required'} />
                    <c:if test="${not empty error_variantID}">
                        <span class="error">${error_variantID}</span>
                    </c:if>
                </div>

                <!-- Existing Variant IDs -->
                <div class="mb-3">
                    <strong>Existing Variant IDs:</strong>
                    <c:forEach var="vItem" items="${variantList}" varStatus="loop">
                        <span class="badge bg-secondary">${vItem.variantID}</span><c:if test="${!loop.last}"> </c:if>
                    </c:forEach>
                    <c:if test="${empty variantList}">
                        <em>(None)</em>
                    </c:if>
                </div>

                <!-- Size -->
                <div class="mb-3">
                    <label>Size:</label>
                    <input type="text" name="txtSize" value="${v.size}" required />
                    <c:if test="${not empty error_size}">
                        <span class="error">${error_size}</span>
                    </c:if>
                </div>

                <!-- Color -->
                <div class="mb-3">
                    <label>Color:</label>
                    <input type="text" name="txtColor" value="${v.color}" required />
                    <c:if test="${not empty error_color}">
                        <span class="error">${error_color}</span>
                    </c:if>
                </div>

                <!-- Stock -->
                <div class="mb-3">
                    <label>Stock:</label>
                    <input type="number" name="txtStock" value="${v.stock}" min="0" required />
                    <c:if test="${not empty error_stock}">
                        <span class="error">${error_stock}</span>
                    </c:if>
                </div>

                <!-- Price -->
                <div class="mb-3">
                    <label>Price:</label>
                    <input type="number" name="txtPrice" value="${v.price}" step="0.01" min="0" required />
                    <c:if test="${not empty error_price}">
                        <span class="error">${error_price}</span>
                    </c:if>
                </div>

                <!-- Variant Image (ĐÃ SỬA – ĐẸP, CHUẨN BOOTSTRAP) -->
                <div class="mb-3">
                    <label class="form-label">Variant Image</label>
                    <input type="file" id="variantImageFile" accept="image/*" class="form-control" />
                    <input type="hidden" name="txtVariantImage" id="variantImage" value="${v.avatarBase64}" />
                    <div class="mt-2">
                        <img id="variantPreview"
                             src="${not empty v.avatarBase64 ? v.avatarBase64 : ''}"
                             alt="Variant Preview"
                             class="preview"
                             style="display: ${not empty v.avatarBase64 ? 'block' : 'none'};">
                    </div>
                </div>

                <!-- Submit -->
                <button type="submit" class="btn btn-primary">
                    ${update ? "Update Product" : "Add Product"}
                </button>
            </form>
        </div>

        <!-- ====================== JAVASCRIPT ====================== -->
        <script>
            // --- Product Image Preview ---
            document.getElementById('productImageFile').addEventListener('change', function () {
                const file = this.files[0];
                if (!file)
                    return;
                const reader = new FileReader();
                reader.onload = function (e) {
                    const base64 = e.target.result;
                    document.getElementById('productImage').value = base64;
                    const preview = document.getElementById('productPreview');
                    preview.src = base64;
                    preview.style.display = 'block';
                };
                reader.readAsDataURL(file);
            });

            // --- Variant Image Preview ---
            document.getElementById('variantImageFile').addEventListener('change', function () {
                const file = this.files[0];
                if (!file)
                    return;
                const reader = new FileReader();
                reader.onload = function (e) {
                    const base64 = e.target.result;
                    document.getElementById('variantImage').value = base64;
                    const preview = document.getElementById('variantPreview');
                    preview.src = base64;
                    preview.style.display = 'block';
                };
                reader.readAsDataURL(file);
            });

            // --- Variant ID Duplication Check ---
            const existingVariantIDs = [
            <c:forEach var="vItem" items="${variantList}" varStatus="loop">
            "${vItem.variantID.toUpperCase()}"<c:if test="${!loop.last}">,</c:if>
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