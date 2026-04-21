# project_overview.md

## 1. Tổng quan hệ thống

- Tên dự án: Website quản lý bán hàng cho tiệm hoa ép khô Dear Floral
- Loại hệ thống: Website gồm trang giới thiệu/bán hàng cho khách hàng và trang quản trị vận hành nội bộ
- Bài toán hiện tại:
- Dear Floral đang bán hàng chủ yếu qua Facebook, Instagram, TikTok và tin nhắn trực tiếp
- Thông tin khách hàng, sản phẩm, đơn hàng đang lưu rời rạc bằng Excel và thao tác thủ công
- Chưa có hệ thống tập trung để theo dõi đơn hàng, trạng thái xử lý, thanh toán và báo cáo
- Mục tiêu hệ thống:
- Số hóa quy trình bán hàng và quản lý thông tin
- Tập trung dữ liệu sản phẩm, khách hàng, đơn hàng
- Hỗ trợ khách hàng xem sản phẩm, đăng ký tài khoản và gửi yêu cầu đặt hàng
- Hỗ trợ nhân viên/quản trị viên quản lý vận hành, cập nhật trạng thái và theo dõi báo cáo

## 2. Actors

- Khách hàng:
- Đăng ký, đăng nhập, cập nhật hồ sơ
- Quản lý nhiều địa chỉ giao hàng
- Xem danh sách sản phẩm và chi tiết sản phẩm
- Tìm kiếm sản phẩm theo tên hoặc loại
- Đặt sản phẩm có sẵn
- Gửi yêu cầu sản phẩm custom
- Chọn khung tranh từ danh sách mặt hàng dùng cho custom
- Xem lại đơn hàng đã đặt
- Theo dõi demo custom và phản hồi chỉnh sửa
- Thanh toán đơn hàng hoặc thanh toán phần còn lại

- Nhân viên:
- Nhận thông tin đơn hàng từ hệ thống
- Quản lý mặt hàng trong hệ thống, bao gồm sản phẩm bán thường và khung tranh
- Quản lý đơn hàng thường và đơn hàng custom
- Đánh giá hoa đầu vào của đơn custom
- Cập nhật demo sản phẩm custom
- Theo dõi giao nhận
- Lập phiếu nhập hàng
- Theo dõi tồn kho mặt hàng

- Quản trị viên:
- Quản lý người dùng và phân quyền
- Quản lý mặt hàng
- Quản lý đơn hàng thường và đơn hàng custom
- Quản lý phiếu nhập hàng
- Quản lý tồn kho
- Theo dõi báo cáo, thống kê
- Kiểm soát dữ liệu quan trọng trong hệ thống

## 3. In-Scope

- Trang khách hàng:
- Đăng ký, đăng nhập, đăng xuất
- Cập nhật thông tin cá nhân
- Quản lý nhiều địa chỉ giao hàng
- Xem danh sách sản phẩm
- Xem chi tiết sản phẩm
- Tìm kiếm sản phẩm
- Đặt hàng sản phẩm có sẵn
- Gửi yêu cầu đặt hàng custom và chọn khung tranh từ danh sách mặt hàng dùng cho custom
- Xem lịch sử đơn hàng
- Xem demo custom và gửi yêu cầu chỉnh sửa

- Trang quản trị:
- Quản lý người dùng
- Phân quyền người dùng theo vai trò
- Quản lý mặt hàng:
- Sản phẩm bán trực tiếp
- Khung tranh dùng cho đơn custom
- Quản lý đơn hàng thường
- Quản lý đơn hàng custom
- Cập nhật trạng thái đơn hàng và thông tin thanh toán
- Quản lý phiếu nhập hàng
- Quản lý tồn kho mặt hàng
- Theo dõi giao nhận
- Báo cáo tổng quan
- Thống kê doanh thu
- Thống kê số lượng đơn hàng

- Trang nhân viên:
- Dashboard tổng quan (đơn hàng mới, tồn kho thấp, đơn custom chờ đánh giá)
- Quản lý đơn hàng thường (xem, cập nhật trạng thái, theo dõi giao nhận)
- Quản lý đơn hàng custom (đánh giá hoa đầu vào, cập nhật demo, gửi thông báo cho khách hàng)
- Quản lý mặt hàng (thêm/sửa/xóa sản phẩm bán thường và khung tranh)
- Quản lý phiếu nhập hàng (tạo phiếu nhập, cập nhật tồn kho)
- Theo dõi tồn kho mặt hàng
- Theo dõi giao nhận đơn hàng

## 4. Out-of-Scope

- Thanh toán online tích hợp cổng thanh toán cụ thể
- Tài liệu có nhắc QR và thanh toán, nhưng chưa xác định nhà cung cấp thanh toán cụ thể
- Tích hợp nền tảng bán hàng bên thứ ba
- Requirement chỉ nhắc như hướng mở rộng trong tương lai
- Mobile app riêng
- Không thấy nêu trong requirement
- Tự động logistics với đơn vị vận chuyển cụ thể
- Không thấy nêu rõ trong requirement

## 5. Mục tiêu nghiệp vụ

- Giảm phụ thuộc vào Excel và tin nhắn thủ công
- Chuẩn hóa quy trình tiếp nhận và xử lý đơn hàng
- Tăng khả năng tra cứu thông tin
- Cải thiện trải nghiệm khách hàng khi tham khảo và đặt hàng
- Hỗ trợ quản lý ra quyết định dựa trên dữ liệu báo cáo

## 6. Phạm vi dữ liệu chính

- Người dùng
- Vai trò
- Khách hàng
- Nhân viên
- Mặt hàng
- Danh mục/loại sản phẩm
- Đơn hàng thường
- Chi tiết đơn hàng thường
- Đơn hàng custom
- Yêu cầu custom
- Demo custom
- Giao nhận
- Phiếu nhập hàng
- Tồn kho
- Địa chỉ giao hàng
- Báo cáo thống kê

## 7. Điểm chưa rõ từ requirement

- CẦN BỔ SUNG: Có tách riêng nhân viên và quản trị viên thành loại tài khoản độc lập hay chỉ là vai trò của bảng người dùng
- CẦN BỔ SUNG: Có hỗ trợ giỏ hàng nhiều sản phẩm hay mỗi lần đặt tạo một đơn trực tiếp
- CẦN BỔ SUNG: Chính sách hủy đơn, hoàn cọc và thời điểm thanh toán phần còn lại
- CẦN BỔ SUNG: Cách xử lý vận chuyển, phí ship và đơn vị giao hàng
