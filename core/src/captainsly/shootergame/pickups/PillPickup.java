package captainsly.shootergame.pickups;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

import captainsly.shootergame.ships.Ship;

public class PillPickup extends Pickup {

	public PillPickup(float xCenter, float yCenter, float pickupWidth, float pickupHeight, TextureRegion pickupRegion) {
		super("pill", xCenter, yCenter, pickupWidth, pickupHeight, pickupRegion);
	}

	public PillPickup(Rectangle pickupBoundingBox, TextureRegion pickupRegion) {
		super("pill", pickupBoundingBox, pickupRegion);
	}

	// --------- Private Methods ------------

	// --------- Public Methods -------------

	@Override
	public void onPickup(Ship ship) {
		ship.addToShieldAmount(8);
		System.out.println("QUACK on good ole " + ship.getShipId()
				+ ", we just added 8 to your max shields. MAX SHIELDS AT: " + ship.getShipShieldAmount());
	}

	@Override
	public void onUpdate(float delta) {
	}

	// --------- Getters and Setters ------------

}
