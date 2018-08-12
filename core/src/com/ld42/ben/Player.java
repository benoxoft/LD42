package com.ld42.ben;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

public class Player implements InputProcessor {

    private static final int FRAME_COLS = 4, FRAME_ROWS = 4;
    private static final float FRAME_DURATION = 0.1f;
    private static final int WALK_RIGHT_COL_INDEX = 0;
    private static final int WALK_LEFT_COL_INDEX = 1;
    private static final int WALK_DOWN_COL_INDEX = 2;
    private static final int WALK_UP_COL_INDEX = 3;

    // Player
    private Texture walkSheet;
    private Animation<TextureRegion> walkAnimationLeft;
    private Animation<TextureRegion> walkAnimationRight;
    private Animation<TextureRegion> walkAnimationDown;
    private Animation<TextureRegion> walkAnimationUp;

    private float playerMoveTime;
    private TextureRegion playerCurrentFrame;
    private Rectangle playerRectangle;
    private Rectangle playerDoorOpenerRectangle;

    private boolean moveUp = false;
    private boolean moveDown = false;
    private boolean moveLeft = false;
    private boolean moveRight = false;

    private TiledMap tiledMap;
    private MapLayer collisionObjectLayer;
    private MapLayer doorsObjectLayer;

    public Player(TiledMap tiledMap) {
        this.tiledMap = tiledMap;
        collisionObjectLayer = tiledMap.getLayers().get("collision");
        doorsObjectLayer = tiledMap.getLayers().get("doors");

        walkSheet = new Texture(Gdx.files.internal("mervin.png"));
        TextureRegion[][] tmp = TextureRegion.split(walkSheet,
                walkSheet.getWidth() / FRAME_COLS,
                walkSheet.getHeight() / FRAME_ROWS);

        walkAnimationRight = createWalkAnimation(tmp, WALK_RIGHT_COL_INDEX);
        walkAnimationLeft = createWalkAnimation(tmp, WALK_LEFT_COL_INDEX);
        walkAnimationDown = createWalkAnimation(tmp, WALK_DOWN_COL_INDEX);
        walkAnimationUp = createWalkAnimation(tmp, WALK_UP_COL_INDEX);

        playerCurrentFrame = walkAnimationDown.getKeyFrame(playerMoveTime, true);
        playerRectangle = new Rectangle(-100, -100, 4, 4);
        playerDoorOpenerRectangle = new Rectangle(-100, -100, 16, 20);

    }

    public void render(Batch batch, OrthographicCamera camera) {
        move(camera);
        batch.draw(playerCurrentFrame, camera.viewportWidth / 2f, camera.viewportHeight / 2f, 48, 48);
    }

    private void move(OrthographicCamera camera) {
        playerMoveTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time

        float moveX = 0;
        float moveY = 0;

        if(moveLeft) {
            moveX = -1;
            playerCurrentFrame = walkAnimationLeft.getKeyFrame(playerMoveTime, true);
        }
        if(moveRight) {
            moveX = 1;
            playerCurrentFrame = walkAnimationRight.getKeyFrame(playerMoveTime, true);
        }
        if(moveUp) {
            moveY = 1;
            playerCurrentFrame = walkAnimationUp.getKeyFrame(playerMoveTime, true);
        }
        if(moveDown) {
            moveY = -1;
            playerCurrentFrame = walkAnimationDown.getKeyFrame(playerMoveTime, true);
        }

        playerRectangle.x = camera.position.x + 4;
        playerRectangle.y = camera.position.y;
        playerDoorOpenerRectangle.x = playerRectangle.x + 4;
        playerDoorOpenerRectangle.y = playerRectangle.y - 6;

        playerRectangle.x += moveX;
        boolean collide = false;
        for (RectangleMapObject rectangleObject : collisionObjectLayer.getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = rectangleObject.getRectangle();
            if (Intersector.overlaps(rectangle, playerRectangle)) {
                collide = true;
                break;
            }
        }
        for (RectangleMapObject rectangleObject : doorsObjectLayer.getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = rectangleObject.getRectangle();
            if((Boolean) rectangleObject.getProperties().get("closed")) {
                if (Intersector.overlaps(rectangle, playerRectangle)) {
                    collide = true;
                    break;
                }
            }
        }

        if(!collide) {
            camera.translate(moveX, 0);
        }
        playerRectangle.x -= moveX;


        playerRectangle.y += moveY;
        collide = false;
        for (RectangleMapObject rectangleObject : collisionObjectLayer.getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = rectangleObject.getRectangle();
            if (Intersector.overlaps(rectangle, playerRectangle)) {
                collide = true;
                break;
            }
        }
        for (RectangleMapObject rectangleObject : doorsObjectLayer.getObjects().getByType(RectangleMapObject.class)) {
            Rectangle rectangle = rectangleObject.getRectangle();
            if((Boolean) rectangleObject.getProperties().get("closed")) {
                if (Intersector.overlaps(rectangle, playerRectangle)) {
                    collide = true;
                    break;
                }
            }
        }
        playerRectangle.y -= moveY;
        if(!collide) {
            camera.translate(0, moveY);
        }

    }

    private Animation<TextureRegion> createWalkAnimation(TextureRegion[][] treg, int colIndex) {
        TextureRegion[] walkFrames = new TextureRegion[FRAME_COLS];
        walkFrames[0] = treg[colIndex][0];
        walkFrames[1] = treg[colIndex][1];
        walkFrames[2] = treg[colIndex][2];
        walkFrames[3] = treg[colIndex][3];
        return new Animation<TextureRegion>(FRAME_DURATION, walkFrames);
    }

    @Override
    public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.A)
            moveLeft = true;
        if(keycode == Input.Keys.D)
            moveRight = true;
        if(keycode == Input.Keys.W)
            moveUp = true;
        if(keycode == Input.Keys.S)
            moveDown = true;
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        if(keycode == Input.Keys.A)
            moveLeft = false;
        if(keycode == Input.Keys.D)
            moveRight = false;
        if(keycode == Input.Keys.W)
            moveUp = false;
        if(keycode == Input.Keys.S)
            moveDown = false;
        if(keycode == Input.Keys.SPACE) {
            for (RectangleMapObject rectangleObject : doorsObjectLayer.getObjects().getByType(RectangleMapObject.class)) {
                Rectangle rectangle = rectangleObject.getRectangle();
                if (Intersector.overlaps(rectangle, playerDoorOpenerRectangle)) {
                    if(!Intersector.overlaps(rectangle, playerRectangle)) {
                        rectangleObject.getProperties().put("closed", !((Boolean)rectangleObject.getProperties().get("closed")));
                        tiledMap.getLayers().get(rectangleObject.getName()).setVisible(!tiledMap.getLayers().get(rectangleObject.getName()).isVisible());
                    }
                    break;
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
