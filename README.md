# 🧀 Maze Chase Game – Tom & Jerry

## 📌 Giới thiệu
**Maze Chase Game – Tom & Jerry** là một trò chơi đuổi bắt theo lượt trong mê cung, lấy cảm hứng từ Tom & Jerry.  
Người chơi điều khiển **Jerry**, có nhiệm vụ thu thập toàn bộ **Cheese** trên bản đồ và tìm đường đến **EXIT**, trong khi **Tom** được điều khiển bởi AI với mục tiêu bắt Jerry trước khi nhiệm vụ hoàn thành.

Trò chơi tập trung vào yếu tố chiến thuật, quản lý lượt đi, sử dụng vật phẩm hợp lý và tránh né AI trong môi trường mê cung dạng ma trận.

---

## 🎯 Mục tiêu nhân vật

### 🐭 Jerry (Người chơi)
- Thu thập **tất cả Cheese** trên bản đồ
- Di chuyển đến **EXIT** để chiến thắng
- Có **3 mạng**
- Có thể sử dụng các vật phẩm hỗ trợ

### 🐱 Tom (AI)
- Di chuyển tự động
- Có tốc độ cao hơn Jerry
- Mục tiêu là **bắt được Jerry**

---

## 🎮 Luật chơi

### 🔄 Cơ chế lượt
- Trò chơi diễn ra theo **lượt**
  - Jerry di chuyển trước
  - Tom di chuyển sau
- **Thời gian mỗi lượt: 10 giây**
  - Hết thời gian → tự động đổi lượt

### 🚶‍♂️ Tốc độ di chuyển
| Nhân vật | Tốc độ |
|--------|--------|
| Jerry  | 1 ô / lượt |
| Tom    | 2 ô / lượt |

---

## 💥 Bắt và mạng sống
- Tom bắt được Jerry khi **cả hai đứng cùng một ô**
- Mỗi lần Jerry bị bắt:
  - Mất **1 mạng**
  - Tom và Jerry quay về **vị trí xuất phát**
  - Jerry tiếp tục thu thập số Cheese còn lại

---

## 🏁 Điều kiện kết thúc game

### ✅ Jerry thắng khi:
- Thu thập **tất cả Cheese**
- Đến **EXIT**
- Trước khi bị Tom bắt hết mạng

### ❌ Tom thắng khi:
- Bắt được Jerry **trước khi Jerry hoàn thành nhiệm vụ**

---

## 🗺️ Bản đồ & Môi trường
- Bản đồ là một **ma trận các ô**
- **Ô tường**: không thể di chuyển qua
- Các **vật phẩm (Item)** được đặt trên các ô trống
- Vật phẩm sẽ **biến mất sau khi được nhặt**

---

## 🎁 Vật phẩm (Items)

### 📋 Bảng tác dụng vật phẩm

| Nhân vật | SpeedBoost | Trap | Shield | Cheese |
|--------|-----------|------|--------|--------|
| **Tom** | Không áp dụng | Mất lượt kế tiếp | Bảo vệ khỏi Trap 1 lần | Không thể vào ô có Cheese |
| **Jerry** | +1 ô/lượt trong 3 lượt | Mất lượt kế tiếp | Bảo vệ Trap & không bị bắt 1 lần | Thu thập để chiến thắng |

---

### ⚡ SpeedBoost (Jerry)
- Khi đến lượt của Jerry:
  - Jerry được đi **thêm 1 ô**
- Hiệu lực trong **3 lượt di chuyển tiếp theo**

---

### 🕳️ Trap (Tom & Jerry)
- Khi giẫm phải Trap:
  - Nhân vật **mất lượt kế tiếp**
- Nếu đang có **Shield**:
  - Không bị mất lượt
  - Shield tự động biến mất sau khi bảo vệ

---

### 🛡️ Shield
- Bảo vệ nhân vật khỏi Trap
- Đối với **Jerry**:
  - Nếu Tom bắt kịp Jerry khi Jerry đang có Shield → **Jerry không bị bắt**
- Shield chỉ có hiệu lực **1 lần**

---

### 🧀 Cheese & Hang
- Mỗi Cheese tạo ra một **“hang”** có bán kính **1 ô**
- Hang có **nhiều lối vào**, tránh việc Tom đứng chặn
- Khi Jerry ăn Cheese:
  - Jerry được **+2 ô di chuyển ở lượt kế tiếp**

---

## 🧱 Công nghệ sử dụng
- Ngôn ngữ: **Java**
- Lập trình hướng đối tượng (OOP)
- AI di chuyển theo luật
- Mô hình bản đồ dạng ma trận

---

## 📁 Cấu trúc project (tham khảo)
```text
Maze-Chase-Game-Tom-Jerry/
│── src/
│   ├── model/
│   ├── controller/
│   ├── view/
│   └── ai/
│── assets/
│── README.md
│── .gitignore
