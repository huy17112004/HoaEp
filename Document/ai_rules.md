# ai_rules.md

## 1. Mục tiêu file này

- Đây là file ràng buộc bắt buộc cho mọi AI tham gia phát triển dự án
- Mọi thay đổi code, schema, API, flow đều phải tuân theo file này

## 2. Quy tắc bất biến

- Không được tự ý thay đổi kiến trúc đã chốt trong `system_architecture.md`
- Không được tạo business logic ngoài phạm vi `business_flow.md`
- Không được tự ý thêm field database hoặc bảng mới nếu không đối chiếu `database_design.md`
- Không được tạo API trái với `api_contract.md`
- Không được đổi format response chung
- Không được đổi tên role, trạng thái, entity khi chưa cập nhật toàn bộ context system

## 3. Quy tắc về requirement

- Chỉ triển khai những gì requirement đã xác nhận
- Nếu requirement chưa rõ, phải ghi `CẦN BỔ SUNG`
- Khi thiếu thông tin để quyết định logic nghiệp vụ, phải hỏi lại
- Không được tự suy đoán chính sách kinh doanh, quy trình thanh toán, hoàn cọc, vận chuyển

## 4. Quy tắc về kiến trúc

- Mọi nghiệp vụ phải đi qua Service layer
- Controller không được chứa business logic
- Repository không được chứa rule nghiệp vụ
- Domain không phụ thuộc vào framework/web layer
- Không được gọi database trực tiếp từ route/controller/view

## 5. Quy tắc về business logic

- Không duplicate logic giữa đơn hàng có sẵn và đơn custom
- Không để logic tồn kho nằm rải rác ở nhiều controller/service không liên quan
- Các rule về thanh toán, chỉnh sửa demo, trạng thái đơn hàng phải tái sử dụng ở một nơi trung tâm trong module đơn hàng tương ứng
- Các rule nhập hàng và cộng/trừ tồn kho phải tái sử dụng ở một nơi trung tâm
- Không được tách riêng khung tranh thành domain dữ liệu độc lập nếu `database_design.md` đang quy định khung là một loại mặt hàng trong `products`
- Không được tạo shortcut bỏ qua validation nghiệp vụ
- Không được chuyển trạng thái đơn hàng trái flow đã mô tả

## 6. Quy tắc về API

- Mọi endpoint mới phải có lý do nghiệp vụ rõ ràng
- Mọi request/response phải theo contract
- Mọi API phải kiểm tra quyền truy cập theo role
- Không trả field nội bộ hoặc dữ liệu nhạy cảm ra ngoài

## 7. Quy tắc về database

- Không tạo bảng/thêm cột chỉ vì tiện cho code nếu chưa có lý do nghiệp vụ
- Không gộp nhiều ý nghĩa nghiệp vụ khác nhau vào cùng một field mơ hồ
- Trạng thái phải dùng enum/code ổn định
- Dữ liệu quan trọng phải có lịch sử hoặc audit nếu thay đổi ảnh hưởng vận hành

## 8. Quy tắc về tái sử dụng

- Phải ưu tiên reuse code hiện có
- Không copy-paste logic giống nhau ở nhiều module
- Nếu thấy logic giống nhau từ 2 nơi trở lên, phải refactor về shared service/helper phù hợp
- Shared code không được chứa logic lẫn lộn giữa nhiều domain không liên quan

## 9. Quy tắc về thay đổi tài liệu

- Nếu thay đổi kiến trúc, API, flow, schema hoặc convention:
- Phải cập nhật các file context liên quan trong cùng một lần thay đổi
- Không được sửa code trước rồi để tài liệu lệch sau

- Tối thiểu phải rà soát:
- `project_overview.md`
- `system_architecture.md`
- `business_flow.md`
- `database_design.md`
- `api_contract.md`
- `coding_convention.md`
- `ai_rules.md`
- `tech_stack.md`

## 10. Quy tắc về xử lý thiếu thông tin

- Khi thiếu thông tin, không được tự chốt theo thói quen cá nhân
- Phải chọn một trong hai cách:
- Gắn `CẦN BỔ SUNG` trong tài liệu
- Hỏi lại người dùng nếu chuẩn bị code và quyết định đó ảnh hưởng kiến trúc hoặc business

## 11. Quy tắc về chất lượng đầu ra

- Code phải nhất quán naming
- Code phải nhất quán structure thư mục
- Code phải có validation
- Code phải có error handling rõ ràng
- Code phải có logging ở các điểm nghiệp vụ quan trọng
- Không để một module viết theo phong cách khác hoàn toàn module còn lại

## 12. Quy tắc kiểm tra trước khi implement

- Trước khi code một chức năng mới, AI phải kiểm tra:
- Chức năng này có nằm trong `project_overview.md` không
- Flow này có nằm trong `business_flow.md` không
- Dữ liệu đã có trong `database_design.md` chưa
- API đã được định nghĩa trong `api_contract.md` chưa
- Có vi phạm dependency rule trong `system_architecture.md` không

## 13. Ưu tiên khi có xung đột

- Requirement gốc
- `ai_rules.md`
- `business_flow.md`
- `api_contract.md`
- `database_design.md`
- `system_architecture.md`
- `coding_convention.md`
- Sở thích cá nhân của AI hoặc developer
