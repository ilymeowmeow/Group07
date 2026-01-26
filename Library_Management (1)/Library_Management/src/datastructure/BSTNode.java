package datastructure;

import model.Sach;

/**
 * Node của Binary Search Tree chứa thông tin Sách
 */
public class BSTNode {
    private Sach sach;
    private BSTNode left;
    private BSTNode right;

    public BSTNode(Sach sach) {
        this.sach = sach;
        this.left = null;
        this.right = null;
    }

    // Getters và Setters
    public Sach getSach() {
        return sach;
    }

    public void setSach(Sach sach) {
        this.sach = sach;
    }

    public BSTNode getLeft() {
        return left;
    }

    public void setLeft(BSTNode left) {
        this.left = left;
    }

    public BSTNode getRight() {
        return right;
    }

    public void setRight(BSTNode right) {
        this.right = right;
    }
}
