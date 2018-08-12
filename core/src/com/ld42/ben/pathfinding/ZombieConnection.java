package com.ld42.ben.pathfinding;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Intersector;
import com.ld42.ben.Collision;
import com.ld42.ben.Player;
import com.ld42.ben.Zombie;

import java.util.ArrayList;

public class ZombieConnection implements Connection {

    private final Node fromNode;
    private final Node toNode;
    private final TiledMap tiledMap;
    private final Player player;
    private final ArrayList<Zombie> zombies;

    public ZombieConnection(Node fromNode, Node toNode, TiledMap tiledMap, Player player, ArrayList<Zombie> zombies) {

        this.zombies = zombies;
        this.player = player;
        this.tiledMap = tiledMap;
        this.fromNode = fromNode;
        this.toNode = toNode;
    }

    @Override
    public float getCost() {
        int cost = 100;

        if(Collision.detectCollision(toNode.getRectangle(), tiledMap.getLayers().get("Walls")) != null) {
            return 1000;
        }
        else if(Collision.detectCollision(toNode.getRectangle(), tiledMap.getLayers().get("doors")) != null) {
            cost -= 10;
        } else if(Intersector.overlaps(toNode.getRectangle(), player.getCharacterRectangle())) {
            return 0;
        } else {
            for(Zombie z : zombies) {
                if(Intersector.overlaps(toNode.getRectangle(), z.getCharacterRectangle())) {
                    cost -= 10;
                    return cost;
                }
            }
        }
        return cost;
    }

    @Override
    public Object getFromNode() {
        return fromNode;
    }

    @Override
    public Object getToNode() {
        return toNode;
    }
}
