## 📁 1. Tạo file `secrets.properties`

Tạo file `secrets.properties` tại **root của project** (cùng cấp với `settings.gradle` và `build.gradle`).

**Cấu trúc thư mục:**
/your-project-root/
├── app/
├── build.gradle
├── secrets.properties ← tạo file này
└── settings.gradle

## ✍️ 2. Thêm thông tin vào `secrets.properties`

```properties
BACKEND_URL =http://10.0.2.2:8080/api/v1/
