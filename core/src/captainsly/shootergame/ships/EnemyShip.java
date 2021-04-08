package captainsly.shootergame.ships;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import captainsly.shootergame.ShooterGame;
import captainsly.shootergame.lasers.Laser;

public class EnemyShip extends Ship {

	private Vector2 directionVector;
	private float timeSinceLastDirChange = 0;
	private float directionChangeFrequency = 0.75f;

	public EnemyShip(float xCenter, float yCenter, float shipWidth, float shipHeight, float shipMovementSpeed,
			int shipShieldAmount, float shipLaserFireSpeed, float shipLaserCoolDown, TextureRegion shipRegion,
			TextureRegion shipShieldRegion, TextureRegion shipLaserRegion) {
		super("enemy", xCenter, yCenter, shipWidth, shipHeight, shipMovementSpeed, shipShieldAmount, shipLaserFireSpeed,
				shipLaserCoolDown, shipRegion, shipShieldRegion, shipLaserRegion);
		setupDefaultShip();
	}

	public EnemyShip(Rectangle shipBoundingBox, float shipMovementSpeed, int shipShieldAmount, float shipLaserFireSpeed,
			float shipLaserCoolDown, TextureRegion shipRegion, TextureRegion shipShieldRegion,
			TextureRegion shipLaserRegion) {
		super("enemy", shipBoundingBox, shipMovementSpeed, shipShieldAmount, shipLaserFireSpeed, shipLaserCoolDown,
				shipRegion, shipShieldRegion, shipLaserRegion);
		setupDefaultShip();
	}

	public EnemyShip(Rectangle shipBoundingBox, TextureRegion shipRegion, TextureRegion shipShieldRegion,
			TextureRegion shipLaserRegion) {
		super("enemy", shipBoundingBox, shipRegion, shipShieldRegion, shipLaserRegion);

		setupDefaultShip();
	}

	// ------ Private Methods ---------

	private void setupDefaultShip() {
		directionVector = new Vector2(0, -1);
	}

	private void randomizedDirectionVector() {
		double bearing = ShooterGame.RN_JESUS.nextDouble() * 6.283185; // o to 2 * PI

		directionVector.x = (float) Math.sin(bearing);
		directionVector.y = (float) Math.cos(bearing);

	}

	// ------ Public Methods ---------

	@Override
	public void update(float delta) {
		super.update(delta);

		timeSinceLastDirChange += delta;

		if (timeSinceLastDirChange > directionChangeFrequency) {
			randomizedDirectionVector();
			timeSinceLastDirChange -= directionChangeFrequency;
		}
	}

	@Override
	public void onDestroyed() {
		shouldRemove = true;
	}

	@Override
	public void onDamage(Laser laser) {
		super.onDamage(laser);

	}

	@Override
	public Laser[] fireLasers() {
		Laser[] lasers = new Laser[2];
		lasers[0] = new Laser(getBoundingBox().x + getBoundingBox().width * 0.18f,
				getBoundingBox().y - getBoundingBox().height, 0.3f, 5, 54, shipLaserRegion);

		lasers[1] = new Laser(getBoundingBox().x + getBoundingBox().width * 0.82f,
				getBoundingBox().y - getBoundingBox().height, 0.3f, 5, 54, shipLaserRegion);

		shipLaserTimeSinceCoolDown = 0;
		return lasers;
	}

	// ---------- Getters and Setters -------------

	public boolean isShouldRemove() {
		return shouldRemove;
	}

	public Vector2 getDirectionVector() {
		return directionVector;
	}

}
