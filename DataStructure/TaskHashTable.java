package DataStructure;
public class TaskHashTable<K, V> {
    private Entry<K, V>[] buckets;
    private int capacity;
    private int size;

    public TaskHashTable(int capacity) {
        this.capacity = capacity;
        this.size = 0;
        this.buckets = new Entry[capacity];
    }

    private static class Entry<K, V> {
        K key;
        V value;
        Entry<K, V> next;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }

    private int getBucketIndex(K key) {
        int hashCode = key.hashCode();
        return Math.abs(hashCode % capacity);
    }

    public void put(K key, V value) {
        int bucketIndex = getBucketIndex(key);
        Entry<K, V> entry = new Entry<>(key, value);

        if (buckets[bucketIndex] == null) {
            buckets[bucketIndex] = entry;
            size++;
        } else {
            Entry<K, V> current = buckets[bucketIndex];
            while (current.next != null) {
                if (current.key.equals(key)) {
                    current.value = value;
                    return;
                }
                current = current.next;
            }
            if (current.key.equals(key)) {
                current.value = value;
            } else {
                current.next = entry;
                size++;
            }
        }
    }

    public V get(K key) {
        int bucketIndex = getBucketIndex(key);
        Entry<K, V> current = buckets[bucketIndex];

        while (current != null) {
            if (current.key.equals(key)) {
                return current.value;
            }
            current = current.next;
        }

        return null;
    }

    public boolean containsKey(K key) {
        int bucketIndex = getBucketIndex(key);
        Entry<K, V> current = buckets[bucketIndex];

        while (current != null) {
            if (current.key.equals(key)) {
                return true;
            }
            current = current.next;
        }

        return false;
    }
}