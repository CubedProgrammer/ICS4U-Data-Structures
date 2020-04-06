package structs;
import java.util.*;
/**
 * Hash table class for ICS4U
 * HashMap_03_29.java
 * @author Kevin Zhang
 */
public class HashMap_03_29<K,V> {

    /**
     * @param<K> The type of key
     * @param<V> The type of value
     */
    public static final class KVPair<K, V> {

        /**
         * The key of the key-value pair
         */
        private K key;

        /**
         * The value of the key-value pair
         */
        private V val;

        /**
         * Constructor for KVPair
         *
         * @param key Key of the pair
         * @param val Value of the pair
         */
        public KVPair(K key, V val) {
            this.key = key;
            this.val = val;
        }

    }

    /**
     * Default bucket count for hash map
     */
    public static final int DEFAULT_BUCKET_COUNT = 1000;

    /**
     * Default load factor for hash map
     */
    public static final double DEFAULT_LOAD_FACTOR = 0.75;

    /**
     * Specialized hashing function
     * @param o An object that you want hashed
     * @return The hashcode for that object
     */
    public static final int betterHash(Object o) {
        //special cases for hashing primitive arrays
        if (o.getClass() == byte[].class)
            return Arrays.hashCode((byte[]) o);
        else if (o.getClass() == short[].class)
            return Arrays.hashCode((short[]) o);
        else if (o.getClass() == int[].class)
            return Arrays.hashCode((int[]) o);
        else if (o.getClass() == long[].class)
            return Arrays.hashCode((long[]) o);
        else if (o.getClass() == char[].class)
            return Arrays.hashCode((char[]) o);
        else if (o.getClass() == boolean[].class)
            return Arrays.hashCode((boolean[]) o);
        else if (o.getClass() == float[].class)
            return Arrays.hashCode((float[]) o);
        else if (o.getClass() == double[].class)
            return Arrays.hashCode((double[]) o);
        else if (o.getClass().isArray())//special case for object arrays
            return Arrays.deepHashCode((Object[]) o);
        return o.hashCode();//normal hash code

    }

    /**
     * Stuff in the hash map
     */
    private ArrayList<LinkedList<KVPair<K, V>>> stuff;

    /**
     * Load factor of the map
     */
    private double load = 0;

    /**
     * Maximum size of the map before resizing
     */
    private int maxsize = 0;

    /**
     * Size of the hash map
     */
    private int size = 0;

    /**
     * Constructs an empty hash map
     */
    public HashMap_03_29() {
        //make new arraylist and make it have default number of buckets
        this.stuff = new ArrayList<>();
        for (int i = 0; i < DEFAULT_BUCKET_COUNT; i++)
            stuff.add(new LinkedList<>());
        //set load and max size
        this.load = DEFAULT_LOAD_FACTOR;
        this.maxsize = (int) (this.load * this.stuff.size());

    }

    /**
     * Adds an item to the map
     * @param key The key
     */
    public void add(K key) {
        add(key, null);
    }

    /**
     * Adds a key-value pair to the map
     * @param key The key
     * @param val The value
     */
    public void add(K key, V val) {
        //get hashcode and iterator
        int h = hashCode(key);
        Iterator<KVPair<K, V>> it = stuff.get(h).iterator();
        KVPair<K, V> tmp;
        boolean exists = false;
        //loops through to find it
        while (it.hasNext()) {

            tmp = it.next();
            //if it exists
            if (tmp.key.equals(key)) {
                exists = true;//mark it as exists
                tmp.val = val;//change the value
            }

        }
        //if it doesn't exist
        if (!exists) {
            stuff.get(h).add(new KVPair<K, V>(key, val));//add it
            ++size;//increment size
        }
        //if size is bigger than allowed size
        if (size > maxsize)
            realloc(stuff.size() * 4 / 3);//reallocate 4/3 the memory

    }

    /**
     * Removes a key from the hash map
     * @param key The key to look for
     */
    public void remove(K key) {
        //hash the key, and get the iterator
        int h = hashCode(key);
        Iterator<KVPair<K, V>> it = stuff.get(h).iterator();
        KVPair<K, V> tmp;
        //loops through to find the key
        while (it.hasNext()) {
            tmp = it.next();
            //if the key exists
            if (tmp.key.equals(key)) {
                it.remove();//remove it
                --size;//and decrement size
            }
        }
        //if size is too small compared to max size
        if (size < load * maxsize)
            realloc(maxsize);//reallocate smaller

    }

    /**
     * Checks if a key is contained in the hash map
     * @param key The key to look for
     * @return True if the key was found
     */
    public boolean contains(K key) {
        //get the hashcode and iterator
        int h=hashCode(key);
        Iterator<KVPair<K, V>> it = stuff.get(h).iterator();
        KVPair<K, V> tmp;
        //if the thing exists
        boolean exists = false;
        //loops through linked list to find it
        while (it.hasNext()) {

            tmp = it.next();
            if (tmp.key.equals(key)) {
                exists=true;//set exist to true
                break;//and break, when found
            }

        }

        return exists;

    }

    /**
     * Reallocates to a certain bucket count
     * @param size The bucket count to reallocate to
     */
    private void realloc(int size) {
        //keep a record of old stuff, iterator and temporary variable
        ArrayList<LinkedList<KVPair<K,V>>>oldstuff=stuff;
        Iterator<KVPair<K,V>>it;
        KVPair<K,V>tmp;
        //make a new array list for the stuff
        stuff=new ArrayList<>();
        //and add linked lists into it
        for(int i=0;i<size;i++)
            stuff.add(new LinkedList<>());
        //reset size variable and set maxsize variable
        this.size=0;
        this.maxsize=(int)(load*stuff.size());
        //goes through the old stuff
        for(int i=0;i<oldstuff.size();i++) {
            //and add everything into the new stuff
            it=oldstuff.get(i).iterator();
            while(it.hasNext()) {
                tmp=it.next();
                add(tmp.key,tmp.val);
            }

        }

    }

    /**
     * Get the load
     * @return The load, a number between 0 and 1
     */
    public double getLoad() {
        return(double)size/stuff.size();
    }

    /**
     * Set and force the load
     * @param load The load to force
     */
    public void setLoad(double load) {
        if(load<this.load)
            realloc((int)(size/Math.min(0.8,Math.max(0.1,load))));
    }

    /**
     * Set the maximum load
     * @param maxload The maximum load
     */
    public void setMaxLoad(double maxload) {
        this.load=Math.min(0.8,Math.max(0.1,maxload));
    }

    /**
     * Converts the hash map to a key array
     * @return The array of keys
     */
    public ArrayList<K>toArray() {
        //array list an iterator
        ArrayList<K>arr=new ArrayList<K>();
        Iterator<KVPair<K,V>>it;
        //going through all the linked lists
        for(int i=0;i<stuff.size();i++) {
            it=stuff.get(i).iterator();//get the iterator
            while(it.hasNext())arr.add(it.next().key);//add the key to the array
        }

        return arr;

    }

    /**
     * Hashes a key based on the size of the hash map
     * @param key The key to hash
     * @return The hashcode
     */
    public int hashCode(K key) {
        return(betterHash(key)%stuff.size()+stuff.size())%stuff.size();
    }

    /**
     * Gets the size of the hash map
     * @return The size of the hash map
     */
    public int size() {
        return size;
    }

}