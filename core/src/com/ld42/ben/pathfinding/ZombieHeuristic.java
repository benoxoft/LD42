package com.ld42.ben.pathfinding;

import com.badlogic.gdx.ai.pfa.Heuristic;

public class ZombieHeuristic implements Heuristic<Node> {

    @Override
    public float estimate(Node node, Node endNode) {
        return 0;
    }
}
