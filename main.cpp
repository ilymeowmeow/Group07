#include <iostream>
#include <fstream>
#include <string>
#include <iomanip>
using namespace std;

/* ===== STRUCT NHAN VIEN ===== */
struct NhanVien {
    string maNV;
    string hoTen;
    string ngaySinh;
    string email;
    string diaChi;
    string sdt;
    int soNgayCong;
    double luongNgay;
    double thucLinh;
};

/* ===== NODE & LIST ===== */
struct Node {
    NhanVien data;
    Node* next;
};

typedef Node* LIST;

/* ===== KHOI TAO ===== */
void init(LIST& l) {
    l = NULL;
}

/* ===== TINH THUC LINH ===== */
double tinhThucLinh(NhanVien nv) {
    return nv.soNgayCong * nv.luongNgay;
}

/* ===== TAO NODE ===== */
Node* createNode(NhanVien nv) {
    Node* p = new Node;
    p->data = nv;
    p->next = NULL;
    return p;
}

/* ===== KIEM TRA TRUNG MA ===== */
bool tonTaiMa(LIST l, string ma) {
    while (l != NULL) {
        if (l->data.maNV == ma)
            return true;
        l = l->next;
    }
    return false;
}

/* ===== THEM CUOI ===== */
void addTail(LIST& l, NhanVien nv) {
    Node* p = createNode(nv);
    if (l == NULL)
        l = p;
    else {
        Node* q = l;
        while (q->next != NULL)
            q = q->next;
        q->next = p;
    }
}

/* ===== NHAP 1 NHAN VIEN ===== */
NhanVien nhapNV(LIST l) {
    NhanVien nv;
    cin.ignore(); 
    do {
        cout << "Ma NV: ";
        getline(cin, nv.maNV);
        if (tonTaiMa(l, nv.maNV))
            cout << "Ma bi trung! Nhap lai!\n";
    } while (tonTaiMa(l, nv.maNV));

    cout << "Ho ten: "; getline(cin, nv.hoTen);
    cout << "Ngay sinh: "; getline(cin, nv.ngaySinh);
    cout << "Email: "; getline(cin, nv.email);
    cout << "Dia chi: "; getline(cin, nv.diaChi);
    cout << "SDT: "; getline(cin, nv.sdt);
    cout << "So ngay cong: "; cin >> nv.soNgayCong;
    cout << "Luong ngay: "; cin >> nv.luongNgay;

    nv.thucLinh = tinhThucLinh(nv);
    return nv;
}

/* ===== XUAT 1 NHAN VIEN ===== */
void xuatNV(NhanVien nv) {
    cout << left << setw(10) << nv.maNV
         << setw(20) << nv.hoTen
         << setw(12) << nv.ngaySinh
         << setw(20) << nv.email
         << setw(15) << nv.sdt
         << setw(10) << nv.soNgayCong
         << setw(12) << size_t(nv.luongNgay)
         << setw(12) << size_t(nv.thucLinh) << endl;
}

/* ===== XUAT DS ===== */
void xuatDS(LIST l) {
    cout << "\n===== DANH SACH NHAN VIEN =====\n";
    while (l != NULL) {
        xuatNV(l->data);
        l = l->next;
    }
}

/* ===== GHI FILE ===== */
void ghiFile(LIST l, string tenFile) {
    ofstream f(tenFile);
    if (!f) {
        cout << "Khong the tao file " << tenFile << endl;
        return;
    }
    while (l != NULL) {
        f << l->data.maNV << "|"
          << l->data.hoTen << "|"
          << l->data.ngaySinh << "|"
          << l->data.email << "|"
          << l->data.diaChi << "|"
          << l->data.sdt << "|"
          << l->data.soNgayCong << "|"
          << size_t(l->data.luongNgay) << "|"
          << size_t(l->data.thucLinh) << endl;
        l = l->next;
    }
    f.close();
    cout << "Da ghi file: " << tenFile << endl;
}

/* ===== DOC FILE ===== */
void docFile(LIST& l, string tenFile) {
    ifstream f(tenFile);
    if (!f) {
        cout << "Khong tim thay file " << tenFile << endl;
        return;
    }
    NhanVien nv;
    while (getline(f, nv.maNV, '|')) {
        getline(f, nv.hoTen, '|');
        getline(f, nv.ngaySinh, '|');
        getline(f, nv.email, '|');
        getline(f, nv.diaChi, '|');
        getline(f, nv.sdt, '|');
        f >> nv.soNgayCong; f.ignore();
        f >> nv.luongNgay; f.ignore();
        f >> nv.thucLinh; f.ignore();
        addTail(l, nv);
    }
    f.close();
    cout << "Doc file thanh cong!\n";
}

/* ===== TIM THEO MA ===== */
Node* timTheoMa(LIST l, string ma) {
    while (l != NULL) {
        if (l->data.maNV == ma)
            return l;
        l = l->next;
    }
    return NULL;
}

/* ===== TIM THEO TEN ===== */
void timTheoTen(LIST l, string ten) {
    bool thay = false;
    while (l != NULL) {
        if (l->data.hoTen.find(ten) != string::npos) {
            xuatNV(l->data);
            thay = true;
        }
        l = l->next;
    }
    if (!thay) cout << "Khong tim thay nhan vien nao co ten: " << ten << endl;
}

/* ===== LUONG THAP NHAT ===== */
void luongThapNhat(LIST l) {
    if (l == NULL) return;
    double min = l->data.thucLinh;
    for (Node* p = l; p != NULL; p = p->next)
        if (p->data.thucLinh < min)
            min = p->data.thucLinh;

    cout << "Nhan vien co luong thap nhat (" << size_t(min) << "):\n";
    for (Node* p = l; p != NULL; p = p->next)
        if (p->data.thucLinh == min)
            xuatNV(p->data);
}

/* ===== SAP XEP GIAM DAN ===== */
void sapXep(LIST l) {
    for (Node* i = l; i != NULL; i = i->next)
        for (Node* j = i->next; j != NULL; j = j->next)
            if (i->data.thucLinh < j->data.thucLinh)
                swap(i->data, j->data);
}

/* ===== XOA THEO MA ===== */
void xoaTheoMa(LIST& l, string ma) {
    if (l == NULL) return;
    if (l->data.maNV == ma) {
        Node* t = l;
        l = l->next;
        delete t;
        cout << "Da xoa nhan vien " << ma << endl;
        return;
    }
    Node* p = l;
    while (p->next != NULL && p->next->data.maNV != ma)
        p = p->next;
    if (p->next != NULL) {
        Node* t = p->next;
        p->next = t->next;
        delete t;
        cout << "Da xoa nhan vien " << ma << endl;
    } else {
        cout << "Khong tim thay ma de xoa!\n";
    }
}

/* ===== MENU ===== */
void menu() {
    cout << "\n-----------------------------------";
    cout << "\n1. Nhap DS va ghi file DSNV.txt";
    cout << "\n2. Doc DS tu file va xuat";
    cout << "\n3. Tim theo ma";
    cout << "\n4. Tim theo ten";
    cout << "\n5. NV co luong thap nhat";
    cout << "\n6. Sap xep giam dan va ghi file";
    cout << "\n7. Xoa theo ma va ghi file DSNV_XOA.txt";
    cout << "\n8. Them nhan vien va ghi file DSNV_THEM.txt";
    cout << "\n9. Sua thong tin va ghi file DSNV_SUA.txt";
    cout << "\n0. Thoat";
    cout << "\n-----------------------------------";
}

/* ===== MAIN ===== */
int main() {
    LIST l;
    init(l);
    int chon;
    string ma, ten;

    do {
        menu();
        cout << "\nChon: ";
        cin >> chon;

        switch (chon) {
        case 1:
            addTail(l, nhapNV(l));
            ghiFile(l, "DSNV.txt");
            break;
        case 2:
            init(l);
            docFile(l, "DSNV.txt");
            xuatDS(l);
            break;
        case 3:
            cin.ignore();
            cout << "Nhap ma: ";
            getline(cin, ma);
            if (timTheoMa(l, ma))
                xuatNV(timTheoMa(l, ma)->data);
            else
                cout << "Khong tim thay!\n";
            break;
        case 4:
            cin.ignore();
            cout << "Nhap ten: ";
            getline(cin, ten);
            timTheoTen(l, ten);
            break;
        case 5:
            luongThapNhat(l);
            break;
        case 6:
            sapXep(l);
            xuatDS(l);
            ghiFile(l, "DSNV_SAPXEP.txt");
            break;
        case 7:
            cin.ignore();
            cout << "Nhap ma can xoa: ";
            getline(cin, ma);
            xoaTheoMa(l, ma);
            ghiFile(l, "DSNV_XOA.txt");
            break;
        case 8:
            cout << "\n--- THEM NHAN VIEN ---\n";
            addTail(l, nhapNV(l)); 
            ghiFile(l, "DSNV_THEM.txt");
            break;
        case 9:
            cin.ignore();
            cout << "\n--- SUA THONG TIN NHAN VIEN ---\n";
            cout << "Nhap ma NV can sua: ";
            getline(cin, ma);
            Node* p = timTheoMa(l, ma);
            if (p != NULL) {
                cout << "Tim thay NV! Nhap thong tin moi (giu nguyen ma):\n";
                // Nhap lai cac thong tin tru MA NV
                cout << "Ho ten: "; getline(cin, p->data.hoTen);
                cout << "Ngay sinh: "; getline(cin, p->data.ngaySinh);
                cout << "Email: "; getline(cin, p->data.email);
                cout << "Dia chi: "; getline(cin, p->data.diaChi);
                cout << "SDT: "; getline(cin, p->data.sdt);
                cout << "So ngay cong: "; cin >> p->data.soNgayCong;
                cout << "Luong ngay: "; cin >> p->data.luongNgay;
                p->data.thucLinh = tinhThucLinh(p->data);
                
                cout << "Da cap nhat thong tin!\n";
                ghiFile(l, "DSNV_SUA.txt");
            } else {
                cout << "Khong tim thay ma NV nay!\n";
            }
            break;
        }
    } while (chon != 0);

    return 0;
}