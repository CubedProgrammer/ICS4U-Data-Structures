import java.util.Comparator;
/**
 * LList.java
 * @param <T> The type of object.
 * @author Kevin Zhang
 */
public class LList<T> {

    /**
     * Class LNode
     * @param <T> The type of object.
     */
    public static class LNode<T> {

        /**
         * Previous node
         */
        private LNode<T>prev;

        /**
         * Next node
         */
        private LNode<T>next;

        /**
         * Value of this node
         */
        private T val;

        /**
         * Constructs an LNode
         * @param val The value of the node
         * @param next The next node
         * @param prev The previous node
         */
        public LNode(T val,LNode<T>next,LNode<T>prev) {

            this.val=val;
            this.next=next;
            this.prev=prev;

        }

    }

    /**
     * Head of the list
     */
    private LNode<T>head;

    /**
     * Tail of the list
     */
    private LNode<T>tail;

    /**
     * Size of the list
     */
    private int size;

    /**
     * Constructs an empty list
     */
    public LList(){}

    /**
     * Gets the size of the list
     * @return The size of the list
     */
    public int size() {
        return size;
    }

    /**
     * Pushes a value onto the stack
     * @param val The value to push
     */
    public void push(T val) {
        append(val);
    }

    /**
     * Pops an element off of the stack
     * @return The element popped off
     */
    public T pop() {
        return popb();
    }

    /**
     * Enqueues an element onto the queue
     * @param val The element to put at the beginning of the queue.
     */
    public void enqueue(T val) {
        append(val);
    }

    /**
     * Dequeues an element off of the queue
     * @return The element taken off of the end of the queue
     */
    public T dequeue() {
        return popf();
    }

    /**
     * Inserts an element assuming the list is sorted from greatest to least
     */
    public void sortedInsert(Comparator<?super T> compar,T val) {

        //insert at the end if it is less than tail
        if(compar.compare(val,tail.val)<0) {
            append(val);
            return;
        }

        //loops through nodes until it finds one that is less than it
        for(LNode<T>node=head;node!=null;node=node.next) {
            if(compar.compare(val,node.val)>0) {

                //and add it to the list
                node.prev=new LNode<>(val,node,node.prev);
                if(node==head)//head corner case
                    head=node.prev;
                else if(node.prev.prev!=null)//second from head corner case
                    node.prev.prev.next=node.prev;
                ++size;
                return;

            }
        }

    }

    /**
     * Removes duplicates from the list so the list only has unique values
     */
    public void removeDuplicates() {

        //loops through the list
        for(LNode<T>node=head;node!=null;node=node.next) {
            //loops through the list
            for(LNode<T>node2=node;node2!=null;node2=node2.next) {
                //if values are equal and pointers are not
                if(node2.val.equals(node.val)&&node2!=node) {
                    delete(node2);//delete node
                    node2=node2.prev;//and set node2 back one step
                }

            }

        }

    }

    /**
     * Reverses the order of the list
     */
    public void reverse() {

        //first and last iterators
        LNode<T>__f=head,__l=tail;
        T tmp;

        //keep moving while iterators don't touch
        while(__f!=__l) {

            //swap values
            tmp=__f.val;
            __f.val=__l.val;
            __l.val=tmp;

            //move iterators along and terminate if they meet
            __f=__f.next;
            if(__f==__l)
                return;
            __l=__l.prev;

        }

    }

    /**
     * {@inheritDoc}
     */
    public Object clone() {

        //make new list
        LList<T>lis=new LList<T>();

        //put everything into new list
        for(LNode<T>node=head;node!=null;node=node.next) {
            lis.append(node.val);
        }

        return lis;

    }

    /**
     * {@inheritDoc}
     */
    public String toString() {

        String s="";//string representation
        for(LNode<T>node=head;node!=null;node=node.next) {

            s+=node.val;//add the value to string
            if(node!=tail)
                s+=", ";//and a comma if it isn't the last element

        }

        return"["+s+"]";//attach the brackets

    }

    /**
     * Deletes an element at a specified index
     * @param ind The index to delete at
     */
    public void deleteAt(int ind) {

        if(ind<0||ind>=size)//throw an exception if it is out of bounds
            throw new IndexOutOfBoundsException("Linked List index is out of range.");

        //if index is in the first half of the list
        if(ind<=size>>>1) {

            //loop from head until the index is reached
            LNode<T>node=head;
            for(int i=0;i<ind;i++) {
                node=node.next;
            }

            //delete the node
            delete(node);
            return;

        }

        //loop from tail until index is reached
        LNode<T>node=tail;
        for(int i=size-1;i>ind;i--) {
            node=node.prev;
        }
        //delete the node
        delete(node);

    }

    /**
     * Deletes a node from the list
     * @param node The node to delete
     */
    private T delete(LNode<T>node) {

        //corner cases
        if(node==head)
            return popf();
        else if(node==tail)
            return popb();

        //reroute pointers of previous and next
        node.prev.next=node.next;
        node.next.prev=node.prev;
        --size;
        return node.val;

    }

    /**
     * Delete a value from the list, if it exists
     */
    public void delete(T val) {

        //loops through the list until the value is found
        for(LNode<T>node=head;node!=null;node=node.next) {
            if(node.val==val) {
                delete(node);//delete the node assosiated with that value
                return;//and terminate the function
            }
        }

    }

    /**
     * Adds to the end of the list
     * @param val The value to append
     */
    public void append(T val) {

        if(size==0)//adding to empty list
            this.head=this.tail=new LNode<T>(val,null,null);
        else {
            tail.next=new LNode<T>(val,null,tail);//make node
            this.tail=tail.next;//redirect tail pointer
        }
        ++size;

    }

    /**
     * Adds to the beginning of the list
     * @param val The value to prepend
     */
    public void prepend(T val) {

        if(size==0)//adding to empty list
            this.head=this.tail=new LNode<T>(val,null,null);
        else {
            head.prev=new LNode<T>(val,head,null);//make node
            this.head=head.prev;//redirect head pointer
        }
        ++size;

    }

    /**
     * Pops from the back of the list
     * @return The value that was popped
     */
    public T popb() {

        T val=tail.val;//get the value
        this.tail=tail.prev;//redirect tail pointer
        if(tail==null)//case if list had one remaining
            this.head=null;
        else//normal case
            tail.next=null;
        --size;
        return val;

    }

    /**
     * Pops from the front of the list
     * @return The value that was popped
     */
    public T popf() {

        T val=head.val;//get the value
        this.head=head.next;//redirect head pointer
        if(head==null)//case if list had one remaining
            this.tail=null;
        else//normal case
            head.prev=null;
        --size;
        return val;

    }

}