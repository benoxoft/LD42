package com.ld42.ben;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;

public class Zombie extends Character {

    private ZombieBrain brain;

    public Zombie(TiledMap tiledMap) {
        super(tiledMap);
        float startx = (Float) tiledMap.getLayers().get("StartingPoint").getObjects().get("zombie_starting_point").getProperties().get("x");
        float starty = (Float) tiledMap.getLayers().get("StartingPoint").getObjects().get("zombie_starting_point").getProperties().get("y");
        getCharacterRectangle().x = startx;
        getCharacterRectangle().y = starty;

    }

    public void setBrain(ZombieBrain brain) {
        this.brain = brain;
    }

    @Override
    protected Texture createWalkSheet() {
        return new Texture(Gdx.files.internal("zombie.png"));
    }

    @Override
    public void render(Batch batch, OrthographicCamera camera) {
        Vector2 v = brain.think();
        if(v.y < 0) {
            setMoveDown(true);
            setMoveUp(false);
        } else if(v.y > 0) {
            setMoveUp(true);
            setMoveDown(false);
        }
        if(v.x < 0) {
            setMoveLeft(true);
            setMoveRight(false);
        } else if(v.x > 0) {
            setMoveRight(true);
            setMoveLeft(false);
        }

        moveCharacter();

        batch.setProjectionMatrix(camera.combined);
        batch.draw(getCurrentFrame(), getCharacterRectangle().x, getCharacterRectangle().y, 12, 12);
    }

    @Override
    protected float getMoveX() {
        return 0.3f;
    }

    @Override
    protected float getMoveY() {
        return 0.3f;
    }

    @Override
    protected float getAnimationSpeed() {
        return 0.3f;
    }

}
