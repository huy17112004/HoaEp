# tech_stack.md

## 1. Nguyên tắc

- File này chỉ ghi những công nghệ đã được requirement xác nhận hoặc những điểm còn thiếu cần chốt
- Không tự suy đoán framework khi tài liệu chưa nêu

## 2. Công nghệ đã được xác nhận từ requirement

- Runtime/Platform:
- Node.js
- Lý do:
- Requirement có nêu `Node.js và npm` trong phần công cụ thực hiện đề tài

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

## 3. Công nghệ chưa được xác nhận và cần chốt

- Backend language:
- CẦN BỔ SUNG
- Requirement chỉ xác nhận Node.js, chưa chốt JavaScript hay TypeScript

- Backend framework:
- CẦN BỔ SUNG
- Requirement chưa nêu Express, NestJS, Fastify hoặc framework khác

- Frontend framework:
- CẦN BỔ SUNG
- Requirement chưa nêu React, Next.js, Vue, Angular hoặc SSR/CSR

- ORM/Query layer:
- CẦN BỔ SUNG
- Requirement chưa nêu Prisma, Sequelize, TypeORM hoặc query builder khác

- Authentication mechanism:
- CẦN BỔ SUNG
- Requirement chưa chốt JWT, session, cookie-based auth hay giải pháp khác

- File storage:
- CẦN BỔ SUNG
- Requirement có nhu cầu lưu ảnh sản phẩm, ảnh hoa đầu vào, ảnh demo nhưng chưa chốt nơi lưu

- Email service:
- CẦN BỔ SUNG
- Requirement có nhắc gửi email khi có demo custom nhưng chưa nêu nhà cung cấp

- Deployment target:
- CẦN BỔ SUNG
- Requirement chưa nêu môi trường triển khai, hosting hoặc cloud

## 4. Đề xuất mức nguyên tắc, chưa phải quyết định công nghệ

- Hệ thống nên dùng stack phù hợp với kiến trúc modular monolith
- Nên ưu tiên công nghệ phổ biến, tài liệu rõ, dễ bảo trì
- Nên ưu tiên công nghệ tương thích tốt với PostgreSQL
- Nên chốt sớm backend framework, frontend framework và cơ chế auth trước khi code

## 5. Quy tắc sử dụng file này

- Chỉ cập nhật thành công nghệ cụ thể khi:
- Có xác nhận chính thức từ người dùng
- Hoặc có quyết định kiến trúc được chấp thuận rõ ràng

- Sau khi chốt tech stack, phải đồng bộ lại:
- `system_architecture.md`
- `coding_convention.md`
- `api_contract.md`
