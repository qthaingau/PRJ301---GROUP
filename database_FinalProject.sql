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
    [password] VARCHAR(255) NOT NULL,         -- Mật khẩu đã hash
    fullName NVARCHAR(100),                     -- Họ và tên
    phoneNumber VARCHAR(20),                    -- Số điện thoại
    role VARCHAR(20) DEFAULT 'customer',        -- Vai trò: admin/staff/customer
    createdAt DATETIME DEFAULT GETDATE(),        -- Ngày tạo tài khoản
	avatar NVARCHAR(MAX) NULL
); -- Bảng lưu người dùng
GO
ALTER TABLE [User]
ADD active BIT DEFAULT 1;
INSERT INTO [User] (userID, username, email, [password], fullName, phoneNumber, role, createdAt)
VALUES
('U001', 'admin01', 'admin01@example.com', 'admin123', N'Nguyễn Văn Admin', '0901000001', 'admin', GETDATE()),
('U002', 'staff01', 'staff01@example.com', 'staff123', N'Lê Thị Nhân Viên', '0902000002', 'staff', GETDATE()),
('U003', 'customer01', 'customer01@example.com', 'cust123', N'Trần Minh Khách', '0903000003', 'customer', GETDATE()),
('U004', 'customer02', 'customer02@example.com', 'cust456', N'Nguyễn Thị Lan', '0904000004', 'customer', GETDATE()),
('U005', 'customer03', 'customer03@example.com', 'cust789', N'Phạm Minh Tú', '0905000005', 'customer', GETDATE()),
('U006', 'customer04', 'customer04@example.com', 'cust012', N'Vũ Thị Bình', '0906000006', 'customer', GETDATE()),
('U007', 'customer05', 'customer05@example.com', 'cust345', N'Nguyễn Văn Hùng', '0907000007', 'customer', GETDATE()),
('U008', 'customer06', 'customer06@example.com', 'cust678', N'Lê Thị Mai', '0908000008', 'customer', GETDATE()),
('U009', 'customer07', 'customer07@example.com', 'cust901', N'Phan Minh Quang', '0909000009', 'customer', GETDATE());
SELECT * FROM [User]
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
INSERT INTO Address (addressID, userID, recipientName, phoneNumber, street, ward, district, city, isDefault)
VALUES
-- Admin
('A001', 'U001', N'Nguyễn Văn Admin', '0901000001', N'123 Lê Lợi', N'Phường Bến Nghé', N'Quận 1', N'TP. Hồ Chí Minh', 1),

-- Staff
('A002', 'U002', N'Lê Thị Nhân Viên', '0902000002', N'45 Nguyễn Huệ', N'Phường Bến Thành', N'Quận 1', N'TP. Hồ Chí Minh', 1),

-- Customer - có 2 địa chỉ (1 mặc định)
('A003', 'U003', N'Trần Minh Khách', '0903000003', N'56 Phan Chu Trinh', N'Phường Hòa Cường Bắc', N'Quận Hải Châu', N'Đà Nẵng', 1),
('A004', 'U003', N'Trần Minh Khách', '0903000003', N'99 Trần Hưng Đạo', N'Phường An Phú', N'Quận Ninh Kiều', N'Cần Thơ', 0);
SELECT * FROM Address

/* ===========================
   2. CATEGORY
   =========================== */
CREATE TABLE Category (
    categoryID NVARCHAR(50) PRIMARY KEY,                  -- Khóa chính
    categoryName NVARCHAR(100) UNIQUE NOT NULL,           -- Tên danh mục (duy nhất)
    sportType NVARCHAR(50),                               -- Loại thể thao
    isActive BIT DEFAULT 1                                -- Trạng thái hiển thị (1: active, 0: inactive)
);
GO

-- Dữ liệu mẫu cho Category
INSERT INTO Category (categoryID, categoryName, sportType)
VALUES
    ('C001', 'Football Shoes', 'Football'),
    ('C002', 'Running Shoes', 'Running'),
    ('C003', 'Training Shoes', 'Training'),
    ('C004', 'Basketball Shoes', 'Basketball'),
    ('C005', 'Gym Shoes', 'Gym');
GO
SELECT * FROM Category;
GO
/* ===========================
   3. BRAND
   =========================== */
CREATE TABLE Brand (
    brandID NVARCHAR(50) PRIMARY KEY,                     -- Khóa chính
    brandName NVARCHAR(100) NOT NULL UNIQUE,              -- Tên thương hiệu (duy nhất)
    origin NVARCHAR(100),                                 -- Quốc gia / nguồn gốc
    isActive BIT DEFAULT 1                                -- Trạng thái hiển thị
);
GO

-- Dữ liệu mẫu cho Brand
INSERT INTO Brand (brandID, brandName, origin)
VALUES
    ('B001', 'Nike', 'USA'),
    ('B002', 'Adidas', 'Germany'),
    ('B003', 'Puma', 'Germany'),
    ('B004', 'Reebok', 'USA'),
    ('B005', 'Under Armour', 'USA');
GO

SELECT * FROM Brand;
GO

/* ===========================
   4. PRODUCT
   =========================== */
CREATE TABLE Product ( -- Tạo bảng Product
    productID NVARCHAR(50) PRIMARY KEY,         -- Chuỗi ID tự truyền từ Java
    productName NVARCHAR(150) NOT NULL,        -- Tên sản phẩm
    description NVARCHAR(MAX),                 -- Mô tả
    categoryID NVARCHAR(50) NOT NULL,          -- FK Category
    brandID NVARCHAR(50) NOT NULL,             -- FK Brand
    createdAt DATETIME DEFAULT GETDATE(),      -- Ngày tạo
    isActive BIT DEFAULT 1,                    -- Trạng thái hiển thị
	productImage NVARCHAR(MAX) NULL
    FOREIGN KEY (categoryID) REFERENCES Category(categoryID),
    FOREIGN KEY (brandID) REFERENCES Brand(brandID)
); -- Bảng sản phẩm
GO
INSERT INTO Product (productID, productName, description, categoryID, brandID)
VALUES
    ('P001', 'Nike Football Shoes', 'High-quality football shoes', 'C001', 'B001'),
    ('P002', 'Adidas Running Shoes', 'Comfortable running shoes', 'C002', 'B002'),
    ('P003', 'Puma Training Shoes', 'Training shoes for athletes', 'C003', 'B003'),
    ('P004', 'Reebok Football Boots', 'Football boots for professionals', 'C001', 'B004'),
    ('P005', 'Under Armour Running Shoes', 'Shoes for long-distance running', 'C002', 'B005'),
    ('P006', 'New Balance Training Shoes', 'Stable and durable training shoes', 'C003', 'B001'),
    ('P007', 'Nike Basketball Shoes', 'Basketball shoes for fast movements', 'C004', 'B001'),
    ('P008', 'Adidas Gym Shoes', 'Comfortable shoes for gym workouts', 'C005', 'B002'),
    ('P009', 'Asics Running Shoes', 'Lightweight running shoes', 'C002', 'B003'),
    ('P010', 'Nike Crossfit Shoes', 'Perfect for crossfit workouts', 'C003', 'B004'),
    ('P011', 'Adidas Football Boots', 'Lightweight boots for football', 'C001', 'B002'),
    ('P012', 'Puma Running Shoes', 'High-performance shoes for runners', 'C002', 'B003'),
    ('P013', 'Reebok Crossfit Shoes', 'Shoes for weightlifting and CrossFit', 'C003', 'B004'),
    ('P014', 'Under Armour Basketball Shoes', 'Shoes for basketball players', 'C004', 'B005'),
    ('P015', 'New Balance Football Shoes', 'Comfortable football shoes', 'C001', 'B001'),
    ('P016', 'Nike Running Shoes', 'Running shoes with great cushioning', 'C002', 'B001'),
    ('P017', 'Asics Training Shoes', 'Durable shoes for intense training', 'C003', 'B003'),
    ('P018', 'Adidas Training Boots', 'Heavy-duty boots for training', 'C003', 'B002'),
    ('P019', 'Puma Basketball Shoes', 'Stylish basketball shoes', 'C004', 'B003'),
    ('P020', 'Reebok Running Shoes', 'Perfect fit for long runs', 'C002', 'B004'),
    ('P021', 'Under Armour Crossfit Shoes', 'Comfort and performance in one shoe', 'C003', 'B005'),
    ('P022', 'Nike Gym Shoes', 'Shoes for all kinds of gym activities', 'C005', 'B001'),
    ('P023', 'Adidas Crossfit Shoes', 'High-performance shoes for workouts', 'C003', 'B002'),
    ('P024', 'Puma Basketball Boots', 'Strong and supportive basketball boots', 'C004', 'B003'),
    ('P025', 'New Balance Running Shoes', 'Shoes for daily runners', 'C002', 'B001'),
    ('P026', 'Reebok Basketball Shoes', 'Strong support for basketball players', 'C004', 'B004'),
    ('P027', 'Under Armour Football Shoes', 'Great for playing football', 'C001', 'B005'),
    ('P028', 'Asics Basketball Shoes', 'Shoes for maximum agility on the court', 'C004', 'B003'),
    ('P029', 'Nike Training Shoes', 'All-purpose training shoes', 'C003', 'B001'),
    ('P030', 'Adidas Gym Boots', 'Durable boots for gym exercises', 'C005', 'B002');
SELECT * FROM Product
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
	variantImage NVARCHAR(MAX) NULL
    CONSTRAINT UQProductVariant UNIQUE (productID, size, color), -- Không trùng biến thể
    FOREIGN KEY (productID) REFERENCES Product(productID) -- FK Product
); -- Mỗi variant là 1 combo product+size+color
GO
/* =======================================
    INSERT DỮ LIỆU VÀO BẢNG PRODUCTVARIANT (54 Biến thể)
    ======================================= */
INSERT INTO ProductVariant (variantID, productID, size, color, stock, price, salesCount)
VALUES
    -- P001: Nike Football Shoes
    ('PV001', 'P001', '40', 'Black', 50, 150.00, 12),
    ('PV002', 'P001', '41', 'Black', 30, 150.00, 8),
    ('PV003', 'P001', '42', 'Red', 45, 155.00, 5),

    -- P002: Adidas Running Shoes
    ('PV004', 'P002', '38', 'White', 60, 120.50, 15),
    ('PV005', 'P002', '39', 'Blue', 40, 125.99, 10),
    ('PV006', 'P002', '40', 'Black', 55, 120.50, 7),

    -- P003: Puma Training Shoes
    ('PV007', 'P003', '41', 'Grey', 70, 95.00, 11),
    ('PV008', 'P003', '42', 'Grey', 60, 95.00, 6),

    -- P004: Reebok Football Boots
    ('PV009', 'P004', '43', 'Green', 25, 189.99, 4),
    ('PV010', 'P004', '44', 'Green', 15, 189.99, 2),

    -- P005: Under Armour Running Shoes
    ('PV011', 'P005', '37', 'Pink', 80, 110.00, 14),
    ('PV012', 'P005', '38', 'Pink', 75, 110.00, 9),

    -- P006: New Balance Training Shoes
    ('PV013', 'P006', '40', 'White/Blue', 50, 105.00, 5),
    ('PV014', 'P006', '41', 'White/Blue', 45, 105.00, 3),

    -- P007: Nike Basketball Shoes
    ('PV015', 'P007', '43', 'Black/White', 35, 180.00, 7),
    ('PV016', 'P007', '44', 'Black/White', 25, 180.00, 4),

    -- P008: Adidas Gym Shoes
    ('PV017', 'P008', '39', 'Red', 90, 75.00, 13),
    ('PV018', 'P008', '40', 'Red', 85, 75.00, 10),

    -- P009: Asics Running Shoes
    ('PV019', 'P009', '38', 'Yellow', 100, 130.00, 16),

    -- P010: Nike Crossfit Shoes
    ('PV020', 'P010', '42', 'Camo', 40, 160.00, 3),

    -- P011: Adidas Football Boots
    ('PV021', 'P011', '41', 'Yellow', 30, 170.00, 6),
    ('PV022', 'P011', '42', 'Yellow', 20, 170.00, 4),

    -- P016: Nike Running Shoes
    ('PV023', 'P016', '37', 'White', 70, 99.99, 14),
    ('PV024', 'P016', '38', 'White', 65, 99.99, 12),
    ('PV025', 'P016', '39', 'Black', 50, 102.00, 9),

    -- P022: Nike Gym Shoes
    ('PV026', 'P022', '41', 'Grey', 80, 85.00, 11),
    ('PV027', 'P022', '42', 'Black', 75, 85.00, 8),

    -- P027: Under Armour Football Shoes
    ('PV028', 'P027', '40', 'Green', 40, 145.00, 5),
    ('PV029', 'P027', '41', 'Green', 30, 145.00, 3),
    ('PV030', 'P027', '42', 'Blue', 25, 148.00, 1),
    
    -- P012: Puma Running Shoes
    ('PV031', 'P012', '37', 'Red/Black', 60, 115.00, 15),
    ('PV032', 'P012', '38', 'Red/Black', 55, 115.00, 10),

    -- P013: Reebok Crossfit Shoes
    ('PV033', 'P013', '42', 'Black', 40, 140.00, 7),
    ('PV034', 'P013', '43', 'White', 35, 145.00, 5),

    -- P014: Under Armour Basketball Shoes
    ('PV035', 'P014', '44', 'Blue', 30, 175.00, 4),
    ('PV036', 'P014', '45', 'Blue', 20, 175.00, 2),

    -- P015: New Balance Football Shoes
    ('PV037', 'P015', '41', 'Navy', 50, 130.00, 10),
    ('PV038', 'P015', '42', 'Navy', 40, 130.00, 8),

    -- P017: Asics Training Shoes
    ('PV039', 'P017', '39', 'Grey/Yellow', 65, 90.00, 12),

    -- P018: Adidas Training Boots
    ('PV040', 'P018', '43', 'Black/Red', 30, 155.00, 6),

    -- P019: Puma Basketball Shoes
    ('PV041', 'P019', '42', 'White/Orange', 40, 165.00, 9),

    -- P020: Reebok Running Shoes
    ('PV042', 'P020', '37', 'Black', 70, 105.00, 14),
    ('PV043', 'P020', '38', 'Black', 60, 105.00, 11),

    -- P021: Under Armour Crossfit Shoes
    ('PV044', 'P021', '41', 'Red', 45, 135.00, 8),

    -- P023: Adidas Crossfit Shoes
    ('PV045', 'P023', '42', 'Green', 50, 140.00, 10),
    ('PV046', 'P023', '43', 'Green', 40, 140.00, 7),

    -- P024: Puma Basketball Boots
    ('PV047', 'P024', '44', 'Black', 25, 185.00, 3),

    -- P025: New Balance Running Shoes
    ('PV048', 'P025', '39', 'Grey', 80, 99.00, 18),
    ('PV049', 'P025', '40', 'Grey', 75, 99.00, 16),

    -- P026: Reebok Basketball Shoes
    ('PV050', 'P026', '43', 'White/Blue', 35, 170.00, 5),

    -- P028: Asics Basketball Shoes
    ('PV051', 'P028', '42', 'Orange', 40, 160.00, 9),

    -- P029: Nike Training Shoes
    ('PV052', 'P029', '41', 'Black/Grey', 60, 110.00, 12),
    ('PV053', 'P029', '42', 'Black/Grey', 55, 110.00, 10),

    -- P030: Adidas Gym Boots
    ('PV054', 'P030', '40', 'Olive', 50, 80.00, 8);
GO
-- Kiểm tra dữ liệu
SELECT * FROM ProductVariant;

/* ===========================
   6. PRODUCTIMAGE
   =========================== */
/*CREATE TABLE ProductImage ( -- Tạo bảng ảnh sản phẩm
    imageID NVARCHAR(50) PRIMARY KEY,          -- Khoá chính ảnh
    productID NVARCHAR(50) NOT NULL,           -- FK tới Product (ảnh gắn chung cho product)
    imageUrl NVARCHAR(max) NOT NULL,           -- URL hoặc đường dẫn file ảnh
    isMain BIT DEFAULT 0,                      -- 1 = ảnh chính
    FOREIGN KEY (productID) REFERENCES Product(productID) -- FK Product
); -- Ảnh lưu theo product để tiện load gallery
GO
*/
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
/*CREATE TABLE Promotion ( -- Tạo bảng sự kiện giảm giá (**ĐÃ ĐỔI TÊN**)
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
GO*/

/* ===========================
   9. CART & CARTITEM
   =========================== */
CREATE TABLE Cart ( -- Tạo bảng giỏ hàng
    cartID NVARCHAR(50) PRIMARY KEY,           -- ID giỏ hàng
    userID NVARCHAR(50) UNIQUE NOT NULL,       -- Mỗi user có 1 cart duy nhất
    FOREIGN KEY (userID) REFERENCES [User](userID) -- FK Users
); -- Giỏ hàng trên mỗi user
GO

CREATE TABLE CartItem ( 
    cartItemID NVARCHAR(50) PRIMARY KEY,        -- ID mục giỏ hàng
    cartID NVARCHAR(50) NOT NULL,               -- FK tới Cart
    variantID NVARCHAR(50) NOT NULL,            -- FK tới ProductVariant
    quantity INT DEFAULT 1 CHECK (quantity > 0),-- Số lượng
    FOREIGN KEY (cartID) REFERENCES Cart(cartID) ON DELETE CASCADE,
    FOREIGN KEY (variantID) REFERENCES ProductVariant(variantID)
);
GO

-- Dữ liệu mẫu (tuỳ chọn)
INSERT INTO Cart (cartID, userID)
VALUES ('CART_U003', 'U003'); -- Giỏ hàng cho customer01

INSERT INTO CartItem (cartItemID, cartID, variantID, quantity)
VALUES 
('CI001', 'CART_U003', 'PV001', 2),
('CI002', 'CART_U003', 'PV004', 1);
GO

SELECT * FROM Cart;
SELECT * FROM CartItem;


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
INSERT INTO Review (reviewID, userID, productID, rating, comment, createdAt)
VALUES
-- U003 đánh giá
('R001', 'U003', 'P001', 5, N'Giày đi rất êm và bám sân tốt!', GETDATE()),
('R002', 'U003', 'P002', 4, N'Chạy bộ thoải mái nhưng hơi nặng.', GETDATE()),
('R003', 'U003', 'P003', 5, N'Sản phẩm tốt, đáng tiền.', GETDATE()),

-- U004 đánh giá
('R004', 'U004', 'P004', 3, N'Màu sắc đẹp nhưng hơi chật.', GETDATE()),
('R005', 'U004', 'P005', 5, N'Giày nhẹ, chất liệu tốt.', GETDATE()),
('R006', 'U004', 'P006', 4, N'Rất ổn cho việc tập gym hàng ngày.', GETDATE()),

-- U005 đánh giá
('R007', 'U005', 'P007', 5, N'Rất thích hợp để chơi bóng rổ.', GETDATE()),
('R008', 'U005', 'P008', 4, N'Phù hợp để tập gym, form đẹp.', GETDATE()),
('R009', 'U005', 'P009', 5, N'Giày chạy rất nhẹ và thoáng.', GETDATE()),

-- U006 đánh giá
('R010', 'U006', 'P010', 4, N'Dễ phối đồ, đế giày bám tốt.', GETDATE()),
('R011', 'U006', 'P011', 5, N'Đôi giày bóng đá tuyệt vời.', GETDATE()),
('R012', 'U006', 'P012', 4, N'Chạy ổn, đệm êm ái.', GETDATE()),

-- U007 đánh giá
('R013', 'U007', 'P013', 3, N'Hơi cứng khi mới đi, nhưng ổn sau vài ngày.', GETDATE()),
('R014', 'U007', 'P014', 5, N'Rất đẹp và nhẹ.', GETDATE()),
('R015', 'U007', 'P015', 4, N'Đi khá bền và thoáng khí.', GETDATE()),

-- U008 đánh giá
('R016', 'U008', 'P016', 5, N'Cảm giác chạy rất tốt, cực kỳ êm.', GETDATE()),
('R017', 'U008', 'P017', 4, N'Chất lượng tốt, form ôm chân.', GETDATE()),
('R018', 'U008', 'P018', 3, N'Hơi nặng so với mong đợi.', GETDATE()),

-- U009 đánh giá
('R019', 'U009', 'P019', 5, N'Giày bóng rổ cực kỳ đẹp và chắc chắn.', GETDATE()),
('R020', 'U009', 'P020', 4, N'Giày chạy nhẹ, thoải mái.', GETDATE()),
('R021', 'U009', 'P021', 5, N'Cảm giác tập Crossfit rất tốt.', GETDATE());
GO