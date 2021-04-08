package captainsly.shootergame.pickups;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import captainsly.shootergame.ships.Ship;

public abstract class Pickup {

	// Pickup Id and BoundingBox
	private String pickupId;
	private Rectangle pickupBoundingBox;

	// Pickup Characteristics

	// Pickup Graphics
	private TextureRegion pickupRegion;

	public Pickup(String pickupId, float xCenter, float yCenter, float pickupWidth, float pickupHeight,
			TextureRegion pickupRegion) {
		this.pickupId = pickupId;
		this.pickupBoundingBox = new Rectangle(xCenter - pickupWidth / 2, yCenter - pickupHeight / 2, pickupWidth,
				pickupHeight);
		this.pickupRegion = pickupRegion;
	}

	public Pickup(String pickupId, Rectangle pickupBoundingBox, TextureRegion pickupRegion) {
		this.pickupId = pickupId;
		this.pickupBoundingBox = pickupBoundingBox;
		this.pickupRegion = pickupRegion;
	}

	// -------- Private Methods ----------

	// -------- Public Methods -----------

	public void movePosition(float xPosition, float yPosition) {
		pickupBoundingBox.x += xPosition;
		pickupBoundingBox.y += yPosition;
	}

	public void draw(Batch batch) {
		batch.draw(pickupRegion, pickupBoundingBox.x, pickupBoundingBox.y, pickupBoundingBox.width,
				pickupBoundingBox.height);
	}

	// -------- Abstract Methods ----------

	public abstract void onPickup(Ship ship);

	public abstract void onUpdate(float delta);

	// -------- Getters and Setters ----------

	public String getPickupId() {
		return pickupId;
	}

	public Rectangle getBoundingBox() {
		return pickupBoundingBox;
	}

}
