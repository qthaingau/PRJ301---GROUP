<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="vi">
  <head>
    <meta charset="UTF-8" />
    <title>Đăng nhập</title>
    <meta name="viewport" content="width=device-width, initial-scale=1" />

    <!-- Bootstrap CSS -->
    <link
      href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
      rel="stylesheet"
      integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH"
      crossorigin="anonymous"
    />

    <!-- Custom CSS nhẹ -->
    <style>
      body {
        min-height: 100vh;
        display: grid;
        place-items: center;
        background: linear-gradient(135deg, #f8f9fa, #e9ecef);
      }
      .login-card {
        width: 100%;
        max-width: 420px;
        border-radius: 1rem;
        box-shadow: 0 10px 30px rgba(0,0,0,.08);
      }
      .brand {
        font-weight: 700;
        letter-spacing: .2px;
      }
    </style>
  </head>

  <body>
    <!-- Nếu đã có user, chuyển sang trang thành công -->
    <c:if test="${not empty user}">
      <c:redirect url="loginSuccess.jsp" />
    </c:if>

    <div class="container px-3">
      <div class="card login-card">
        <div class="card-body p-4">
          <h1 class="h4 brand text-center mb-3">LOGIN FORM</h1>

          <!-- Hiện thông báo lỗi từ server (nếu có) -->
          <c:if test="${not empty msg}">
            <div class="alert alert-danger py-2" role="alert">
              ${msg}
            </div>
          </c:if>

          <form
            action="MainController"
            method="post"
            class="needs-validation"
            novalidate
          >
            <input type="hidden" name="txtAction" value="login" />

            <div class="mb-3">
              <label for="username" class="form-label">Username</label>
              <input
                id="username"
                type="text"
                name="txtUsername"
                value="${username}"
                class="form-control"
                placeholder="Nhập tên đăng nhập"
                required
              />
              <div class="invalid-feedback">Vui lòng nhập username.</div>
            </div>

            <div class="mb-3">
              <label for="password" class="form-label">Password</label>
              <div class="input-group">
                <input
                  id="password"
                  type="password"
                  name="txtPassword"
                  class="form-control"
                  placeholder="Nhập mật khẩu"
                  required
                />
                <button
                  class="btn btn-outline-secondary"
                  type="button"
                  id="togglePassword"
                  aria-label="Hiện/ẩn mật khẩu"
                >
                  <i class="bi bi-eye"></i>
                </button>
                <div class="invalid-feedback">Vui lòng nhập mật khẩu.</div>
              </div>
            </div>

            <div class="d-flex gap-2">
              <button type="submit" class="btn btn-primary w-100">Login</button>
              <button type="reset" class="btn btn-outline-secondary w-100">
                Reset
              </button>
            </div>
          </form>

          <!-- Gợi ý demo -->
          <div class="text-center mt-3">
            <small class="text-muted">
              Dùng tài khoản do hệ thống cấp để đăng nhập.
            </small>
          </div>
        </div>
      </div>
    </div>

    <!-- Bootstrap Icons (cho nút mắt) -->
    <link
      rel="stylesheet"
      href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.css"
    />

    <!-- Bootstrap JS (và Popper nếu cần) -->
    <script
      src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
      integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
      crossorigin="anonymous"
    ></script>

    <!-- JS: Validation + Toggle password -->
    <script>
      // Bootstrap client-side validation
      (() => {
        const forms = document.querySelectorAll(".needs-validation");
        Array.from(forms).forEach((form) => {
          form.addEventListener(
            "submit",
            (event) => {
              if (!form.checkValidity()) {
                event.preventDefault();
                event.stopPropagation();
              }
              form.classList.add("was-validated");
            },
            false
          );
        });
      })();

      // Toggle show/hide password
      const toggleBtn = document.getElementById("togglePassword");
      const pwdInput = document.getElementById("password");
      if (toggleBtn && pwdInput) {
        toggleBtn.addEventListener("click", () => {
          const isPwd = pwdInput.getAttribute("type") === "password";
          pwdInput.setAttribute("type", isPwd ? "text" : "password");
          toggleBtn.innerHTML = isPwd
            ? '<i class="bi bi-eye-slash"></i>'
            : '<i class="bi bi-eye"></i>';
        });
      }
    </script>
  </body>
</html>
