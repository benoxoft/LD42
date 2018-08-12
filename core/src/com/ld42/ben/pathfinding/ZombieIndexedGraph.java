package com.ld42.ben.pathfinding;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.utils.Array;
import com.ld42.ben.Player;
import com.ld42.ben.Zombie;

import java.util.ArrayList;

public class ZombieIndexedGraph implements IndexedGraph {

    // private final ArrayList<Node> nodes = new ArrayList<Node>();
    private final Node[][] nodes;

    public static final int NODE_COUNT = 9;

    private static Node[][] initializeGraph(Zombie owner, int node_count) {
        Node[][] nodes;

        int layers = (int) Math.sqrt(node_count);
        int shift = -((layers - 1) / 2);

        nodes = new Node[layers][layers];

        float posx = owner.getCharacterRectangle().x;
        float posy = owner.getCharacterRectangle().y;
        float width = owner.getCharacterRectangle().width;
        float height = owner.getCharacterRectangle().height;

        for(int i = shift; i < layers + shift; i++) {
            for(int j = shift; j < layers + shift; j++) {
                nodes[i- shift][j- shift] = new Node(posx + width * i, posy + height * j);
            }
        }
        return nodes;
    }

    private static Array<Connection> createConnections(Node[][] nodes, int node_count, int posx, int posy, TiledMap tiledMap, Player player, ArrayList<Zombie> zombies) {
        int outer = Math.max(Math.abs(posx), Math.abs(posy)) + 1;
        int layers = (int) Math.sqrt(node_count);
        int shift = -((layers - 1) / 2);

        Array<Connection> connections = new Array<Connection>();

        for(int i = shift; i < layers + shift; i++) {
            for(int j = shift; j < layers + shift; j++) {
                if(Math.abs(i) < outer && Math.abs(j) < outer) {
                    continue;
                }
                if(i - posx > 1 || j - posx > 1) {
                    continue;
                }
                ZombieConnection connection = new ZombieConnection(nodes[posx][posy], nodes[i - shift][j - shift], tiledMap, player, zombies);
                connections.add(connection);

            }
        }
        return connections;
    }

    public ZombieIndexedGraph(Zombie owner, TiledMap tiledMap, Player player, ArrayList<Zombie> zombies) {
        nodes = initializeGraph(owner, NODE_COUNT);

        int layers = (int) Math.sqrt(NODE_COUNT);
        int shift = -((layers - 1) / 2);

        for(int i = shift + 1; i < layers + shift - 1; i++) {
            for(int j = shift + 1; j < layers + shift - 1; j++) {
                Node fromNode = nodes[i][j];
                fromNode.getConnections().addAll(createConnections(nodes, NODE_COUNT, i, j, tiledMap, player, zombies));
            }
        }
    }

    @Override
    public int getIndex(Object node) {
        for(int i = 0; i < Math.sqrt(NODE_COUNT); i++) {
            for(int j = 0; j < Math.sqrt(NODE_COUNT); j++) {
                if(nodes[i][j] == node) {
                    return i * 3 + j;
                }
            }
        }
        return -1;
    }

    public Node getMiddleNode() {
        return nodes[(int) Math.sqrt(NODE_COUNT) - 1][(int) Math.sqrt(NODE_COUNT) - 1];
    }

    public Node getNodeByIndex(int index) {
        if(index > NODE_COUNT) {
            return null;
        }
        return nodes[(int) Math.floor(index / Math.sqrt(NODE_COUNT))][(int) (index / Math.sqrt(NODE_COUNT))];
    }

    @Override
    public int getNodeCount() {
        return NODE_COUNT;
    }

    @Override
    public Array<Connection> getConnections(Object fromNode) {
        return ((Node)fromNode).getConnections();
    }
}
