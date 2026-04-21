# api_contract.md

## 1. Nguyên tắc chung

- API chỉ mô tả contract, không gắn chặt vào framework
- Tất cả response phải theo một chuẩn thống nhất
- Mọi endpoint phải kiểm tra phân quyền theo vai trò
- Không tự ý thêm endpoint ngoài `business_flow.md` nếu chưa cập nhật tài liệu

## 2. Chuẩn response chung

- Success response:
- success: true
- code: mã nghiệp vụ hoặc mã hệ thống
- message: thông điệp ngắn gọn
- data: dữ liệu chính
- meta: phân trang hoặc thông tin bổ sung khi cần

- Error response:
- success: false
- code: mã lỗi
- message: mô tả lỗi
- errors: danh sách lỗi field hoặc lỗi nghiệp vụ

## 3. Auth API

### 3.1. Đăng ký khách hàng

- Endpoint: `/api/auth/register`
- Method: `POST`
- Request:
- fullName
- phone
- email
- password

- Response:
- success
- message
- data:
- userId
- role

### 3.2. Đăng nhập

- Endpoint: `/api/auth/login`
- Method: `POST`
- Request:
- email
- password

- Response:
- success
- message
- data:
- accessToken hoặc session info
- userProfile
- role

- CẦN BỔ SUNG: Cơ chế auth chính thức là session hay token

### 3.3. Đăng xuất

- Endpoint: `/api/auth/logout`
- Method: `POST`
- Request:
- không có body hoặc theo cơ chế auth đã chọn

- Response:
- success
- message

## 4. Customer Profile API

### 4.1. Lấy hồ sơ hiện tại

- Endpoint: `/api/me`
- Method: `GET`
- Response:
- success
- data:
- user info

### 4.2. Cập nhật hồ sơ hiện tại

- Endpoint: `/api/me`
- Method: `PUT`
- Request:
- fullName
- phone
- email hoặc các field được phép sửa

- Response:
- success
- message
- data:
- updated profile

### 4.3. Lấy danh sách địa chỉ giao hàng

- Endpoint: `/api/me/addresses`
- Method: `GET`
- Role: `customer`
- Response:
- success
- data:
- items

### 4.4. Tạo địa chỉ giao hàng

- Endpoint: `/api/me/addresses`
- Method: `POST`
- Role: `customer`
- Request:
- receiverName
- receiverPhone
- addressLine
- ward
- district
- province
- isDefault
- note

- Response:
- success
- message
- data:
- addressId

### 4.5. Cập nhật địa chỉ giao hàng

- Endpoint: `/api/me/addresses/{addressId}`
- Method: `PUT`
- Role: `customer`
- Request:
- các field địa chỉ được phép cập nhật

- Response:
- success
- message
- data:
- updated address

### 4.6. Xóa địa chỉ giao hàng

- Endpoint: `/api/me/addresses/{addressId}`
- Method: `DELETE`
- Role: `customer`
- Response:
- success
- message

## 5. Product API

### 5.1. Lấy danh sách sản phẩm công khai

- Endpoint: `/api/products`
- Method: `GET`
- Query:
- keyword
- categoryId
- productKind
- isSellableDirectly
- page
- limit

- Response:
- success
- data:
- items
- meta

### 5.2. Lấy chi tiết sản phẩm

- Endpoint: `/api/products/{productId}`
- Method: `GET`
- Response:
- success
- data:
- product detail

### 5.3. Lấy danh sách mặt hàng dùng cho đơn custom

- Endpoint: `/api/products/custom-selectable`
- Method: `GET`
- Query:
- keyword
- categoryId
- page
- limit

- Response:
- success
- data:
- items
- meta

### 5.4. Tạo mặt hàng

- Endpoint: `/api/admin/products`
- Method: `POST`
- Role: `admin`, `staff`
- Request:
- name
- description
- price
- categoryId
- productKind
- isSellableDirectly
- isCustomSelectable
- image data hoặc image reference
- size
- material
- flowerType
- status

- Response:
- success
- message
- data:
- productId

### 5.5. Cập nhật mặt hàng

- Endpoint: `/api/admin/products/{productId}`
- Method: `PUT`
- Role: `admin`, `staff`
- Request:
- các field được phép cập nhật

- Response:
- success
- message
- data:
- updated product

### 5.6. Xóa mặt hàng

- Endpoint: `/api/admin/products/{productId}`
- Method: `DELETE`
- Role: `admin`, `staff`
- Response:
- success
- message

- CẦN BỔ SUNG: Chính sách xóa là ẩn sản phẩm hay xóa thật

## 6. User Management API

### 6.1. Lấy danh sách người dùng

- Endpoint: `/api/admin/users`
- Method: `GET`
- Role: `admin`
- Query:
- keyword
- role
- page
- limit

- Response:
- success
- data:
- items
- meta

### 6.2. Tạo người dùng nội bộ

- Endpoint: `/api/admin/users`
- Method: `POST`
- Role: `admin`
- Request:
- fullName
- phone
- email
- password
- role

- Response:
- success
- message
- data:
- userId

### 6.3. Cập nhật người dùng

- Endpoint: `/api/admin/users/{userId}`
- Method: `PUT`
- Role: `admin`
- Request:
- thông tin cập nhật

- Response:
- success
- message

### 6.4. Xóa hoặc khóa người dùng

- Endpoint: `/api/admin/users/{userId}`
- Method: `DELETE`
- Role: `admin`
- Response:
- success
- message

- CẦN BỔ SUNG: Xóa vật lý hay khóa tài khoản

## 7. Available Order API

### 7.1. Tạo đơn hàng sản phẩm có sẵn

- Endpoint: `/api/orders/available`
- Method: `POST`
- Role: `customer`
- Request:
- productId
- quantity
- shippingAddressId hoặc shippingAddressObject
- paymentMethod
- transactionRef
- paymentProof

- Ghi chú:
- Thông tin thanh toán được lưu ở bảng `available_order_payments` theo `database_design.md`, không lưu trực tiếp trong bảng đơn hàng

- Response:
- success
- message
- data:
- orderId
- orderCode
- orderStatus
- paymentStatus

### 7.2. Lấy danh sách đơn thường của khách hàng hiện tại

- Endpoint: `/api/orders/available/my-orders`
- Method: `GET`
- Role: `customer`
- Response:
- success
- data:
- items
- meta

### 7.3. Lấy chi tiết đơn thường

- Endpoint: `/api/orders/available/{orderId}`
- Method: `GET`
- Role: `customer`, `staff`, `admin`
- Response:
- success
- data:
- order detail

### 7.4. Lấy danh sách đơn thường quản trị

- Endpoint: `/api/admin/orders/available`
- Method: `GET`
- Role: `staff`, `admin`
- Query:
- keyword
- orderStatus
- paymentStatus
- page
- limit

- Response:
- success
- data:
- items
- meta

### 7.5. Cập nhật trạng thái đơn thường

- Endpoint: `/api/admin/orders/available/{orderId}/status`
- Method: `PATCH`
- Role: `staff`, `admin`
- Request:
- status
- reason

- Response:
- success
- message
- data:
- orderId
- status

## 8. Custom Order API

### 8.1. Tạo đơn hàng custom

- Endpoint: `/api/orders/custom`
- Method: `POST`
- Role: `customer`
- Request:
- selectedFrameProductId
- flowerType
- personalizationContent
- requestedDeliveryDate
- flowerInputImage
- shippingAddressId hoặc shippingAddressObject
- depositPaymentMethod
- depositTransactionRef
- depositPaymentProof

- Ghi chú:
- Thông tin thanh toán đặt cọc được lưu ở bảng `custom_order_payments` với `payment_stage = deposit` theo `database_design.md`

- Response:
- success
- message
- data:
- orderId
- orderCode
- depositStatus
- paymentStatus

### 8.2. Lấy danh sách đơn custom của khách hàng hiện tại

- Endpoint: `/api/orders/custom/my-orders`
- Method: `GET`
- Role: `customer`
- Response:
- success
- data:
- items
- meta

### 8.3. Lấy chi tiết đơn custom

- Endpoint: `/api/orders/custom/{orderId}`
- Method: `GET`
- Role: `customer`, `staff`, `admin`
- Response:
- success
- data:
- order detail

### 8.4. Lấy danh sách đơn custom quản trị

- Endpoint: `/api/admin/orders/custom`
- Method: `GET`
- Role: `staff`, `admin`
- Query:
- keyword
- orderStatus
- paymentStatus
- page
- limit

- Response:
- success
- data:
- items
- meta

### 8.5. Cập nhật trạng thái đơn custom

- Endpoint: `/api/admin/orders/custom/{orderId}/status`
- Method: `PATCH`
- Role: `staff`, `admin`
- Request:
- status
- reason

- Response:
- success
- message
- data:
- orderId
- status

### 8.6. Thanh toán phần còn lại cho đơn custom

- Endpoint: `/api/orders/custom/{orderId}/remaining-payment`
- Method: `POST`
- Role: `customer`
- Request:
- paymentMethod
- transactionRef
- paymentProof

- Ghi chú:
- API tạo thêm bản ghi trong `custom_order_payments` với `payment_stage = remaining` và cập nhật `paymentStatus` tổng hợp trong đơn

- Response:
- success
- message
- data:
- paymentStatus
- remainingAmount

## 9. Staff Dashboard API

### 9.1. Lấy dashboard tổng quan nhân viên

- Endpoint: `/api/staff/dashboard`
- Method: `GET`
- Role: `staff`
- Response:
- success
- data:
- pendingAvailableOrders: số đơn thường chưa xử lý
- pendingCustomOrders: số đơn custom chờ đánh giá hoa 
- demosPendingApproval: số demo chờ duyệt
- lowInventoryProducts: danh sách mặt hàng tồn kho thấp
- recentOrdersToday: các đơn hàng hôm nay

## 10. Staff Operations API

### 10.1. Đánh giá chất lượng hoa đầu vào

- Endpoint: `/api/admin/orders/custom/{orderId}/evaluate-flower-input`
- Method: `PATCH`
- Role: `staff`, `admin`
- Request:
- evaluationStatus (pass, fail)
- evaluationNote

- Response:
- success
- message
- data:
- orderId
- flowerEvaluationStatus
- nextStep (status đơn được cập nhật)

### 10.2. Cập nhật trạng thái giao nhận đơn thường

- Endpoint: `/api/admin/orders/available/{orderId}/delivery`
- Method: `PATCH`
- Role: `staff`, `admin`
- Request:
- deliveryStatus
- deliveryNote
- deliveryTime

- Response:
- success
- message
- data:
- orderId
- deliveryStatus

### 10.3. Cập nhật trạng thái giao nhận đơn custom

- Endpoint: `/api/admin/orders/custom/{orderId}/delivery`
- Method: `PATCH`
- Role: `staff`, `admin`
- Request:
- deliveryType (pickup_input, ship_output)
- deliveryStatus
- deliveryNote
- deliveryTime

- Response:
- success
- message
- data:
- orderId
- deliveryStatus

## 11. Custom Order Demo API

### 11.1. Tải demo lên

- Endpoint: `/api/admin/orders/custom/{orderId}/demos`
- Method: `POST`
- Role: `staff`, `admin`
- Request:
- demoImage
- demoDescription

- Response:
- success
- message
- data:
- demoId
- versionNo

### 11.2. Lấy danh sách demo của đơn custom

- Endpoint: `/api/orders/custom/{orderId}/demos`
- Method: `GET`
- Role: `customer`, `staff`, `admin`
- Response:
- success
- data:
- items

### 11.3. Phản hồi demo

- Endpoint: `/api/orders/custom/{orderId}/demos/{demoId}/feedback`
- Method: `POST`
- Role: `customer`
- Request:
- action
- feedback

- Ghi chú:
- `action` gồm `approve` hoặc `request_revision`

- Response:
- success
- message
- data:
- currentOrderStatus
- revisionCount

## 12. Inventory API

### 12.1. Tạo phiếu nhập hàng

- Endpoint: `/api/admin/purchase-receipts`
- Method: `POST`
- Role: `staff`, `admin`
- Request:
- receiptDate
- note
- items:
- productId
- quantity
- unitCost

- Response:
- success
- message
- data:
- purchaseReceiptId
- receiptCode

### 12.2. Lấy danh sách phiếu nhập hàng

- Endpoint: `/api/admin/purchase-receipts`
- Method: `GET`
- Role: `staff`, `admin`
- Query:
- fromDate
- toDate
- keyword
- page
- limit

- Response:
- success
- data:
- items
- meta

### 12.3. Lấy tồn kho

- Endpoint: `/api/admin/inventory`
- Method: `GET`
- Role: `staff`, `admin`
- Query:
- productKind
- isSellableDirectly
- isCustomSelectable
- keyword
- page
- limit

- Response:
- success
- data:
- items
- meta

## 13. Report API

### 13.1. Báo cáo tổng quan

- Endpoint: `/api/admin/reports/overview`
- Method: `GET`
- Role: `admin`
- Query:
- fromDate
- toDate

- Response:
- success
- data:
- totalProducts
- totalOrders
- processingOrders
- completedOrders

### 13.2. Báo cáo doanh thu

- Endpoint: `/api/admin/reports/revenue`
- Method: `GET`
- Role: `admin`
- Query:
- fromDate
- toDate
- groupBy

- Response:
- success
- data:
- report items

### 13.3. Thống kê đơn hàng

- Endpoint: `/api/admin/reports/orders`
- Method: `GET`
- Role: `admin`
- Query:
- fromDate
- toDate
- orderDomain

- Response:
- success
- data:
- report items

### 13.4. Thống kê tồn kho

- Endpoint: `/api/admin/reports/inventory`
- Method: `GET`
- Role: `admin`
- Query:
- productKind

- Response:
- success
- data:
- report items

## 14. Các điểm còn thiếu

- CẦN BỔ SUNG: Upload file dùng local storage, object storage hay dịch vụ nào khác
- CẦN BỔ SUNG: Chuẩn mã lỗi chi tiết
- CẦN BỔ SUNG: Cơ chế phân trang thống nhất
- CẦN BỔ SUNG: Chuẩn filter/sort chính thức
- CẦN BỔ SUNG: API hủy đơn và hoàn cọc có nằm trong phase đầu hay không
