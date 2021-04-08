package captainsly.shootergame.ships;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import captainsly.shootergame.lasers.Laser;

public abstract class Ship {

	// Ship Id and BoundingBox
	private String shipId;
	private Rectangle shipBoundingBox;

	// Ship Characteristics
	protected float shipMovementSpeed;
	protected int shipShieldAmount;
	protected float shipLaserFireSpeed;
	protected float shipLaserCoolDown;
	protected float shipLaserTimeSinceCoolDown = 0;
	protected  boolean shouldRemove = false;

	// Ship Graphics
	protected TextureRegion shipRegion, shipShieldRegion, shipLaserRegion;

	public Ship(String shipId, float xCenter, float yCenter, float shipWidth, float shipHeight, float shipMovementSpeed,
			int shipShieldAmount, float shipLaserFireSpeed, float shipLaserCoolDown, TextureRegion shipRegion,
			TextureRegion shipShieldRegion, TextureRegion shipLaserRegion) {
		this.shipId = shipId;
		this.shipBoundingBox = new Rectangle(xCenter - shipWidth / 2, yCenter - shipHeight / 2, shipWidth, shipHeight);
		this.shipMovementSpeed = shipMovementSpeed;
		this.shipShieldAmount = shipShieldAmount;
		this.shipLaserFireSpeed = shipLaserFireSpeed;
		this.shipLaserCoolDown = shipLaserCoolDown;
		this.shipLaserTimeSinceCoolDown = 0;
		this.shipRegion = shipRegion;
		this.shipShieldRegion = shipShieldRegion;
		this.shipLaserRegion = shipLaserRegion;

	}

	public Ship(String shipId, Rectangle shipBoundingBox, float shipMovementSpeed, int shipShieldAmount,
			float shipLaserFireSpeed, float shipLaserCoolDown, TextureRegion shipRegion, TextureRegion shipShieldRegion,
			TextureRegion shipLaserRegion) {
		this.shipId = shipId;
		this.shipBoundingBox = shipBoundingBox;
		this.shipMovementSpeed = shipMovementSpeed;
		this.shipShieldAmount = shipShieldAmount;
		this.shipLaserFireSpeed = shipLaserFireSpeed;
		this.shipLaserCoolDown = shipLaserCoolDown;
		this.shipLaserTimeSinceCoolDown = 0;
		this.shipRegion = shipRegion;
		this.shipShieldRegion = shipShieldRegion;
		this.shipLaserRegion = shipLaserRegion;
	}

	public Ship(String shipId, Rectangle shipBoundingBox, TextureRegion shipRegion, TextureRegion shipShieldRegion,
			TextureRegion shipLaserRegion) {
		this.shipId = shipId;
		this.shipBoundingBox = shipBoundingBox;
		this.shipRegion = shipRegion;
		this.shipShieldRegion = shipShieldRegion;
		this.shipLaserRegion = shipLaserRegion;

		setupDefaultShip();

	}

	// --------- Private Methods -----------

	private void setupDefaultShip() {
		this.shipMovementSpeed = 65;
		this.shipShieldAmount = 3;
		this.shipLaserFireSpeed = 45;
		this.shipLaserCoolDown = 0.5f;
		this.shipLaserTimeSinceCoolDown = 0;

	}

	// ---------- Public Methods --------------

	public void movePosition(float xPosition, float yPosition) {
		shipBoundingBox.x += xPosition;
		shipBoundingBox.y += yPosition;
	}

	public void onDamage(Laser laser) {
		if (shipShieldAmount > 0) {
			shipShieldAmount--;
			System.out.println(this.shipId + ": CURRENT SHIELDS AT " + this.getShipShieldAmount());
		} else if(shipShieldAmount == 0) onDestroyed();

	}

	public void update(float delta) {
		shipLaserTimeSinceCoolDown += delta;
	}

	public void addToShieldAmount(int amount) {
		shipShieldAmount += amount;
	}

	public void addToMovementSpeed(int amount) {
		shipMovementSpeed += amount;
	}

	public void addToLaserShootSpeed(float amount) {
		shipLaserCoolDown -= amount;
	}

	public void draw(Batch batch) {
		if (shipShieldAmount > 0)
			batch.draw(shipShieldRegion, shipBoundingBox.x, shipBoundingBox.y, shipBoundingBox.width,
					shipBoundingBox.height);

		batch.draw(shipRegion, shipBoundingBox.x, shipBoundingBox.y, shipBoundingBox.width, shipBoundingBox.height);

	}

	// --------- Abstract Methods -----------

	public abstract void onDestroyed();

	public abstract Laser[] fireLasers();

	// ---------- Getters and Setters -------------

	public boolean canFireLaser() {
		return (shipLaserTimeSinceCoolDown - shipLaserCoolDown) >= 0;
	}

	public int getShipShieldAmount() {
		return shipShieldAmount;
	}

	public String getShipId() {
		return shipId;
	}

	public Rectangle getBoundingBox() {
		return shipBoundingBox;
	}

	public float getShipMovementSpeed() {
		return shipMovementSpeed;
	}

}
