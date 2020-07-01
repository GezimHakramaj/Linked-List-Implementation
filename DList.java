import java.util.*;

public class DList<E> implements List<E>{
    //LinkedList implementation using nil as a Sentinel node rather than a tail & head reference to null.
    private DListNode<E> nil;
    private int size;
    private static class DListNode<E> { // Creating static inner class node
        E data; // Generic data
        DListNode<E> next, previous; // Next and Prev references

        DListNode(E data) { // DListNode constructor.
            this.data = data;
        }
    }

    public DList() { // Doubly linked list constructor
        nil = new DListNode<>(null); // Nil node which will replace Null pointer
        nil.next = nil; // Nils next is nil
        nil.previous = nil; // Nils prev is nil
    }


    class DListIterator implements ListIterator<E> { // Iterator inner class to iterate throughout the list
        private DListNode<E> pointer; // Pointer node to keep integrity of the list.
        private int index; // Index counter
        private boolean inOrder; // Boolean variable to check if user wants a in order list or a list in reverse.

        public DListIterator(boolean inOrder, int index) { // Iterator constructor.
            this.inOrder = inOrder;
            this.index = index;
            if(inOrder){
                if (nil.next == nil)
                    pointer = nil;
                else {
                    if (index > 0) {
                        if (index >= size())
                            throw new NoSuchElementException();
                        pointer = marchTo(index);
                    } else
                        pointer = nil.next;
                }
            } else {
                if (nil.previous == nil)
                    pointer = nil;
                else
                    pointer = nil.previous;
            }
        }

        public boolean hasPrevious() { // Reverse order hasNext method
            return pointer != nil;
        }

        public E previous() { // Returns previous node
            E old = pointer.data;
            pointer = pointer.previous;
            return old;
        }

        public int nextIndex() { // Returns next index.
            return (index + 1);
        }

        public int previousIndex() { // Returns previous index.
            return (index - 1);
        }

        public boolean hasNext() { // In order has next
            return pointer != nil;
        }

        public E next() { // Returns next node
            E old = pointer.data;
            if(inOrder) {
                pointer = pointer.next;
            }else{
                pointer = pointer.previous;
            }
            return old;
        }

        public void remove() {
            //optional
        }

        public void set(E e) {
            //optional
        }

        public void add(E e) {
            //optional
        }

    }

    public ListIterator<E> listIterator(int index){ // Create a list iterator at index
        return new DListIterator(true, index);
    }

    public Iterator<E> descendingIterator() { // Create a reverse list ierator at 0 index.
        return new DListIterator(false, this.size - 1);
    }

    public DListNode<E> marchTo(int index){
        //Helper method to go to a specific node which is index.
        int position = 0;
        DListNode<E> temp = nil.next;
        if(index > size() || index < 0)
            throw new NoSuchElementException();
        while(position != index){
            temp = temp.next;
            position++;
        }
        return temp;
    }

    public boolean add(E e) {
        //Checks if the list contains(e) returns false if false otherwise adds to the end with addLast and returns true;
        if(contains(e))
            return false;
        //If your list doesn't contain duplicates.
        addLast(e);
        return true;
    }

    public void add(int index, E element){
        /*Inserts element into the list by index which is position in this method. When position equals index and is
        //less than size temp's previous's next is the new node and the new nodes next is the temp node(original node)
        shifted all to the right.
        */
        if(index > size())
            throw new IndexOutOfBoundsException();
        DListNode<E> temp = marchTo(index);
        DListNode<E> node = new DListNode<>(element);
        temp.previous.next = node;
        node.next = temp;
        node.previous = temp.previous;
        temp.previous = node;
        size++;
    }

    public boolean addAll(Collection<? extends E> c){
        //Method to add all elements from a collection into our class object returns bool when finished..
        boolean bool = false;
        if(c.size() == 0)
            throw new NullPointerException();
        for(E e: c) {
            addLast(e);
            bool = true;
        }
        return bool;
    }

    public boolean addAll(int index, Collection<? extends E> c) {
        ////Method to add all elements from a collection into our class object at a specific index.
        boolean bool = false;
        if (index > size())
            throw new IndexOutOfBoundsException();
        if (c.size() == 0)
            throw new NullPointerException();
        for (E e : c) {
            add(index, e);
            bool = true;
        }
        return bool;
    }

    public void addFirst(E elem) {
        /*
        Method to add to the beginning of the list. If the list is empty we set nil's previous and next to node.
        We set the node's previous and next to nil. Add one to the size. Otherwise we set nil's next's previous to the node.
        (which is the node after nil to then set its previous to nil.) node's next to equal nil's original next. Then
        we set Nil's next to the node. Then the nodes previous to nil and increment the size.
        */
        DListNode<E> node = new DListNode<>(elem);
        if (nil.next == nil && nil.previous == nil) {
            nil.next = node;
            nil.previous = node;
            node.next = nil;
            node.previous = nil;
            size++;
        } else {
            nil.next.previous = node;
            node.next = nil.next;
            nil.next = node;
            node.previous = nil;
            size++;
        }
    }

    public void addLast(E elem) {
     /*
        Method to add to the end of the list. If the list is empty we set nil's previous and next to node.
        We set the node's previous and next to nil. Add one to the size. Otherwise we set nil's previous' next to the node.
        (which is the node before nil to then set its next to nil.) node's previous to equal nil's original previous. Then
        we set Nil's previous to the node. Then the nodes next to nil and increment the size.
     */
        DListNode<E> node = new DListNode<>(elem);
        if (nil.previous == nil && nil.next == nil) {
            nil.previous = node;
            nil.next = node;
            node.previous = nil;
            node.next = nil;
            size++;
        } else {
            nil.previous.next = node;
            node.previous = nil.previous;
            nil.previous = node;
            node.next = nil;
            size++;
        }
    }

    public void clear(){
        //Method to clear the list
        nil.next = nil;
        nil.previous = nil;
        size = 0;
    }

    public Object clone(){
        //For each element in the list you add the new element in the new list.
        //addLast is called rather than add because if you do allow duplicates.
        DList<E> newList = new DList<>();
        for(E e: this)
            //newList.add(e) for duplicate check
            newList.addLast(e);
        return newList;
    }

    public E element() {
        //Method that returns the head's data otherwise throws NoSuchElementException
        if(size() == 0)
            throw new NoSuchElementException();
        return nil.next.data;
    }

    public E get(int index){
        //Method to return the node by given index. Throws an IndexOutOfBoundsException if index is greater than size.
        if(index > size())
            throw new IndexOutOfBoundsException();
        DListNode<E> node = marchTo(index);
        return node.data;
    }

    public E getFirst(){
        //Method to get the first node in the list. Throws a NoSuchElementException if list is empty.
        if(this.isEmpty())
            throw new NoSuchElementException();
        return element();
    }

    public E getLast(){
        //Method to get the last node in the list. Throws a NoSuchElementException if list is empty.
        if(this.isEmpty())
            throw new NoSuchElementException();
        return get(size()-1);
    }

    public int lastIndexOf(Object obj){
        //Method keeps track similar to LastOccurrence and sets occurrence to position while the
        //list is being traversed and returns position.
        int position = 0;
        int occurrence = 0;
        DListNode<E> temp = nil.next;
        while(temp != nil){
            if(temp.data.equals(obj)) {
                occurrence = position;
            }
            temp = temp.next;
            position++;
        }
        return occurrence;
    }

    public boolean offer(E e){
        //Returns true when node is added to end of list. Same as addLast method.
        addLast(e);
        return true;
    }

    public boolean offerFirst(E e){
        //Method that returns true and adds to the front of the list same as addFirst.
        addFirst(e);
        return true;

    }

    public boolean offerLast(E e){
        //Returns true when node is added to end of list. Same as addLast method.
        addLast(e);
        return true;
    }

    public E peek(){
        //Method to return the head which is nil.next or null if empty.
        return element();
    }

    public E peekFirst(){
        //Method to return the head which is nil.next or null if empty.
        return getFirst();
    }

    public E peekLast(){
        //Method to return the tail which is nil.previous or null if empty.
        return getLast();
    }

    public E poll(){
        //Method to remove and return the head which is nil.next or null if empty.
        return remove();
    }

    public E pollFirst(){
        //Method to remove and return the head which is nil.next or null if empty.
        return removeFirst();
    }

    public E pollLast(){
        //Method to remove and return the tail which is nil.previous or null if empty.
        return removeLast();
    }

    public E pop(){
        //Pops and returns the first element.(Top of the stack represented by this list.)
        return removeFirst();
    }

    public void push(E e){
        //Pushes e to the front of the list as represented by a stack.
        addFirst(e);
    }

    public E remove(){
        //Method to remove the first node using a temp node to return removed node's data
        //If size = 0 then the list is empty.
        if(size == 0)
            throw new NoSuchElementException();
        DListNode<E> temp = new DListNode<>(nil.next.data);
        nil.next = nil.next.next;
        nil.next.previous = nil;
        size--;
        return temp.data;
    }

    public E remove(int index) {
        //Method to remove the node at given index. Uses a second temp node to store the first temp's data
        //as to when you return the removed node you return the correct data.
        if (index > size())
            throw new IndexOutOfBoundsException();
        DListNode<E> temp = marchTo(index);
        DListNode<E> node = new DListNode<>(temp.data);
        remove(temp.data);
        return node.data;
    }

    public boolean remove(Object obj){
        //Method to remove any object in the List which is equal to Object obj.
        DListNode<E> temp = nil.next;
        while(temp != nil){
            if(temp.data.equals(obj)){
                temp.previous.next = temp.next;
                temp.next.previous = temp.previous;
                size--;
                return true;
            }
            temp = temp.next;
        }
        return false;
    }

    public boolean remove(Object obj, boolean inOrder){
        // If the list is in order then remove object like above
        if(inOrder)
            return remove(obj);
        else{ // Otherwise we remove the object going in reverse order.
            DListNode<E> temp = nil.previous;
            while(temp != nil){
                if(temp.data.equals(obj)){
                    temp.next.previous = temp.previous;
                    temp.previous.next = temp.next;
                    size--;
                    return true;
                }
                temp = temp.previous;
            }
        }
        return false;
    }

    public E removeFirst(){
        //Method to remove the first node using a temp node to return removed node's data
        //If size = 0 then the list is empty.
        if(size == 0)
            throw new NoSuchElementException();
        return remove();
    }

    public boolean removeFirstOccurrence(Object o){
        //Method is the same as LastOccurrence but removed the object when it firsts sees it. Head to Tail traversal.
        return remove(o);
    }

    public E removeLast(){
        //Method to remove the last node using a temp node to return removed node's data
        //If size = 0 then the list is empty.
        if(size == 0)
            throw new NoSuchElementException();
        return remove(size-1);
    }

    public boolean removeLastOccurrence(Object o){
        //Traverses the list backwards and removes the first occurrence.
        Iterator iter = this.descendingIterator();
        while(iter.hasNext()){
            if(iter.next() == o) {
               return remove(o, false);
            }
        }
        return false;
    }

    public E set(int index, E element){
        /*Instantiate two nodes, one temp which is the head to loop through the list and oldNode which is
          initialized with temp's data inside the while loop so every time temp becomes its next oldNode will
          also change its value. When position is equal to desired index temp's data becomes element and we return
          the oldNode's data.
        */
        if(index > size())
            throw new IndexOutOfBoundsException();
        DListNode<E> temp = marchTo(index);
        DListNode<E> oldNode = new DListNode<>(temp.data);
        temp.data = element;
        return oldNode.data;
    }

    public int size() {
        //Method to return the size of the list.
        return size;
    }

    public Object[] toArray(){
        Object[] array = new Object[this.size()];
        DListNode temp = nil.next;
        for(int i = 0; i < array.length; i++){
            array[i] = temp.data;
            temp = temp.next;

        }
        return array;
    }

    public <T> T[] toArray(T[] a){
        if(a == null)
            throw new NullPointerException();
        if(size() > a.length)
            throw new ArrayStoreException();
        a = (T[]) this.toArray();
        return a;
    }

    //required List Methods below

    public ListIterator<E> listIterator() {
        //Returns a listIterator at index 0 in order traversal.
        return new DListIterator(true, 0);
    }

    public Iterator<E> iterator() {
        //Returns an iterator from head to tail.
        return new DListIterator(true, 0);
    }

    public boolean isEmpty() {
        //Checks to see if the list is empty.
        return size() == 0;
    }

    public boolean contains(Object o) {
        //Checks to see if the list contains said object (o)
        DListNode<E> temp = nil.next;
        while(temp != nil){
            if(o.equals(temp.data))
                return true;
            temp = temp.next;
        }
        return false;
    }

    public boolean containsAll(Collection<?> c) {
        //Checks to see if this list contains all elements of c and returns true if it does else false.
        //ClassCastException
        if(c == null)
            throw new NullPointerException();
        boolean changed = false;
        for(Object e: c)
            if (this.contains(e))
                changed = true;
            else
                changed = false;
            return changed;
    }

    public int indexOf(Object o) {
        //Finds the index of when E e is equal to temp's data and returns the index. Otherwise returns -1
        //ClassCastException
        int index = 0;
        DListNode<E> temp = nil.next;
        while(temp != nil){
            if(o.equals(temp.data))
                return index;
            temp = temp.next;
            index++;
        }
        return -1;
    }

    public List<E> subList(int fromIndex, int toIndex) {
        //Returns a portion of the list in a new List from fromIndex to toIndex.
        if(fromIndex < 0 || toIndex > size() || fromIndex > toIndex)
            throw new IndexOutOfBoundsException();
        List<E> newList = new LinkedList();
        if(fromIndex == toIndex)
            return newList;
        DListNode<E> nodeFrom = marchTo(fromIndex);
        DListNode<E> nodeTo = marchTo(toIndex);
        while(nodeFrom != nodeTo){
            newList.add(nodeFrom.data);
            nodeFrom = nodeFrom.next;
        }
        return newList;
    }

    public boolean removeAll(Collection<?> c) {
        //Removes from the list whatever c does have and returns true if the list has been changed
        //UnsupportedOperationException
        //ClassCastException
        if(c == null)
            throw new NullPointerException();
        boolean changed = false;
        for(Object e: c)
            if(this.contains(e)) {
                this.remove(e);
                changed = true;
            }
        return changed;
    }

    public boolean retainAll(Collection<?> c) {
        //Retains whatever elements c has and removes whichever c doesn't have and returns true if the list has been changed
        //UnsupportedOperationException
        //ClassCastException
        if(c == null)
            throw new NullPointerException();
        boolean changed = false;
        for(Object e: this)
            if(!c.contains(e)) {
                this.remove(e);
                changed = true;
            }
        return changed;
    }

    //End required List Methods.

    public static void main(String[] args) {
        DList list = new DList<>();
        DList list2 = new DList<>();

        list.add("Hello, world");
        list.add(1);
        list.add(10);

        list2.add("Hello, world");
        list2.add(1);

        Iterator iter = list.descendingIterator();

        while(iter.hasNext()) {
            System.out.println(iter.next());
        }

    }
}

