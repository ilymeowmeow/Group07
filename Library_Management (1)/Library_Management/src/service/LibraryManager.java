package service;

import datastructure.BookBST;
import datastructure.ReaderBST;
import model.*;
import java.time.LocalDate;
import java.util.*;

/**
 * Lớp quản lý toàn bộ hệ thống thư viện
 */
public class LibraryManager {
    private static LibraryManager instance;
    
    private BookBST bookBST;
    private ReaderBST readerBST;
    private List<PhieuMuonSach> borrowRecords;
    private List<Admin> admins;
    private Admin currentAdmin;
    private DocGia currentReader;

    private LibraryManager() {
        bookBST = new BookBST();
        readerBST = new ReaderBST();
        borrowRecords = new ArrayList<>();
        admins = new ArrayList<>();
        initializeSampleData();
    }

    public static LibraryManager getInstance() {
        if (instance == null) {
            instance = new LibraryManager();
        }
        return instance;
    }

    // Khởi tạo dữ liệu mẫu
    private void initializeSampleData() {
        // Thêm admin mặc định
        Admin admin = new Admin("admin", "Nguyễn Văn A", "admin@library.com",
                               "0123456789", "admin", "Quản trị viên");
        admin.themQuyenTruyCap("Quản lý sách");
        admin.themQuyenTruyCap("Quản lý độc giả");
        admin.themQuyenTruyCap("Quản lý mượn trả");
        admins.add(admin);

        // Thêm độc giả mẫu
        DocGia reader1 = new DocGia("DG001", "Trần Thị B", "reader1@email.com", 
                                   "0987654321", "123", "THE001",
                                   LocalDate.now(), LocalDate.now().plusYears(1));
        DocGia reader2 = new DocGia("DG002", "Lê Văn C", "reader2@email.com", 
                                   "0976543210", "123", "THE002",
                                   LocalDate.now(), LocalDate.now().plusYears(1));
        readerBST.insert(reader1);
        readerBST.insert(reader2);

        // Thêm sách mẫu
        bookBST.insert(new Sach("S001", "How do you live", "Miyazaki Hayao",
                               "Văn học", 2023, 10, "Tiểu thuyết kinh điển của Nhật Bản"));
        bookBST.insert(new Sach("S002", "Cấu trúc dữ liệu và giải thuật", "Trần Thị Anh",
                               "Công nghệ", 2022, 8, "Học về cấu trúc dữ liệu"));
        bookBST.insert(new Sach("S003", "Đắc nhân tâm", "Dale Carnegie", 
                               "Kỹ năng sống", 2020, 15, "Nghệ thuật giao tiếp và làm việc"));
        bookBST.insert(new Sach("S004", "Nhà giả kim", "Paulo Coelho", 
                               "Văn học", 2018, 12, "Truyện ngụ ngôn về hành trình tìm kiếm ước mơ"));
        bookBST.insert(new Sach("S005", "Thuật toán và ứng dụng", "Lê Văn Anh",
                               "Công nghệ", 2023, 6, "Các thuật toán phổ biến"));
    }

    // ========== QUẢN LÝ ĐĂNG NHẬP ==========
    public Admin loginAdmin(String username, String password) {
        for (Admin admin : admins) {
            if (admin.getMaNguoiDung().equals(username) && admin.getMatKhau().equals(password)) {
                currentAdmin = admin;
                return admin;
            }
        }
        return null;
    }

    public DocGia loginReader(String username, String password) {
        DocGia reader = readerBST.search(username);
        if (reader != null && reader.getMatKhau().equals(password)) {
            currentReader = reader;
            return reader;
        }
        return null;
    }

    public void logout() {
        currentAdmin = null;
        currentReader = null;
    }

    // ========== QUẢN LÝ SÁCH ==========
    public void addBook(Sach sach) {
        bookBST.insert(sach);
    }

    public boolean deleteBook(String maSach) {
        Sach sach = bookBST.searchByMaSach(maSach);
        if (sach != null) {
            return bookBST.delete(sach.getTenSach());
        }
        return false;
    }

    public List<Sach> getAllBooks() {
        return bookBST.getAllBooks();
    }

    public Sach searchBookById(String maSach) {
        return bookBST.searchByMaSach(maSach);
    }

    public List<Sach> searchBooksByKeyword(String keyword) {
        return bookBST.searchByKeyword(keyword);
    }

    // ========== QUẢN LÝ ĐỘC GIẢ ==========
    public void addReader(DocGia docGia) {
        readerBST.insert(docGia);
    }

    public boolean deleteReader(String maDocGia) {
        return readerBST.delete(maDocGia);
    }

    public List<DocGia> getAllReaders() {
        return readerBST.getAllReaders();
    }

    public DocGia searchReaderById(String maDocGia) {
        return readerBST.search(maDocGia);
    }

    public List<DocGia> searchReadersByName(String name) {
        return readerBST.searchByName(name);
    }

    // ========== QUẢN LÝ MƯỢN TRẢ ==========
    public boolean borrowBook(String maDocGia, String maSach, int soNgayMuon) {
        DocGia reader = readerBST.search(maDocGia);
        Sach book = bookBST.searchByMaSach(maSach);

        if (reader == null || book == null || book.getSoLuong() <= 0) {
            return false;
        }

        // Kiểm tra thẻ hết hạn
        if (reader.getNgayHetHan().isBefore(LocalDate.now())) {
            return false;
        }

        // Tạo phiếu mượn
        String maPhieu = "PM" + System.currentTimeMillis();
        LocalDate ngayMuon = LocalDate.now();
        LocalDate ngayTraDuKien = ngayMuon.plusDays(soNgayMuon);
        
        PhieuMuonSach phieu = new PhieuMuonSach(maPhieu, maDocGia, maSach, ngayMuon, ngayTraDuKien);
        borrowRecords.add(phieu);

        // Giảm số lượng sách
        book.setSoLuong(book.getSoLuong() - 1);

        // Thêm vào lịch sử mượn
        reader.themLichSuMuon("Mượn: " + book.getTenSach() + " - " + ngayMuon);

        return true;
    }

    public boolean returnBook(String maPhieu) {
        for (PhieuMuonSach phieu : borrowRecords) {
            if (phieu.getMaPhieu().equals(maPhieu) && phieu.getTinhTrang().equals("Đang mượn")) {
                // Cập nhật phiếu
                phieu.setNgayTraThucTe(LocalDate.now());
                phieu.setTinhTrang("Đã trả");

                // Tăng số lượng sách
                Sach book = bookBST.searchByMaSach(phieu.getMaSach());
                if (book != null) {
                    book.setSoLuong(book.getSoLuong() + 1);
                }

                // Cập nhật lịch sử
                DocGia reader = readerBST.search(phieu.getMaDocGia());
                if (reader != null) {
                    reader.themLichSuMuon("Trả: " + book.getTenSach() + " - " + LocalDate.now());
                }

                return true;
            }
        }
        return false;
    }

    public List<PhieuMuonSach> getAllBorrowRecords() {
        return new ArrayList<>(borrowRecords);
    }

    public List<PhieuMuonSach> getBorrowRecordsByReader(String maDocGia) {
        List<PhieuMuonSach> result = new ArrayList<>();
        for (PhieuMuonSach phieu : borrowRecords) {
            if (phieu.getMaDocGia().equals(maDocGia)) {
                result.add(phieu);
            }
        }
        return result;
    }

    public List<PhieuMuonSach> getActiveBorrowRecords() {
        List<PhieuMuonSach> result = new ArrayList<>();
        for (PhieuMuonSach phieu : borrowRecords) {
            if (phieu.getTinhTrang().equals("Đang mượn")) {
                result.add(phieu);
            }
        }
        return result;
    }

    // ========== THỐNG KÊ ==========
    public int getTotalBooks() {
        return bookBST.getSize();
    }

    public int getTotalReaders() {
        return readerBST.getSize();
    }

    public int getTotalBorrowRecords() {
        return borrowRecords.size();
    }

    public int getActiveBorrowCount() {
        int count = 0;
        for (PhieuMuonSach phieu : borrowRecords) {
            if (phieu.getTinhTrang().equals("Đang mượn")) {
                count++;
            }
        }
        return count;
    }

    public int getOverdueBorrowCount() {
        int count = 0;
        for (PhieuMuonSach phieu : borrowRecords) {
            if (phieu.isQuaHan()) {
                count++;
            }
        }
        return count;
    }

    // Getters
    public Admin getCurrentAdmin() {
        return currentAdmin;
    }

    public DocGia getCurrentReader() {
        return currentReader;
    }
}
