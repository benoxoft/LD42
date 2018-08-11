package com.ld42.ben;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class GdxGame extends ApplicationAdapter implements InputProcessor {

    private SpriteBatch batch;
    private TextureAtlas textureAtlas;
    private Animation animation;
    private TiledMap tiledMap;
	private OrthographicCamera camera;
	private TiledMapRenderer tiledMapRenderer;
    private Texture walkSheet;

    private Animation<TextureRegion> walkAnimationLeft;
    private Animation<TextureRegion> walkAnimationRight;
    private Animation<TextureRegion> walkAnimationDown;
    private Animation<TextureRegion> walkAnimationUp;

    private float playerMoveTime;
    private float playerPositionX;
    private float playerPositionY;
    private TextureRegion playerCurrentFrame;

	private boolean moveUp = false;
	private boolean moveDown = false;
	private boolean moveLeft = false;
	private boolean moveRight = false;

    private static final int FRAME_COLS = 4, FRAME_ROWS = 4;

	private void move() {
        playerMoveTime += Gdx.graphics.getDeltaTime(); // Accumulate elapsed animation time

        if(moveLeft) {
            camera.translate(-1,0);
            playerCurrentFrame = walkAnimationLeft.getKeyFrame(playerMoveTime, true);
        }
        if(moveRight) {
            camera.translate(1,0);
            playerCurrentFrame = walkAnimationRight.getKeyFrame(playerMoveTime, true);
        }
        if(moveUp) {
            camera.translate(0,1);
            playerCurrentFrame = walkAnimationUp.getKeyFrame(playerMoveTime, true);
        }
        if(moveDown) {
            camera.translate(0,-1);
            playerCurrentFrame = walkAnimationDown.getKeyFrame(playerMoveTime, true);
        }
    }

	@Override
	public void create () {
        walkSheet = new Texture(Gdx.files.internal("mervin.png"));
        TextureRegion[][] tmp = TextureRegion.split(walkSheet,
                walkSheet.getWidth() / FRAME_COLS,
                walkSheet.getHeight() / FRAME_ROWS);

        TextureRegion[] walkFramesRight = new TextureRegion[FRAME_COLS];
        walkFramesRight[0] = tmp[0][0];
        walkFramesRight[1] = tmp[0][1];
        walkFramesRight[2] = tmp[0][2];
        walkFramesRight[3] = tmp[0][3];
        walkAnimationRight = new Animation<TextureRegion>(0.1f, walkFramesRight);

        TextureRegion[] walkFramesLeft = new TextureRegion[FRAME_COLS];
        walkFramesLeft[0] = tmp[1][0];
        walkFramesLeft[1] = tmp[1][1];
        walkFramesLeft[2] = tmp[1][2];
        walkFramesLeft[3] = tmp[1][3];
        walkAnimationLeft = new Animation<TextureRegion>(0.1f, walkFramesLeft);

        TextureRegion[] walkFramesDown = new TextureRegion[FRAME_COLS];
        walkFramesDown[0] = tmp[2][0];
        walkFramesDown[1] = tmp[2][1];
        walkFramesDown[2] = tmp[2][2];
        walkFramesDown[3] = tmp[2][3];
        walkAnimationDown = new Animation<TextureRegion>(0.1f, walkFramesDown);

        TextureRegion[] walkFramesUp = new TextureRegion[FRAME_COLS];
        walkFramesUp[0] = tmp[3][0];
        walkFramesUp[1] = tmp[3][1];
        walkFramesUp[2] = tmp[3][2];
        walkFramesUp[3] = tmp[3][3];
        walkAnimationUp = new Animation<TextureRegion>(0.1f, walkFramesUp);

		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, w, h);
		camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
		camera.zoom = 0.25f;
		camera.update();

        playerCurrentFrame = walkAnimationDown.getKeyFrame(playerMoveTime, true);
        playerPositionX = w / 2f;
        playerPositionY = h / 2f;

		tiledMap = new TmxMapLoader().load("50_50.tmx");
		tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

		Gdx.input.setInputProcessor(this);

		batch = new SpriteBatch();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        move();
		camera.update();
		tiledMapRenderer.setView(camera);
		tiledMapRenderer.render();

		batch.begin();
        batch.draw(playerCurrentFrame, playerPositionX, playerPositionY, 48, 48);
		batch.end();
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
		if(keycode == Input.Keys.NUM_1)
			tiledMap.getLayers().get(0).setVisible(!tiledMap.getLayers().get(0).isVisible());
		if(keycode == Input.Keys.NUM_2)
			tiledMap.getLayers().get(1).setVisible(!tiledMap.getLayers().get(1).isVisible());
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

	@Override
	public void dispose () {
		batch.dispose();
	}
}
