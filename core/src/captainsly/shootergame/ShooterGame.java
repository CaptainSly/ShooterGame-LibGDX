package captainsly.shootergame;

import java.util.Random;

import com.badlogic.gdx.Game;

public class ShooterGame extends Game {

	private GameScreen gameScreen;
	
	public static Random RN_JESUS = new Random();

	@Override
	public void create() {
		gameScreen = new GameScreen();
		setScreen(gameScreen);
	}

	@Override
	public void dispose() {
		gameScreen.dispose();
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void resize(int width, int height) {
		gameScreen.resize(width, height);
	}

}
