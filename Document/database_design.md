# database_design.md

## 1. Nguyên tắc thiết kế

- Dùng cơ sở dữ liệu quan hệ
- Tách riêng đơn hàng thường và đơn hàng custom thành 2 nhóm bảng độc lập
- Tách thông tin thanh toán ra bảng riêng (không lưu cột giao dịch/chứng từ thanh toán trực tiếp trong bảng đơn hàng)
- Không tạo bảng `frames` riêng
- Khung tranh được lưu chung trong bảng `products`
- Khung tranh khác sản phẩm bán thường ở chỗ:
- Không bán trực tiếp cho khách hàng như sản phẩm bình thường
- Được dùng như lựa chọn đầu vào khi khách hàng tạo đơn custom
- Tồn kho được quản lý chung theo bảng `products`
- Phiếu nhập hàng và biến động tồn kho đều ghi nhận theo `product_id`
- Những gì chưa được requirement chốt chắc chắn sẽ ghi `CẦN BỔ SUNG`

## 2. Danh sách bảng đề xuất

### 2.1. users

- Mục đích: Lưu tài khoản đăng nhập chung
- Field chính:
- id
- full_name
- phone
- email
- password_hash
- role_id
- status
- created_at
- updated_at

### 2.2. roles

- Mục đích: Lưu vai trò người dùng
- Field chính:
- id
- code
- name
- description

### 2.3. customer_profiles

- Mục đích: Lưu thông tin mở rộng cho khách hàng
- Field chính:
- id
- user_id
- note

### 2.4. customer_addresses

- Mục đích: Lưu nhiều địa chỉ giao hàng cho một khách hàng
- Field chính:
- id
- customer_user_id
- receiver_name
- receiver_phone
- address_line
- ward
- district
- province
- is_default
- note
- created_at
- updated_at

### 2.5. product_categories

- Mục đích: Phân loại sản phẩm
- Field chính:
- id
- name
- description
- status

### 2.6. products

- Mục đích: Lưu toàn bộ mặt hàng của hệ thống, bao gồm sản phẩm bán thường và khung tranh dùng cho đơn custom
- Field chính:
- id
- category_id
- name
- slug
- description
- price
- product_kind
- is_sellable_directly
- is_custom_selectable
- image_url
- size
- material
- flower_type
- status
- created_by
- created_at
- updated_at

- Ghi chú:
- `product_kind` dùng để phân biệt `standard_product` và `frame_option`
- `is_sellable_directly = false` cho khung tranh
- `is_custom_selectable = true` cho khung tranh được phép chọn khi tạo đơn custom

### 2.7. product_images

- Mục đích: Lưu nhiều ảnh cho một mặt hàng
- Field chính:
- id
- product_id
- image_url
- sort_order

- CẦN BỔ SUNG: Requirement chưa chốt một mặt hàng có một ảnh hay nhiều ảnh

## 3. Nhóm bảng đơn hàng thường

### 3.1. available_orders

- Mục đích: Lưu đơn hàng sản phẩm bán thường
- Field chính:
- id
- order_code
- customer_user_id
- shipping_address_id
- order_status
- payment_status
- total_amount
- assigned_staff_id (CẦN BỔ SUNG: nhân viên được gán xử lý đơn này, nullable)
- ordered_at
- completed_at
- canceled_at
- note

### 3.2. available_order_items

- Mục đích: Lưu chi tiết mặt hàng trong đơn thường
- Field chính:
- id
- available_order_id
- product_id
- quantity
- unit_price
- subtotal

- Ghi chú:
- Chỉ áp dụng cho `products.product_kind = standard_product`

### 3.3. available_delivery_records

- Mục đích: Theo dõi giao nhận của đơn thường
- Field chính:
- id
- available_order_id
- delivery_status
- shipped_time
- delivered_time
- receiver_note

### 3.4. available_order_status_histories

- Mục đích: Lưu lịch sử chuyển trạng thái đơn thường
- Field chính:
- id
- available_order_id
- from_status
- to_status
- changed_by
- changed_at
- reason

### 3.5. available_order_payments

- Mục đích: Lưu các lần thanh toán của đơn hàng thường (tách khỏi `available_orders`)
- Field chính:
- id
- available_order_id
- payment_method
- amount
- payment_status
- transaction_ref
- payment_proof_url
- paid_at
- created_at
- note

- Ghi chú:
- `available_orders.payment_status` là trạng thái tổng hợp để lọc nhanh; nguồn dữ liệu thanh toán nằm ở bảng này
- CẦN BỔ SUNG: Nếu tích hợp cổng thanh toán/webhook thì bổ sung thêm `provider`, `raw_payload`, `verified_at`... khi requirement chốt

## 4. Nhóm bảng đơn hàng custom

### 4.1. custom_orders

- Mục đích: Lưu đơn hàng custom
- Field chính:
- id
- order_code
- customer_user_id
- shipping_address_id
- selected_frame_product_id
- order_status
- payment_status
- deposit_amount
- remaining_amount
- total_amount
- flower_type
- personalization_content
- requested_delivery_date
- flower_input_image_url
- flower_evaluation_status
- flower_evaluation_note
- assigned_staff_id (CẦN BỔ SUNG: nhân viên được gán xử lý đơn này, nullable)
- demo_revision_count
- extra_revision_fee_rate
- ordered_at
- completed_at
- canceled_at
- note

- Ghi chú:
- `selected_frame_product_id` tham chiếu tới bảng `products`
- Chỉ chấp nhận `products.product_kind = frame_option`

### 4.2. custom_demos

- Mục đích: Lưu từng phiên bản demo của đơn custom
- Field chính:
- id
- custom_order_id
- version_no
- demo_image_url
- demo_description
- customer_response_status
- customer_feedback
- uploaded_by
- uploaded_at
- responded_at

### 4.3. custom_delivery_records

- Mục đích: Theo dõi giao nhận của đơn custom
- Field chính:
- id
- custom_order_id
- delivery_type
- delivery_status
- pickup_time
- shipped_time
- delivered_time
- receiver_note

- Ghi chú:
- Dùng cho cả nhận hoa đầu vào và giao thành phẩm

### 4.4. custom_order_status_histories

- Mục đích: Lưu lịch sử chuyển trạng thái đơn custom
- Field chính:
- id
- custom_order_id
- from_status
- to_status
- changed_by
- changed_at
- reason

### 4.5. custom_order_payments

- Mục đích: Lưu thanh toán đặt cọc và thanh toán phần còn lại của đơn custom (tách khỏi `custom_orders`)
- Field chính:
- id
- custom_order_id
- payment_stage
- payment_method
- amount
- payment_status
- transaction_ref
- payment_proof_url
- paid_at
- created_at
- note

- Ghi chú:
- `payment_stage` dùng để phân biệt `deposit` và `remaining`
- CẦN BỔ SUNG: Nếu sau này có nhiều lần thanh toán/hoàn tiền/đối soát thì cần mở rộng theo hướng nhiều record theo thời gian (không chỉ 2 stage cố định)

## 5. Nhóm bảng nhập hàng và tồn kho

### 5.1. purchase_receipts

- Mục đích: Lưu phiếu nhập hàng
- Field chính:
- id
- receipt_code
- receipt_date
- created_by
- note
- created_at

### 5.2. purchase_receipt_items

- Mục đích: Lưu chi tiết từng mặt hàng trong phiếu nhập
- Field chính:
- id
- purchase_receipt_id
- product_id
- quantity
- unit_cost
- subtotal

- Ghi chú:
- Một dòng phiếu nhập chỉ tham chiếu `product_id`
- Khung tranh cũng được nhập qua bảng này vì đã gộp vào `products`

### 5.3. inventory_items

- Mục đích: Lưu tồn kho hiện tại của từng mặt hàng
- Field chính:
- id
- product_id
- quantity_on_hand
- updated_at

### 5.4. inventory_transactions

- Mục đích: Lưu biến động tồn kho để truy vết nhập/xuất
- Field chính:
- id
- product_id
- transaction_type
- quantity_change
- reference_type
- reference_id
- note
- created_by
- created_at

- Ghi chú:
- `reference_type` có thể tham chiếu tới phiếu nhập, đơn thường, đơn custom hoặc điều chỉnh tồn kho

## 6. Bảng hỗ trợ quản trị

### 6.1. audit_logs

- Mục đích: Theo dõi thao tác quản trị quan trọng
- Field chính:
- id
- actor_user_id
- action
- target_type
- target_id
- payload_summary
- created_at

- CẦN BỔ SUNG: Requirement không nêu trực tiếp, nhưng nên có để kiểm soát dữ liệu quan trọng

## 7. Quan hệ giữa các bảng

- roles 1-n users
- users 1-0..1 customer_profiles
- users 1-n customer_addresses
- product_categories 1-n products
- products 1-n product_images

- users 1-n available_orders
- customer_addresses 1-n available_orders
- available_orders 1-n available_order_items
- products 1-n available_order_items
- available_orders 1-n available_delivery_records
- available_orders 1-n available_order_status_histories
- available_orders 1-n available_order_payments

- users 1-n custom_orders
- customer_addresses 1-n custom_orders
- products 1-n custom_orders qua `selected_frame_product_id`
- custom_orders 1-n custom_demos
- custom_orders 1-n custom_delivery_records
- custom_orders 1-n custom_order_status_histories
- custom_orders 1-n custom_order_payments

- purchase_receipts 1-n purchase_receipt_items
- products 1-n purchase_receipt_items
- products 1-n inventory_items
- products 1-n inventory_transactions
- users 1-n audit_logs

## 8. Dữ liệu bắt buộc cần có

- Role:
- admin
- staff
- customer

- Product kind:
- standard_product
- frame_option

- Available order status:
- received
- processing
- shipping
- completed
- canceled

- Custom order status:
- deposited
- waiting_flower_review
- in_progress
- waiting_demo_feedback
- waiting_remaining_payment
- completed
- canceled

- Inventory transaction type:
- import
- reserve
- export
- adjust

- Custom payment stage:
- deposit
- remaining

- Payment status:
- CẦN BỔ SUNG: Danh sách trạng thái thanh toán chính thức (cần thống nhất cho cả `*_orders.payment_status` và `*_order_payments.payment_status`)

## 9. Quy tắc dữ liệu quan trọng

- Mặt hàng là khung tranh phải có:
- `product_kind = frame_option`
- `is_sellable_directly = false`
- `is_custom_selectable = true`

- Mặt hàng là sản phẩm bán thường phải có:
- `product_kind = standard_product`
- `is_sellable_directly = true`
- `is_custom_selectable = false` hoặc theo nhu cầu nghiệp vụ

- Đơn thường chỉ được chứa mặt hàng bán trực tiếp
- Đơn custom chỉ được chọn khung tranh từ bảng `products`
- Tồn kho khung tranh và sản phẩm được quản lý chung nhưng phân biệt bằng `product_kind`
- Thanh toán đơn thường được lưu trong `available_order_payments` (đơn chỉ giữ `payment_status` tổng hợp)
- Thanh toán đặt cọc và thanh toán phần còn lại của đơn custom được lưu trong `custom_order_payments` (đơn chỉ giữ `payment_status` tổng hợp)

## 10. Điểm chưa rõ từ requirement

- CẦN BỔ SUNG: Khi tạo đơn custom có cần giữ chỗ tồn kho khung ngay lúc đặt cọc hay chỉ trừ khi bắt đầu thực hiện
- CẦN BỔ SUNG: Chính sách soft delete hay hard delete cho người dùng/mặt hàng
- CẦN BỔ SUNG: Có cần bảng coupon/discount hay không
- CẦN BỔ SUNG: Cần lưu chứng từ thanh toán dạng file ảnh hay chỉ mã giao dịch (nếu có ảnh thì lưu ở `*_order_payments.payment_proof_url`)
- CẦN BỔ SUNG: Có cần bảng nhà cung cấp cho phiếu nhập hàng hay chưa
- CẦN BỔ SUNG: Nếu có hoàn tiền thì thiết kế bảng riêng (ví dụ `order_refunds`) hay lưu chung trong `*_order_payments` với `payment_type = refund`
