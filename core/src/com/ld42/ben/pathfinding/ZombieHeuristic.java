package com.ld42.ben.pathfinding;

import com.badlogic.gdx.ai.pfa.Heuristic;

public class ZombieHeuristic implements Heuristic<Node> {

    @Override
    public float estimate(Node node, Node endNode) {
        float x = (float) Math.sqrt(Math.pow((endNode.getRectangle().x - node.getRectangle().x), 2) + Math.pow((endNode.getRectangle().y - node.getRectangle().y), 2));
        return x;
    }
}
