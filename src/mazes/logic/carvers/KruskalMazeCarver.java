package mazes.logic.carvers;

import graphs.EdgeWithData;
import graphs.minspantrees.MinimumSpanningTree;
import graphs.minspantrees.MinimumSpanningTreeFinder;
import mazes.entities.Room;
import mazes.entities.Wall;
import mazes.logic.MazeGraph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

/**
 * Carves out a maze based on Kruskal's algorithm.
 */
public class KruskalMazeCarver extends MazeCarver {
    MinimumSpanningTreeFinder<MazeGraph, Room, EdgeWithData<Room, Wall>> minimumSpanningTreeFinder;
    private final Random rand;

    public KruskalMazeCarver(MinimumSpanningTreeFinder
                                 <MazeGraph, Room, EdgeWithData<Room, Wall>> minimumSpanningTreeFinder) {
        this.minimumSpanningTreeFinder = minimumSpanningTreeFinder;
        this.rand = new Random();
    }

    public KruskalMazeCarver(MinimumSpanningTreeFinder
                                 <MazeGraph, Room, EdgeWithData<Room, Wall>> minimumSpanningTreeFinder,
                             long seed) {
        this.minimumSpanningTreeFinder = minimumSpanningTreeFinder;
        this.rand = new Random(seed);
    }

    @Override
    protected Set<Wall> chooseWallsToRemove(Set<Wall> walls) {
        // Initial implementation of chooseWallsToRemove
        List<EdgeWithData<Room, Wall>> mazeGraphInput = new ArrayList<>();
        Set<Wall> toRemove = new HashSet<>();
        for (Wall wall : walls) {
            mazeGraphInput.add(new EdgeWithData<>(wall.getRoom1(), wall.getRoom2(),
                rand.nextDouble(), wall));
        }

        MinimumSpanningTree<Room, EdgeWithData<Room, Wall>> mst =
            this.minimumSpanningTreeFinder.findMinimumSpanningTree(new MazeGraph(mazeGraphInput));

        if (mst.exists()) {
            Set<EdgeWithData<Room, Wall>> mstEdges = new HashSet<>(mst.edges());
            for (EdgeWithData<Room, Wall> newWall : mazeGraphInput) {
                if (mstEdges.contains(newWall)) {
                    toRemove.add(newWall.data());
                }
            }
        }
        return toRemove;
    }
}
