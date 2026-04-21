# tech_stack.md

## 1. Nguyên tắc

- File này chỉ ghi những công nghệ đã được requirement xác nhận hoặc những điểm còn thiếu cần chốt
- Không tự suy đoán framework khi tài liệu chưa nêu

## 2. Công nghệ đã được xác nhận từ requirement

- Backend language:
- Java
- Lý do:
- Yêu cầu từ Product Owner

- Backend framework:
- Spring Boot
- Lý do:
- Yêu cầu từ Product Owner

- Backend ORM/Data access:
- Spring Data JPA / Hibernate
- Lý do:
- Mặc định được sử dụng với Spring Boot, tương thích tốt với PostgreSQL

- Frontend framework:
- React + TypeScript
- Location: `d:\HoaEpProject\FE\dear-floral-bloom`
- Lý do:
- Yêu cầu từ Product Owner

- Database:
- PostgreSQL
- Lý do:
- Requirement có nêu `Hệ quản trị cơ sở dữ liệu PostgreSQL`

- Development tool:
- Visual Studio Code
- Lý do:
- Requirement có nêu `Visual Studio Code (VS Code)` là công cụ thực hiện

- Diagram/Documentation tool:
- Draw.io
- Lý do:
- Requirement có nêu `Draw.io (diagrams.net)` để mô hình hóa hệ thống

- Authentication mechanism:
- JWT (JSON Web Token) + Spring Security
- Lý do:
- Phù hợp với Spring Boot backend, hỗ trợ stateless authentication, dễ tích hợp với REST API

- File storage:
- Local file system trong project directory
- Location: `/src/main/resources/uploads` (hoặc tương tự)
- Lý do:
- Yêu cầu lưu trữ trực tiếp trong project, không sử dụng external storage

- Email service:
- Gmail (SMTP)
- Configuration: Spring Mail + JavaMailSender
- Lý do:
- Yêu cầu từ Product Owner, dễ cấu hình với Spring Boot

## 3. Công nghệ chưa được xác nhận và cần chốt

- Deployment target:
- CẦN BỔ SUNG
- Requirement chưa nêu môi trường triển khai, hosting hoặc cloud

## 4. Đề xuất mức nguyên tắc, chưa phải quyết định công nghệ

- Backend:
  - Spring Boot ưu tiên Spring Data JPA cho data access layer
  - Dùng Spring Security + JWT cho authentication
  - Cấu hình Spring Mail với Gmail SMTP
  - Tạo `/uploads` folder trong project cho file storage

- Frontend:
  - React TypeScript ưu tiên component-based architecture
  - Sử dụng TypeScript strict mode
  - Quản lý state phù hợp (Context API, Redux, hoặc Zustand)

- Database:
  - PostgreSQL với connection pool (HikariCP mặc định trong Spring Boot)
  - Sử dụng migrations (Flyway hoặc Liquibase)

- Tích hợp:
  - REST API giữa Spring Boot backend và React frontend
  - CORS cấu hình phù hợp để React client có thể gọi API
  - Error handling thống nhất

## 5. Quy tắc sử dụng file này

- Chỉ cập nhật thành công nghệ cụ thể khi:
- Có xác nhận chính thức từ người dùng
- Hoặc có quyết định kiến trúc được chấp thuận rõ ràng

- Sau khi chốt tech stack, phải đồng bộ lại:
- `system_architecture.md`
- `coding_convention.md`
- `api_contract.md`
