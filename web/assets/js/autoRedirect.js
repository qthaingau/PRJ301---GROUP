// Only runs when home.jsp is loaded without productList
window.onload = function () {
    var form = document.getElementById("viewProductsForm");
    if (form) {
        form.submit();
    }
};
