package com.ld42.ben;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class GdxGame extends ApplicationAdapter implements InputProcessor {

    private SpriteBatch batch;
	private TiledMap tiledMap;
	private OrthographicCamera camera;
	private TiledMapRenderer tiledMapRenderer;

	private boolean moveUp = false;
	private boolean moveDown = false;
	private boolean moveLeft = false;
	private boolean moveRight = false;


	private void move() {
        if(moveLeft)
            camera.translate(-3,0);
        if(moveRight)
            camera.translate(3,0);
        if(moveUp)
            camera.translate(0,3);
        if(moveDown)
            camera.translate(0,-3);
    }

	@Override
	public void create () {
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, w, h);
		camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
		camera.zoom = 0.25f;
		camera.update();
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

		// batch.begin();
		// batch.end();
	}

	@Override
	public boolean keyDown(int keycode) {
        if(keycode == Input.Keys.LEFT)
            moveLeft = true;
        if(keycode == Input.Keys.RIGHT)
            moveRight = true;
        if(keycode == Input.Keys.UP)
            moveUp = true;
        if(keycode == Input.Keys.DOWN)
            moveDown = true;

		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if(keycode == Input.Keys.LEFT)
			moveLeft = false;
		if(keycode == Input.Keys.RIGHT)
			moveRight = false;
		if(keycode == Input.Keys.UP)
			moveUp = false;
		if(keycode == Input.Keys.DOWN)
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
