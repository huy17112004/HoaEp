# system_architecture.md

## 1. Kiến trúc được chọn

- Kiến trúc đề xuất: Modular Monolith + Layered Architecture
- Tổ chức tầng chính:
- Controller
- Service
- Repository
- Domain/Entity
- Lý do chọn:
- Phù hợp với quy mô bài toán hiện tại của Dear Floral
- Dễ triển khai, dễ bảo trì hơn microservices trong giai đoạn đầu
- Giữ business logic tập trung và nhất quán
- Thuận lợi cho AI và developer đọc hiểu, mở rộng theo module
- Có thể mở rộng sau này mà không phá vỡ kiến trúc nền

## 2. Nguyên tắc tổ chức module

- Mỗi module nghiệp vụ nên tách theo domain:
- Auth
- Users
- Products
- Categories
- AvailableOrders (quản trị đơn thường, chỉ staff/admin)
- CustomOrders (quản trị đơn custom, chỉ staff/admin)
- Delivery (quản lý giao nhận, chỉ staff/admin)
- Inventory (quản lý phiếu nhập và tồn kho, chỉ staff/admin)
- PurchaseReceipts (phiếu nhập hàng, chỉ staff/admin)
- Dashboard (dashboard tổng quan cho staff)
- Reports (báo cáo, chỉ admin)

- Ghi chú:
- `Products` quản lý chung mặt hàng bán thường và khung tranh qua thuộc tính phân loại
- `AvailableOrders` và `CustomOrders` là hai module tách biệt ở tầng dữ liệu và nghiệp vụ
- Dữ liệu thanh toán được tách ra bảng riêng theo từng nhóm đơn (`available_order_payments`, `custom_order_payments`) nhưng logic thanh toán vẫn được xử lý bên trong module đơn hàng tương ứng; chưa tách module `Payments` riêng

- Mỗi module giữ cấu trúc layer thống nhất
- Không viết logic chéo tắt giữa các module
- Giao tiếp giữa module thông qua service interface hoặc application service

## 3. Sơ đồ layer

- Controller layer:
- Nhận request
- Validate input mức giao tiếp
- Gọi service
- Trả response theo `api_contract.md`

- Service layer:
- Chứa business logic
- Điều phối workflow
- Kiểm tra rule nghiệp vụ
- Quản lý transaction
- Gọi repository và service liên quan

- Repository layer:
- Chỉ truy cập dữ liệu
- Không chứa business logic
- Chịu trách nhiệm query, insert, update, delete

- Domain/Entity layer:
- Mô hình hóa thực thể nghiệp vụ
- Chứa state và rule cốt lõi nếu có
- Không phụ thuộc framework web hay database cụ thể

## 4. Dependency rules

- Controller chỉ được phụ thuộc vào Service DTO/contract
- Service được phụ thuộc vào Domain và Repository abstraction
- Repository implementation được phụ thuộc vào database/ORM
- Domain không được phụ thuộc Controller, Service framework, Repository implementation
- Không cho phép Controller gọi Repository trực tiếp
- Không cho phép Repository gọi ngược Service
- Không cho phép business logic nằm trong Controller
- Không cho phép business logic nằm trong migration, view hoặc route
- Shared utilities chỉ chứa logic dùng chung thuần kỹ thuật, không chứa logic nghiệp vụ đặc thù

## 5. Quy tắc transaction

- Các thao tác thay đổi trạng thái đơn hàng, thanh toán, demo custom phải được xử lý trong boundary rõ ràng tại Service layer
- Các thao tác nhập hàng và cập nhật tồn kho phải được xử lý trong cùng transaction boundary tại Service layer
- Không cập nhật nhiều bảng nghiệp vụ phân tán từ Controller
- Mọi thay đổi trạng thái cần có điều kiện chuyển trạng thái hợp lệ

## 6. Quy tắc mở rộng

- Nếu có thêm module mới, phải khai báo rõ:
- Nghiệp vụ mới là gì
- Có thuộc phạm vi requirement hay là extension
- Ảnh hưởng tới entity nào
- Ảnh hưởng tới API nào

- Không tách microservice khi chưa có yêu cầu thực tế về scale, đội ngũ vận hành hoặc ranh giới domain đủ rõ

## 7. Kiến trúc triển khai mức khái niệm

- Frontend web cho khách hàng (`/FE` - public site)
- Frontend web cho nhân viên/quản trị viên (`/backoffice` - nội bộ)
  - Dashboard nhân viên: tổng quan đơn hàng, tồn kho, nhiệm vụ
  - Quản lý đơn hàng (thường và custom)
  - Quản lý sản phẩm/mặt hàng
  - Quản lý tồn kho và phiếu nhập
  - Báo cáo (chỉ admin)
- Backend application xử lý nghiệp vụ và API (Node.js + framework chưa xác định)
- Database quan hệ PostgreSQL lưu trữ dữ liệu tập trung
- Email/notification service:
  - CẦN BỔ SUNG: Requirement có nhắc gửi email khi có demo custom, nhưng chưa nêu rõ hạ tầng gửi mail

## 8. Phân quyền theo role tại backend

- Tất cả API phải kiểm tra role người dùng
- Các endpoint `/api/admin/*` chỉ cho phép `staff` và `admin`
- Các endpoint `/api/staff/*` chỉ cho phép `staff`
- Các endpoint `/api/orders/*` cho phép `customer` trên public site
- Không được bypass authorization ở bất cứ layer nào
- Mọi thao tác nhạy cảm phải log kèm actor và timestamp

## 9. Quyết định kiến trúc cố định cho AI

- Chỉ sử dụng một backend thống nhất cho toàn hệ thống
- Mọi logic nghiệp vụ phải đi qua Service layer
- Tất cả API phải tuân thủ response chung
- Mọi frontend cần riêng (customer vs staff) nhưng tuân theo quy tắc phân quyền backend
- Mọi thay đổi kiến trúc phải cập nhật đồng thời:
  - `project_overview.md`
  - `system_architecture.md`
  - `business_flow.md`
  - `database_design.md`
  - `api_contract.md`
  - `ai_rules.md`
