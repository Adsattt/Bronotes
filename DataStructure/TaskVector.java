package DataStructure;
import java.util.Comparator;
import java.util.Iterator;

public class TaskVector<E> implements Iterable {
    private static final int INITIAL_CAPACITY = 10;
    private Object[] elements;
    private int size;
    
    public TaskVector() {
        elements = new Object[INITIAL_CAPACITY];
        size = 0;
    }
    
    public void add(E element) {
        if (size == elements.length) {
            resize();
        }
        elements[size++] = element;
    }
    
    @SuppressWarnings("unchecked")
    public E get(int index) {
        return (E) elements[index];
    }

    public void set(int index, E element) {
        elements[index] = element;
    }
    
    public E remove(int index) {
        E removed = (E) elements[index];
        System.arraycopy(elements, index + 1, elements, index, size - index - 1);
        elements[--size] = null;
        return removed;
    }
    
    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }
    
    private void resize() {
        int newCapacity = elements.length * 2;
        Object[] newElements = new Object[newCapacity];
        System.arraycopy(elements, 0, newElements, 0, size);
        elements = newElements;
    }

    @Override
    public Iterator iterator() {
        return new TaskArrayListIterator();
    }

    private class TaskArrayListIterator implements Iterator<E> {
        private int currentIndex = 0;
        
        @Override
        public boolean hasNext() {
            return currentIndex < size;
        }
        
        @SuppressWarnings("unchecked")
        @Override
        public E next() {
            return (E) elements[currentIndex++];
        }
        
        @Override
        public void remove() {
            System.out.println("remove() operation is not supported.");
        }
    }

    public void sort(Comparator<? super E> comparator) {
        quicksort(0, size - 1, comparator);
    }
    
    private void quicksort(int low, int high, Comparator<? super E> comparator) {
        if (low < high) {
            int pivotIndex = partition(low, high, comparator);
            quicksort(low, pivotIndex - 1, comparator);
            quicksort(pivotIndex + 1, high, comparator);
        }
    }
    
    private int partition(int low, int high, Comparator<? super E> comparator) {
        E pivot = get(high);
        int i = low - 1;
        
        for (int j = low; j < high; j++) {
            if (comparator.compare(get(j), pivot) <= 0) {
                i++;
                swap(i, j);
            }
        }
        
        swap(i + 1, high);
        return i + 1;
    }

    private void swap(int i, int j) {
        E temp = get(i);
        set(i, get(j));
        set(j, temp);
    }
}