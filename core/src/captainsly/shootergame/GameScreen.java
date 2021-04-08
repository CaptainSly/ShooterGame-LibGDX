package captainsly.shootergame;

import java.util.LinkedList;
import java.util.ListIterator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import captainsly.shootergame.lasers.Laser;
import captainsly.shootergame.pickups.Pickup;
import captainsly.shootergame.pickups.PillPickup;
import captainsly.shootergame.ships.EnemyShip;
import captainsly.shootergame.ships.PlayerShip;
import captainsly.shootergame.ships.Ship;

public class GameScreen implements Screen {

	// Screen
	private Camera camera;
	private Viewport viewport;

	// Graphics
	private TextureAtlas textureAtlas;
	private SpriteBatch spriteBatch;
	private TextureRegion[] backgrounds;
	private TextureRegion playerShipRegion, playerShieldRegion, playerLaserRegion, enemyShipRegion, enemyShieldRegion,
			enemyLaserRegion;

	// Timing
	private float[] backgroundOffsets = { 0, 0, 0, 0 };
	private float backgroundMaxScrollSpeed;

	// World Parameters
	private final int WORLD_WIDTH = 144;
	private final int WORLD_HEIGHT = 128;

	private float timeBetweenEnemySpawns = 2f;
	private float enemySpawnTimer = 0;

	// Game Objects
	private PlayerShip playerShip;
	private LinkedList<Laser> playerLaserList;
	private LinkedList<Laser> enemyLaserList;
	private LinkedList<Pickup> pickupList;
	private LinkedList<EnemyShip> enemyShipList;

	public GameScreen() {
		camera = new OrthographicCamera();
		viewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
		textureAtlas = new TextureAtlas("images.atlas");
		backgrounds = new TextureRegion[4];

		// Create TextureRegions
		playerShipRegion = textureAtlas.findRegion("playerShip1_green");
		playerShieldRegion = textureAtlas.findRegion("shield3");
		playerShieldRegion.flip(false, true);
		playerLaserRegion = textureAtlas.findRegion("laserBlue02");

		enemyShipRegion = textureAtlas.findRegion("enemyRed3");
		enemyShieldRegion = textureAtlas.findRegion("shield1");
		enemyShieldRegion.flip(false, true);
		enemyLaserRegion = textureAtlas.findRegion("laserRed02");

		backgrounds[0] = textureAtlas.findRegion("Starscape00");
		backgrounds[1] = textureAtlas.findRegion("Starscape01");
		backgrounds[2] = textureAtlas.findRegion("Starscape02");
		backgrounds[3] = textureAtlas.findRegion("Starscape03");

		// -=- Create Game Objects -=-

		// Create Ships

		playerShip = new PlayerShip(new Rectangle(WORLD_WIDTH / 2, WORLD_HEIGHT / 4, 10, 10), playerShipRegion,
				playerShieldRegion, playerLaserRegion);

		enemyShipList = new LinkedList<EnemyShip>();

		// LAZORS BLAGHHRH
		playerLaserList = new LinkedList<Laser>();
		enemyLaserList = new LinkedList<Laser>();

		// Pickups
		PillPickup pillPickup = new PillPickup(WORLD_WIDTH / 2, WORLD_HEIGHT - 55, 3, 3,
				textureAtlas.findRegion("pill_green"));
		PillPickup movementPill = new PillPickup(WORLD_WIDTH / 2, WORLD_HEIGHT - 55, 3, 3,
				textureAtlas.findRegion("pill_red")) {
			@Override
			public void onPickup(Ship ship) {
				playerShip.addToMovementSpeed(10);
				playerShip.addToLaserShootSpeed(0.3f);
			}
		};

		pickupList = new LinkedList<Pickup>();
		pickupList.add(pillPickup);
		pickupList.add(movementPill);

		backgroundMaxScrollSpeed = WORLD_HEIGHT / 4; // CHANGE TO WORLD_WIDTH FOR SIDEWAYS SCROLLING

		spriteBatch = new SpriteBatch();
	}

	// Render Method separated from public methods because of importance

	@Override
	public void render(float delta) {

		// Master Draw Call
		spriteBatch.begin();
		{

			// Draw Star Background
			renderBackground(delta);

			// -=- Input -=-
			detectInput(delta);

			// -=- Update -=-
			playerShip.update(delta);

			spawnEnemyShips(delta);

			ListIterator<EnemyShip> enemyIterator = (ListIterator<EnemyShip>) enemyShipList.iterator();
			while (enemyIterator.hasNext()) {
				EnemyShip enemyShip = enemyIterator.next();

				moveEnemies(enemyShip, delta);
				enemyShip.update(delta);
				enemyShip.draw(spriteBatch);

			}

			// Player Ship
			playerShip.draw(spriteBatch);

			renderLasers(delta);
			renderPickups(delta);
			renderExplosions(delta);

			detectCollisions();

		}
		spriteBatch.end();
	}

	// ----------- Private Methods ------------------

	private void detectInput(float delta) {
		float leftLimit, rightLimit, upLimit, downLimit;

		leftLimit = -playerShip.getBoundingBox().x;
		rightLimit = WORLD_WIDTH - playerShip.getBoundingBox().x - playerShip.getBoundingBox().width;

		upLimit = (float) WORLD_HEIGHT / 2 - playerShip.getBoundingBox().y - playerShip.getBoundingBox().height;
		downLimit = -playerShip.getBoundingBox().y;

		if (Gdx.input.isKeyPressed(Keys.W) && upLimit > 0) {
			float yChange = playerShip.getShipMovementSpeed() * delta;
			yChange = Math.min(yChange, upLimit);

			playerShip.movePosition(0, yChange);

		}

		if (Gdx.input.isKeyPressed(Keys.A) && leftLimit < 0) {

			float xChange = -playerShip.getShipMovementSpeed() * delta;
			xChange = Math.max(xChange, leftLimit);

			playerShip.movePosition(xChange, 0f);
		}

		if (Gdx.input.isKeyPressed(Keys.S) && downLimit < 0) {

			float yChange = -playerShip.getShipMovementSpeed() * delta;
			yChange = Math.max(yChange, downLimit);

			playerShip.movePosition(0, yChange);
		}

		if (Gdx.input.isKeyPressed(Keys.D) && rightLimit > 0) {
			float xChange = playerShip.getShipMovementSpeed() * delta;
			xChange = Math.min(xChange, rightLimit);

			playerShip.movePosition(xChange, 0f);
		}

		if (Gdx.input.isKeyPressed(Keys.SPACE)) {
			// Create New Lasers
			if (playerShip.canFireLaser()) { // Player Lasers
				Laser[] lasers = playerShip.fireLasers();

				for (Laser laser : lasers) {
					playerLaserList.add(laser);
				}

			}

		}
	}

	private void detectCollisions() {
		// Laser Collisions
		ListIterator<Laser> laserIterator = (ListIterator<Laser>) playerLaserList.iterator();
		while (laserIterator.hasNext()) {
			Laser laser = laserIterator.next();

			ListIterator<EnemyShip> enemyIterator = (ListIterator<EnemyShip>) enemyShipList.iterator();
			while (enemyIterator.hasNext()) {
				EnemyShip enemyShip = enemyIterator.next();
				if (enemyShip.getBoundingBox().overlaps(laser.getBoundingBox())) {
					enemyShip.onDamage(laser);
					laserIterator.remove();
					if (enemyShip.isShouldRemove()) {
						enemyIterator.remove();
					}
				}
			}
		}

		laserIterator = (ListIterator<Laser>) enemyLaserList.iterator();
		while (laserIterator.hasNext()) {
			Laser laser = laserIterator.next();
			if (playerShip.getBoundingBox().overlaps(laser.getBoundingBox())) {
				playerShip.onDamage(laser);
				laserIterator.remove();
			}
		}

		// Pickup Collisions
		ListIterator<Pickup> pickupIterator = (ListIterator<Pickup>) pickupList.iterator();
		while (pickupIterator.hasNext()) {
			Pickup pickup = pickupIterator.next();

			if (playerShip.getBoundingBox().overlaps(pickup.getBoundingBox())) {
				pickup.onPickup(playerShip);
				pickupIterator.remove();
			}
		}

	}

	private void moveEnemies(EnemyShip enemyShip, float delta) {

		float leftLimit, rightLimit, upLimit, downLimit;
		leftLimit = -enemyShip.getBoundingBox().x;
		rightLimit = (float) WORLD_WIDTH - enemyShip.getBoundingBox().x - enemyShip.getBoundingBox().width;
		upLimit = WORLD_HEIGHT - enemyShip.getBoundingBox().y - enemyShip.getBoundingBox().height;
		downLimit = WORLD_HEIGHT / 2 - enemyShip.getBoundingBox().y;

		float xMove = enemyShip.getDirectionVector().x * enemyShip.getShipMovementSpeed() * delta;
		float yMove = enemyShip.getDirectionVector().y * enemyShip.getShipMovementSpeed() * delta;

		if (xMove > 0)
			xMove = Math.min(xMove, rightLimit);
		else
			xMove = Math.max(xMove, leftLimit);

		if (yMove > 0)
			yMove = Math.min(yMove, upLimit);
		else
			yMove = Math.max(yMove, downLimit);

		enemyShip.movePosition(xMove, yMove);

	}

	private void renderPickups(float delta) {
		// Pickups - Draw and Move, remove if collided with or picked up
		ListIterator<Pickup> pickupIterator = (ListIterator<Pickup>) pickupList.iterator();
		while (pickupIterator.hasNext()) {
			Pickup pickup = pickupIterator.next();

			pickup.draw(spriteBatch);
			pickup.movePosition(0, -25 * delta); // Moves pickups down the screen

			if (pickup.getBoundingBox().y + pickup.getBoundingBox().height < 0)
				pickupIterator.remove();

		}
	}

	private void renderExplosions(float delta) {

	}

	private void renderLasers(float delta) {
		// -=- Lasers -=-
		ListIterator<EnemyShip> enemyIterator = (ListIterator<EnemyShip>) enemyShipList.iterator();
		while (enemyIterator.hasNext()) {
			EnemyShip enemyShip = enemyIterator.next();
			if (enemyShip.canFireLaser()) { // Enemy Lasers
				Laser[] lasers = enemyShip.fireLasers();

				for (Laser laser : lasers) {
					enemyLaserList.add(laser);
				}
			}
		}

		// Draw and Remove Old Lasers
		ListIterator<Laser> laserIterator = (ListIterator<Laser>) playerLaserList.iterator();
		while (laserIterator.hasNext()) {
			Laser laser = laserIterator.next();

			laser.draw(spriteBatch);
			laser.movePosition(0, laser.getMovementSpeed() * delta);

			if (laser.getBoundingBox().y > WORLD_HEIGHT) {
				laserIterator.remove();
			}
		}

		laserIterator = (ListIterator<Laser>) enemyLaserList.iterator();
		while (laserIterator.hasNext()) {
			Laser laser = laserIterator.next();

			laser.draw(spriteBatch);
			laser.movePosition(0, -laser.getMovementSpeed() * delta);

			if (laser.getBoundingBox().y + laser.getBoundingBox().height < 0) {
				laserIterator.remove();
			}
		}
	}

	private void renderBackground(float delta) {
		backgroundOffsets[0] += delta * backgroundMaxScrollSpeed / 8;
		backgroundOffsets[1] += delta * backgroundMaxScrollSpeed / 4;
		backgroundOffsets[2] += delta * backgroundMaxScrollSpeed / 2;
		backgroundOffsets[3] += delta * backgroundMaxScrollSpeed;

		for (int layer = 0; layer < backgroundOffsets.length; layer++) {
			if (backgroundOffsets[layer] > WORLD_HEIGHT)
				backgroundOffsets[layer] = 0;

			float offsetFormChange = -backgroundOffsets[layer] + WORLD_HEIGHT; // CHANGE TO WORLD_WIDTH FOR SIDEWAYS
																				// SCROLLING

			spriteBatch.draw(backgrounds[layer], 0, -backgroundOffsets[layer], WORLD_WIDTH, WORLD_HEIGHT);
			spriteBatch.draw(backgrounds[layer], 0, offsetFormChange, WORLD_WIDTH, WORLD_HEIGHT);
		}

	}

	private void spawnEnemyShips(float delta) {

		enemySpawnTimer += delta;

		System.out.println(enemySpawnTimer + "= enemySpawnTimer");

		if (enemySpawnTimer > timeBetweenEnemySpawns) {
			System.out.println("Spawning new enemy");
			enemyShipList.add(new EnemyShip(
					new Rectangle(ShooterGame.RN_JESUS.nextFloat() * (WORLD_WIDTH - 10) + 5, WORLD_HEIGHT - 5, 10, 10),
					enemyShipRegion, enemyShieldRegion, enemyLaserRegion));

			enemySpawnTimer -= timeBetweenEnemySpawns;
		}
	}

	// -------------- Public Methods ----------------

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height, true);
		spriteBatch.setProjectionMatrix(camera.combined);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void dispose() {
	}

	@Override
	public void show() {
	}

}
