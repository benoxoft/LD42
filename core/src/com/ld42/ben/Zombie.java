package com.ld42.ben;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;

public class Zombie extends Character {

    public Zombie(TiledMap tiledMap) {
        super(tiledMap);
        float startx = (Float) tiledMap.getLayers().get("StartingPoint").getObjects().get("zombie_starting_point").getProperties().get("x");
        float starty = (Float) tiledMap.getLayers().get("StartingPoint").getObjects().get("zombie_starting_point").getProperties().get("y");
        getCharacterRectangle().x = startx;
        getCharacterRectangle().y = starty;

    }

    @Override
    protected Texture createWalkSheet() {
        return new Texture(Gdx.files.internal("zombie.png"));
    }

    @Override
    public void render(Batch batch, OrthographicCamera camera) {
        think();
        moveCharacter();

        batch.setProjectionMatrix(camera.combined);
        batch.draw(getCurrentFrame(), getCharacterRectangle().x, getCharacterRectangle().y, 12, 12);
    }

    private void think() {
        setMoveDown(true);
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
