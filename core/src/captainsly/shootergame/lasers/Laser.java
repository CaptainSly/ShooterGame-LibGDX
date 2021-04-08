package captainsly.shootergame.lasers;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Laser {

	// Laser Id and BoundingBox
	private String laserId;
	private Rectangle laserBoundingBox;

	// Laser Characteristics
	private float laserMovementSpeed;

	// Laser Graphics
	private TextureRegion laserRegion;

	public Laser(float xCenter, float yCenter, float laserWidth, float laserHeight,
			float laserMovementSpeed, TextureRegion laserRegion) {
		this.laserId = "laser";
		this.laserBoundingBox = new Rectangle(xCenter - laserWidth / 2, yCenter - laserHeight / 2, laserWidth,
				laserHeight);
		this.laserMovementSpeed = laserMovementSpeed;
		this.laserRegion = laserRegion;
	}

	public Laser(Rectangle laserBoundingBox, float laserMovementSpeed, TextureRegion laserRegion) {
		this.laserId = "laser";
		this.laserBoundingBox = laserBoundingBox;
		this.laserMovementSpeed = laserMovementSpeed;
		this.laserRegion = laserRegion;
	}

	public Laser(Rectangle laserBoundingBox, TextureRegion laserRegion) {
		this.laserId = "laser";
		this.laserBoundingBox = laserBoundingBox;
		this.laserRegion = laserRegion;
		this.laserMovementSpeed = 5f;
	}

	// --------- Private Methods ----------

	// --------- Public Methods -----------

	public void movePosition(float xPosition, float yPosition) {
		laserBoundingBox.x += xPosition;
		laserBoundingBox.y += yPosition;
	}
	
	public void draw(Batch batch) {
		batch.draw(laserRegion, laserBoundingBox.x, laserBoundingBox.y, laserBoundingBox.width,
				laserBoundingBox.height);
	}

	// --------- Getters and Setters ----------

	public String getLaserId() {
		return laserId;
	}

	public float getMovementSpeed() {
		return laserMovementSpeed;
	}

	public Rectangle getBoundingBox() {
		return laserBoundingBox;
	}

}
