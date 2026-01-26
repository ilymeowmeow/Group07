package datastructure;

import model.DocGia;
import java.util.ArrayList;
import java.util.List;

/**
 * Cây nhị phân tìm kiếm (BST) để lưu trữ và quản lý độc giả
 */
public class ReaderBST {
    private ReaderBSTNode root;
    private int size;

    public ReaderBST() {
        this.root = null;
        this.size = 0;
    }

    // Thêm độc giả vào BST
    public void insert(DocGia docGia) {
        root = insertRec(root, docGia);
        size++;
    }

    private ReaderBSTNode insertRec(ReaderBSTNode node, DocGia docGia) {
        if (node == null) {
            return new ReaderBSTNode(docGia);
        }

        int cmp = docGia.getMaNguoiDung().compareToIgnoreCase(node.getDocGia().getMaNguoiDung());
        
        if (cmp < 0) {
            node.setLeft(insertRec(node.getLeft(), docGia));
        } else if (cmp > 0) {
            node.setRight(insertRec(node.getRight(), docGia));
        } else {
            // Nếu mã trùng, cập nhật thông tin
            node.setDocGia(docGia);
            size--; // Không tăng size vì chỉ update
        }
        
        return node;
    }

    // Tìm kiếm độc giả theo mã
    public DocGia search(String maDocGia) {
        return searchRec(root, maDocGia);
    }

    private DocGia searchRec(ReaderBSTNode node, String maDocGia) {
        if (node == null) {
            return null;
        }

        int cmp = maDocGia.compareToIgnoreCase(node.getDocGia().getMaNguoiDung());
        
        if (cmp == 0) {
            return node.getDocGia();
        } else if (cmp < 0) {
            return searchRec(node.getLeft(), maDocGia);
        } else {
            return searchRec(node.getRight(), maDocGia);
        }
    }

    // Xóa độc giả
    public boolean delete(String maDocGia) {
        int sizeBefore = size;
        root = deleteRec(root, maDocGia);
        return size < sizeBefore;
    }

    private ReaderBSTNode deleteRec(ReaderBSTNode node, String maDocGia) {
        if (node == null) {
            return null;
        }

        int cmp = maDocGia.compareToIgnoreCase(node.getDocGia().getMaNguoiDung());

        if (cmp < 0) {
            node.setLeft(deleteRec(node.getLeft(), maDocGia));
        } else if (cmp > 0) {
            node.setRight(deleteRec(node.getRight(), maDocGia));
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
            ReaderBSTNode successor = findMin(node.getRight());
            node.setDocGia(successor.getDocGia());
            node.setRight(deleteRec(node.getRight(), successor.getDocGia().getMaNguoiDung()));
        }

        return node;
    }

    private ReaderBSTNode findMin(ReaderBSTNode node) {
        while (node.getLeft() != null) {
            node = node.getLeft();
        }
        return node;
    }

    // Duyệt cây theo thứ tự (In-order) - trả về danh sách độc giả đã sắp xếp theo mã
    public List<DocGia> inorderTraversal() {
        List<DocGia> result = new ArrayList<>();
        inorderRec(root, result);
        return result;
    }

    private void inorderRec(ReaderBSTNode node, List<DocGia> result) {
        if (node != null) {
            inorderRec(node.getLeft(), result);
            result.add(node.getDocGia());
            inorderRec(node.getRight(), result);
        }
    }

    // Tìm kiếm độc giả theo tên
    public List<DocGia> searchByName(String name) {
        List<DocGia> result = new ArrayList<>();
        searchByNameRec(root, name.toLowerCase(), result);
        return result;
    }

    private void searchByNameRec(ReaderBSTNode node, String name, List<DocGia> result) {
        if (node == null) {
            return;
        }

        DocGia docGia = node.getDocGia();
        if (docGia.getHoTen().toLowerCase().contains(name)) {
            result.add(docGia);
        }

        searchByNameRec(node.getLeft(), name, result);
        searchByNameRec(node.getRight(), name, result);
    }

    // Kiểm tra độc giả có tồn tại không
    public boolean contains(String maDocGia) {
        return search(maDocGia) != null;
    }

    // Lấy tất cả độc giả
    public List<DocGia> getAllReaders() {
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
