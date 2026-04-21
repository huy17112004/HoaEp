# coding_convention.md

## 1. Mục tiêu

- Giữ code nhất quán giữa nhiều người và nhiều lượt AI hỗ trợ
- Ưu tiên dễ đọc, dễ bảo trì, dễ mở rộng
- Không tối ưu hóa sớm bằng kỹ thuật phức tạp khi requirement chưa cần

## 2. Naming convention

- Class: `PascalCase`
- Interface/contract: `PascalCase`
- Method/function: `camelCase`
- Variable: `camelCase`
- Constant: `UPPER_SNAKE_CASE`
- File name:
- Backend source: `kebab-case` hoặc theo convention framework đã chọn
- Database table: `snake_case`
- Column name: `snake_case`
- API path: `kebab-case`
- Enum value:
- Mã nội bộ: `UPPER_SNAKE_CASE`
- Giá trị API nếu public: thống nhất một chuẩn và dùng xuyên suốt

## 3. Folder structure nguyên tắc

- Tách theo module nghiệp vụ trước, sau đó theo layer
- Ví dụ hướng tổ chức:
- auth
- users
- products
- available-orders
- custom-orders
- inventory
- purchase-receipts
- reports

- Trong mỗi module nên có tối thiểu:
- controller
- service
- repository
- dto
- entity hoặc domain model
- mapper nếu cần

## 4. Quy tắc viết code

- Mỗi function chỉ nên làm một việc rõ ràng
- Controller mỏng, không chứa business logic
- Service chứa nghiệp vụ
- Repository chỉ truy cập dữ liệu
- Không viết query database trực tiếp trong Controller
- Không hard-code giá trị nghiệp vụ rải rác
- Giá trị nghiệp vụ quan trọng phải gom vào constant, config hoặc enum
- Tránh duplicate logic giữa đơn thường và đơn custom
- Tách reusable component/helper nhưng không biến helper thành nơi chứa business logic lẫn lộn

## 5. DTO và mapping

- Dữ liệu request/response phải đi qua DTO hoặc contract rõ ràng
- Không trả thẳng entity database ra API nếu có nguy cơ lộ field nội bộ
- Mapping giữa entity và response model phải nhất quán

## 6. Validation

- Validate ở 2 mức:
- Input validation ở boundary request
- Business validation ở Service layer

- Message lỗi phải rõ, không mơ hồ
- Không bỏ qua validate chỉ vì frontend đã kiểm tra

## 7. Error handling

- Phân biệt rõ:
- Validation error
- Authentication error
- Authorization error
- Not found
- Business rule violation
- System error

- Không nuốt exception
- Log lỗi kỹ thuật đầy đủ
- Không trả stack trace cho client
- Mã lỗi phải đủ ổn định để frontend xử lý

## 8. Logging

- Log tại các điểm quan trọng:
- Đăng nhập thất bại
- Tạo đơn hàng
- Cập nhật trạng thái đơn hàng
- Upload demo custom
- Thanh toán
- Thao tác quản trị quan trọng

- Không log mật khẩu, token, dữ liệu nhạy cảm
- Log phải có context:
- actor
- action
- target
- timestamp
- result

## 9. Comment rule

- Chỉ comment khi logic khó hiểu hoặc có lý do nghiệp vụ cần ghi nhớ
- Không comment những điều code đã nói rõ
- Ưu tiên tên hàm/tên biến rõ nghĩa thay cho comment dài
- Nếu có business rule đặc biệt, comment phải bám đúng `business_flow.md`

## 10. Database convention

- Table dùng `snake_case`
- Primary key dùng `id`
- Foreign key dùng `{entity}_id`
- Timestamp chuẩn:
- created_at
- updated_at

- Trạng thái nên dùng enum/code nhất quán
- Không lưu dữ liệu suy diễn nếu có thể tính từ nguồn chính, trừ khi phục vụ báo cáo/performance đã xác định

## 11. API convention

- Response theo chuẩn ở `api_contract.md`
- Dùng HTTP method đúng ngữ nghĩa
- Không dùng một endpoint cho quá nhiều hành vi khác nhau
- Endpoint quản trị và endpoint khách hàng cần tách rõ phạm vi quyền

## 12. Testing convention

- Khi có code, phải ưu tiên test cho:
- Auth
- Tạo đơn hàng
- Chuyển trạng thái đơn hàng
- Thanh toán
- Demo custom

- CẦN BỔ SUNG: Chưa có quy định framework test cụ thể

## 13. Các điều cần khóa từ đầu

- Không đổi tên entity hoặc trạng thái nghiệp vụ tùy ý
- Không tạo schema khác với `database_design.md` nếu chưa cập nhật tài liệu
- Không viết code theo phong cách khác nhau giữa các module
