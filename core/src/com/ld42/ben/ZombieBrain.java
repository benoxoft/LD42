package com.ld42.ben;

import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.ld42.ben.pathfinding.Node;
import com.ld42.ben.pathfinding.ZombieHeuristic;
import com.ld42.ben.pathfinding.ZombieIndexedGraph;

import java.util.ArrayList;

public class ZombieBrain {

    private ZombieIndexedGraph graph;
    private IndexedAStarPathFinder<Node> aStarFinder;
    private int nodeIndex = 0;
    private GraphPath<Node> path;

    private Zombie owner;
    private TiledMap tiledMap;
    private Player player;
    private ArrayList<Zombie> zombies;
    private boolean firstTime = true;
    private int resetGraph = 80;
    private Vector2 currentVector;

    public ZombieBrain(Zombie owner, TiledMap tiledMap, Player player, ArrayList<Zombie> zombies) {
        this.owner = owner;
        this.tiledMap = tiledMap;
        this.player = player;
        this.zombies = zombies;

        findNextPath();

    }

    public Vector2 think() {
        resetGraph--;
        Node next = path.get(nodeIndex);
        double dist = Math.sqrt(Math.pow((next.getRectangle().x - owner.getCharacterRectangle().x), 2) + Math.pow((next.getRectangle().y - owner.getCharacterRectangle().y), 2));

        if (dist < 1 || resetGraph <= 0) {
            nodeIndex++;
            if (path.getCount() == nodeIndex || resetGraph <= 0) {
                findNextPath();
            }
            next = path.get(nodeIndex);
            float x = next.getRectangle().x - owner.getCharacterRectangle().x;
            float y = next.getRectangle().y - owner.getCharacterRectangle().y;
            currentVector = new Vector2(x, y);
        }
        return currentVector;

    }

    private void findNextPath() {
        graph = new ZombieIndexedGraph(owner, tiledMap, player, zombies);
        aStarFinder = new IndexedAStarPathFinder<Node>(graph);

        Node fromNode = graph.getMiddleNode();
        Node toNode;
        toNode = graph.getInterestingNode(owner.getCharacterRectangle());

        path = new DefaultGraphPath<Node>();
        aStarFinder.searchNodePath(fromNode, toNode, new ZombieHeuristic(), path);
        nodeIndex = 0;
        resetGraph = 120;
    }

}
