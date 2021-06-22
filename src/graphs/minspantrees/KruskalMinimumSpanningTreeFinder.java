package graphs.minspantrees;

import disjointsets.DisjointSets;
// import disjointsets.QuickFindDisjointSets;
import disjointsets.UnionBySizeCompressingDisjointSets;
import graphs.BaseEdge;
import graphs.KruskalGraph;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Computes minimum spanning trees using Kruskal's algorithm.
 * @see MinimumSpanningTreeFinder for more documentation.
 */
public class KruskalMinimumSpanningTreeFinder<G extends KruskalGraph<V, E>, V, E extends BaseEdge<V, E>>
    implements MinimumSpanningTreeFinder<G, V, E> {

    protected DisjointSets<V> createDisjointSets() {
        // return new QuickFindDisjointSets<>();
        return new UnionBySizeCompressingDisjointSets<>();
    }

    @Override
    public MinimumSpanningTree<V, E> findMinimumSpanningTree(G graph) {

        // sort edges in the graph in ascending weight order
        List<E> edges = new ArrayList<>(graph.allEdges());
        if (edges.size() == 0 && graph.allVertices().size() > 1) {
            return new MinimumSpanningTree.Failure<>();
        }
        edges.sort(Comparator.comparingDouble(E::weight));

        if (edges.size() == 0 && graph.allVertices().size() > 1) {
            return new MinimumSpanningTree.Failure<>();
        }

        DisjointSets<V> disjointSets = createDisjointSets();
        List<E> finalMST = new ArrayList<>();
        int counter = 0;

        for (V vertex : graph.allVertices()) {
            disjointSets.makeSet(vertex);
        }

        for (E edge : edges) {
            int uMST = disjointSets.findSet(edge.from());
            int vMST = disjointSets.findSet(edge.to());

            if (uMST != vMST) {
                finalMST.add(edge);
                counter++;
                disjointSets.union(edge.from(), edge.to());
            }
        }

        if (counter != 0 && counter != graph.allVertices().size() - 1) {
            return new MinimumSpanningTree.Failure<>();
        }

        return new MinimumSpanningTree.Success<>(finalMST);
    }
}
