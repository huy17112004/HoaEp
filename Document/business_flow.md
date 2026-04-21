# business_flow.md

## 1. Flow đăng ký tài khoản

- Actor: Khách hàng
- Input:
- Họ tên
- Số điện thoại
- Email
- Mật khẩu

- Step-by-step:
- Khách hàng mở form đăng ký
- Nhập thông tin bắt buộc
- Hệ thống kiểm tra dữ liệu hợp lệ
- Hệ thống tạo tài khoản khách hàng
- Hệ thống trả kết quả đăng ký thành công

- Validation:
- Email đúng định dạng
- Số điện thoại đúng định dạng
- Email chưa tồn tại
- Trường bắt buộc không được rỗng
- CẦN BỔ SUNG: Quy tắc độ dài và độ mạnh mật khẩu

- Output:
- Tài khoản khách hàng được tạo

## 2. Flow đăng nhập

- Actor: Khách hàng, Nhân viên, Quản trị viên
- Input:
- Email hoặc thông tin định danh đăng nhập
- Mật khẩu

- Step-by-step:
- Người dùng nhập thông tin đăng nhập
- Hệ thống xác thực tài khoản và mật khẩu
- Hệ thống xác định vai trò người dùng
- Hệ thống cấp phiên đăng nhập
- Hệ thống điều hướng tới khu vực phù hợp

- Validation:
- Tài khoản tồn tại
- Mật khẩu đúng
- Tài khoản còn hiệu lực

- Output:
- Người dùng đăng nhập thành công

## 3. Flow cập nhật hồ sơ cá nhân

- Actor: Khách hàng
- Input:
- Thông tin hồ sơ muốn thay đổi

- Step-by-step:
- Khách hàng mở trang hồ sơ
- Chỉnh sửa thông tin cá nhân
- Hệ thống kiểm tra dữ liệu hợp lệ
- Hệ thống lưu thay đổi
- Hệ thống trả hồ sơ đã cập nhật

- Validation:
- Các trường định danh đúng định dạng
- Không trùng email nếu email được phép đổi
- CẦN BỔ SUNG: Có cho phép đổi số điện thoại/email hay không

- Output:
- Hồ sơ được cập nhật

## 4. Flow quản lý địa chỉ giao hàng

- Actor: Khách hàng
- Input:
- Tên người nhận
- Số điện thoại người nhận
- Địa chỉ giao hàng
- Ghi chú địa chỉ
- Cờ địa chỉ mặc định

- Step-by-step:
- Khách hàng mở danh sách địa chỉ giao hàng
- Thêm mới, chỉnh sửa, xóa hoặc chọn địa chỉ mặc định
- Hệ thống kiểm tra dữ liệu hợp lệ
- Hệ thống lưu thay đổi
- Hệ thống trả danh sách địa chỉ đã cập nhật

- Validation:
- Một khách hàng có thể lưu nhiều địa chỉ
- Thông tin người nhận phải đầy đủ
- Số điện thoại đúng định dạng
- Chỉ một địa chỉ được đặt làm mặc định tại một thời điểm

- Output:
- Danh sách địa chỉ giao hàng của khách hàng được cập nhật

## 5. Flow xem và tìm kiếm sản phẩm

- Actor: Khách hàng
- Input:
- Từ khóa tìm kiếm
- Loại sản phẩm

- Step-by-step:
- Khách hàng truy cập danh sách sản phẩm
- Hệ thống tải danh sách sản phẩm đang hiển thị
- Khách hàng nhập từ khóa hoặc chọn loại
- Hệ thống lọc dữ liệu
- Hệ thống hiển thị danh sách và chi tiết sản phẩm

- Validation:
- Từ khóa tìm kiếm hợp lệ
- Loại sản phẩm hợp lệ nếu có lọc

- Output:
- Danh sách sản phẩm phù hợp

## 5A. Flow xem và chọn khung tranh cho đơn custom

- Actor: Khách hàng
- Input:
- Từ khóa tìm kiếm
- Thuộc tính khung tranh

- Step-by-step:
- Khách hàng mở khu vực tạo đơn custom
- Hệ thống hiển thị danh sách mặt hàng được phép chọn cho custom
- Khách hàng xem danh sách khung tranh phù hợp
- Khách hàng chọn một khung tranh
- Hệ thống gắn khung tranh đã chọn vào yêu cầu đơn custom

- Validation:
- Chỉ hiển thị mặt hàng có `is_custom_selectable = true`
- Mặt hàng được chọn phải tồn tại và đang hoạt động

- Output:
- Khung tranh được chọn cho đơn custom

## 6. Flow đặt hàng sản phẩm có sẵn

- Actor: Khách hàng, Nhân viên
- Input:
- Sản phẩm được chọn
- Số lượng
- Địa chỉ giao hàng đã lưu hoặc địa chỉ nhập mới
- Thông tin thanh toán

- Step-by-step:
- Khách hàng chọn sản phẩm có sẵn
- Hệ thống hiển thị thông tin sản phẩm và tổng tiền
- Khách hàng chọn địa chỉ giao hàng đã lưu hoặc nhập địa chỉ mới
- Hệ thống hiển thị mã QR thanh toán
- Khách hàng thực hiện thanh toán
- Hệ thống kiểm tra trạng thái giao dịch
- Nếu thanh toán thất bại, hệ thống yêu cầu thanh toán lại
- Nếu thanh toán thành công, hệ thống tạo đơn hàng và lưu thông tin thanh toán ở bảng `available_order_payments`, đồng thời cập nhật `payment_status` tổng hợp trong đơn
- Hệ thống lưu thông tin đơn hàng
- Hệ thống gửi thông tin đơn tới nhân viên xử lý

- Validation:
- Sản phẩm tồn tại
- Sản phẩm phải là mặt hàng được bán trực tiếp
- Số lượng hợp lệ
- Thông tin nhận hàng đầy đủ
- Tồn kho sản phẩm phải đủ để tạo đơn
- Thanh toán thành công trước khi xác nhận đơn

- Output:
- Đơn hàng sản phẩm có sẵn được tạo

## 7. Flow đặt hàng sản phẩm custom

- Actor: Khách hàng, Nhân viên
- Input:
- Mặt hàng khung tranh được chọn
- Loại hoa
- Nội dung cá nhân hóa
- Thời gian mong muốn nhận hàng
- Hình ảnh hoa thực tế
- Địa chỉ giao hàng đã lưu hoặc địa chỉ nhập mới
- Thông tin đặt cọc

- Step-by-step:
- Khách hàng chọn khung tranh cho đơn custom từ danh sách mặt hàng cho phép
- Khách hàng nhập yêu cầu thiết kế và thông tin liên quan
- Khách hàng tải hình ảnh hoa thực tế
- Hệ thống kiểm tra tồn kho khung tranh được chọn
- Hệ thống hiển thị mức đặt cọc theo niêm yết
- Hệ thống tạo đơn custom (trạng thái chờ đặt cọc)
- Khách hàng thanh toán đặt cọc
- Hệ thống tạo bản ghi thanh toán đặt cọc trong `custom_order_payments` và cập nhật `payment_status` tổng hợp của đơn
- Hệ thống thông báo cho nhân viên
- Nhân viên đánh giá chất lượng hoa đầu vào
- Nếu hoa không đạt yêu cầu:
- Hệ thống cập nhật đơn sang trạng thái hủy
- Hệ thống gửi thông báo hủy đơn
- Hệ thống xử lý hoàn cọc theo chính sách
- Nếu hoa đạt yêu cầu:
- Hệ thống cập nhật đơn sang trạng thái đang thực hiện

- Validation:
- Thông tin yêu cầu custom phải đầy đủ
- Khung tranh phải là một mặt hàng hợp lệ, có `is_custom_selectable = true` và còn đủ tồn kho để nhận đơn
- Có hình ảnh hoa thực tế
- Đặt cọc thành công
- Trạng thái hoa đầu vào phải được nhân viên đánh giá
- CẦN BỔ SUNG: Mức đặt cọc cụ thể
- CẦN BỔ SUNG: Chính sách hoàn cọc cụ thể

- Output:
- Đơn custom được tạo và chuyển sang trạng thái phù hợp

## 8. Flow gửi demo và phản hồi demo custom

- Actor: Nhân viên, Khách hàng
- Input:
- Hình ảnh demo
- Mô tả demo
- Phản hồi chấp nhận hoặc yêu cầu chỉnh sửa
- Nội dung chỉnh sửa

- Step-by-step:
- Nhân viên hoàn thành demo
- Nhân viên cập nhật demo lên hệ thống
- Hệ thống gửi email thông báo cho khách hàng
- Khách hàng truy cập website để xem demo
- Nếu khách hàng chấp nhận demo:
- Hệ thống hiển thị thông tin thanh toán phần còn lại
- Chuyển sang flow thanh toán hoàn tất đơn
- Nếu khách hàng chưa chấp nhận:
- Khách hàng nhập yêu cầu chỉnh sửa
- Hệ thống ghi nhận yêu cầu và gửi cho nhân viên
- Nhân viên cập nhật demo mới
- Quy trình lặp lại cho đến khi khách hàng chấp nhận
- Nếu số lần chỉnh sửa vượt quá 3:
- Hệ thống cộng thêm 10% giá trị đơn hàng trước khi tiếp tục

- Validation:
- Demo phải gắn với đúng đơn custom
- Chỉ cho phép phản hồi khi demo đang ở trạng thái chờ duyệt
- Số lần chỉnh sửa phải được đếm chính xác

- Output:
- Demo được chấp nhận hoặc được yêu cầu chỉnh sửa

## 9. Flow thanh toán phần còn lại của đơn custom

- Actor: Khách hàng
- Input:
- Đơn custom đã được duyệt demo
- Thông tin thanh toán phần còn lại

- Step-by-step:
- Hệ thống hiển thị số tiền còn lại phải thanh toán
- Khách hàng thực hiện thanh toán
- Hệ thống kiểm tra giao dịch
- Nếu thanh toán thành công, hệ thống tạo bản ghi thanh toán ở bảng `custom_order_payments`, đồng thời cập nhật `payment_status` tổng hợp trong đơn custom
- Hệ thống cho phép tiếp tục hoàn thiện/giao đơn

- Validation:
- Đơn phải ở trạng thái demo đã được chấp nhận
- Số tiền thanh toán còn lại phải đúng
- Giao dịch phải thành công

- Output:
- Đơn custom được ghi nhận đã thanh toán đủ hoặc đã thanh toán phần còn lại

## 10. Flow quản lý người dùng

- Actor: Quản trị viên
- Input:
- Thông tin tài khoản
- Vai trò người dùng

- Step-by-step:
- Quản trị viên mở danh sách người dùng
- Thêm mới hoặc chỉnh sửa thông tin
- Gán vai trò cho tài khoản
- Hệ thống lưu dữ liệu
- Hệ thống trả danh sách đã cập nhật

- Validation:
- Vai trò hợp lệ
- Email không trùng
- Không cho phép xóa dữ liệu quan trọng trái quyền
- CẦN BỔ SUNG: Có cho phép nhân viên tự đăng ký hay chỉ do admin tạo

- Output:
- Người dùng được tạo/cập nhật/xóa theo quyền

## 11. Flow quản lý mặt hàng

- Actor: Nhân viên, Quản trị viên
- Input:
- Tên mặt hàng
- Mô tả
- Giá
- Hình ảnh
- Loại mặt hàng
- Kích thước
- Chất liệu
- Loại hoa sử dụng
- Cờ bán trực tiếp
- Cờ được chọn cho custom
- Trạng thái sản phẩm

- Step-by-step:
- Người có quyền mở danh sách mặt hàng
- Thêm mới hoặc chỉnh sửa thông tin mặt hàng
- Hệ thống kiểm tra dữ liệu
- Hệ thống lưu dữ liệu
- Hệ thống cập nhật trạng thái hiển thị

- Validation:
- Tên mặt hàng bắt buộc
- Giá hợp lệ
- Loại mặt hàng hợp lệ
- Hình ảnh đúng định dạng
- Nếu là khung tranh thì không được bật bán trực tiếp
- Nếu là khung tranh dùng cho custom thì phải bật cờ chọn cho custom

- Output:
- Mặt hàng được tạo/cập nhật/xóa

## 12. Flow quản lý phiếu nhập hàng

- Actor: Nhân viên, Quản trị viên
- Input:
- Loại phiếu nhập
- Danh sách mặt hàng nhập
- Số lượng
- Đơn giá nhập
- Ngày nhập
- Ghi chú

- Step-by-step:
- Người có quyền tạo phiếu nhập hàng
- Chọn danh sách mặt hàng cần nhập
- Hệ thống kiểm tra dữ liệu từng dòng
- Hệ thống tạo phiếu nhập
- Hệ thống cập nhật tồn kho cho từng mặt hàng
- Hệ thống lưu lịch sử nhập hàng

- Validation:
- Mặt hàng phải tồn tại trong hệ thống
- Số lượng nhập phải lớn hơn 0
- Đơn giá nhập hợp lệ
- Không cho phép phiếu nhập rỗng

- Output:
- Phiếu nhập hàng được tạo và tồn kho được cập nhật

## 13. Flow quản lý tồn kho

- Actor: Nhân viên, Quản trị viên
- Input:
- Bộ lọc mặt hàng
- Loại mặt hàng hoặc cờ bán hàng/custom

- Step-by-step:
- Người có quyền mở màn hình tồn kho
- Hệ thống hiển thị tồn kho hiện tại của mặt hàng
- Người dùng tìm kiếm hoặc lọc theo loại mặt hàng
- Hệ thống trả dữ liệu tồn kho

- Validation:
- Chỉ hiển thị dữ liệu cho người có quyền
- Số lượng tồn kho phải được tính nhất quán từ các nghiệp vụ nhập hàng và xuất dùng

- Output:
- Danh sách tồn kho theo mặt hàng

## 14. Flow quản lý đơn hàng thường

- Actor: Nhân viên, Quản trị viên
- Input:
- Bộ lọc tìm kiếm
- Trạng thái đơn hàng mới

- Step-by-step:
- Người có quyền xem danh sách đơn hàng
- Tìm kiếm đơn theo tiêu chí
- Mở chi tiết đơn
- Cập nhật trạng thái đơn hàng
- Hệ thống lưu lịch sử trạng thái

- Validation:
- Chỉ cho phép chuyển trạng thái hợp lệ
- Chỉ người có quyền mới được cập nhật
- Không cập nhật trạng thái trái thứ tự nghiệp vụ
- CẦN BỔ SUNG: State machine chính thức cho từng loại đơn

- Output:
- Đơn hàng được cập nhật trạng thái

## 15. Flow quản lý đơn hàng custom

- Actor: Nhân viên, Quản trị viên
- Input:
- Bộ lọc tìm kiếm
- Trạng thái đơn hàng custom mới

- Step-by-step:
- Người có quyền xem danh sách đơn hàng custom
- Tìm kiếm đơn theo tiêu chí
- Mở chi tiết đơn
- Cập nhật trạng thái đơn hàng custom
- Hệ thống lưu lịch sử trạng thái
- Hệ thống đồng bộ trạng thái thanh toán, demo và giao nhận liên quan nếu cần

- Validation:
- Chỉ cho phép chuyển trạng thái hợp lệ của đơn custom
- Chỉ người có quyền mới được cập nhật
- Không cập nhật trạng thái trái thứ tự nghiệp vụ

- Output:
- Đơn hàng custom được cập nhật trạng thái

## 16. Flow báo cáo và thống kê

- Actor: Quản trị viên
- Input:
- Khoảng thời gian
- Loại báo cáo

- Step-by-step:
- Quản trị viên chọn loại báo cáo
- Nhập khoảng thời gian
- Hệ thống tổng hợp dữ liệu
- Hệ thống hiển thị báo cáo tổng quan hoặc doanh thu hoặc số lượng đơn

- Validation:
- Khoảng thời gian hợp lệ
- Chỉ vai trò quản trị được truy cập

- Output:
- Báo cáo tổng quan
- Báo cáo doanh thu
- Thống kê số lượng đơn hàng
- Thống kê tồn kho theo mặt hàng

## 17. Danh sách trạng thái nghiệp vụ đã xác nhận

- Trạng thái đơn hàng có sẵn:
- Đã tiếp nhận
- Đang xử lý
- Đang giao
- Đã hoàn thành
- Hủy

- Trạng thái đơn custom:
- Đã tạo/đã đặt cọc
- Chờ đánh giá hoa đầu vào
- Đang thực hiện
- Chờ duyệt demo
- Chờ thanh toán phần còn lại
- Hoàn thành
- Hủy

- Trạng thái trên là suy ra từ requirement mô tả
- CẦN BỔ SUNG: Danh sách trạng thái chính thức để khóa API và database
