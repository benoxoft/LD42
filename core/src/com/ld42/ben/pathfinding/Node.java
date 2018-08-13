package com.ld42.ben.pathfinding;

import com.badlogic.gdx.ai.pfa.Connection;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class Node {

    private final Rectangle rectangle;
    private final Array<Connection> connections;

    public Node(float x, float y, float width, float height) {
        connections = new Array<Connection>();

        this.rectangle = new Rectangle(x, y, width, height);
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    public Array<Connection> getConnections() {
        return connections;
    }

}
