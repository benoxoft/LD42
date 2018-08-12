package com.ld42.ben;

import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;
import com.ld42.ben.pathfinding.Node;
import com.ld42.ben.pathfinding.ZombieHeuristic;
import com.ld42.ben.pathfinding.ZombieIndexedGraph;

import java.util.ArrayList;

public class ZombieBrain {

    private ZombieIndexedGraph graph;
    private IndexedAStarPathFinder<Node> aStarFinder;

    public ZombieBrain(Zombie owner, TiledMap tiledMap, Player player, ArrayList<Zombie> zombies) {
        graph = new ZombieIndexedGraph(owner, tiledMap, player, zombies);
        aStarFinder = new IndexedAStarPathFinder<Node>(graph);

    }

    public Vector2 think() {
        Node fromNode = graph.getMiddleNode();
        Node toNode = graph.getNodeByIndex((int) Math.floor(Math.random() * 9));

        GraphPath<Node> path = new DefaultGraphPath<Node>();
        aStarFinder.searchNodePath(fromNode, toNode, new ZombieHeuristic(), path);
        return new Vector2(0, -1);
    }

}
