package com.ld42.ben;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;

public class Player extends Character implements InputProcessor {

    private Rectangle doorOpenerRectangle;

    public Player(TiledMap tiledMap) {
        super(tiledMap);
        doorOpenerRectangle = new Rectangle(-100, -100, 16, 20);
        getCharacterRectangle().x = (Float) tiledMap.getLayers().get("StartingPoint").getObjects().get("starting_point").getProperties().get("x");
        getCharacterRectangle().y = (Float) tiledMap.getLayers().get("StartingPoint").getObjects().get("starting_point").getProperties().get("y");

    }

    protected Texture createWalkSheet() {
        return new Texture(Gdx.files.internal("mervin.png"));
    }

    public void render(Batch batch, OrthographicCamera camera) {

        doorOpenerRectangle.x = getCharacterRectangle().x + 4;
        doorOpenerRectangle.y = getCharacterRectangle().y - 6;

        float posx = getCharacterRectangle().x;
        float posy = getCharacterRectangle().y;

        moveCharacter();

        camera.translate(getCharacterRectangle().x - posx, getCharacterRectangle().y - posy);

        batch.draw(getCurrentFrame(), getCharacterRectangle().x - getCharacterRectangle().width, getCharacterRectangle().y - getCharacterRectangle().height, 12, 12);
    }

    @Override
    protected float getMoveX() {
        return 1;
    }

    @Override
    protected float getMoveY() {
        return 1;
    }

    @Override
    protected float getAnimationSpeed() {
        return 0.1f;
    }

    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.A)
            setMoveLeft(true);
        if(keycode == Input.Keys.D)
            setMoveRight(true);
        if(keycode == Input.Keys.W)
            setMoveUp(true);
        if(keycode == Input.Keys.S)
            setMoveDown(true);
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if(keycode == Input.Keys.A)
            setMoveLeft(false);
        if(keycode == Input.Keys.D)
            setMoveRight(false);
        if(keycode == Input.Keys.W)
            setMoveUp(false);
        if(keycode == Input.Keys.S)
            setMoveDown(false);
        if(keycode == Input.Keys.SPACE) {
            RectangleMapObject rectangleObject = Collision.detectCollision(doorOpenerRectangle, getDoorsObjectLayer());
            if(rectangleObject != null) {
                if(Collision.detectCollision(getCharacterRectangle(), getDoorsObjectLayer()) == null) {
                    rectangleObject.getProperties().put("closed", !((Boolean)rectangleObject.getProperties().get("closed")));
                    getTiledMap().getLayers().get(rectangleObject.getName()).setVisible(!getTiledMap().getLayers().get(rectangleObject.getName()).isVisible());
                }
            }
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {

        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

}
