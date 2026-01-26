package model;

import java.time.LocalDate;

/**
 * Lớp PhieuMuonSach đại diện cho một phiếu mượn sách
 */
public class PhieuMuonSach {
    private String maPhieu;
    private String maDocGia;
    private String maSach;
    private LocalDate ngayMuon;
    private LocalDate ngayTraDuKien;
    private LocalDate ngayTraThucTe;
    private String tinhTrang; // "Đang mượn", "Đã trả", "Quá hạn"

    public PhieuMuonSach() {
    }

    public PhieuMuonSach(String maPhieu, String maDocGia, String maSach, 
                        LocalDate ngayMuon, LocalDate ngayTraDuKien) {
        this.maPhieu = maPhieu;
        this.maDocGia = maDocGia;
        this.maSach = maSach;
        this.ngayMuon = ngayMuon;
        this.ngayTraDuKien = ngayTraDuKien;
        this.tinhTrang = "Đang mượn";
    }

    // Getters and Setters
    public String getMaPhieu() {
        return maPhieu;
    }

    public void setMaPhieu(String maPhieu) {
        this.maPhieu = maPhieu;
    }

    public String getMaDocGia() {
        return maDocGia;
    }

    public void setMaDocGia(String maDocGia) {
        this.maDocGia = maDocGia;
    }

    public String getMaSach() {
        return maSach;
    }

    public void setMaSach(String maSach) {
        this.maSach = maSach;
    }

    public LocalDate getNgayMuon() {
        return ngayMuon;
    }

    public void setNgayMuon(LocalDate ngayMuon) {
        this.ngayMuon = ngayMuon;
    }

    public LocalDate getNgayTraDuKien() {
        return ngayTraDuKien;
    }

    public void setNgayTraDuKien(LocalDate ngayTraDuKien) {
        this.ngayTraDuKien = ngayTraDuKien;
    }

    public LocalDate getNgayTraThucTe() {
        return ngayTraThucTe;
    }

    public void setNgayTraThucTe(LocalDate ngayTraThucTe) {
        this.ngayTraThucTe = ngayTraThucTe;
    }

    public String getTinhTrang() {
        return tinhTrang;
    }

    public void setTinhTrang(String tinhTrang) {
        this.tinhTrang = tinhTrang;
    }

    // Kiểm tra quá hạn
    public boolean isQuaHan() {
        if (ngayTraThucTe != null) {
            return false; // Đã trả rồi
        }
        return LocalDate.now().isAfter(ngayTraDuKien);
    }

    @Override
    public String toString() {
        return "PhieuMuonSach{" +
                "maPhieu='" + maPhieu + '\'' +
                ", maDocGia='" + maDocGia + '\'' +
                ", maSach='" + maSach + '\'' +
                ", ngayMuon=" + ngayMuon +
                ", ngayTraDuKien=" + ngayTraDuKien +
                ", ngayTraThucTe=" + ngayTraThucTe +
                ", tinhTrang='" + tinhTrang + '\'' +
                '}';
    }
}
