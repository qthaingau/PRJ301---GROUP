<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>${update ? "Update Product" : "Add New Product"}</title>

        <!-- Gọi JavaScript riêng -->
        <!--<script src="assets/js/productImage.js"></script>-->
    </head>
    <body>

        <div>
            <h2>${update ? "Update Product": "Add New Product"}</h2>

            <!-- General error message -->
            <c:if test="${not empty error}">
                <p style="color:red">${error}</p>
            </c:if>

            <form action="MainController" method="post">
                <!-- Action tuỳ theo add/update -->
                <input type="hidden" name="txtAction"
                       value="${update ? 'updateProductWithVariant' : 'addProductWithVariant'}"/>

                <!-- Gửi lại cờ update cho servlet -->
                <input type="hidden" name="update" value="${update}" />

                <!-- ================= PRODUCT ================= -->
                <h3>Product</h3>

                <!-- Product ID -->
                Product ID (P***):
                <input type="text"
                       name="txtProductID"
                       value="${p.productID}" ${update ? "readonly" : "required"}
                       required
                       pattern="[Pp][0-9]{3}"
                       title="Product ID must follow the format P***, e.g., P001"
                       ${update ? 'readonly="readonly"' : ''} /><br/>
                <c:if test="${not empty error_productID}">
                    <span style="color:red">${error_productID}</span><br/>
                </c:if>
                <br/>

                <!-- Product Name -->
                Product Name:
                <input type="text"
                       name="txtProductName"
                       value="${p.productName}"
                       required/><br/>
                <c:if test="${not empty error_productName}">
                    <span style="color:red">${error_productName}</span><br/>
                </c:if>
                <br/>

                <!-- Description -->
                Description:<br/>
                <textarea name="txtDescription" required>${p.description}</textarea><br/>
                <c:if test="${not empty error_description}">
                    <span style="color:red">${error_description}</span><br/>
                </c:if>
                <br/>

                <!-- ================= CATEGORY (DROPDOWN) ================= -->
                Category:
                <select name="txtCategoryID" required>
                    <option value="">-- Select Category --</option>

                    <c:forEach var="c" items="${categoryList}">
                        <%-- xác định selected bằng biến tạm cho dễ debug --%>
                        <c:set var="selected" value=""/>

                        <c:if test="${not empty p && p.categoryID eq c.categoryID}">
                            <c:set var="selected" value="selected='selected'"/>
                        </c:if>

                        <option value="${c.categoryID}" ${selected}>
                            ${c.categoryID} - ${c.categoryName}
                        </option>
                    </c:forEach>
                </select><br/>
                <c:if test="${not empty error_categoryID}">
                    <span style="color:red">${error_categoryID}</span><br/>
                </c:if>


                <!-- ================= BRAND (DROPDOWN) ================= -->
                Brand:
                <select name="txtBrandID" required>
                    <option value="">-- Select Brand --</option>

                    <c:forEach var="b" items="${brandList}">
                        <%-- xác định selected bằng biến tạm cho dễ debug --%>
                        <c:set var="selected" value=""/>

                        <c:if test="${not empty p && p.brandID eq b.brandID}">
                            <c:set var="selected" value="selected='selected'"/>
                        </c:if>

                        <option value="${b.brandID}" ${selected}>
                            ${b.brandID} - ${b.brandName}
                            <c:if test="${not empty b.origin}"> (${b.origin})</c:if>
                            </option>
                    </c:forEach>
                </select><br/>
                <c:if test="${not empty error_brandID}">
                    <span style="color:red">${error_brandID}</span><br/>
                </c:if>
                <br/>

                <!-- Avatar Upload -->
                <div class="mb-3">
                    <label class="form-label">Avatar</label>
                    <input type="file" id="productImageFile" accept="image/*" class="form-control" />
                    <!-- Trường ẩn để lưu base64 gửi lên server -->
                    <input type="hidden" name="txtProductImage" id="productImage" value="${p.productImage}" />

                    <!-- Xem trước ảnh -->
                    <div class="mt-3">
                        <img id="productPreview" 
                             src="${not empty p.productImage ? p.productImage : ''}" 
                             alt="Avatar Preview" 
                             class="rounded border" 
                             style="max-width: 150px; max-height: 150px; display: ${not empty p.productImage ? 'block' : 'none'};">
                    </div>
                </div>

                <!-- ================= VARIANT ================= -->

                <h3>Variant</h3>

                <!-- Variant ID -->
                Variant ID (V***):
                <input type="text"
                       name="txtVariantID"
                       value="${v.variantID}"
                       required
                       pattern="(?i)[Vv][0-9]{3}"
                       title="Variant ID must follow the format V***, e.g., V001"
                       ${update ? 'readonly="readonly"' : ''} /><br/>
                <c:if test="${not empty error_variantID}">
                    <span style="color:red">${error_variantID}</span><br/>
                </c:if>
                <br/>

                <!-- Size -->
                Size:
                <input type="text"
                       name="txtSize"
                       value="${v.size}"
                       required/><br/>
                <c:if test="${not empty error_size}">
                    <span style="color:red">${error_size}</span><br/>
                </c:if>
                <br/>

                <!-- Color -->
                Color:
                <input type="text"
                       name="txtColor"
                       value="${v.color}"
                       required/><br/>
                <c:if test="${not empty error_color}">
                    <span style="color:red">${error_color}</span><br/>
                </c:if>
                <br/>

                <!-- Stock -->
                Stock Quantity:
                <input type="number"
                       name="txtStock"
                       value="${v.stock}"
                       min="0"
                       required/><br/>
                <c:if test="${not empty error_stock}">
                    <span style="color:red">${error_stock}</span><br/>
                </c:if>
                <br/>

                <!-- Price -->
                Price:
                <input type="number"
                       name="txtPrice"
                       value="${v.price}"
                       step="0.01"
                       min="0"
                       required/><br/>
                <c:if test="${not empty error_price}">
                    <span style="color:red">${error_price}</span><br/>
                </c:if>
                <br/>

                <button type="submit">
                    ${update ? "Update Product" : "Add Product"}
                </button>
            </form>

        </div>
                 <!--JavaScript: convert ảnh sang base64--> 
<script>
document.getElementById('productImageFile').addEventListener('change', function() {
    const file = this.files[0];
    if (!file) return;

    const reader = new FileReader();
    reader.onload = function(e) {
        const base64String = e.target.result; // chứa cả header data:image/png;base64,...
        document.getElementById('productImage').value = base64String;

        // Hiển thị xem trước
        const imgPreview = document.getElementById('productPreview');
        imgPreview.src = base64String;
        imgPreview.style.display = 'block';
    };
    reader.readAsDataURL(file); // đọc file dưới dạng Base64
});
</script>

    </body>
</html>
