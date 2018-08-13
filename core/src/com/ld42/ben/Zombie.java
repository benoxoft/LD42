package com.ld42.ben;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Vector2;

public class Zombie extends Character {

    private ZombieBrain brain;
    private Vector2 currentVector;

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
        currentVector = brain.think().nor();
        setMoveLeft(false);
        setMoveRight(false);
        setMoveUp(false);
        setMoveDown(false);

        if(currentVector.y < 0) {
            setMoveDown(true);
        } else if(currentVector.y > 0) {
            setMoveUp(true);
        }
        if(currentVector.x < 0) {
            setMoveLeft(true);
        } else if(currentVector.x > 0) {
            setMoveRight(true);
        }

        moveCharacter();

        batch.setProjectionMatrix(camera.combined);
        batch.draw(getCurrentFrame(), getCharacterRectangle().x - getCharacterRectangle().width, getCharacterRectangle().y - getCharacterRectangle().height, 12, 12);
    }

    @Override
    protected float getMoveX() {
        return Math.abs(currentVector.x) * 0.6f;
    }

    @Override
    protected float getMoveY() {
        return Math.abs(currentVector.y) * 0.6f;
    }

    @Override
    protected float getAnimationSpeed() {
        return 0.3f;
    }

}
