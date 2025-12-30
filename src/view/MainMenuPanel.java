package view;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

public class MainMenuPanel extends JPanel {
    private Image backgroundImage;

    public MainMenuPanel(ActionListener listener) {
        try {
            File file = new File("src/images/background.png");
            if (file.exists()) {
                backgroundImage = ImageIO.read(file);
            } else {
                backgroundImage = ImageIO.read(new File("images/background.png"));
            }
        } catch (IOException e) {
            System.err.println("Không thể đọc file bằng đường dẫn tương đối!");
        }

        setLayout(new GridBagLayout());
        setOpaque(false); 

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.weightx = 1.0; 
        gbc.weighty = 1.0; 
        gbc.insets = new Insets(60, 0, 0, 60); 

        // Tăng khoảng cách giữa các nút lên một chút vì nút có viền sẽ to hơn
        JPanel buttonContainer = new JPanel(new GridLayout(3, 1, 0, 25));
        buttonContainer.setOpaque(false); 

        buttonContainer.add(createStyledButton("BẮT ĐẦU CHƠI", listener, "START"));
        buttonContainer.add(createStyledButton("CHỌN MÀN CHƠI", listener, "CHOOSE_MAP"));
        buttonContainer.add(createStyledButton("LUẬT CHƠI", listener, "RULE"));

        add(buttonContainer, gbc);
    }

    private JButton createStyledButton(String text, ActionListener listener, String command) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 28)); // Giảm nhẹ font để cân đối với viền
        btn.setForeground(new Color(200, 0, 0)); 
        
        // Cấu hình nền mờ (Alpha = 150/255)
        btn.setBackground(new Color(255, 255, 255, 150)); 
        btn.setContentAreaFilled(true); // Cho phép vẽ nền
        btn.setOpaque(false); // Cần thiết để Alpha của Background hoạt động chính xác trong Swing
        
        // Cấu hình viền nút
        btn.setBorderPainted(true);
        btn.setBorder(new LineBorder(new Color(200, 0, 0), 2, true)); // Viền đỏ, dày 2px, bo góc nhẹ
        
        btn.setFocusPainted(false);
        btn.setActionCommand(command);
        btn.addActionListener(listener);

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                // Khi hover: Nền đậm hơn, chữ chuyển vàng
                btn.setBackground(new Color(255, 255, 255, 220));
                btn.setForeground(new Color(255, 150, 0));
                btn.setBorder(new LineBorder(new Color(255, 150, 0), 2, true));
            }
            public void mouseExited(MouseEvent evt) {
                // Trở về trạng thái ban đầu
                btn.setBackground(new Color(255, 255, 255, 150));
                btn.setForeground(new Color(200, 0, 0));
                btn.setBorder(new LineBorder(new Color(200, 0, 0), 2, true));
            }
        });
        return btn;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}