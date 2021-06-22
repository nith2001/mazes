package disjointsets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A quick-union-by-size data structure with path compression.
 *
 * @see DisjointSets for more documentation.
 */
public class UnionBySizeCompressingDisjointSets<T> implements DisjointSets<T> {
    // Do NOT rename or delete this field. We will be inspecting it directly in our private tests.
    List<Integer> pointers;
    private final HashMap<T, Integer> idMap;

    public UnionBySizeCompressingDisjointSets() {
        this.pointers = new ArrayList<>();
        this.idMap = new HashMap<>();
    }

    @Override
    public void makeSet(T item) {
        if (this.idMap.containsKey(item)) {
            throw new IllegalArgumentException();
        }
        // insert to map and initial the starting size
        idMap.put(item, pointers.size());
        pointers.add(-1);
    }

    @Override
    public int findSet(T item) {
        if (!this.idMap.containsKey(item)) {
            throw new IllegalArgumentException();
        }
        int setID = this.idMap.get(item);
        List<Integer> indices = new ArrayList<>();

        // find set ID and gather nodes during traversal
        while (pointers.get(setID) >= 0) {
            indices.add(setID);
            setID = pointers.get(setID);
        }
        // carrying out path compression
        for (int index : indices) {
            pointers.set(index, setID);
        }

        return setID;
    }

    @Override
    public boolean union(T item1, T item2) {
        // get set IDs for each item
        int id1 = findSet(item1);
        int id2 = findSet(item2);

        if (id1 == id2) {
            // duplicate sets, no union possible
            return false;
        } else if (pointers.get(id1) >= pointers.get(id2)) {
            // id2 set is larger
            pointers.set(id2, pointers.get(id1) + pointers.get(id2));
            pointers.set(id1, id2);
        } else {
            // id1 set is larger
            pointers.set(id1, pointers.get(id1) + pointers.get(id2));
            pointers.set(id2, id1);
        }
        return true;
    }
}
