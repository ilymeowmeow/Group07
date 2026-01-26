package ui;

import service.LibraryManager;
import model.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Giao diện cho độc giả
 */
public class ReaderFrame extends JFrame {
    private LibraryManager libraryManager;
    private JTabbedPane tabbedPane;
    
    // Tab danh sách sách
    private JTable tableSach;
    private DefaultTableModel modelSach;
    private JTextField txtSearchBook;
    
    // Tab sách đang mượn
    private JTable tableDangMuon;
    private DefaultTableModel modelDangMuon;
    
    // Tab lịch sử mượn
    private JTextArea txtLichSu;

    public ReaderFrame() {
        libraryManager = LibraryManager.getInstance();
        initComponents();
        loadData();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("Độc giả - Hệ thống Quản lý Thư viện");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Dialog", Font.PLAIN, 13));
        
        tabbedPane.addTab("Danh sách sách", createBookListPanel());
        tabbedPane.addTab("Sách đang mượn", createBorrowingPanel());
        tabbedPane.addTab("Lịch sử mượn", createHistoryPanel());
        tabbedPane.addTab("Thông tin cá nhân", createProfilePanel());
        
        add(tabbedPane, BorderLayout.CENTER);
    }

    // TAB DANH SÁCH SÁCH
    private JPanel createBookListPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel toolbarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        txtSearchBook = new JTextField(20);
        txtSearchBook.setFont(new Font("Dialog", Font.PLAIN, 14));
        txtSearchBook.setPreferredSize(new Dimension(250, 32));
        
        JButton btnSearch = new JButton("Tìm kiếm");
        btnSearch.setFont(new Font("Dialog", Font.PLAIN, 14));
        btnSearch.setMargin(new Insets(8, 15, 8, 15));
        btnSearch.addActionListener(e -> searchBooks());
        
        JButton btnRefresh = new JButton("Làm mới");
        btnRefresh.setFont(new Font("Dialog", Font.PLAIN, 14));
        btnRefresh.setMargin(new Insets(8, 15, 8, 15));
        btnRefresh.addActionListener(e -> loadBooks());
        
        JButton btnViewDetail = new JButton("Xem chi tiết");
        btnViewDetail.setFont(new Font("Dialog", Font.PLAIN, 14));
        btnViewDetail.setMargin(new Insets(8, 15, 8, 15));
        btnViewDetail.addActionListener(e -> viewBookDetail());
        
        JLabel lblSearch = new JLabel("Tìm kiếm sách:");
        lblSearch.setFont(new Font("Dialog", Font.PLAIN, 14));
        toolbarPanel.add(lblSearch);
        toolbarPanel.add(txtSearchBook);
        toolbarPanel.add(btnSearch);
        toolbarPanel.add(btnRefresh);
        toolbarPanel.add(btnViewDetail);
        

        toolbarPanel.add(Box.createHorizontalStrut(30));
        JButton btnLogout = new JButton("Đăng xuất");
        btnLogout.setFont(new Font("Dialog", Font.PLAIN, 14));
        btnLogout.setMargin(new Insets(8, 15, 8, 15));
        btnLogout.addActionListener(e -> logout());
        toolbarPanel.add(btnLogout);
        
        panel.add(toolbarPanel, BorderLayout.NORTH);

        String[] columns = {"Mã sách", "Tên sách", "Tác giả", "Thể loại", "Năm XB", "Số lượng"};
        modelSach = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableSach = new JTable(modelSach);
        tableSach.setFont(new Font("Dialog", Font.PLAIN, 13));
        tableSach.setRowHeight(26);
        tableSach.getTableHeader().setFont(new Font("Dialog", Font.BOLD, 14));
        tableSach.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(tableSach);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel infoPanel = new JPanel();
        JLabel lblInfo = new JLabel("Gợi ý: Nhấp đúp vào sách để xem chi tiết và mượn sách");
        infoPanel.add(lblInfo);
        panel.add(infoPanel, BorderLayout.SOUTH);


        tableSach.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    viewBookDetail();
                }
            }
        });

        return panel;
    }

    // TAB SÁCH ĐANG MƯỢN
    private JPanel createBorrowingPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel toolbarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JButton btnRefresh = new JButton("Làm mới");
        btnRefresh.setFont(new Font("Dialog", Font.PLAIN, 14));
        btnRefresh.setMargin(new Insets(8, 15, 8, 15));
        btnRefresh.addActionListener(e -> loadBorrowingBooks());
        
        toolbarPanel.add(btnRefresh);
        panel.add(toolbarPanel, BorderLayout.NORTH);

        String[] columns = {"Mã phiếu", "Mã sách", "Tên sách", "Ngày mượn", "Ngày trả dự kiến", "Tình trạng"};
        modelDangMuon = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableDangMuon = new JTable(modelDangMuon);
        tableDangMuon.setFont(new Font("Dialog", Font.PLAIN, 13));
        tableDangMuon.setRowHeight(26);
        tableDangMuon.getTableHeader().setFont(new Font("Dialog", Font.BOLD, 14));
        tableDangMuon.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(tableDangMuon);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // TAB LỊCH SỬ MƯỢN
    private JPanel createHistoryPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel lblTitle = new JLabel("Lịch sử mượn sách");
        lblTitle.setFont(new Font("Dialog", Font.BOLD, 16));
        panel.add(lblTitle, BorderLayout.NORTH);

        txtLichSu = new JTextArea();
        txtLichSu.setFont(new Font("Monospaced", Font.PLAIN, 13));
        txtLichSu.setEditable(false);
        
        JScrollPane scrollPane = new JScrollPane(txtLichSu);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton btnRefresh = new JButton("Làm mới");
        btnRefresh.setFont(new Font("Dialog", Font.PLAIN, 14));
        btnRefresh.setMargin(new Insets(8, 15, 8, 15));
        btnRefresh.addActionListener(e -> loadHistory());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(btnRefresh);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    // TAB THÔNG TIN CÁ NHÂN
    private JPanel createProfilePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        DocGia reader = libraryManager.getCurrentReader();
        if (reader == null) return panel;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        JPanel infoPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        infoPanel.setBorder(BorderFactory.createTitledBorder("Thông tin độc giả"));

        Font labelFont = new Font("Dialog", Font.PLAIN, 14);
        
        JLabel lbl1 = new JLabel("Mã độc giả:");
        lbl1.setFont(labelFont);
        infoPanel.add(lbl1);
        JLabel val1 = new JLabel(reader.getMaNguoiDung());
        val1.setFont(labelFont);
        infoPanel.add(val1);
        
        JLabel lbl2 = new JLabel("Họ tên:");
        lbl2.setFont(labelFont);
        infoPanel.add(lbl2);
        JLabel val2 = new JLabel(reader.getHoTen());
        val2.setFont(labelFont);
        infoPanel.add(val2);
        
        JLabel lbl3 = new JLabel("Email:");
        lbl3.setFont(labelFont);
        infoPanel.add(lbl3);
        JLabel val3 = new JLabel(reader.getEmail());
        val3.setFont(labelFont);
        infoPanel.add(val3);
        
        JLabel lbl4 = new JLabel("Số điện thoại:");
        lbl4.setFont(labelFont);
        infoPanel.add(lbl4);
        JLabel val4 = new JLabel(reader.getSoDienThoai());
        val4.setFont(labelFont);
        infoPanel.add(val4);
        
        JLabel lbl5 = new JLabel("Mã thẻ:");
        lbl5.setFont(labelFont);
        infoPanel.add(lbl5);
        JLabel val5 = new JLabel(reader.getMaThe());
        val5.setFont(labelFont);
        infoPanel.add(val5);
        
        JLabel lbl6 = new JLabel("Ngày đăng ký:");
        lbl6.setFont(labelFont);
        infoPanel.add(lbl6);
        JLabel val6 = new JLabel(reader.getNgayDangKy().format(formatter));
        val6.setFont(labelFont);
        infoPanel.add(val6);
        
        JLabel lbl7 = new JLabel("Ngày hết hạn:");
        lbl7.setFont(labelFont);
        infoPanel.add(lbl7);
        JLabel val7 = new JLabel(reader.getNgayHetHan().format(formatter));
        val7.setFont(labelFont);
        infoPanel.add(val7);

        panel.add(infoPanel);

        return panel;
    }

    //  LOAD DATA
    private void loadData() {
        loadBooks();
        loadBorrowingBooks();
        loadHistory();
    }

    private void loadBooks() {
        modelSach.setRowCount(0);
        List<Sach> books = libraryManager.getAllBooks();
        for (Sach sach : books) {
            modelSach.addRow(new Object[]{
                sach.getMaSach(),
                sach.getTenSach(),
                sach.getTacGia(),
                sach.getTheLoai(),
                sach.getNamXuatBan(),
                sach.getSoLuong(),
            });
        }
    }

    private void loadBorrowingBooks() {
        modelDangMuon.setRowCount(0);
        DocGia reader = libraryManager.getCurrentReader();
        if (reader == null) return;

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        List<PhieuMuonSach> records = libraryManager.getBorrowRecordsByReader(reader.getMaNguoiDung());
        
        for (PhieuMuonSach phieu : records) {
            if (phieu.getTinhTrang().equals("Đang mượn")) {
                Sach book = libraryManager.searchBookById(phieu.getMaSach());
                String tinhTrang = phieu.isQuaHan() ? "Quá hạn" : "Bình thường";
                
                modelDangMuon.addRow(new Object[]{
                    phieu.getMaPhieu(),
                    phieu.getMaSach(),
                    book != null ? book.getTenSach() : "",
                    phieu.getNgayMuon().format(formatter),
                    phieu.getNgayTraDuKien().format(formatter),
                    tinhTrang
                });
            }
        }
    }

    private void loadHistory() {
        DocGia reader = libraryManager.getCurrentReader();
        if (reader == null) return;

        StringBuilder sb = new StringBuilder();
        sb.append("=== LỊCH SỬ MƯỢN SÁCH ===\n\n");
        
        List<String> history = reader.getLichSuMuonList();
        if (history.isEmpty()) {
            sb.append("Chưa có lịch sử mượn sách.\n");
        } else {
            for (int i = 0; i < history.size(); i++) {
                sb.append((i + 1)).append(". ").append(history.get(i)).append("\n");
            }
        }
        
        txtLichSu.setText(sb.toString());
    }

    // SEARCH
    private void searchBooks() {
        String keyword = txtSearchBook.getText().trim();
        if (keyword.isEmpty()) {
            loadBooks();
            return;
        }

        modelSach.setRowCount(0);
        List<Sach> books = libraryManager.searchBooksByKeyword(keyword);
        for (Sach sach : books) {
            modelSach.addRow(new Object[]{
                sach.getMaSach(),
                sach.getTenSach(),
                sach.getTacGia(),
                sach.getTheLoai(),
                sach.getNamXuatBan(),
                sach.getSoLuong(),
            });
        }

        if (books.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Không tìm thấy sách nào phù hợp!", 
                "Thông báo", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    //  VIEW DETAIL & BORROW
    private void viewBookDetail() {
        int selectedRow = tableSach.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sách!");
            return;
        }

        String maSach = (String) modelSach.getValueAt(selectedRow, 0);
        Sach sach = libraryManager.searchBookById(maSach);
        if (sach == null) return;

        JDialog dialog = new JDialog(this, "Chi tiết sách", true);
        dialog.setSize(450, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));


        JPanel detailPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        detailPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        detailPanel.add(new JLabel("Mã sách:"));
        detailPanel.add(new JLabel(sach.getMaSach()));
        detailPanel.add(new JLabel("Tên sách:"));
        detailPanel.add(new JLabel(sach.getTenSach()));
        detailPanel.add(new JLabel("Tác giả:"));
        detailPanel.add(new JLabel(sach.getTacGia()));
        detailPanel.add(new JLabel("Thể loại:"));
        detailPanel.add(new JLabel(sach.getTheLoai()));
        detailPanel.add(new JLabel("Năm xuất bản:"));
        detailPanel.add(new JLabel(String.valueOf(sach.getNamXuatBan())));
        detailPanel.add(new JLabel("Số lượng còn:"));
        detailPanel.add(new JLabel(String.valueOf(sach.getSoLuong())));
        detailPanel.add(new JLabel("Mô tả:"));
        JTextArea txtMoTa = new JTextArea(sach.getMoTa());
        txtMoTa.setEditable(false);
        txtMoTa.setLineWrap(true);
        txtMoTa.setWrapStyleWord(true);
        detailPanel.add(new JScrollPane(txtMoTa));

        dialog.add(detailPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        JButton btnBorrow = new JButton("Mượn sách này");
        btnBorrow.setEnabled(sach.getSoLuong() > 0);
        btnBorrow.addActionListener(e -> {
            borrowBook(sach);
            dialog.dispose();
        });
        
        JButton btnClose = new JButton("Đóng");
        btnClose.addActionListener(e -> dialog.dispose());

        buttonPanel.add(btnBorrow);
        buttonPanel.add(btnClose);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private void borrowBook(Sach sach) {
        DocGia reader = libraryManager.getCurrentReader();
        if (reader == null) return;

        // Hỏi số ngày mượn
        String input = JOptionPane.showInputDialog(this, 
            "Nhập số ngày mượn (1-30):", 
            "14");
        
        if (input == null || input.trim().isEmpty()) {
            return;
        }

        try {
            int soNgay = Integer.parseInt(input.trim());
            if (soNgay < 1 || soNgay > 30) {
                JOptionPane.showMessageDialog(this, 
                    "Số ngày mượn phải từ 1 đến 30!", 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            boolean success = libraryManager.borrowBook(
                reader.getMaNguoiDung(),
                sach.getMaSach(),
                soNgay
            );

            if (success) {
                JOptionPane.showMessageDialog(this, 
                    "Mượn sách thành công!\nVui lòng trả sách đúng hạn.", 
                    "Thành công", 
                    JOptionPane.INFORMATION_MESSAGE);
                loadBooks();
                loadBorrowingBooks();
                loadHistory();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Không thể mượn sách!\nKiểm tra lại thông tin hoặc thẻ đã hết hạn.", 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, 
                "Số ngày không hợp lệ!", 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Bạn có chắc chắn muốn đăng xuất?", 
            "Xác nhận", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            libraryManager.logout();
            SwingUtilities.invokeLater(() -> {
                new LoginFrame().setVisible(true);
            });
            dispose();
        }
    }
}
