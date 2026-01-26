package datastructure;

import model.Sach;
import java.util.ArrayList;
import java.util.List;

/**
 * Cây nhị phân tìm kiếm (BST) để lưu trữ và quản lý sách
 */
public class BookBST {
    private BSTNode root;
    private int size;

    public BookBST() {
        this.root = null;
        this.size = 0;
    }

    // Thêm sách vào BST
    public void insert(Sach sach) {
        root = insertRec(root, sach);
        size++;
    }

    private BSTNode insertRec(BSTNode node, Sach sach) {
        if (node == null) {
            return new BSTNode(sach);
        }

        int cmp = sach.getTenSach().compareToIgnoreCase(node.getSach().getTenSach());
        
        if (cmp < 0) {
            node.setLeft(insertRec(node.getLeft(), sach));
        } else if (cmp > 0) {
            node.setRight(insertRec(node.getRight(), sach));
        }
        // Nếu cmp == 0, không thêm (tránh trùng lặp tên)
        
        return node;
    }

    // Tìm kiếm sách theo tên
    public Sach search(String tenSach) {
        return searchRec(root, tenSach);
    }

    private Sach searchRec(BSTNode node, String tenSach) {
        if (node == null) {
            return null;
        }

        int cmp = tenSach.compareToIgnoreCase(node.getSach().getTenSach());
        
        if (cmp == 0) {
            return node.getSach();
        } else if (cmp < 0) {
            return searchRec(node.getLeft(), tenSach);
        } else {
            return searchRec(node.getRight(), tenSach);
        }
    }

    // Tìm kiếm sách theo mã sách
    public Sach searchByMaSach(String maSach) {
        return searchByMaSachRec(root, maSach);
    }

    private Sach searchByMaSachRec(BSTNode node, String maSach) {
        if (node == null) {
            return null;
        }

        if (node.getSach().getMaSach().equals(maSach)) {
            return node.getSach();
        }

        Sach leftResult = searchByMaSachRec(node.getLeft(), maSach);
        if (leftResult != null) {
            return leftResult;
        }

        return searchByMaSachRec(node.getRight(), maSach);
    }

    // Xóa sách
    public boolean delete(String tenSach) {
        int sizeBefore = size;
        root = deleteRec(root, tenSach);
        return size < sizeBefore;
    }

    private BSTNode deleteRec(BSTNode node, String tenSach) {
        if (node == null) {
            return null;
        }

        int cmp = tenSach.compareToIgnoreCase(node.getSach().getTenSach());

        if (cmp < 0) {
            node.setLeft(deleteRec(node.getLeft(), tenSach));
        } else if (cmp > 0) {
            node.setRight(deleteRec(node.getRight(), tenSach));
        } else {
            // Node cần xóa được tìm thấy
            size--;

            // Trường hợp 1: Node lá hoặc có 1 con
            if (node.getLeft() == null) {
                return node.getRight();
            } else if (node.getRight() == null) {
                return node.getLeft();
            }

            // Trường hợp 2: Node có 2 con
            // Tìm node nhỏ nhất bên phải (successor)
            BSTNode successor = findMin(node.getRight());
            node.setSach(successor.getSach());
            node.setRight(deleteRec(node.getRight(), successor.getSach().getTenSach()));
        }

        return node;
    }

    private BSTNode findMin(BSTNode node) {
        while (node.getLeft() != null) {
            node = node.getLeft();
        }
        return node;
    }

    // Duyệt cây theo thứ tự (In-order) - trả về danh sách sách đã sắp xếp
    public List<Sach> inorderTraversal() {
        List<Sach> result = new ArrayList<>();
        inorderRec(root, result);
        return result;
    }

    private void inorderRec(BSTNode node, List<Sach> result) {
        if (node != null) {
            inorderRec(node.getLeft(), result);
            result.add(node.getSach());
            inorderRec(node.getRight(), result);
        }
    }

    // Tìm kiếm sách theo từ khóa (mã sách, tên, tác giả, hoặc thể loại)
    public List<Sach> searchByKeyword(String keyword) {
        List<Sach> result = new ArrayList<>();
        searchByKeywordRec(root, keyword.toLowerCase(), result);
        return result;
    }

    private void searchByKeywordRec(BSTNode node, String keyword, List<Sach> result) {
        if (node == null) {
            return;
        }

        Sach sach = node.getSach();
        if (sach.getMaSach().toLowerCase().contains(keyword) ||
            sach.getTenSach().toLowerCase().contains(keyword) || 
            sach.getTacGia().toLowerCase().contains(keyword) ||
            sach.getTheLoai().toLowerCase().contains(keyword)) {
            result.add(sach);
        }

        searchByKeywordRec(node.getLeft(), keyword, result);
        searchByKeywordRec(node.getRight(), keyword, result);
    }

    // Lấy tất cả sách
    public List<Sach> getAllBooks() {
        return inorderTraversal();
    }

    // Lấy kích thước
    public int getSize() {
        return size;
    }

    // Kiểm tra rỗng
    public boolean isEmpty() {
        return root == null;
    }

    // Xóa tất cả
    public void clear() {
        root = null;
        size = 0;
    }
}
