package view;

import controller.GameController;
import data.MazeLoader;
import model.*;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class GameFrame extends JFrame implements ActionListener {

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private GameController controller;
    private boolean paused = false;
    private List<Maze> defaultMazeList = new ArrayList<>();

    public GameFrame() throws IOException {
    		loadDefaultMazes();
    	
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // Thêm các màn hình điều hướng
        mainPanel.add(new MainMenuPanel(this), "MENU");
        mainPanel.add(new MapSelectPanel(this, defaultMazeList), "MAP_SELECT");

        add(mainPanel);

        setTitle("Tom & Jerry Game");
        
        // --- SỬA KÍCH THƯỚC TẠI ĐÂY ---
        setSize(1000, 800); 
        setMinimumSize(new Dimension(800, 600)); // Đảm bảo cửa sổ không quá bé làm HUD bị lỗi
        
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(true); // Cho phép co giãn để MazePanel thực hiện Dynamic Scaling
        setVisible(true);

        cardLayout.show(mainPanel, "MENU");
    }
    
    private void loadDefaultMazes() throws IOException {
    		defaultMazeList.clear();
    		// Load danh sách map mặc định để khi nhấn "START" từ menu vẫn có map tiếp theo
        defaultMazeList.add(MazeLoader.loadFromFile("src/data/maze1.txt"));
        defaultMazeList.add(MazeLoader.loadFromFile("src/data/maze1.txt"));
        defaultMazeList.add(MazeLoader.loadFromFile("src/data/maze2.txt"));
        defaultMazeList.add(MazeLoader.loadFromFile("src/data/maze1.txt"));
        defaultMazeList.add(MazeLoader.loadFromFile("src/data/maze2.txt"));
        defaultMazeList.add(MazeLoader.loadFromFile("src/data/maze2.txt"));
        defaultMazeList.add(MazeLoader.loadFromFile("src/data/maze1.txt"));
    }
    
    public List<Maze> getAllMazes() {
        return defaultMazeList;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();
        if (command == null) return;

        switch (command) {
            case "START":
                startGameWithMap(defaultMazeList, 0);
                break;
            case "CHOOSE_MAP":
                cardLayout.show(mainPanel, "MAP_SELECT");
                break;
            case "RULE":
                showRules();
                break;
        }
    }

    private void showRules() {
        String ruleMsg = """
                🎮 TOM & JERRY – CÁCH CHƠI
                
                🎯 MỤC TIÊU
                • Jerry: Ăn hết 🧀 và đến EXIT 🚪
                • Tom: Bắt Jerry trước khi Jerry thoát
                
                🔄 LƯỢT CHƠI
                • Mỗi lượt có 10 giây. Hết giờ tự động đổi lượt.
                
                ❤️ MẠNG SỐNG
                • Jerry có 3 mạng. Bị bắt sẽ hồi sinh ở điểm đầu.
                
                背包 VẬT PHẨM
                • ⚡ Speed: Tăng thêm 1 bước di chuyển.
                • 🛡 Shield: Chặn bẫy hoặc ngăn Tom bắt (1 lần).
                • 🪤 Trap: Dẫm phải sẽ bị mất lượt ngay lập tức.
                """;
        JOptionPane.showMessageDialog(this, ruleMsg, "Luật chơi", JOptionPane.INFORMATION_MESSAGE);
    }

    public void startGameWithMap(List<Maze> allMazes, int startIndex) {
        MazeManager mazeManager = new MazeManager();
        
        // QUAN TRỌNG: Thêm tất cả map vào manager
        for (Maze m : allMazes) {
            mazeManager.addMaze(m);
        }
        
        // Nhảy tới map người dùng chọn
        mazeManager.setCurrentMaze(startIndex);

        Maze currentMaze = mazeManager.getCurrentMaze();
        Jerry jerry = new Jerry(currentMaze.getJerryStart(), 0);
        Tom tom = new Tom(currentMaze.getTomStart());

        GameState gameState = new GameState(
                jerry, tom, mazeManager,
                currentMaze.getCheese().size(),
                TurnType.JERRY, 10
        );

        MazePanel mazePanel = new MazePanel(gameState);
        controller = new GameController(gameState, mazePanel);

        mazePanel.setFocusable(true);
        mazePanel.addKeyListener(controller.getKeyListener());

        JPanel gameScreen = new JPanel(new BorderLayout());
        gameScreen.add(new ControlPanel(this::backToMenu, this::togglePause, this::replayGame), BorderLayout.NORTH);
        gameScreen.add(mazePanel, BorderLayout.CENTER);

        mainPanel.add(gameScreen, "GAME");
        cardLayout.show(mainPanel, "GAME");

        SwingUtilities.invokeLater(mazePanel::requestFocusInWindow);
        controller.startGame();
    }

    private void togglePause() {
        paused = !paused;
        if (controller != null) {
            controller.setPaused(paused);
            // Sau khi bấm nút Pause, cần focus lại vào MazePanel để chơi tiếp
            if (!paused) {
                Component[] comps = mainPanel.getComponents();
                for (Component c : comps) {
                    if (c.isVisible() && c instanceof JPanel) {
                        for (Component sub : ((JPanel) c).getComponents()) {
                            if (sub instanceof MazePanel) sub.requestFocusInWindow();
                        }
                    }
                }
            }
        }
    }

    public void backToMenu() {
        if (controller != null) controller.stopGame();
        cardLayout.show(mainPanel, "MENU");
    }

    private void replayGame() {
        if (controller != null) {
            controller.replayCurrentMaze();
            // Đảm bảo focus lại sau khi replay
            for (Component c : mainPanel.getComponents()) {
                if (c.isVisible()) {
                    // Tìm MazePanel để requestFocus
                }
            }
        }
    }
    
    public void goToMapSelect() {
        if (controller != null) controller.stopGame();
        cardLayout.show(mainPanel, "MAP_SELECT");
    }
}