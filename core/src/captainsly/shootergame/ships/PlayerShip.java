package captainsly.shootergame.ships;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import captainsly.shootergame.lasers.Laser;

public class PlayerShip extends Ship {

	// Player Ship Stats
	private int playerLives;

	public PlayerShip(float xCenter, float yCenter, float shipWidth, float shipHeight, float shipMovementSpeed,
			int shipShieldAmount, float shipLaserFireSpeed, float shipLaserCoolDown, TextureRegion shipRegion,
			TextureRegion shipShieldRegion, TextureRegion shipLaserRegion) {
		super("player", xCenter, yCenter, shipWidth, shipHeight, shipMovementSpeed, shipShieldAmount,
				shipLaserFireSpeed, shipLaserCoolDown, shipRegion, shipShieldRegion, shipLaserRegion);

		setupPlayer();
	}

	public PlayerShip(Rectangle shipBoundingBox, float shipMovementSpeed, int shipShieldAmount,
			float shipLaserFireSpeed, float shipLaserCoolDown, TextureRegion shipRegion, TextureRegion shipShieldRegion,
			TextureRegion shipLaserRegion) {
		super("player", shipBoundingBox, shipMovementSpeed, shipShieldAmount, shipLaserFireSpeed, shipLaserCoolDown,
				shipRegion, shipShieldRegion, shipLaserRegion);

		setupPlayer();
	}

	public PlayerShip(Rectangle shipBoundingBox, TextureRegion shipRegion, TextureRegion shipShieldRegion,
			TextureRegion shipLaserRegion) {
		super("player", shipBoundingBox, shipRegion, shipShieldRegion, shipLaserRegion);

		setupPlayer();
	}

	// --------- Private Methods ------------

	private void setupPlayer() {
		playerLives = 3;
	}

	// --------- Public Methods ------------

	@Override
	public void onDestroyed() {
		if (playerLives != 0) {
			playerLives--;
			System.out.println("NUMBER OF LIVES: " + playerLives); 
		} else if (playerLives == 0) {
			System.out.println("DED");
			shouldRemove = true;
			//TODO: GAME OVER
		}
	}

	@Override
	public void onDamage(Laser laser) {
		super.onDamage(laser);
	}

	@Override
	public Laser[] fireLasers() {
		Laser[] laser = new Laser[2];
		laser[0] = new Laser(getBoundingBox().x + getBoundingBox().width * 0.07f,
				getBoundingBox().y + getBoundingBox().height * 0.45f, 0.4f, 4, 45, shipLaserRegion);

		laser[1] = new Laser(getBoundingBox().x + getBoundingBox().width * 0.93f,
				getBoundingBox().y + getBoundingBox().height * 0.45f, 0.4f, 4, 45, shipLaserRegion);

		shipLaserTimeSinceCoolDown = 0;
		return laser;
	}

	// ---------- Getters and Setters ------------

	public int getPlayerLives() {
		return playerLives;
	}

	public void setPlayerLives(int lives) {
		playerLives = lives;
	}

}
