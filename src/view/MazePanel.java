package view;

import model.*;
import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.ImageIO;

public class MazePanel extends JPanel {
    private GameState gameState;
    private Maze maze;
    private BufferedImage imgTom, imgJerry, imgCheese, imgTrap, imgSpeed, imgShield, imgExit, imgHeart;

    // Biến điều khiển kích thước động
    private int dynamicTileSize;
    private int offsetX;
    private int offsetY;
    private final int HUD_HEIGHT = 100; 

    public MazePanel(GameState gameState) {
        this.gameState = gameState;
        this.maze = gameState.getMaze().getCurrentMaze();
        loadResources();
        setBackground(new Color(30, 30, 30));
        setOpaque(true);
    }

    private void loadResources() {
        try {
            imgTom = ImageIO.read(getClass().getResourceAsStream("/images/tom.png"));
            imgJerry = ImageIO.read(getClass().getResourceAsStream("/images/jerry.png"));
            imgCheese = ImageIO.read(getClass().getResourceAsStream("/images/cheese.png"));
            imgTrap = ImageIO.read(getClass().getResourceAsStream("/images/trap.png"));
            imgSpeed = ImageIO.read(getClass().getResourceAsStream("/images/speed.png"));
            imgShield = ImageIO.read(getClass().getResourceAsStream("/images/shield.png"));
            imgExit = ImageIO.read(getClass().getResourceAsStream("/images/exit.png"));
            imgHeart = ImageIO.read(getClass().getResourceAsStream("/images/heart.png"));
        } catch (Exception e) {
            System.err.println("Lỗi tải tài nguyên: " + e.getMessage());
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        this.maze = gameState.getMaze().getCurrentMaze();
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 1. TÍNH TOÁN KÍCH THƯỚC ĐỘNG
        int mazeRows = maze.getLayout().length;
        int mazeCols = maze.getLayout()[0].length;
        int availableWidth = getWidth() - 60; 
        int availableHeight = getHeight() - HUD_HEIGHT - 40;

        dynamicTileSize = Math.min(availableWidth / mazeCols, availableHeight / mazeRows);
        offsetX = (getWidth() - (mazeCols * dynamicTileSize)) / 2;
        offsetY = HUD_HEIGHT + (availableHeight - (mazeRows * dynamicTileSize)) / 2;

        // 2. VẼ HUD (FIXED VỊ TRÍ)
        drawHUD(g2d);

        // 3. VẼ MÊ CUNG
        drawMazeGrid(g2d, mazeRows, mazeCols);

        // 4. VẼ ĐỐI TƯỢNG
        drawEntities(g2d);

        // 5. PAUSE OVERLAY
        if (gameState.isPaused()) {
            drawPauseOverlay(g2d);
        }
    }

    private void drawHUD(Graphics2D g2d) {
        // Khung nền HUD
        g2d.setColor(new Color(255, 255, 255, 40));
        g2d.fillRoundRect(10, 10, getWidth() - 20, HUD_HEIGHT - 20, 15, 15);
        g2d.setColor(Color.WHITE);
        g2d.drawRoundRect(10, 10, getWidth() - 20, HUD_HEIGHT - 20, 15, 15);

        // --- Tên Map (Giữa trên) ---
        g2d.setFont(new Font("Arial", Font.BOLD, 18));
        String mapTitle = "MÀN CHƠI: " + (gameState.getMaze().getCurrentMazeIndex() + 1);
        g2d.drawString(mapTitle, (getWidth() - g2d.getFontMetrics().stringWidth(mapTitle)) / 2, 35);

        // --- Info Jerry (Góc trái HUD) ---
        int jHudX = 30; 
        if (imgJerry != null) g2d.drawImage(imgJerry, jHudX, 30, 50, 50, null);
        
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        g2d.setColor(Color.WHITE);
        // Mạng Jerry
        if (imgHeart != null) g2d.drawImage(imgHeart, jHudX + 60, 35, 20, 20, null);
        g2d.drawString("x " + gameState.getJerry().getLives(), jHudX + 85, 52);
        // Phô mai Jerry
        if (imgCheese != null) g2d.drawImage(imgCheese, jHudX + 60, 60, 20, 20, null);
        int totalC = Cheese.getNumber();
        int collC = gameState.getJerry().getCheeseCollected();
        g2d.drawString("x " + collC + " / " + totalC, jHudX + 85, 77);

        // --- Info Tom (Góc phải HUD) ---
        int tHudX = getWidth() - 150;
        if (imgTom != null) g2d.drawImage(imgTom, tHudX + 60, 30, 50, 50, null);
        g2d.setColor(new Color(135, 206, 250));
        g2d.drawString("BƯỚC: " + (gameState.getTom().getTotalSpeed() + 1), tHudX - 30, 60);

        // --- Đếm ngược (Giữa dưới) ---
        g2d.setFont(new Font("Monospaced", Font.BOLD, 45));
        String time = String.valueOf(gameState.getTurnTimeLeft());
        g2d.setColor(gameState.getTurn() == TurnType.JERRY ? Color.GREEN : Color.RED);
        g2d.drawString(time, (getWidth() - g2d.getFontMetrics().stringWidth(time)) / 2, 85);
    }

    private void drawMazeGrid(Graphics2D g2d, int rows, int cols) {
        int[][] layout = maze.getLayout();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int x = j * dynamicTileSize + offsetX;
                int y = i * dynamicTileSize + offsetY;
                g2d.setColor(layout[i][j] == 1 ? new Color(40, 40, 50) : Color.WHITE);
                g2d.fillRect(x, y, dynamicTileSize, dynamicTileSize);
                g2d.setColor(Color.GRAY);
                g2d.drawRect(x, y, dynamicTileSize, dynamicTileSize);
            }
        }
    }

    private void drawEntities(Graphics2D g2d) {
        // Cheese
        for (Cheese c : maze.getCheese()) {
            drawAt(g2d, imgCheese, c.getPosition(), 0.6f);
        }
        // Trap
        for (Trap t : maze.getListTrap()) {
            drawAt(g2d, imgTrap, t.getPosition(), 0.7f);
        }
        // Items
        for (Item it : maze.getListItem()) {
            BufferedImage itemImg = (it instanceof SpeedBoostItem) ? imgSpeed : imgShield;
            drawAt(g2d, itemImg, it.getPosition(), 0.6f);
        }
        // Exit
        Position ex = maze.getExitPosition();
        if (ex != null) drawAt(g2d, imgExit, ex, 0.9f);

        // Jerry + Shield
        Position jp = gameState.getJerry().getPosition();
        if (gameState.getJerry().isHasShield()) {
            g2d.setColor(new Color(0, 255, 255, 80));
            g2d.fillOval(jp.getY()*dynamicTileSize + offsetX, jp.getX()*dynamicTileSize + offsetY, dynamicTileSize, dynamicTileSize);
        }
        drawAt(g2d, imgJerry, jp, 0.8f);

        // Tom
        drawAt(g2d, imgTom, gameState.getTom().getPosition(), 0.9f);
    }

    private void drawAt(Graphics2D g, BufferedImage img, Position p, float scale) {
        if (img == null || p == null) return;
        int size = (int) (dynamicTileSize * scale);
        int pad = (dynamicTileSize - size) / 2;
        g.drawImage(img, p.getY() * dynamicTileSize + offsetX + pad, p.getX() * dynamicTileSize + offsetY + pad, size, size, null);
    }

    private void drawPauseOverlay(Graphics2D g2d) {
        g2d.setColor(new Color(0, 0, 0, 150));
        g2d.fillRect(0, 0, getWidth(), getHeight());
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 50));
        String txt = "PAUSED";
        g2d.drawString(txt, (getWidth() - g2d.getFontMetrics().stringWidth(txt))/2, getHeight()/2);
    }
}