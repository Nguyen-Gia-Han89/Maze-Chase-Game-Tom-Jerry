package view;

import model.*;
import javax.swing.*;
import java.awt.*;

public class MazePreviewPanel extends JPanel {

    private Maze maze;

    public MazePreviewPanel(Maze maze) {
        this.maze = maze;
        // Không setPreferredSize cố định ở đây nữa để Grid tự điều phối
        setOpaque(false); 
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (maze == null) return;

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int[][] layout = maze.getLayout();
        int rows = layout.length;
        int cols = layout[0].length;

        // 1️⃣ TÍNH TOÁN TILE SIZE ĐỘNG ĐỂ VỪA VỚI Ô PREVIEW
        // Chừa khoảng trống lề (padding) 10px để bản đồ không dính sát viền
        int padding = 10;
        int availableWidth = getWidth() - (padding * 2);
        int availableHeight = getHeight() - (padding * 2);

        int tileSize = Math.min(availableWidth / cols, availableHeight / rows);

        // Căn giữa bản đồ trong ô preview
        int offsetX = (getWidth() - (cols * tileSize)) / 2;
        int offsetY = (getHeight() - (rows * tileSize)) / 2;

        // 2️⃣ VẼ TƯỜNG & ĐƯỜNG
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int x = offsetX + j * tileSize;
                int y = offsetY + i * tileSize;

                if (layout[i][j] == 1) {
                    g2d.setColor(new Color(60, 60, 70)); // Màu tường tối
                } else {
                    g2d.setColor(new Color(240, 240, 240)); // Màu đường đi sáng
                }

                g2d.fillRect(x, y, tileSize, tileSize);
                
                // Vẽ lưới mờ
                g2d.setColor(new Color(200, 200, 200, 50));
                g2d.drawRect(x, y, tileSize, tileSize);
            }
        }

        // 3️⃣ VẼ CÁC ĐỐI TƯỢNG (Với kích thước scale theo tileSize)
        int objectSize = (int) (tileSize * 0.7);
        int centerPad = (tileSize - objectSize) / 2;

        // Vẽ Cheese (Vàng cam)
        g2d.setColor(Color.ORANGE);
        for (Cheese c : maze.getCheese()) {
            Position p = c.getPosition();
            g2d.fillOval(offsetX + p.getY() * tileSize + centerPad, 
                         offsetY + p.getX() * tileSize + centerPad, 
                         objectSize, objectSize);
        }

        // Vẽ Trap (Đỏ)
        g2d.setColor(new Color(200, 50, 50));
        for (Trap t : maze.getListTrap()) {
            Position p = t.getPosition();
            g2d.fillRect(offsetX + p.getY() * tileSize + centerPad, 
                         offsetY + p.getX() * tileSize + centerPad, 
                         objectSize, objectSize);
        }

        // Vẽ Exit (Xanh lá)
        Position e = maze.getExitPosition();
        if (e != null) {
            g2d.setColor(new Color(50, 180, 50));
            g2d.fillRect(offsetX + e.getY() * tileSize, offsetY + e.getX() * tileSize, tileSize, tileSize);
        }

        // 4️⃣ VẼ VỊ TRÍ START (Jerry: Xanh dương, Tom: Hồng đậm)
        Position js = maze.getJerryStart();
        Position ts = maze.getTomStart();

        if (js != null) {
            g2d.setColor(new Color(50, 100, 255));
            g2d.fillOval(offsetX + js.getY() * tileSize + centerPad, 
                         offsetY + js.getX() * tileSize + centerPad, 
                         objectSize, objectSize);
        }

        if (ts != null) {
            g2d.setColor(new Color(255, 50, 255));
            g2d.fillOval(offsetX + ts.getY() * tileSize + centerPad, 
                         offsetY + ts.getX() * tileSize + centerPad, 
                         objectSize, objectSize);
        }
    }
}