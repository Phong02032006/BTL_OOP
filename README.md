# BTL_OOP – Arkanoid Game (JavaFX)

## Giới thiệu
BTL môn **Lập trình Hướng Đối Tượng (OOP)**, được phát triển bằng **Java + JavaFX**.  
Game **Arkanoid** mô phỏng lại gameplay cổ điển: người chơi điều khiển thanh paddle để bật bóng phá gạch, qua nhiều cấp độ với các hiệu ứng đặc biệt (Power-Up).

---
## Thành viên
- **Nguyễn Tiên Phong** : Làm các phần liên quan đến đối tượng(trừ Bricks), hệ thống powerup đa dạng, GameManager, xử lý load ảnh và âm thanh.
- **Nguyễn Minh Quân** : Làm các phần liên quan đến hệ thống gạch, các levels, xử lý va chạm, Main, Renderer.
- **Phạm Thế Hùng** : Thiết kế UI/hệ thống menu và xử lý phần load/tải game.
- Các file ở thư mục core được hoàn thiện dần và bổ sung trong quá trình làm bởi cả 3 thành viên.

## Tính năng chính
- Cấu trúc OOP hoàn chỉnh: tách biệt rõ các module `core`, `object`, `util`, `ui`.
- Áp dụng kế thừa, đa hình, đóng gói, singleton.
- Hai chế độ chơi: `NORMAL` và `FUNNY`.
- Power-ups: mở rộng paddle, nhân đôi bóng, bắn laser, v.v.
- Nhiều cấp độ: định nghĩa trong `resources/levels/level1.txt → level7.txt`.
- Hiệu ứng âm thanh và đồ họa bằng JavaFX.
- Hệ thống kiểm thử (JUnit) đảm bảo tính ổn định của logic game.

---

## Cấu trúc thư mục
```plaintext
src/
 ├─ main/
 │   ├─ java/arkanoid/
 │   │    ├─ core/        → Lõi xử lý game (GameManager, Renderer, Main)
 │   │    ├─ object/      → Các đối tượng: Ball, Paddle, Brick, PowerUp...
 │   │    ├─ ui/          → Giao diện người chơi
 │   │    └─ util/        → Hằng số, bộ nạp level, âm thanh, chế độ
 │   └─ resources/
 │        └─ levels/      → File định nghĩa màn chơi
 │        └─ sounds/      → File âm thanh của game
 │        └─ images/      → File hình ảnh của từng object trong game
 └─ test/
      └─ java/arkanoid/   → Các lớp kiểm thử JUnit
```

---

## Test
Thư mục `src/test/java` chứa các file:
- `GameManagerTest.java`
- `BallTest.java`
- `PaddleTest.java`
- `PowerUpTest.java`

---
