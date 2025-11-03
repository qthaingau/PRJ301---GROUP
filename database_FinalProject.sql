USE [master]; -- Chuyển ngữ cảnh sang DB hệ thống để thao tác
GO

IF EXISTS (SELECT name FROM sys.databases WHERE name = N'PRJ301_SPORTSHOP') -- Kiểm tra DB cũ
BEGIN
    ALTER DATABASE [PRJ301_SPORTSHOP] SET SINGLE_USER WITH ROLLBACK IMMEDIATE; -- Buộc đóng kết nối
    DROP DATABASE [PRJ301_SPORTSHOP]; -- Xóa DB cũ nếu có
END
GO

CREATE DATABASE [PRJ301_SPORTSHOP]; -- Tạo database mới
GO

USE [PRJ301_SPORTSHOP]; -- Sử dụng database vừa tạo
GO

/* ===========================
   1. USERS
   =========================== */
CREATE TABLE [User] ( -- Tạo bảng Users
    userID NVARCHAR(50) PRIMARY KEY,            -- Khoá chính tự tăng
    username VARCHAR(50) UNIQUE NOT NULL,       -- Tên đăng nhập (unique)
    email VARCHAR(100) UNIQUE NOT NULL,         -- Email (unique)
    passwordHash VARCHAR(255) NOT NULL,         -- Mật khẩu đã hash
    fullName NVARCHAR(100),                     -- Họ và tên
    phoneNumber VARCHAR(20),                    -- Số điện thoại
    role VARCHAR(20) DEFAULT 'customer',        -- Vai trò: admin/staff/customer
    createdAt DATETIME DEFAULT GETDATE()        -- Ngày tạo tài khoản
); -- Bảng lưu người dùng
GO

/* ===========================
   1A. ADDRESS (BẢNG MỚI)
   -- >> BỔ SUNG: Bảng Address mới để làm sổ địa chỉ
   =========================== */
CREATE TABLE Address (
    addressID NVARCHAR(50) PRIMARY KEY,         -- Khoá chính
    userID NVARCHAR(50) NOT NULL,               -- FK tới User
    recipientName NVARCHAR(100) NOT NULL,       -- Tên người nhận
    phoneNumber VARCHAR(20) NOT NULL,           -- SĐT người nhận
    street NVARCHAR(255) NOT NULL,              -- Số nhà, đường
    ward NVARCHAR(100) NOT NULL,                -- Phường/Xã
    district NVARCHAR(100) NOT NULL,            -- Quận/Huyện
    city NVARCHAR(100) NOT NULL,                -- Tỉnh/Thành phố
    isDefault BIT DEFAULT 0,                   -- Địa chỉ mặc định
    FOREIGN KEY (userID) REFERENCES [User](userID) ON DELETE CASCADE -- FK tới User
); -- Bảng lưu sổ địa chỉ của người dùng

/* ===========================
   2. CATEGORY
   =========================== */
CREATE TABLE Category ( -- Tạo bảng Category
    categoryID NVARCHAR(50) PRIMARY KEY,        -- Khoá chính
    categoryName NVARCHAR(100) UNIQUE NOT NULL,         -- Tên danh mục
    sportType NVARCHAR(50),                     -- Môn thể thao (Football, Running,...)
    parentCategoryID NVARCHAR(50) NULL,         -- ID danh mục cha (nếu có)
    FOREIGN KEY (parentCategoryID) REFERENCES Category(categoryID) -- FK tự tham chiếu
); -- Bảng phân loại sản phẩm
GO

/* ===========================
   3. BRAND
   =========================== */
CREATE TABLE Brand ( -- Tạo bảng Brand
    brandID NVARCHAR(50) PRIMARY KEY,          -- Khoá chính
    brandName NVARCHAR(100) NOT NULL,          -- Tên thương hiệu
    origin NVARCHAR(100)                       -- Nguồn gốc/Quốc gia
); -- Bảng thương hiệu
GO

/* ===========================
   4. PRODUCT
   =========================== */
CREATE TABLE Product ( -- Tạo bảng Product
    productID NVARCHAR(50) PRIMARY KEY,         -- Chuỗi ID tự truyền từ Java
    productName NVARCHAR(150) NOT NULL,        -- Tên sản phẩm
    [description] NVARCHAR(MAX),                 -- Mô tả
    categoryID NVARCHAR(50) NOT NULL,          -- FK Category
    brandID NVARCHAR(50) NOT NULL,             -- FK Brand
    createdAt DATETIME DEFAULT GETDATE(),      -- Ngày tạo
    isActive BIT DEFAULT 1,                    -- Trạng thái hiển thị
    FOREIGN KEY (categoryID) REFERENCES Category(categoryID),
    FOREIGN KEY (brandID) REFERENCES Brand(brandID)
); -- Bảng sản phẩm
GO

/* ===========================
   5. PRODUCTVARIANT (gồm size + color)
   =========================== */
CREATE TABLE ProductVariant ( -- Tạo bảng biến thể sản phẩm
    variantID NVARCHAR(50) PRIMARY KEY,          -- Khoá chính biến thể
    productID NVARCHAR(50) NOT NULL,             -- FK tới Product
    size VARCHAR(20) NOT NULL,                   -- Kích cỡ (vd: 40, M, L)
    color NVARCHAR(50) NOT NULL,                 -- Màu sắc (vd: Black)
    stock INT DEFAULT 0,                         -- Tồn kho
    price DECIMAL(18,2) NOT NULL,                -- Giá bán
	salesCount INT DEFAULT 0,					-- Lượt bán
    CONSTRAINT UQProductVariant UNIQUE (productID, size, color), -- Không trùng biến thể
    FOREIGN KEY (productID) REFERENCES Product(productID) -- FK Product
); -- Mỗi variant là 1 combo product+size+color
GO

/* ===========================
   6. PRODUCTIMAGE
   =========================== */
CREATE TABLE ProductImage ( -- Tạo bảng ảnh sản phẩm
    imageID NVARCHAR(50) PRIMARY KEY,          -- Khoá chính ảnh
    productID NVARCHAR(50) NOT NULL,           -- FK tới Product (ảnh gắn chung cho product)
    imageUrl NVARCHAR(MAX) NOT NULL,           -- URL hoặc đường dẫn file ảnh
    isMain BIT DEFAULT 0,                      -- 1 = ảnh chính
    FOREIGN KEY (productID) REFERENCES Product(productID) -- FK Product
); -- Ảnh lưu theo product để tiện load gallery
GO

/* ===========================
   7. FAVORITE
   =========================== */
--CREATE TABLE Favorite ( -- Tạo bảng favorite (yêu thích)
    --userID INT NOT NULL,                        -- Người dùng thích
    --productID INT NOT NULL,                     -- Sản phẩm được thích
    --created_at DATETIME DEFAULT GETDATE(),      -- Ngày thêm
    --PRIMARY KEY (userID, productID),            -- Khóa chính kết hợp
    --FOREIGN KEY (userID) REFERENCES [User](userID), -- FK Users
    --FOREIGN KEY (productID) REFERENCES Product(productID) -- FK Product
--); -- Lưu sản phẩm người dùng đánh dấu yêu thích
--GO

--8. PROMOTION (ĐÃ ĐỔI TÊN TỪ DISCOUNT EVENT) + LINK
   -- =========================== */
CREATE TABLE Promotion ( -- Tạo bảng sự kiện giảm giá (**ĐÃ ĐỔI TÊN**)
    discountID NVARCHAR(50) PRIMARY KEY,      -- ID sự kiện
    discountName NVARCHAR(100) NOT NULL,      -- Tên sự kiện
    description NVARCHAR(255),                 -- Mô tả
    discountPercent DECIMAL(5,2) NOT NULL CHECK (discountPercent BETWEEN 0 AND 100), -- % giảm
    startDate DATE NOT NULL,                   -- Ngày bắt đầu
    endDate DATE NOT NULL,                     -- Ngày kết thúc
    isActive BIT DEFAULT 1                     -- 1 = đang áp dụng
); -- Bảng quản lý chương trình giảm giá
GO

CREATE TABLE ProductDiscount ( -- Tạo bảng trung gian liên kết product <-> discount
    productID NVARCHAR(50) NOT NULL,                     -- ID sản phẩm
    discountID NVARCHAR(50) NOT NULL,                    -- ID event giảm giá
    PRIMARY KEY (productID, discountID),        -- Khóa chính kết hợp
    FOREIGN KEY (productID) REFERENCES Product(productID), -- FK Product
    FOREIGN KEY (discountID) REFERENCES Promotion(discountID) -- FK DiscountEvent
); -- Liên kết nhiều-nhiều giữa sản phẩm và sự kiện giảm giá
GO

/* ===========================
   9. CART & CARTITEM
   =========================== */
CREATE TABLE Cart ( -- Tạo bảng giỏ hàng
    cartID NVARCHAR(50) PRIMARY KEY,           -- ID giỏ hàng
    userID NVARCHAR(50) UNIQUE NOT NULL,       -- Mỗi user có 1 cart duy nhất
    createdAt DATETIME DEFAULT GETDATE(),      -- Ngày tạo giỏ
    FOREIGN KEY (userID) REFERENCES [User](userID) -- FK Users
); -- Giỏ hàng trên mỗi user
GO

CREATE TABLE CartItem ( -- Tạo bảng chi tiết giỏ hàng
    cartItemID NVARCHAR(50) PRIMARY KEY,        -- ID mục giỏ hàng
    cartID NVARCHAR(50) NOT NULL,               -- FK tới Cart
    variantID NVARCHAR(50) NOT NULL,                     -- FK tới ProductVariant
    quantity INT DEFAULT 1,                     -- Số lượng
    FOREIGN KEY (cartID) REFERENCES Cart(cartID), -- FK Cart
    FOREIGN KEY (variantID) REFERENCES ProductVariant(variantID) -- FK Variant
); -- Lưu biến thể + số lượng trong giỏ
GO

/* ===========================
   10. ORDERS (đổi tên tránh dùng từ khoá)
   =========================== */
CREATE TABLE [Order] ( -- Tạo bảng Orders (không dùng tên 'Order')
    orderID NVARCHAR(50) PRIMARY KEY,          -- ID đơn hàng
    userID NVARCHAR(50) NOT NULL,              -- Người đặt hàng
    order_date DATETIME DEFAULT GETDATE(),     -- Ngày tạo đơn
    totalAmount DECIMAL(18,2) NOT NULL,        -- Tổng tiền (tính khi checkout)
    status NVARCHAR(50) DEFAULT 'Pending',     -- Trạng thái: Pending, Paid, Shipped, Cancelled
    shippingAddress NVARCHAR(255),             -- Địa chỉ giao hàng
    paymentMethod NVARCHAR(50),                -- Phương thức thanh toán (COD, Card, ...)
    FOREIGN KEY (userID) REFERENCES [User](userID) -- FK Users
); -- Bảng lưu thông tin đơn hàng
GO

CREATE TABLE OrderDetail ( -- Tạo bảng chi tiết đơn hàng
    orderDetailID NVARCHAR(50) PRIMARY KEY,      -- ID chi tiết
    orderID NVARCHAR(50) NOT NULL,               -- FK tới Orders
    variantID NVARCHAR(50) NOT NULL,               -- FK tới ProductVariant
    quantity INT NOT NULL,                       -- Số lượng mua
    pricePerUnit DECIMAL(18,2) NOT NULL,       -- Giá từng sản phẩm tại thời điểm mua
    FOREIGN KEY (orderID) REFERENCES [Order](orderID), -- FK Orders
    FOREIGN KEY (variantID) REFERENCES ProductVariant(variantID) -- FK Variant
); -- Mỗi đơn có nhiều dòng chi tiết
GO

/* ===========================
   11. PAYMENT
   =========================== */
CREATE TABLE Payment ( -- Tạo bảng Payment
    paymentID NVARCHAR(50) PRIMARY KEY,         -- ID thanh toán
    orderID NVARCHAR(50) UNIQUE NOT NULL,        -- Mỗi order 1 payment (ở demo) -> UNIQUE
    paymentMethod NVARCHAR(50),                 -- Phương thức thanh toán thực tế
    paymentDate DATETIME DEFAULT GETDATE(),     -- Ngày thanh toán
    paymentStatus NVARCHAR(50) DEFAULT 'Pending', -- Trạng thái thanh toán
    amount DECIMAL(18,2),                        -- Số tiền đã thanh toán
    FOREIGN KEY (orderID) REFERENCES [Order](orderID) -- FK Order
); -- Lưu thông tin thanh toán
GO

/* ===========================
   12. SHIPPING
   =========================== */
CREATE TABLE Shipping ( -- Tạo bảng Shipping
    shippingID NVARCHAR(50) PRIMARY KEY,       -- ID vận chuyển
    orderID NVARCHAR(50) NOT NULL,             -- FK tới Orders
    shippingAddress NVARCHAR(255) NOT NULL,    -- Địa chỉ giao
    shippingStatus NVARCHAR(50) DEFAULT 'Preparing', -- Trạng thái giao
    shippedDate DATETIME NULL,                 -- Ngày gửi
    carrier NVARCHAR(100) NULL,                 -- Đơn vị vận chuyển
    trackingNumber NVARCHAR(100) NULL,         -- Mã tracking
    FOREIGN KEY (orderID) REFERENCES [Order](orderID) -- FK Orders
); -- Lưu trạng thái vận chuyển
GO

/* ===========================
   13. REVIEW
   =========================== */
CREATE TABLE Review ( -- Tạo bảng Review
    reviewID NVARCHAR(50) PRIMARY KEY,          -- ID đánh giá
    userID NVARCHAR(50) NOT NULL,               -- Người đánh giá
    productID NVARCHAR(50) NOT NULL,           -- Sản phẩm được đánh giá
    rating INT CHECK (rating BETWEEN 1 AND 5),  -- Điểm 1-5
    comment NVARCHAR(500),                      -- Nội dung đánh giá
    createdAt DATETIME DEFAULT GETDATE(),      -- Ngày tạo đánh giá
    FOREIGN KEY (userID) REFERENCES [User](userID), -- FK Users
    FOREIGN KEY (productID) REFERENCES Product(productID) -- FK Product
); -- Lưu đánh giá khách hàng
GO
