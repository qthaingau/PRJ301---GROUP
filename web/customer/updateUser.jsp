<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!doctype html>
<html>
<head>
    <meta charset="utf-8"/>
    <title>Update user</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet"/>
</head>
<body class="bg-light">
<div class="container py-4">
    <h2 class="mb-4">Update user</h2>

    <c:if test="${not empty msg}">
        <div class="alert alert-info">${msg}</div>
    </c:if>

    <form action="MainController" method="post">
        <input type="hidden" name="txtAction" value="updateUser"/>
        <input type="hidden" name="userID" value="${userToEdit.userID}"/>

        <div class="mb-3">
            <label class="form-label">Username</label>
            <input type="text" class="form-control" value="${userToEdit.username}" disabled/>
        </div>

        <div class="mb-3">
            <label class="form-label">Full name</label>
            <input type="text" name="fullName" class="form-control" value="${userToEdit.fullName}" required/>
        </div>

        <div class="mb-3">
            <label class="form-label">Email</label>
            <input type="email" name="email" class="form-control" value="${userToEdit.email}" required/>
        </div>

        <div class="mb-3">
            <label class="form-label">Phone</label>
            <input type="text" name="phoneNumber" class="form-control" value="${userToEdit.phoneNumber}" />
        </div>

        <div class="mb-3">
            <label class="form-label">Role</label>
            <select name="role" class="form-select">
                <option value="customer" ${userToEdit.role == 'customer' ? 'selected' : ''}>Customer</option>
                <option value="staff" ${userToEdit.role == 'staff' ? 'selected' : ''}>Staff</option>
                <option value="admin" ${userToEdit.role == 'admin' ? 'selected' : ''}>Admin</option>
            </select>
        </div>

        <div class="form-check mb-3">
            <input class="form-check-input" type="checkbox" id="activeCheck" name="active" 
                   ${userToEdit.active ? 'checked' : ''}>
            <label class="form-check-label" for="activeCheck">Active</label>
        </div>

        <button type="submit" class="btn btn-primary">Save</button>
        <a href="MainController?txtAction=searchUser" class="btn btn-secondary ms-2">Cancel</a>
    </form>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
