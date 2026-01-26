package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Lớp Admin kế thừa từ NguoiDung
 */
public class Admin extends NguoiDung {
    private String chucVu;
    private List<String> quyenTruyCapList;

    public Admin() {
        super();
        this.quyenTruyCapList = new ArrayList<>();
    }

    public Admin(String maNguoiDung, String hoTen, String email, String soDienThoai, String matKhau, String chucVu) {
        super(maNguoiDung, hoTen, email, soDienThoai, matKhau);
        this.chucVu = chucVu;
        this.quyenTruyCapList = new ArrayList<>();
    }

    // Getters and Setters
    public String getChucVu() {
        return chucVu;
    }

    public void setChucVu(String chucVu) {
        this.chucVu = chucVu;
    }

    public List<String> getQuyenTruyCapList() {
        return quyenTruyCapList;
    }

    public void setQuyenTruyCapList(List<String> quyenTruyCapList) {
        this.quyenTruyCapList = quyenTruyCapList;
    }

    // Thêm quyền truy cập
    public void themQuyenTruyCap(String quyen) {
        if (!quyenTruyCapList.contains(quyen)) {
            quyenTruyCapList.add(quyen);
        }
    }

    // Xóa quyền truy cập
    public void xoaQuyenTruyCap(String quyen) {
        quyenTruyCapList.remove(quyen);
    }

    @Override
    public void hienThiThongTin() {
        System.out.println("=== THÔNG TIN ADMIN ===");
        System.out.println("Mã Admin: " + maNguoiDung);
        System.out.println("Họ tên: " + hoTen);
        System.out.println("Email: " + email);
        System.out.println("Số điện thoại: " + soDienThoai);
        System.out.println("Chức vụ: " + chucVu);
        System.out.println("Quyền truy cập: " + quyenTruyCapList);
    }
}
