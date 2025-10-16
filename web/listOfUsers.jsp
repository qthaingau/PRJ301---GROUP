<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8" />
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <title>List of Users</title>

        <!-- Bootstrap CSS -->
        <link
            href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"
            rel="stylesheet"
            />

        <!-- Custom CSS (tuỳ chọn) -->
        <style>
            /* Căn giữa theo chiều dọc cho các ô */
            table td, table th {
                vertical-align: middle;
            }
            /* Nhẹ nhàng hơn cho tiêu đề cột */
            thead th {
                font-weight: 600;
            }

            /* Styling the delete button */
            .delete-btn {
                color: #dc3545; /* Red color */
                text-decoration: none;
                font-weight: bold;
            }

            .delete-btn:hover {
                color: #bd2130; /* Darker red when hover */
                text-decoration: underline;
            }
        </style>
    </head>
    <body class="bg-light">

        <div class="container py-4">
            <h1 class="mb-4 text-primary">List of Users</h1>

            <!-- Form Search -->
            <form action="MainController" method="post" class="row g-2 mb-4">
                <input type="hidden" name="txtAction" value="searchUser"/>

                <div class="col-12 col-md-6">
                    <input
                        type="text"
                        name="txtName"
                        class="form-control"
                        placeholder="Enter name"
                        value="${name}"
                        />
                </div>

                <div class="col-12 col-md-auto">
                    <button type="submit" class="btn btn-primary">
                        Search
                    </button>
                </div>
            </form>

            <!-- Kết quả -->
            <div class="card shadow-sm">
                <div class="card-body p-0">
                    <div class="table-responsive">
                        <table class="table table-striped table-hover mb-0 align-middle">
                            <thead class="table-light">
                                <tr>
                                    <th>UserID</th>
                                    <th>Full Name</th>
                                    <th>Role</th>
                                    <th>Status</th>
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="u" items="${listOfUsers}">
                                    <tr>
                                        <td>${u.userName}</td>
                                        <td>${u.fullName}</td>
                                        <td>
                                            <span class="badge text-bg-secondary">
                                                ${u.role}
                                            </span>
                                        </td>
                                        <td>
                                            <c:choose>
                                                <c:when test="${u.active}">
                                                    <span class="badge text-bg-success">Active</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge text-bg-danger">Inactive</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td><a href="MainController?txtAction=callUpdateUser&uid=${u.userName}">Update</a> | 
                                            <a href="MainController?txtAction=deleteUser&uid=${u.userName}" class="delete-btn" onclick="return confirmDelete('${u.fullName}')">Delete</a></td>
                                    </tr>
                                </c:forEach>

                                <!-- Trường hợp rỗng -->
                                <c:if test="${empty listOfUsers}">
                                    <tr>
                                        <td colspan="4" class="text-center py-4">
                                            <div class="text-muted">No users found.</div>
                                        </td>
                                    </tr>
                                </c:if>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>

        <!-- Bootstrap JS (Bundle đã gồm Popper) -->
        <script
            src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js">
        </script>

        <!-- JavaScript confirmation before delete -->
        <script>
            function confirmDelete() {
                return confirm("Are you sure you want to delete this user?");
            }
        </script>

    </body>
</html>
