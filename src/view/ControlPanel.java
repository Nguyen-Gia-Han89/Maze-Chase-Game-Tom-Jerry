package view;

import javax.swing.*;
import java.awt.*;

public class ControlPanel extends JPanel {

    public ControlPanel(Runnable onHome, Runnable onPause, Runnable onReplay) {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setBackground(new Color(220, 220, 220));

        JButton btnHome = new JButton("🏠 Trang chủ");
        JButton btnPause = new JButton("⏸ Tạm dừng");
        JButton btnReplay = new JButton("🔄 Chơi lại");

        btnHome.addActionListener(e -> onHome.run());
        btnPause.addActionListener(e -> onPause.run());
        btnReplay.addActionListener(e -> onReplay.run());

        add(btnHome);
        add(btnPause);
        add(btnReplay);
    }
}

