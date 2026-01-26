package model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp DocGia kế thừa từ NguoiDung
 */
public class DocGia extends NguoiDung {
    private String maThe;
    private LocalDate ngayDangKy;
    private LocalDate ngayHetHan;
    private List<String> lichSuMuonList;

    public DocGia() {
        super();
        this.lichSuMuonList = new ArrayList<>();
    }

    public DocGia(String maNguoiDung, String hoTen, String email, String soDienThoai, 
                  String matKhau, String maThe, LocalDate ngayDangKy, LocalDate ngayHetHan) {
        super(maNguoiDung, hoTen, email, soDienThoai, matKhau);
        this.maThe = maThe;
        this.ngayDangKy = ngayDangKy;
        this.ngayHetHan = ngayHetHan;
        this.lichSuMuonList = new ArrayList<>();
    }

    // Getters and Setters
    public String getMaThe() {
        return maThe;
    }

    public void setMaThe(String maThe) {
        this.maThe = maThe;
    }

    public LocalDate getNgayDangKy() {
        return ngayDangKy;
    }

    public void setNgayDangKy(LocalDate ngayDangKy) {
        this.ngayDangKy = ngayDangKy;
    }

    public LocalDate getNgayHetHan() {
        return ngayHetHan;
    }

    public void setNgayHetHan(LocalDate ngayHetHan) {
        this.ngayHetHan = ngayHetHan;
    }

    public List<String> getLichSuMuonList() {
        return lichSuMuonList;
    }

    public void setLichSuMuonList(List<String> lichSuMuonList) {
        this.lichSuMuonList = lichSuMuonList;
    }

    // Thêm lịch sử mượn
    public void themLichSuMuon(String lichSu) {
        lichSuMuonList.add(lichSu);
    }

    @Override
    public void hienThiThongTin() {
        System.out.println("=== THÔNG TIN ĐỘC GIẢ ===");
        System.out.println("Mã độc giả: " + maNguoiDung);
        System.out.println("Họ tên: " + hoTen);
        System.out.println("Email: " + email);
        System.out.println("Số điện thoại: " + soDienThoai);
        System.out.println("Mã thẻ: " + maThe);
        System.out.println("Ngày đăng ký: " + ngayDangKy);
        System.out.println("Ngày hết hạn: " + ngayHetHan);
    }
}
