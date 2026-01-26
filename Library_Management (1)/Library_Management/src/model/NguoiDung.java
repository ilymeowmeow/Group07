package model;

/**
 * Lớp cha cho tất cả người dùng trong hệ thống
 */
public abstract class NguoiDung {
    protected String maNguoiDung;
    protected String hoTen;
    protected String email;
    protected String soDienThoai;
    protected String matKhau;

    public NguoiDung() {
    }

    public NguoiDung(String maNguoiDung, String hoTen, String email, String soDienThoai, String matKhau) {
        this.maNguoiDung = maNguoiDung;
        this.hoTen = hoTen;
        this.email = email;
        this.soDienThoai = soDienThoai;
        this.matKhau = matKhau;
    }

    // Getters and Setters
    public String getMaNguoiDung() {
        return maNguoiDung;
    }

    public void setMaNguoiDung(String maNguoiDung) {
        this.maNguoiDung = maNguoiDung;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }

    // Abstract method
    public abstract void hienThiThongTin();
}
