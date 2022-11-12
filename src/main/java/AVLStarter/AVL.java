package AVLStarter;
import java.util.Optional;

import static java.lang.Integer.max;

/********************************************************************************************
 * CISC/CMPE 422/835
 * Buggy implementation of AVL trees
 * Starter code for assignment on property-based testing
 ********************************************************************************************/
public class AVL<K extends Comparable<K>,V> {
    public K key;
    public V value;
    public int height;
    public AVL<K,V> left;
    public AVL<K,V> right;

    /**
     * Setter for AVL tree
     * @param k key
     * @param v value
     * @param l left subtree
     * @param r right subtree
     * @param h height (empty tree has height 0)
     */
    public AVL(K k, V v, AVL<K,V> l, AVL<K,V> r, int h) {
        this.key=k; this.value=v; this.left=l; this.right=r; this.height=h;
    }

    /**
     * Returns empty AVL tree (i.e., instance with all attributes set to null)
     * @return empty AVL tree
     */
    public static <K extends Comparable<K>, V> AVL<K, V> empty() {
        return new AVL<>(null,null, null, null, 0);
    }

    /**
     * Emptiness check (i.e., if all attributes are null)
     * @return true, if empty; false, otherwise
     */
    public boolean isEmpty() {
        return this.key==null && this.value==null && this.left==null && this.right==null && this.height==0;
    }

    /**
     * return deep clone, i.e., a new, separate AVL tree that is an exact copy
     * @return deep clone
     */
    @Override
    public AVL<K,V> clone() {
        if (this.isEmpty())
            return AVL.empty();
        else {
            AVL<K,V> leftB = this.left.clone();
            AVL<K,V> rightB = this.right.clone();
            return new AVL<>(this.key, this.value, leftB, rightB, this.height);
        }
    }

    /**
     * Uses in-order traversal to produce string representation of tree: "[key-value-height:left right]"
     * @return string
     */
    @Override
    public String toString() {
        if (this.isEmpty()) return "[]";
        else {
            String kStr = this.key.toString();
            String vStr = this.value.toString();
            int h = this.height;
            return "[" + kStr + "-" + vStr + "-" + h + ":" + this.left.toString() + this.right.toString() + "]";
        }
    }

    /**
     * Find and return value stored in tree under argument key
     * @param k key to look for
     * @return Optional containing value if k in tree, otherwise empty Optional
     */
    public Optional<V> find(K k) {
        if (this.isEmpty())
            return Optional.empty();
        else if (this.key.compareTo(k) < 0)   // k is greater than key at this node
            return this.right.find(k);
        else if (this.key.compareTo(k) > 0)   // k is less than key at this node
            return this.left.find(k);
        else                                  // k is equal to key at this node
            return Optional.of(this.value);
    }

    /**
     * Insert key k with value v in tree. If tree already contains k, replace value currently stored under k by v. If tree does not contain k, create new node with key k and value v.
     * @param k key
     * @param v value
     * @return Optional containing value if k in tree, empty Optional otherwise
     */
    public AVL<K,V> insert(K k, V v) {
        if (this.isEmpty()) {
            this.key = k;
            this.value = v;
            this.height = 1;
            this.left = empty();
            this.right = empty();
            return this;
        }
        else if (this.key.compareTo(k) < 0) {  // key to be inserted is greater than key at root of this tree
            this.right = this.right.insert(k,v);
            this.height = 1 + max(this.left.height, this.right.height);
            return this.rebalance();
        }
        else if (this.key.compareTo(k) > 0) {  // key to be inserted is less than key at root of this tree
            this.left = this.left.insert(k,v);
            this.height = 1 + max(this.left.height, this.right.height);
            return this.rebalance();
        }
        else {                                 // key to be inserted is same as key at root of this tree
            this.value = v;
            return this;
        }
    }

    /**
     * Delete node with key k.
     * Contains at least 2 and at most 4 bugs!
     * @param k key
     * @return Optional containing value if k in tree, empty Optional otherwise.
     */
    public AVL<K,V> delete(K k) {
        if (this.isEmpty())
            return this;

        else if (this.key.compareTo(k) < 0) {    // k is greater than key in root
            this.right = this.right.delete(k);
            this.height = 1 + max(this.left.height, this.right.height);
            return this.rebalance();
        } else if (this.key.compareTo(k) > 0) {   // k is smaller than key in root
            this.left = this.left.delete(k);
            this.height = 1 + max(this.left.height, this.right.height);
            return this.rebalance();
        } else {                                  // k is equal to key in root
            if (this.left.isEmpty())              // node to be deleted has no left child
                if (this.right.isEmpty()) {       // node to be deleted has no children
                    this.value = null;            // empty out this node
                    this.key = null;
                    this.left = null;
                    this.right = null;
                    this.height = 0;
                    return this;                  // return empty tree
                } else {                          // node to be deleted has a right child
                    this.key = this.right.key;
                    this.value = this.right.value;
                    this.left = this.right.left;
                    this.right = this.right.right;
                    this.height = this.right.height;
                    return this.rebalance();
                }
            else {                                          // node to be deleted has a left child
                AVL<K,V> leftRightMostB = this.left;
                while (!leftRightMostB.right.isEmpty())     // find right-most node in left child
                    leftRightMostB = leftRightMostB.right;
                this.key = leftRightMostB.key;
                this.value = leftRightMostB.value;
                this.left = this.left.delete(k);
                this.height = 1 + max(this.left.height, this.right.height);
                return this.rebalance();
            }
        }
    }
    public int balFactor() {
        return this.right.height - this.left.height;  // ==0: balanced, <0: left-heavy, >0: right-heavy
    }
    public AVL<K,V> rebalance() {
        AVL<K,V> p = this;
        if (p.balFactor() > 1) {                  // p is too right-heavy
            if (p.right.balFactor() > 0)
                p = p.rotateLeft();               // need one left rotation around the root
            else {
                p.right = p.right.rotateRight();  // need right rotation around the right child followed by a left rotation around the root
                p = p.rotateLeft();
            }
        }
        else if (p.balFactor() < -1) {            // p is too left-heavy
            if (p.left.balFactor() <= 0)
                p = p.rotateRight();
            else {
                p.left = p.left.rotateLeft();
                p = p.rotateRight();
            }
        }
        return p;
    }
    private AVL<K,V> rotateRight() {
        AVL<K,V> p = this;
        AVL<K,V> l = p.left;
        AVL<K,V> lr = l.right;
        l.right = p;
        p.left = lr;
        p.height = 1 + max(p.left.height, p.right.height);
        l.height = 1 + max(l.left.height, l.right.height);
        return l;
    }
    private AVL<K,V> rotateLeft() {
        AVL<K,V> p = this;
        AVL<K,V> r = p.right;
        AVL<K,V> rl = r.left;
        r.left = p;
        p.right = rl;
        p.height = 1 + max(p.left.height, p.right.height);
        r.height = 1 + max(r.left.height, r.right.height);
        return r;
    }

}
