package com.ld42.ben;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;

public abstract class Character {

    private static final int FRAME_COLS = 4, FRAME_ROWS = 4;
    private static final float FRAME_DURATION = 0.1f;
    private static final int WALK_RIGHT_COL_INDEX = 0;
    private static final int WALK_LEFT_COL_INDEX = 1;
    private static final int WALK_DOWN_COL_INDEX = 2;
    private static final int WALK_UP_COL_INDEX = 3;

    private Texture walkSheet;
    private Animation<TextureRegion> walkAnimationLeft;
    private Animation<TextureRegion> walkAnimationRight;
    private Animation<TextureRegion> walkAnimationDown;
    private Animation<TextureRegion> walkAnimationUp;

    private float moveTime;
    private TextureRegion currentFrame;
    private Rectangle characterRectangle;

    private boolean moveUp = false;
    private boolean moveDown = false;

    private boolean moveLeft = false;
    private boolean moveRight = false;

    private TiledMap tiledMap;
    private MapLayer collisionObjectLayer;
    private MapLayer doorsObjectLayer;

    public Character(TiledMap tiledMap) {
        this.tiledMap = tiledMap;
        collisionObjectLayer = tiledMap.getLayers().get("collision");
        doorsObjectLayer = tiledMap.getLayers().get("doors");

        walkSheet = createWalkSheet();
        TextureRegion[][] tmp = TextureRegion.split(walkSheet,
                walkSheet.getWidth() / FRAME_COLS,
                walkSheet.getHeight() / FRAME_ROWS);

        walkAnimationRight = createWalkAnimation(tmp, WALK_RIGHT_COL_INDEX);
        walkAnimationLeft = createWalkAnimation(tmp, WALK_LEFT_COL_INDEX);
        walkAnimationDown = createWalkAnimation(tmp, WALK_DOWN_COL_INDEX);
        walkAnimationUp = createWalkAnimation(tmp, WALK_UP_COL_INDEX);

        currentFrame = walkAnimationDown.getKeyFrame(moveTime, true);
        characterRectangle = new Rectangle(-100, -100, 4, 4);

    }

    protected abstract Texture createWalkSheet();
    public abstract void render(Batch batch, OrthographicCamera camera);
    protected abstract float getMoveX();
    protected abstract float getMoveY();

    protected void moveCharacter() {
        moveTime += Gdx.graphics.getDeltaTime();
        float moveX = 0;
        float moveY = 0;

        if(moveLeft) {
            moveX = -getMoveX();
            currentFrame = walkAnimationLeft.getKeyFrame(moveTime, true);
        }
        if(moveRight) {
            moveX = getMoveX();
            currentFrame = walkAnimationRight.getKeyFrame(moveTime, true);
        }
        if(moveUp) {
            moveY = getMoveY();
            currentFrame = walkAnimationUp.getKeyFrame(moveTime, true);
        }
        if(moveDown) {
            moveY = -getMoveY();
            currentFrame = walkAnimationDown.getKeyFrame(moveTime, true);
        }

        characterRectangle.x += moveX;
        if(Collision.detectCollision(characterRectangle, collisionObjectLayer) != null ||
                Collision.detectDoorCollision(characterRectangle, doorsObjectLayer) != null) {
            characterRectangle.x -= moveX;
        }

        characterRectangle.y += moveY;
        if(Collision.detectCollision(characterRectangle, collisionObjectLayer) != null ||
                Collision.detectDoorCollision(characterRectangle, doorsObjectLayer) != null) {
            characterRectangle.y -= moveY;
        }

    }

    protected Animation<TextureRegion> createWalkAnimation(TextureRegion[][] treg, int colIndex) {
        TextureRegion[] walkFrames = new TextureRegion[FRAME_COLS];
        walkFrames[0] = treg[colIndex][0];
        walkFrames[1] = treg[colIndex][1];
        walkFrames[2] = treg[colIndex][2];
        walkFrames[3] = treg[colIndex][3];
        return new Animation<TextureRegion>(FRAME_DURATION, walkFrames);
    }

    protected TextureRegion getCurrentFrame() {
        return currentFrame;
    }

    protected void setMoveUp(boolean moveUp) {
        this.moveUp = moveUp;
    }

    protected void setMoveDown(boolean moveDown) {
        this.moveDown = moveDown;
    }

    protected void setMoveLeft(boolean moveLeft) {
        this.moveLeft = moveLeft;
    }

    protected void setMoveRight(boolean moveRight) {
        this.moveRight = moveRight;
    }

    protected Rectangle getCharacterRectangle() {
        return characterRectangle;
    }

    protected TiledMap getTiledMap() {
        return tiledMap;
    }

    public MapLayer getDoorsObjectLayer() {
        return doorsObjectLayer;
    }

}
