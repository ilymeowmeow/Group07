package datastructure;

import model.DocGia;

/**
 * Node của Binary Search Tree chứa thông tin Độc giả
 */
public class ReaderBSTNode {
    private DocGia docGia;
    private ReaderBSTNode left;
    private ReaderBSTNode right;

    public ReaderBSTNode(DocGia docGia) {
        this.docGia = docGia;
        this.left = null;
        this.right = null;
    }

    // Getters và Setters
    public DocGia getDocGia() {
        return docGia;
    }

    public void setDocGia(DocGia docGia) {
        this.docGia = docGia;
    }

    public ReaderBSTNode getLeft() {
        return left;
    }

    public void setLeft(ReaderBSTNode left) {
        this.left = left;
    }

    public ReaderBSTNode getRight() {
        return right;
    }

    public void setRight(ReaderBSTNode right) {
        this.right = right;
    }
}
