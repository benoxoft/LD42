package com.ld42.ben;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;

public class Zombie extends Character {

    public Zombie(TiledMap tiledMap) {
        super(tiledMap);
    }

    @Override
    protected Texture createWalkSheet() {
        return new Texture(Gdx.files.internal("zombie.png"));
    }

    @Override
    public void render(Batch batch, OrthographicCamera camera) {

    }

    @Override
    protected float getMoveX() {
        return 0;
    }

    @Override
    protected float getMoveY() {
        return 0;
    }
}
