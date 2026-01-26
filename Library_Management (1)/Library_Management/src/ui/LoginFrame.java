package ui;

import service.LibraryManager;
import model.Admin;
import model.DocGia;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Giao diện đăng nhập cho Admin và Độc giả
 */
public class LoginFrame extends JFrame {
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JComboBox<String> cboUserType;
    private JButton btnLogin;
    private JButton btnExit;
    
    private LibraryManager libraryManager;

    public LoginFrame() {
        libraryManager = LibraryManager.getInstance();
        initComponents();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("Hệ thống Quản lý Thư viện - Đăng nhập");
        setSize(500, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        Font labelFont = new Font("Dialog", Font.PLAIN, 14);
        Font inputFont = new Font("Dialog", Font.PLAIN, 14);
        Font buttonFont = new Font("Dialog", Font.PLAIN, 14);

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridBagLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Loại người dùng
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel lblUserType = new JLabel("Loại người dùng:");
        lblUserType.setFont(labelFont);
        centerPanel.add(lblUserType, gbc);

        gbc.gridx = 1;
        cboUserType = new JComboBox<>(new String[]{"Admin", "Độc giả"});
        cboUserType.setFont(inputFont);
        centerPanel.add(cboUserType, gbc);

        // Tên đăng nhập
        gbc.gridx = 0;
        gbc.gridy = 1;
        JLabel lblUsername = new JLabel("Tên đăng nhập:");
        lblUsername.setFont(labelFont);
        centerPanel.add(lblUsername, gbc);

        gbc.gridx = 1;
        txtUsername = new JTextField(18);
        txtUsername.setFont(inputFont);
        centerPanel.add(txtUsername, gbc);

        // Mật khẩu
        gbc.gridx = 0;
        gbc.gridy = 2;
        JLabel lblPassword = new JLabel("Mật khẩu:");
        lblPassword.setFont(labelFont);
        centerPanel.add(lblPassword, gbc);

        gbc.gridx = 1;
        txtPassword = new JPasswordField(18);
        txtPassword.setFont(inputFont);
        centerPanel.add(txtPassword, gbc);

        // Gợi ý đăng nhập
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        JLabel lblHint = new JLabel("<html><i>Admin: ADMIN / admin<br>Độc giả: DG001 /123</i></html>");
        lblHint.setFont(new Font("Dialog", Font.ITALIC, 12));
        centerPanel.add(lblHint, gbc);

        add(centerPanel, BorderLayout.CENTER);

        // Panel nút
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));

        btnLogin = new JButton("Đăng nhập");
        btnLogin.setFont(buttonFont);
        btnLogin.setMargin(new Insets(10, 25, 10, 25));
        btnLogin.setPreferredSize(new Dimension(140, 40));
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });

        btnExit = new JButton("Thoát");
        btnExit.setFont(buttonFont);
        btnExit.setMargin(new Insets(10, 25, 10, 25));
        btnExit.setPreferredSize(new Dimension(140, 40));
        btnExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        buttonPanel.add(btnLogin);
        buttonPanel.add(btnExit);
        
        add(buttonPanel, BorderLayout.SOUTH);

        txtPassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });
    }

    private void handleLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        String userType = (String) cboUserType.getSelectedItem();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng nhập đầy đủ thông tin!", 
                "Thông báo", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (userType.equals("Admin")) {
            Admin admin = libraryManager.loginAdmin(username, password);
            if (admin != null) {
                JOptionPane.showMessageDialog(this, 
                    "Đăng nhập thành công!", 
                    "Thành công", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Mở AdminFrame
                SwingUtilities.invokeLater(() -> {
                    new AdminFrame().setVisible(true);
                });
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Tên đăng nhập hoặc mật khẩu không đúng!", 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
            }
        } else { // Độc giả
            DocGia reader = libraryManager.loginReader(username, password);
            if (reader != null) {
                JOptionPane.showMessageDialog(this, 
                    "Đăng nhập thành công!", 
                    "Thành công", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Mở ReaderFrame
                SwingUtilities.invokeLater(() -> {
                    new ReaderFrame().setVisible(true);
                });
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Tên đăng nhập hoặc mật khẩu không đúng!", 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
