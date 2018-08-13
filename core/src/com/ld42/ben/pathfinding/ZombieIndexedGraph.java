package com.ld42.ben.pathfinding;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.ld42.ben.Collision;
import com.ld42.ben.Player;
import com.ld42.ben.Zombie;

import java.util.ArrayList;

public class ZombieIndexedGraph implements IndexedGraph {

    // private final ArrayList<Node> nodes = new ArrayList<Node>();
    private final Node[][] nodes;
    private final TiledMap tiledMap;
    private final Player player;

    public static final int NODE_COUNT = 50 * 50;

    private static Node[][] initializeGraph(Zombie owner, int node_count) {
        Node[][] nodes;

        int layers = (int) Math.sqrt(node_count);
        int shift = -((layers - 1) / 2);

        nodes = new Node[layers][layers];

        float posx = owner.getCharacterRectangle().x;
        float posy = owner.getCharacterRectangle().y;
        float width = 8; //owner.getCharacterRectangle().width;
        float height = 8; //owner.getCharacterRectangle().height;

        for(int i = shift; i < layers + shift; i++) {
            for(int j = shift; j < layers + shift; j++) {
                nodes[i - shift][j - shift] = new Node(posx + width * i, posy - height * j, width, height);
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
                if(Math.abs(i) != outer && Math.abs(j) != outer) {
                    continue;
                }
                if(Math.abs(i - posx) > 1 || Math.abs(j - posy) > 1) {
                    continue;
                }
                ZombieConnection connection = new ZombieConnection(nodes[posx - shift][posy - shift], nodes[i - shift][j - shift], tiledMap, player, zombies);
                connections.add(connection);

            }
        }
        return connections;
    }

    public ZombieIndexedGraph(Zombie owner, TiledMap tiledMap, Player player, ArrayList<Zombie> zombies) {
        this.tiledMap = tiledMap;
        this.player = player;
        nodes = initializeGraph(owner, NODE_COUNT);

        int layers = (int) Math.sqrt(NODE_COUNT);
        int shift = -((layers - 1) / 2);

        for(int i = shift + 1; i < layers + shift - 1; i++) {
            for(int j = shift + 1; j < layers + shift - 1; j++) {
                Node fromNode = nodes[i - shift][j - shift];
                fromNode.getConnections().addAll(createConnections(nodes, NODE_COUNT, i, j, tiledMap, player, zombies));
            }
        }
    }

    @Override
    public int getIndex(Object node) {
        for(int i = 0; i < Math.sqrt(NODE_COUNT); i++) {
            for(int j = 0; j < Math.sqrt(NODE_COUNT); j++) {
                if(nodes[i][j] == node) {
                    return (int)(i * Math.sqrt(NODE_COUNT) + j);
                }
            }
        }
        return -1;
    }

    public Node getMiddleNode() {
        return nodes[(int) (Math.sqrt(NODE_COUNT) - 1) / 2][(int) (Math.sqrt(NODE_COUNT) - 1) / 2];
    }

    public Node getNodeByIndex(int index) {
        if(index > NODE_COUNT) {
            return null;
        }

        int x = (int) (index % Math.sqrt(NODE_COUNT));
        int y = (int) Math.floor(index / Math.sqrt(NODE_COUNT));

        return nodes[y][x];
    }

    public Node getInterestingNode(Rectangle zombiePosition) {
        MapLayer layer = tiledMap.getLayers().get("pathfinding");
        ArrayList<Node> interestingNodes = new ArrayList<Node>();

        for(int i = 0; i < Math.sqrt(NODE_COUNT); i++) {
            for(int j = 0; j < Math.sqrt(NODE_COUNT); j++) {
                Node node = nodes[i][j];
                if(Intersector.overlaps(player.getCharacterRectangle(), node.getRectangle())) {
                    float distance = (float) Math.sqrt(Math.pow((player.getCharacterRectangle().x - zombiePosition.x), 2) + Math.pow((player.getCharacterRectangle().y - zombiePosition.y), 2));
                    if(distance < 60) {
                        System.out.println("Player detected");
                        return node;

                    }
                } else if(Collision.detectCollision(node.getRectangle(), layer) != null) {
                    interestingNodes.add(node);
                }
            }
        }
        return interestingNodes.get((int)Math.floor(Math.random() * interestingNodes.size()));
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
