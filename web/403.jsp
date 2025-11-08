<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
  <title>403 - Access Denied</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet"/>
</head>
<body class="text-center bg-light py-5">
  <h1 class="text-danger">403 - Bạn không có quyền truy cập trang này</h1>
  <a href="<%= request.getContextPath() %>/index.jsp" class="btn btn-primary mt-3">Quay lại trang chủ</a>
</body>
</html>
