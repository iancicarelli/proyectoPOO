package com.badlogic.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

import javax.swing.*;
import java.util.Iterator;

public class PrototipoProjecto extends ApplicationAdapter {

	private Texture alienPNG;
	private Texture navePNG;
	private Sound sonidoChoque;
	private Music musica;
	private SpriteBatch batch;
	private OrthographicCamera cammara;
	private Rectangle nave;
	private Array<Rectangle> aliens;
	private long generacionaliens;
int contador=0;
	long startTime = System.currentTimeMillis();
	long endTime = 0;

	@Override
	public void create () {




		alienPNG = new Texture(Gdx.files.internal("alien.png"));
		navePNG = new Texture(Gdx.files.internal("naveFinal.png"));

		sonidoChoque = Gdx.audio.newSound(Gdx.files.internal("disparoEnemigo.mp3"));
		musica = Gdx.audio.newMusic(Gdx.files.internal("musicaJuego.wav"));

		musica.setLooping(true);
		musica.play();


		cammara = new OrthographicCamera();
		cammara.setToOrtho(false, 1680, 800);
		batch = new SpriteBatch();


		nave = new Rectangle();
		nave.x = 368;
		nave.y = 20;
		nave.width = 64;
		nave.height = 64;



		aliens = new Array<Rectangle>();
		generacion();



	}
	private void generacion() {
		Rectangle raindrop = new Rectangle();
		raindrop.x = MathUtils.random(0, 1680 - 64);
		raindrop.y = 800;
		raindrop.width = 62;
		raindrop.height = 64;
		aliens.add(raindrop);
		generacionaliens = TimeUtils.nanoTime();
	}


	@Override
	public void render () {



		ScreenUtils.clear(0, 0, 0.2f, 1);

		cammara.update();

		batch.setProjectionMatrix(cammara.combined);



		batch.begin();
		batch.draw(navePNG, nave.x, nave.y);

		for(Rectangle raindrop: aliens) {
			batch.draw(alienPNG, raindrop.x, raindrop.y);
		}
		batch.end();




		if(Gdx.input.isTouched()) {
			Vector3 touchPos = new Vector3();
			touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
			cammara.unproject(touchPos);
			nave.x = touchPos.x - 32;
		}
		if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) nave.x -= 400 * Gdx.graphics.getDeltaTime();
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) nave.x += 400 * Gdx.graphics.getDeltaTime();


		if(nave.x < 0) nave.x = 0;
		if(nave.x > 1680 - 64) nave.x = 1680 - 64;

		if(TimeUtils.nanoTime() - generacionaliens > 50000000) generacion();

		for (Iterator<Rectangle> iter = aliens.iterator(); iter.hasNext(); ) {
			Rectangle alienCaido = iter.next();
			alienCaido.y -= 300 * Gdx.graphics.getDeltaTime();
			if(alienCaido.y + 64 < 0) iter.remove();
			if(alienCaido.overlaps(nave)) {
				sonidoChoque.play();
				contador++;
				iter.remove();



				if(contador<5) {
					endTime= System.currentTimeMillis();
					JOptionPane.showMessageDialog(null, "has perdido, tiempo aguantado = " + ((startTime-endTime)*-1)/1000+" segundos");
					endTime= System.currentTimeMillis();
					startTime = System.currentTimeMillis();
					contador=0;
					break;

				}
			}
		}


//salir
		if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)||Gdx.input.isKeyPressed(Input.Keys.END)) dispose();


	}
	
	@Override
	public void dispose () {
//termina
		alienPNG.dispose();
		navePNG.dispose();
		sonidoChoque.dispose();
		musica.dispose();
		batch.dispose();






	}
}
