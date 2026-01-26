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
 * Giao diện quản trị cho Admin
 */
public class AdminFrame extends JFrame {
    private LibraryManager libraryManager;
    private JTabbedPane tabbedPane;
    
    // Tab quản lý sách
    private JTable tableSach;
    private DefaultTableModel modelSach;
    private JTextField txtSearchBook;
    
    // Tab quản lý độc giả
    private JTable tableDocGia;
    private DefaultTableModel modelDocGia;
    private JTextField txtSearchReader;
    
    // Tab quản lý mượn trả
    private JTable tableMuonTra;
    private DefaultTableModel modelMuonTra;
    
    // Tab thống kê
    private JLabel lblTotalBooks, lblTotalReaders, lblTotalBorrows, lblActiveBorrows, lblOverdue;

    public AdminFrame() {
        libraryManager = LibraryManager.getInstance();
        initComponents();
        loadData();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("Quản trị - Hệ thống Quản lý Thư viện");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        Font labelFont = new Font("Dialog", Font.PLAIN, 14);
        Font buttonFont = new Font("Dialog", Font.PLAIN, 14);
        Font tableFont = new Font("Dialog", Font.PLAIN, 13);

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Dialog", Font.PLAIN, 13));
        
        tabbedPane.addTab("Quản lý sách", createBookManagementPanel());
        tabbedPane.addTab("Quản lý độc giả", createReaderManagementPanel());
        tabbedPane.addTab("Quản lý mượn trả", createBorrowManagementPanel());
        tabbedPane.addTab("Thống kê", createStatisticsPanel());
        
        add(tabbedPane, BorderLayout.CENTER);
    }

    // =============== TAB QUẢN LÝ SÁCH ===============
    private JPanel createBookManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel toolbarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JButton btnAdd = new JButton("Thêm sách");
        btnAdd.setFont(new Font("Dialog", Font.PLAIN, 14));
        btnAdd.setMargin(new Insets(8, 15, 8, 15));
        btnAdd.addActionListener(e -> showAddBookDialog());
        
        JButton btnEdit = new JButton("Sửa");
        btnEdit.setFont(new Font("Dialog", Font.PLAIN, 14));
        btnEdit.setMargin(new Insets(8, 15, 8, 15));
        btnEdit.addActionListener(e -> showEditBookDialog());
        
        JButton btnDelete = new JButton("Xóa");
        btnDelete.setFont(new Font("Dialog", Font.PLAIN, 14));
        btnDelete.setMargin(new Insets(8, 15, 8, 15));
        btnDelete.addActionListener(e -> deleteBook());
        
        JButton btnRefresh = new JButton("Làm mới");
        btnRefresh.setFont(new Font("Dialog", Font.PLAIN, 14));
        btnRefresh.setMargin(new Insets(8, 15, 8, 15));
        btnRefresh.addActionListener(e -> loadBooks());
        
        txtSearchBook = new JTextField(15);
        txtSearchBook.setFont(new Font("Dialog", Font.PLAIN, 14));
        txtSearchBook.setPreferredSize(new Dimension(200, 32));
        
        JButton btnSearch = new JButton("Tìm kiếm");
        btnSearch.setFont(new Font("Dialog", Font.PLAIN, 14));
        btnSearch.setMargin(new Insets(8, 15, 8, 15));
        btnSearch.addActionListener(e -> searchBooks());
        
        JLabel lblSearch = new JLabel("Tìm kiếm:");
        lblSearch.setFont(new Font("Dialog", Font.PLAIN, 14));
        
        toolbarPanel.add(btnAdd);
        toolbarPanel.add(btnEdit);
        toolbarPanel.add(btnDelete);
        toolbarPanel.add(btnRefresh);
        toolbarPanel.add(lblSearch);
        toolbarPanel.add(txtSearchBook);
        toolbarPanel.add(btnSearch);

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

        return panel;
    }

    // =============== TAB QUẢN LÝ ĐỘC GIẢ ===============
    private JPanel createReaderManagementPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


        JPanel toolbarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JButton btnAdd = new JButton("Thêm độc giả");
        btnAdd.setFont(new Font("Dialog", Font.PLAIN, 14));
        btnAdd.setMargin(new Insets(8, 15, 8, 15));
        btnAdd.addActionListener(e -> showAddReaderDialog());
        
        JButton btnEdit = new JButton("Sửa");
        btnEdit.setFont(new Font("Dialog", Font.PLAIN, 14));
        btnEdit.setMargin(new Insets(8, 15, 8, 15));
        btnEdit.addActionListener(e -> showEditReaderDialog());
        
        JButton btnDelete = new JButton("Xóa");
        btnDelete.setFont(new Font("Dialog", Font.PLAIN, 14));
        btnDelete.setMargin(new Insets(8, 15, 8, 15));
        btnDelete.addActionListener(e -> deleteReader());
        
        JButton btnRefresh = new JButton("Làm mới");
        btnRefresh.setFont(new Font("Dialog", Font.PLAIN, 14));
        btnRefresh.setMargin(new Insets(8, 15, 8, 15));
        btnRefresh.addActionListener(e -> loadReaders());
        
        txtSearchReader = new JTextField(15);
        txtSearchReader.setFont(new Font("Dialog", Font.PLAIN, 14));
        txtSearchReader.setPreferredSize(new Dimension(200, 32));
        
        JButton btnSearch = new JButton("Tìm kiếm");
        btnSearch.setFont(new Font("Dialog", Font.PLAIN, 14));
        btnSearch.setMargin(new Insets(8, 15, 8, 15));
        btnSearch.addActionListener(e -> searchReaders());
        
        JLabel lblSearch = new JLabel("Tìm kiếm:");
        lblSearch.setFont(new Font("Dialog", Font.PLAIN, 14));
        
        toolbarPanel.add(btnAdd);
        toolbarPanel.add(btnEdit);
        toolbarPanel.add(btnDelete);
        toolbarPanel.add(btnRefresh);
        toolbarPanel.add(lblSearch);
        toolbarPanel.add(txtSearchReader);
        toolbarPanel.add(btnSearch);
        
        panel.add(toolbarPanel, BorderLayout.NORTH);


        String[] columns = {"Mã độc giả", "Họ tên", "Email", "SĐT", "Mã thẻ", "Ngày đăng ký", "Ngày hết hạn"};
        modelDocGia = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableDocGia = new JTable(modelDocGia);
        tableDocGia.setFont(new Font("Dialog", Font.PLAIN, 12));
        tableDocGia.setRowHeight(22);
        tableDocGia.getTableHeader().setFont(new Font("Dialog", Font.BOLD, 12));
        tableDocGia.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(tableDocGia);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // =============== TAB QUẢN LÝ MƯỢN TRẢ ===============
    private JPanel createBorrowManagementPanel() { JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


        JPanel toolbarPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        
        JButton btnBorrow = new JButton("Tạo phiếu mượn");
        btnBorrow.setFont(new Font("Dialog", Font.PLAIN, 14));
        btnBorrow.setMargin(new Insets(8, 15, 8, 15));
        btnBorrow.addActionListener(e -> showBorrowDialog());
        
        JButton btnReturn = new JButton("Trả sách");
        btnReturn.setFont(new Font("Dialog", Font.PLAIN, 14));
        btnReturn.setMargin(new Insets(8, 15, 8, 15));
        btnReturn.addActionListener(e -> returnBook());
        
        JButton btnRefresh = new JButton("Làm mới");
        btnRefresh.setFont(new Font("Dialog", Font.PLAIN, 14));
        btnRefresh.setMargin(new Insets(8, 15, 8, 15));
        btnRefresh.addActionListener(e -> loadBorrowRecords());
        
        toolbarPanel.add(btnBorrow);
        toolbarPanel.add(btnReturn);
        toolbarPanel.add(btnRefresh);
        
        panel.add(toolbarPanel, BorderLayout.NORTH);


        String[] columns = {"Mã phiếu", "Mã độc giả", "Tên độc giả", "Mã sách", "Tên sách", 
                           "Ngày mượn", "Ngày trả dự kiến", "Ngày trả thực tế", "Tình trạng"};
        modelMuonTra = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tableMuonTra = new JTable(modelMuonTra);
        tableMuonTra.setFont(new Font("Dialog", Font.PLAIN, 12));
        tableMuonTra.setRowHeight(22);
        tableMuonTra.getTableHeader().setFont(new Font("Dialog", Font.BOLD, 12));
        tableMuonTra.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        JScrollPane scrollPane = new JScrollPane(tableMuonTra);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // =============== TAB THỐNG KÊ ===============
    private JPanel createStatisticsPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        Font statFont = new Font("Dialog", Font.PLAIN, 14);
        lblTotalBooks = new JLabel("Tổng số sách: 0");
        lblTotalBooks.setFont(statFont);
        lblTotalReaders = new JLabel("Tổng số độc giả: 0");
        lblTotalReaders.setFont(statFont);
        lblTotalBorrows = new JLabel("Tổng phiếu mượn: 0");
        lblTotalBorrows.setFont(statFont);
        lblActiveBorrows = new JLabel("Đang mượn: 0");
        lblActiveBorrows.setFont(statFont);
        lblOverdue = new JLabel("Quá hạn: 0");
        lblOverdue.setFont(statFont);

        panel.add(lblTotalBooks);
        panel.add(Box.createVerticalStrut(15));
        panel.add(lblTotalReaders);
        panel.add(Box.createVerticalStrut(15));
        panel.add(lblTotalBorrows);
        panel.add(Box.createVerticalStrut(15));
        panel.add(lblActiveBorrows);
        panel.add(Box.createVerticalStrut(15));
        panel.add(lblOverdue);

        return panel;
    }

    // =============== LOAD DATA ===============
    private void loadData() {
        loadBooks();
        loadReaders();
        loadBorrowRecords();
        loadStatistics();
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
                sach.getSoLuong()
            });
        }
    }

    private void loadReaders() {
        modelDocGia.setRowCount(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        List<DocGia> readers = libraryManager.getAllReaders();
        for (DocGia reader : readers) {
            modelDocGia.addRow(new Object[]{
                reader.getMaNguoiDung(),
                reader.getHoTen(),
                reader.getEmail(),
                reader.getSoDienThoai(),
                reader.getMaThe(),
                reader.getNgayDangKy().format(formatter),
                reader.getNgayHetHan().format(formatter)
            });
        }
    }

    private void loadBorrowRecords() {
        modelMuonTra.setRowCount(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        List<PhieuMuonSach> records = libraryManager.getAllBorrowRecords();
        
        for (PhieuMuonSach phieu : records) {
            DocGia reader = libraryManager.searchReaderById(phieu.getMaDocGia());
            Sach book = libraryManager.searchBookById(phieu.getMaSach());
            
            String ngayTraThucTe = phieu.getNgayTraThucTe() != null ? 
                phieu.getNgayTraThucTe().format(formatter) : "";
            
            modelMuonTra.addRow(new Object[]{
                phieu.getMaPhieu(),
                phieu.getMaDocGia(),
                reader != null ? reader.getHoTen() : "",
                phieu.getMaSach(),
                book != null ? book.getTenSach() : "",
                phieu.getNgayMuon().format(formatter),
                phieu.getNgayTraDuKien().format(formatter),
                ngayTraThucTe,
                phieu.getTinhTrang()
            });
        }
    }

    private void loadStatistics() {
        lblTotalBooks.setText("Tổng số sách: " + libraryManager.getTotalBooks());
        lblTotalReaders.setText("Tổng số độc giả: " + libraryManager.getTotalReaders());
        lblTotalBorrows.setText("Tổng phiếu mượn: " + libraryManager.getTotalBorrowRecords());
        lblActiveBorrows.setText("Đang mượn: " + libraryManager.getActiveBorrowCount());
        lblOverdue.setText("Quá hạn: " + libraryManager.getOverdueBorrowCount());
    }

    // =============== SEARCH ===============
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
                sach.getSoLuong()
            });
        }
    }

    private void searchReaders() {
        String keyword = txtSearchReader.getText().trim();
        if (keyword.isEmpty()) {
            loadReaders();
            return;
        }

        modelDocGia.setRowCount(0);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        List<DocGia> readers = libraryManager.searchReadersByName(keyword);
        for (DocGia reader : readers) {
            modelDocGia.addRow(new Object[]{
                reader.getMaNguoiDung(),
                reader.getHoTen(),
                reader.getEmail(),
                reader.getSoDienThoai(),
                reader.getMaThe(),
                reader.getNgayDangKy().format(formatter),
                reader.getNgayHetHan().format(formatter)
            });
        }
    }

    // =============== DIALOG THƯ VIỆN ===============
    private void showAddBookDialog() {
        JDialog dialog = new JDialog(this, "Thêm sách mới", true);
        dialog.setSize(450, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        Font fieldFont = new Font("Dialog", Font.PLAIN, 14);
        
        JTextField txtMaSach = new JTextField();
        txtMaSach.setFont(fieldFont);
        JTextField txtTenSach = new JTextField();
        txtTenSach.setFont(fieldFont);
        JTextField txtTacGia = new JTextField();
        txtTacGia.setFont(fieldFont);
        JTextField txtTheLoai = new JTextField();
        txtTheLoai.setFont(fieldFont);
        JTextField txtNamXB = new JTextField();
        txtNamXB.setFont(fieldFont);
        JTextField txtSoLuong = new JTextField();
        txtSoLuong.setFont(fieldFont);
        JTextField txtMoTa = new JTextField();
        txtMoTa.setFont(fieldFont);

        formPanel.add(new JLabel("Mã sách:"));
        formPanel.add(txtMaSach);
        formPanel.add(new JLabel("Tên sách:"));
        formPanel.add(txtTenSach);
        formPanel.add(new JLabel("Tác giả:"));
        formPanel.add(txtTacGia);
        formPanel.add(new JLabel("Thể loại:"));
        formPanel.add(txtTheLoai);
        formPanel.add(new JLabel("Năm xuất bản:"));
        formPanel.add(txtNamXB);
        formPanel.add(new JLabel("Số lượng:"));
        formPanel.add(txtSoLuong);
        formPanel.add(new JLabel("Mô tả:"));
        formPanel.add(txtMoTa);

        dialog.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnSave = new JButton("Lưu");
        btnSave.addActionListener(e -> {
            try {
                Sach sach = new Sach(
                    txtMaSach.getText(),
                    txtTenSach.getText(),
                    txtTacGia.getText(),
                    txtTheLoai.getText(),
                    Integer.parseInt(txtNamXB.getText()),
                    Integer.parseInt(txtSoLuong.getText()),
                    txtMoTa.getText()
                );
                libraryManager.addBook(sach);
                loadBooks();
                loadStatistics();
                dialog.dispose();
                JOptionPane.showMessageDialog(this, "Thêm sách thành công!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton btnCancel = new JButton("Hủy");
        btnCancel.addActionListener(e -> dialog.dispose());

        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private void showEditBookDialog() {
        int selectedRow = tableSach.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sách cần sửa!");
            return;
        }

        String maSach = (String) modelSach.getValueAt(selectedRow, 0);
        Sach sach = libraryManager.searchBookById(maSach);
        if (sach == null) return;

        JDialog dialog = new JDialog(this, "Sửa thông tin sách", true);
        dialog.setSize(450, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        Font fieldFont = new Font("Dialog", Font.PLAIN, 14);
        
        JTextField txtMaSach = new JTextField(sach.getMaSach());
        txtMaSach.setEditable(false);
        txtMaSach.setFont(fieldFont);
        JTextField txtTenSach = new JTextField(sach.getTenSach());
        txtTenSach.setFont(fieldFont);
        JTextField txtTacGia = new JTextField(sach.getTacGia());
        txtTacGia.setFont(fieldFont);
        JTextField txtTheLoai = new JTextField(sach.getTheLoai());
        txtTheLoai.setFont(fieldFont);
        JTextField txtNamXB = new JTextField(String.valueOf(sach.getNamXuatBan()));
        txtNamXB.setFont(fieldFont);
        JTextField txtSoLuong = new JTextField(String.valueOf(sach.getSoLuong()));
        txtSoLuong.setFont(fieldFont);
        JTextField txtMoTa = new JTextField(sach.getMoTa());
        txtMoTa.setFont(fieldFont);

        formPanel.add(new JLabel("Mã sách:"));
        formPanel.add(txtMaSach);
        formPanel.add(new JLabel("Tên sách:"));
        formPanel.add(txtTenSach);
        formPanel.add(new JLabel("Tác giả:"));
        formPanel.add(txtTacGia);
        formPanel.add(new JLabel("Thể loại:"));
        formPanel.add(txtTheLoai);
        formPanel.add(new JLabel("Năm xuất bản:"));
        formPanel.add(txtNamXB);
        formPanel.add(new JLabel("Số lượng:"));
        formPanel.add(txtSoLuong);
        formPanel.add(new JLabel("Mô tả:"));
        formPanel.add(txtMoTa);

        dialog.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnSave = new JButton("Lưu");
        btnSave.addActionListener(e -> {
            try {
                sach.setTenSach(txtTenSach.getText());
                sach.setTacGia(txtTacGia.getText());
                sach.setTheLoai(txtTheLoai.getText());
                sach.setNamXuatBan(Integer.parseInt(txtNamXB.getText()));
                sach.setSoLuong(Integer.parseInt(txtSoLuong.getText()));
                sach.setMoTa(txtMoTa.getText());
                
                loadBooks();
                dialog.dispose();
                JOptionPane.showMessageDialog(this, "Cập nhật sách thành công!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton btnCancel = new JButton("Hủy");
        btnCancel.addActionListener(e -> dialog.dispose());

        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private void deleteBook() {
        int selectedRow = tableSach.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sách cần xóa!");
            return;
        }

        String maSach = (String) modelSach.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Bạn có chắc chắn muốn xóa sách này?", 
            "Xác nhận", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (libraryManager.deleteBook(maSach)) {
                loadBooks();
                loadStatistics();
                JOptionPane.showMessageDialog(this, "Xóa sách thành công!");
            } else {
                JOptionPane.showMessageDialog(this, "Xóa sách thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showAddReaderDialog() {
        JDialog dialog = new JDialog(this, "Thêm độc giả mới", true);
        dialog.setSize(400, 400);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField txtMaDocGia = new JTextField();
        JTextField txtHoTen = new JTextField();
        JTextField txtEmail = new JTextField();
        JTextField txtSDT = new JTextField();
        JPasswordField txtMatKhau = new JPasswordField();
        JTextField txtMaThe = new JTextField();
        JTextField txtSoNamHieuLuc = new JTextField("1");

        formPanel.add(new JLabel("Mã độc giả:"));
        formPanel.add(txtMaDocGia);
        formPanel.add(new JLabel("Họ tên:"));
        formPanel.add(txtHoTen);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(txtEmail);
        formPanel.add(new JLabel("Số điện thoại:"));
        formPanel.add(txtSDT);
        formPanel.add(new JLabel("Mật khẩu:"));
        formPanel.add(txtMatKhau);
        formPanel.add(new JLabel("Mã thẻ:"));
        formPanel.add(txtMaThe);
        formPanel.add(new JLabel("Số năm hiệu lực:"));
        formPanel.add(txtSoNamHieuLuc);

        dialog.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnSave = new JButton("Lưu");
        btnSave.addActionListener(e -> {
            try {
                DocGia docGia = new DocGia(
                    txtMaDocGia.getText(),
                    txtHoTen.getText(),
                    txtEmail.getText(),
                    txtSDT.getText(),
                    new String(txtMatKhau.getPassword()),
                    txtMaThe.getText(),
                    LocalDate.now(),
                    LocalDate.now().plusYears(Integer.parseInt(txtSoNamHieuLuc.getText()))
                );
                libraryManager.addReader(docGia);
                loadReaders();
                loadStatistics();
                dialog.dispose();
                JOptionPane.showMessageDialog(this, "Thêm độc giả thành công!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton btnCancel = new JButton("Hủy");
        btnCancel.addActionListener(e -> dialog.dispose());

        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private void showEditReaderDialog() {
        int selectedRow = tableDocGia.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn độc giả cần sửa!");
            return;
        }

        String maDocGia = (String) modelDocGia.getValueAt(selectedRow, 0);
        DocGia docGia = libraryManager.searchReaderById(maDocGia);
        if (docGia == null) return;

        JDialog dialog = new JDialog(this, "Sửa thông tin độc giả", true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField txtMaDocGia = new JTextField(docGia.getMaNguoiDung());
        txtMaDocGia.setEditable(false);
        JTextField txtHoTen = new JTextField(docGia.getHoTen());
        JTextField txtEmail = new JTextField(docGia.getEmail());
        JTextField txtSDT = new JTextField(docGia.getSoDienThoai());
        JTextField txtMaThe = new JTextField(docGia.getMaThe());
        JTextField txtNgayHetHan = new JTextField(docGia.getNgayHetHan().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        formPanel.add(new JLabel("Mã độc giả:"));
        formPanel.add(txtMaDocGia);
        formPanel.add(new JLabel("Họ tên:"));
        formPanel.add(txtHoTen);
        formPanel.add(new JLabel("Email:"));
        formPanel.add(txtEmail);
        formPanel.add(new JLabel("Số điện thoại:"));
        formPanel.add(txtSDT);
        formPanel.add(new JLabel("Mã thẻ:"));
        formPanel.add(txtMaThe);
        formPanel.add(new JLabel("Ngày hết hạn (dd/MM/yyyy):"));
        formPanel.add(txtNgayHetHan);

        dialog.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnSave = new JButton("Lưu");
        btnSave.addActionListener(e -> {
            try {
                docGia.setHoTen(txtHoTen.getText());
                docGia.setEmail(txtEmail.getText());
                docGia.setSoDienThoai(txtSDT.getText());
                docGia.setMaThe(txtMaThe.getText());
                
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                docGia.setNgayHetHan(LocalDate.parse(txtNgayHetHan.getText(), formatter));
                
                loadReaders();
                dialog.dispose();
                JOptionPane.showMessageDialog(this, "Cập nhật độc giả thành công!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton btnCancel = new JButton("Hủy");
        btnCancel.addActionListener(e -> dialog.dispose());

        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private void deleteReader() {
        int selectedRow = tableDocGia.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn độc giả cần xóa!");
            return;
        }

        String maDocGia = (String) modelDocGia.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Bạn có chắc chắn muốn xóa độc giả này?", 
            "Xác nhận", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (libraryManager.deleteReader(maDocGia)) {
                loadReaders();
                loadStatistics();
                JOptionPane.showMessageDialog(this, "Xóa độc giả thành công!");
            } else {
                JOptionPane.showMessageDialog(this, "Xóa độc giả thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showBorrowDialog() {
        JDialog dialog = new JDialog(this, "Tạo phiếu mượn sách", true);
        dialog.setSize(350, 200);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JTextField txtMaDocGia = new JTextField();
        JTextField txtMaSach = new JTextField();
        JTextField txtSoNgay = new JTextField("14");

        formPanel.add(new JLabel("Mã độc giả:"));
        formPanel.add(txtMaDocGia);
        formPanel.add(new JLabel("Mã sách:"));
        formPanel.add(txtMaSach);
        formPanel.add(new JLabel("Số ngày mượn:"));
        formPanel.add(txtSoNgay);

        dialog.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnSave = new JButton("Tạo phiếu");
        btnSave.addActionListener(e -> {
            try {
                boolean success = libraryManager.borrowBook(
                    txtMaDocGia.getText(),
                    txtMaSach.getText(),
                    Integer.parseInt(txtSoNgay.getText())
                );
                
                if (success) {
                    loadBorrowRecords();
                    loadBooks();
                    loadStatistics();
                    dialog.dispose();
                    JOptionPane.showMessageDialog(this, "Tạo phiếu mượn thành công!");
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Không thể tạo phiếu mượn!\nKiểm tra lại thông tin.", 
                        "Lỗi", 
                        JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton btnCancel = new JButton("Hủy");
        btnCancel.addActionListener(e -> dialog.dispose());

        buttonPanel.add(btnSave);
        buttonPanel.add(btnCancel);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    private void returnBook() {
        int selectedRow = tableMuonTra.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phiếu mượn cần trả!");
            return;
        }

        String maPhieu = (String) modelMuonTra.getValueAt(selectedRow, 0);
        String tinhTrang = (String) modelMuonTra.getValueAt(selectedRow, 8);
        
        if (!tinhTrang.equals("Đang mượn")) {
            JOptionPane.showMessageDialog(this, "Phiếu mượn này đã được trả!");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, 
            "Xác nhận trả sách?", 
            "Xác nhận", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (libraryManager.returnBook(maPhieu)) {
                loadBorrowRecords();
                loadBooks();
                loadStatistics();
                JOptionPane.showMessageDialog(this, "Trả sách thành công!");
            } else {
                JOptionPane.showMessageDialog(this, "Trả sách thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
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
