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
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.ArrayList;

public class GdxGame extends ApplicationAdapter {

    private SpriteBatch batch;

    // Map
    private TiledMap tiledMap;
	private OrthographicCamera camera;
	private OrthogonalTiledMapRenderer tiledMapRenderer;

	// Zombies
    private ArrayList<Zombie> zombies = new ArrayList<Zombie>();

    private Player player;
    private int spawn = 60;

	@Override
	public void create () {

		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();

		tiledMap = new TmxMapLoader().load("50_50.tmx");
		tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, w, h);
        camera.zoom = 0.25f;
        camera.update();

        player = new Player(tiledMap);
        camera.position.x = player.getCharacterRectangle().x;
        camera.position.y = player.getCharacterRectangle().y;

        Zombie zombie = new Zombie(tiledMap);
        zombies.add(zombie);
        zombie.setBrain(new ZombieBrain(zombie, tiledMap, player, zombies));
		Gdx.input.setInputProcessor(player);

		batch = new SpriteBatch();
	}

	@Override
	public void render () {
	    spawn--;
	    if(spawn == 0) {
	        spawn = 60;
	        if(zombies.size() < 50) {
                Zombie zombie = new Zombie(tiledMap);
                zombies.add(zombie);
                zombie.setBrain(new ZombieBrain(zombie, tiledMap, player, zombies));
            }
        }

		Gdx.gl.glClearColor(0.3f, 0, 0, 0);
		Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		camera.update();
		tiledMapRenderer.setView(camera);
		tiledMapRenderer.render();

		batch.begin();

        batch.end();

        tiledMapRenderer.getBatch().begin();
        player.render(tiledMapRenderer.getBatch(), camera);
        for(Zombie zombie : zombies) {
            zombie.render(tiledMapRenderer.getBatch(), camera);
        }
        tiledMapRenderer.renderTileLayer((TiledMapTileLayer)tiledMap.getLayers().get(tiledMap.getLayers().getIndex("OverWalls")));
        tiledMapRenderer.getBatch().end();

    }

	@Override
	public void dispose () {
		batch.dispose();
	}
}
