# PassBookApp (Sổ Tiết Kiệm)

![Kotlin](https://img.shields.io/badge/Kotlin-1.9.0-blue.svg?logo=kotlin)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-1.3.2-4CAF50.svg?logo=android)
![Dagger Hilt](https://img.shields.io/badge/Dagger%20Hilt-2.51.1-green.svg)
![Architecture](https://img.shields.io/badge/Architecture-MVVM%20%2B%20Clean-orange.svg)

PassBookApp là một ứng dụng di động quản lý sổ tiết kiệm số, hỗ trợ đầy đủ các nghiệp vụ dành cho cả phía ngân hàng (Admin) và người dùng (Customer). Ứng dụng được thiết kế dựa trên tiêu chuẩn kiến trúc hiện đại của Android (Clean Architecture, MVVM) cùng Jetpack Compose.

## 🚀 Tính năng nổi bật (Features)

Dự án sử dụng cơ chế **Product Flavors** để phân chia ứng dụng thành 2 phiên bản độc lập:

### 👤 Customer App (Khách hàng & Nghiệp vụ chính)
*   **Trang chủ (Home):** Tổng quan tài khoản và số dư.
*   **Loại tiết kiệm (Saving Type):** Xem thông tin và cấu hình các loại tiết kiệm khác nhau (Không kỳ hạn, 3 tháng, 6 tháng...).
*   **Sổ tiết kiệm (Saving Ticket):** Mở sổ tiết kiệm mới, xem danh sách và chi tiết các sổ đang sở hữu.
*   **Giao dịch (Transaction):** Thực hiện các giao dịch gửi tiền và rút tiền trực tiếp trên ứng dụng.
*   **Báo cáo (Report):** Thống kê, báo cáo hoạt động thu chi, doanh số tháng/ngày.
*   **Quy định (Parameters):** Tra cứu và xem các tham số quy định chung của hệ thống.

### 🛡️ Admin App (Quản trị viên)
*   **Quản lý người dùng (User Manage):** Quản lý thông tin tài khoản khách hàng.
*   **Phân quyền (Permission):** Thiết lập và phân quyền truy cập cho nhân viên/admin.

## 🛠️ Công nghệ sử dụng (Tech Stack)

*   **Ngôn ngữ:** Kotlin
*   **UI Toolkit:** Jetpack Compose, Material3
*   **Kiến trúc:** MVVM (Model-View-ViewModel), Clean Architecture
*   **Dependency Injection:** Dagger Hilt
*   **Mạng (Network):** Retrofit2, OkHttp3 (với Logging Interceptor)
*   **Cơ sở dữ liệu (Local DB):** Room Database, DataStore Preferences
*   **Phân trang (Pagination):** Paging 3
*   **Load ảnh:** Coil Compose
*   **Navigation:** Navigation Compose, Hilt Navigation
*   **UI Components bổ sung:** Accompanist (Permissions, System UI), YCharts (Biểu đồ thống kê), Swipe Action, Animated Navigation Bar.

## 🛡️ Kiến trúc hệ thống (Architecture)

Dự án tuân thủ chặt chẽ nguyên tắc **Clean Architecture** kết hợp với pattern **MVVM**, nhằm đảm bảo tính tái sử dụng, dễ bảo trì và phân tách rõ ràng trách nhiệm của từng tầng:

*   **UI Layer (Presentation):** Nơi chứa toàn bộ code Jetpack Compose, các màn hình (`screen`), và ViewModels. ViewModel đóng vai trò giữ trạng thái UI (StateHolder) và phản hồi lại các action từ người dùng.
*   **Domain Layer:** Trái tim của ứng dụng, chứa các Use Case (Nghiệp vụ cốt lõi), Entities, và định nghĩa các Interface cho Repository. Tầng này độc lập hoàn toàn với Android Framework.
*   **Data Layer:** Đảm nhiệm việc giao tiếp với dữ liệu bên ngoài (Backend API thông qua Retrofit) và lưu trữ cục bộ (Room Database, DataStore). Repository implementation nằm ở đây để cung cấp data cho tầng Domain.
*   **DI (Dependency Injection):** Quản lý toàn bộ vòng đời và khởi tạo các component thông qua Dagger Hilt.

## ⚙️ Hướng dẫn Cài đặt & Chạy (How to run)

1.  **Clone repository:**
    ```bash
    git clone https://github.com/your-username/PassBookApp.git
    cd PassBookApp
    ```

2.  **Cấu hình môi trường (Bắt buộc):**
    Dự án sử dụng file `secrets.properties` để bảo mật các thông tin nhạy cảm (như URL Backend). Không push file này lên Git.
    *   Tạo một file có tên `secrets.properties` ở thư mục gốc của project (ngang hàng với `build.gradle.kts`).
    *   Thêm nội dung sau vào file (thay thế URL bằng URL Backend thực tế của hệ thống Server):
        ```properties
        BACKEND_URL="https://your-api-url.com/api/"
        ```
    *   Nếu bạn thiếu file này, quá trình Gradle Sync và Build sẽ báo lỗi.

3.  **Build và Run:**
    *   Mở project bằng **Android Studio** (Phiên bản tương thích với AGP 8+ và Kotlin 1.9+).
    *   Đợi Gradle sync hoàn tất các dependencies.
    *   Mở panel **Build Variants** ở góc trái màn hình (hoặc View -> Tool Windows -> Build Variants).
    *   Chọn Flavor muốn chạy: 
        *   `customerDebug`: Để chạy app dành cho khách hàng.
        *   `adminDebug`: Để chạy app dành cho quản trị viên.
    *   Nhấn Run (Shift + F10) hoặc chọn nút Play để cài đặt lên máy ảo (Emulator) hoặc thiết bị Android thật.


