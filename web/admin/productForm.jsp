<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>${update ? "Update Product" : "Add New Product"}</title>
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

                <!-- Category ID -->
                Category ID (C***):
                <input type="text"
                       name="txtCategoryID"
                       value="${p.categoryID}"
                       required
                       pattern="[Cc][0-9]{3}"
                       title="Category ID must follow the format C***, e.g., C001"/><br/>
                <c:if test="${not empty error_categoryID}">
                    <span style="color:red">${error_categoryID}</span><br/>
                </c:if>
                <br/>

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


                <!-- ================= VARIANT ================= -->

                <h3>Variant</h3>

                <!-- Variant ID -->
                Variant ID (V***):
                <input type="text"
                       name="txtVariantID"
                       value="${v.variantID}"
                       required
                       pattern="[Vv][0-9]{3}"
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

    </body>
</html>
