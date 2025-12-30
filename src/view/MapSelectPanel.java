package view;

import model.*;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class MapSelectPanel extends JPanel {

    private GameFrame frame;
    private List<Maze> mazes;
    private int selected = -1;
    
    // Thiết lập hằng số khoảng cách để giống MazePanel
    private final int PADDING = 30;
    private final int GAP = 20;

    public MapSelectPanel(GameFrame frame, List<Maze> sharedMazes) {
        this.frame = frame;
        this.mazes = sharedMazes;
        
        setLayout(new BorderLayout());
        setBackground(new Color(30, 30, 30)); // Cùng màu nền với MazePanel

        // --- TOP BAR (Tiêu đề & Nút Back) ---
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JButton btnBack = new JButton("← BACK");
        styleButton(btnBack, new Color(200, 50, 50));
        btnBack.addActionListener(e -> frame.backToMenu());

        JLabel lblTitle = new JLabel("CHỌN MÀN CHƠI", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);

        topPanel.add(btnBack, BorderLayout.WEST);
        topPanel.add(lblTitle, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        // --- GRID CHỌN MAP ---
        // Sử dụng GridLayout(0, 2) để cố định 2 cột, hàng tự động tăng
        JPanel grid = new JPanel(new GridLayout(0, 2, GAP, GAP)) {
            @Override
            public Dimension getPreferredSize() {
                // Tính toán chiều rộng dựa trên cha, chiều cao dựa trên số hàng
                int parentWidth = getParent().getWidth();
                int cols = 2;
                int rows = (int) Math.ceil((double) mazes.size() / cols);
                int itemWidth = (parentWidth - (PADDING * 2) - GAP) / cols;
                // Giữ tỉ lệ ô preview là 4:3
                int itemHeight = (int) (itemWidth * 0.75); 
                return new Dimension(parentWidth, rows * (itemHeight + GAP));
            }
        };
        grid.setOpaque(false);
        grid.setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));

        for (int i = 0; i < mazes.size(); i++) {
            int idx = i;
            MazePreviewPanel preview = new MazePreviewPanel(mazes.get(i));
            preview.setBackground(new Color(50, 50, 60));
            preview.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));

            preview.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    selected = idx;
                    highlight(grid, idx);
                    // Double click để vào game luôn
                    if (e.getClickCount() == 2) {
                        frame.startGameWithMap(mazes, selected);
                    }
                }
            });
            grid.add(preview);
        }

        // --- SCROLLPANE ---
        JScrollPane scrollPane = new JScrollPane(grid);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.CENTER);

        // --- BOTTOM BAR (Nút Play) ---
        JPanel bottomPanel = new JPanel();
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));

        JButton btnPlay = new JButton("BẮT ĐẦU CHƠI");
        styleButton(btnPlay, new Color(50, 150, 50));
        btnPlay.setPreferredSize(new Dimension(300, 50));
        btnPlay.addActionListener(e -> {
            if (selected == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn một bản đồ!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            frame.startGameWithMap(this.mazes, selected);
        });

        bottomPanel.add(btnPlay);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void styleButton(JButton btn, Color color) {
        btn.setFocusPainted(false);
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Arial", Font.BOLD, 16));
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(color.brighter(), 2),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));
    }

    private void highlight(JPanel grid, int index) {
        for (int i = 0; i < grid.getComponentCount(); i++) {
            if (grid.getComponent(i) instanceof JComponent comp) {
                if (i == index) {
                    comp.setBorder(BorderFactory.createLineBorder(new Color(255, 215, 0), 4)); // Màu vàng phô mai
                } else {
                    comp.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2));
                }
            }
        }
        grid.repaint();
    }
}