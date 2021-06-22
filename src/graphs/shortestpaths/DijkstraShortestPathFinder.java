package graphs.shortestpaths;

import graphs.BaseEdge;
import graphs.Graph;
import priorityqueues.DoubleMapMinPQ;
import priorityqueues.ExtrinsicMinPQ;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.Stack;

/**
 * Computes shortest paths using Dijkstra's algorithm.
 * @see SPTShortestPathFinder for more documentation.
 */
public class DijkstraShortestPathFinder<G extends Graph<V, E>, V, E extends BaseEdge<V, E>>
    extends SPTShortestPathFinder<G, V, E> {

    protected <T> ExtrinsicMinPQ<T> createMinPQ() {
        return new DoubleMapMinPQ<>();
    }

    @Override
    protected Map<V, E> constructShortestPathsTree(G graph, V start, V end) {
        if (Objects.equals(start, end)) {
            return new HashMap<>();
        }

        Set<V> known = new HashSet<>();
        Map<V, E> edgeTo = new HashMap<>();
        Map<V, Double> distTo = new HashMap<>();
        ExtrinsicMinPQ<V> queue = createMinPQ();

        distTo.put(start, 0.0);
        queue.add(start, 0.0);

        while (!queue.isEmpty()) {
            V main = queue.removeMin();
            known.add(main);
            if (known.contains(end)) {
                return edgeTo;
            }

            for (E edge : graph.outgoingEdgesFrom(main)) {
                V nextVert = edge.to();
                if (known.contains(nextVert)) {
                    continue;
                }

                double newDist = distTo.get(main) + edge.weight();
                double oldDist = Double.POSITIVE_INFINITY;

                if (distTo.containsKey(nextVert)) {
                    oldDist = distTo.get(nextVert);
                }

                if (newDist < oldDist) {
                    distTo.put(nextVert, newDist);
                    edgeTo.put(nextVert, edge);

                    if (!queue.contains(nextVert)) {
                        queue.add(nextVert, newDist);
                    } else {
                        queue.changePriority(nextVert, newDist);
                    }
                }
            }
        }
        return edgeTo;
    }

    @Override
    protected ShortestPath<V, E> extractShortestPath(Map<V, E> spt, V start, V end) {
        if (Objects.equals(start, end)) {
            return new ShortestPath.SingleVertex<>(start);
        }

        if (spt.get(end) == null) {
            return new ShortestPath.Failure<>();
        }

        V main = end;
        Stack<E> s = new Stack<>();
        List<E> edges = new ArrayList<>();

        while (main != start && spt.containsKey(main)) {
            E mainEdge = spt.get(main);
            s.push(mainEdge);
            main = mainEdge.from();
        }

        while (!s.isEmpty()) {
            edges.add(s.pop());
        }

        return new ShortestPath.Success<>(edges);
    }

}
