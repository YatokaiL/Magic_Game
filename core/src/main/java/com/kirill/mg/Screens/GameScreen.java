package com.kirill.mg.Screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.kirill.mg.Bullets.Bullet;
import com.kirill.mg.Other.Enemy;
import com.kirill.mg.Other.ManaCrystal;

import java.util.ArrayList;
import java.util.Random;

public class GameScreen implements Screen {
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;

    private Texture enemyTextureType1;
    private Texture enemyTextureType2;
    private Texture enemyTextureType3;

    private int playerLevel = 1;
    private int levelUpPoints = 100;
    private int currentPoints = 0;
    private float levelUpTimer = 10;

    private boolean isReversed = false;
    private Texture playerTexture;

    private float damageRadius = 100f;

    private float playerX = 400;
    private float playerY = 240;
    private float speed = 200;
    private int playerRadius = 20;

    private ArrayList<Bullet> bullets;
    private float shootCooldown = 0.5f;
    private float shootTimer = 0;

    private int score = 0;
    private int playerHp = 100;

    private float mana = 0;
    private float maxMana = 100;

    private boolean isPaused = false;
    private boolean isGameOver = false;

    private float spawnTimer = 0;
    private float spawnInterval = 3.0f;

    private ArrayList<Enemy> enemies;
    private Random random = new Random();

    private Texture damageRadiusTexture;

    private float survivalTime = 0;

    private float damageTimer = 0;
    private boolean isDamageTaken = false;

    private int level = 1;
    private int pointsForNextLevel = 100;
    private boolean isLevelUp = false;

    private ArrayList<ManaCrystal> manaCrystals;
    private float manaSpawnTimer = 0;
    private float manaSpawnInterval = 5.0f;


    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 1600, 960);
        batch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fontq.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 11;
        parameter.color = Color.WHITE;

        font = generator.generateFont(parameter);

        generator.dispose();

        enemyTextureType1 = new Texture(Gdx.files.internal("red.png"));
        enemyTextureType2 = new Texture(Gdx.files.internal("blue.png"));
        enemyTextureType3 = new Texture(Gdx.files.internal("yellow.png"));

        bullets = new ArrayList<>();
        enemies = new ArrayList<>();
        manaCrystals = new ArrayList<>();
        damageRadiusTexture = new Texture(Gdx.files.internal("electric_charge.png"));
        playerTexture = new Texture(Gdx.files.internal("player_texture.png"));
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.D)) {
            isReversed = true;
        } else if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.A)) {
            isReversed = false;
        }

        if (Gdx.input.isKeyJustPressed(com.badlogic.gdx.Input.Keys.P)) {
            isPaused = !isPaused;
        }

        if (isPaused) {
            Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            batch.begin();
            font.draw(batch, "Paused - Press P to Resume", 300, 240);
            batch.end();
            return;
        }

        survivalTime += delta;

        Gdx.gl.glClearColor(0.5f, 0.5f, 0.5f, 1);  // Устанавливаем серый фон
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        handlePlayerMovement(delta);

        handleShooting(delta);

        handleBullets(delta);

        updateAndDrawEnemies(delta);

        handleEnemyCollisions(delta);
        if (survivalTime > 60 ) {
            handleEnemyCollisionsInRadius(delta);
            drawDamageRadius();
        }

        drawPlayer();

        drawHpAndScore();

        spawnEnemies(delta);

        spawnManaCrystals(delta);

        updateAndDrawManaCrystals();

        updateLevel();

        checkGameOver();

        drawTimer();

        drawLevelUpMessage();
    }

    private void handlePlayerMovement(float delta) {
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.W)) playerY += speed * delta;
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.S)) playerY -= speed * delta;
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.A)) playerX -= speed * delta;
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.D)) playerX += speed * delta;

        playerX = Math.max(playerRadius, Math.min(playerX, 1600 - playerRadius));
        playerY = Math.max(playerRadius, Math.min(playerY, 960 - playerRadius));
    }

    private void drawLevelUpMessage() {
        if (isLevelUp) {
            font.getData().setScale(2);
            batch.begin();
            font.setColor(1, 1, 0, 1);
            font.draw(batch, "Level Up!", 50, 900);
            batch.end();

            levelUpTimer -= Gdx.graphics.getDeltaTime();
            if (levelUpTimer <= 0) {
                isLevelUp = false;
            }
        }
    }

    private void handleShooting(float delta) {
        shootTimer += delta;
        if (shootTimer >= shootCooldown && Gdx.input.isButtonPressed(com.badlogic.gdx.Input.Buttons.LEFT)) {
            shootTimer = 0;

            float mouseX = Gdx.input.getX();
            float mouseY = 960 - Gdx.input.getY();

            float direction = (float) Math.atan2(mouseY - playerY, mouseX - playerX);

            bullets.add(new Bullet(playerX, playerY, direction, 500));
        }
    }

    private void handleBullets(float delta) {
        ArrayList<Bullet> bulletsToRemove = new ArrayList<>();
        for (Bullet bullet : bullets) {
            bullet.update(delta);
            if (bullet.isOutOfBounds()) {
                bulletsToRemove.add(bullet);
            }

            for (Enemy enemy : enemies) {
                if (isCollision(bullet.x, bullet.y, enemy.x, enemy.y)) {
                    enemy.takeDamage(10);
                    if (!enemy.isAlive()) {
                        enemies.remove(enemy);
                        currentPoints += 10;
                        mana += 5;
                        if (mana > maxMana) {
                            mana = maxMana;
                        }
                        checkLevelUp();
                    }
                    bulletsToRemove.add(bullet);
                    break;
                }
            }

            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(0, 0, 1, 1);
            shapeRenderer.circle(bullet.x, bullet.y, 5);
            shapeRenderer.end();
        }
        bullets.removeAll(bulletsToRemove);
    }

    private void checkLevelUp() {
        if (currentPoints >= levelUpPoints) {
            playerLevel++;
            currentPoints = 0;
            levelUpPoints = (int)(levelUpPoints * 1.5);
            isLevelUp = true;
        }
    }

    private void updateAndDrawEnemies(float delta) {
        for (Enemy enemy : enemies) {
            enemy.update(playerX, playerY, delta);
        }

        for (Enemy enemy : enemies) {
            if (checkCollisionWithPlayer(enemy.x, enemy.y)) {
                playerHp -= 5;
                enemies.remove(enemy);
                break;
            }

            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            batch.begin();
            switch (enemy.getType()) {
                case 1:
                    batch.draw(enemy.getTexture(), enemy.x - 20, enemy.y - 20, 60, 60); // Рисуем текстуру
                    break;
                case 2:
                    batch.draw(enemy.getTexture(), enemy.x - 20, enemy.y - 20, 90, 90); // Рисуем текстуру
                    break;
                case 3:
                    batch.draw(enemy.getTexture(), enemy.x - 20, enemy.y - 20, 40, 40); // Рисуем текстуру
                    break;
                default:
                    shapeRenderer.setColor(1, 1, 1, 1); // Белый (по умолчанию)
            }
            batch.end();
            shapeRenderer.end();
        }
    }

    private void drawHpAndScore() {
        if (isDamageTaken) {
            damageTimer += Gdx.graphics.getDeltaTime();
            if (damageTimer > 1.0f) {
                isDamageTaken = false;
                damageTimer = 0;
            }

            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(1, 0, 0, 1);
            shapeRenderer.rect(playerX - 25, playerY + playerRadius + 5, 50, 5);

            shapeRenderer.setColor(0, 1, 0, 1);
            shapeRenderer.rect(playerX - 25, playerY + playerRadius + 5, (playerHp / 100f) * 50, 5);
            shapeRenderer.end();
        }
    }

    private void spawnEnemies(float delta) {
        spawnTimer += delta;

        Texture enemyTexture;

        if (spawnTimer >= spawnInterval) {
            float enemyX = random.nextFloat() * 1600;
            float enemyY = random.nextFloat() * 960;

            int type = random.nextInt(3) + 1;
            switch (type) {
                case 1:
                    enemyTexture = enemyTextureType1;
                    enemies.add(new Enemy(enemyTexture, enemyX, enemyY, 50, 100, 1));
                    break;
                case 2:
                    enemyTexture = enemyTextureType2;
                    enemies.add(new Enemy(enemyTexture,enemyX, enemyY, 150, 50, 2));
                    break;
                case 3:
                    enemyTexture = enemyTextureType3;
                    enemies.add(new Enemy(enemyTexture,enemyX, enemyY, 30, 200, 3));
                    break;
            }
            spawnTimer = 0;
        }
    }
    private void checkGameOver() {
        if (playerHp <= 0 && !isGameOver) {
            isGameOver = true;
            ((Game) Gdx.app.getApplicationListener()).setScreen(new GameOverScreen(score));
        }
    }

    private void drawPlayer() {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 1, 0, 1);  // Зеленый цвет

        if (isReversed) {
            batch.begin();
            batch.draw(playerTexture, playerX - playerRadius, playerY - playerRadius, playerRadius * 2, playerRadius * 2, 0, 0, playerTexture.getWidth(), playerTexture.getHeight(), true, false);
            batch.end();
        } else {
            batch.begin();
            batch.draw(playerTexture, playerX - playerRadius, playerY - playerRadius, playerRadius * 2, playerRadius * 2);
            batch.end();
        }
        shapeRenderer.end();
    }

    private boolean checkCollisionWithPlayer(float enemyX, float enemyY) {
        if (Math.sqrt(Math.pow(playerX - enemyX, 2) + Math.pow(playerY - enemyY, 2)) < playerRadius + 20) {
            playerHp -= 5;
            isDamageTaken = true;
            enemies.removeIf(enemy -> Math.sqrt(Math.pow(playerX - enemy.x, 2) + Math.pow(playerY - enemy.y, 2)) < playerRadius + 20);
            return true;
        }
        return false;
    }

    private void spawnManaCrystals(float delta) {
        manaSpawnTimer += delta;
        if (manaSpawnTimer >= manaSpawnInterval) {
            manaSpawnTimer = 0;

            float crystalX = random.nextFloat() * 1600;
            float crystalY = random.nextFloat() * 960;

            manaCrystals.add(new ManaCrystal(crystalX, crystalY));
        }
    }

    private void updateLevel() {
        if (score >= pointsForNextLevel) {
            level++;
            isLevelUp = true;

            pointsForNextLevel += 50;
        }
    }

    private void updateAndDrawManaCrystals() {
        ArrayList<ManaCrystal> crystalsToRemove = new ArrayList<>();
        for (ManaCrystal crystal : manaCrystals) {
            if (isCollisionCrystal(playerX, playerY, crystal.getX(), crystal.getY(), playerRadius + crystal.getRadius())) {
                mana += 10;
                currentPoints += 5;
                if (mana > maxMana) {
                    mana = maxMana;
                }
                checkLevelUp();
                crystalsToRemove.add(crystal);
            } else {

                crystal.render(shapeRenderer);
            }
        }
        manaCrystals.removeAll(crystalsToRemove);
    }

    private void handleEnemyCollisionsInRadius(float delta) {
        ArrayList<Enemy> enemiesToRemove = new ArrayList<>();
        for (Enemy enemy : enemies) {
            float distanceToPlayer = (float) Math.sqrt(Math.pow(playerX - enemy.x, 2) + Math.pow(playerY - enemy.y, 2));
            if (distanceToPlayer < damageRadius) {
                enemy.takeDamage(1);
                if (!enemy.isAlive()) {
                    enemiesToRemove.add(enemy);
                    score += 10;
                }
            }
        }

        enemies.removeAll(enemiesToRemove);
    }

    private void handleEnemyCollisions(float delta) {
        for (int i = 0; i < enemies.size(); i++) {
            for (int j = i + 1; j < enemies.size(); j++) {
                Enemy e1 = enemies.get(i);
                Enemy e2 = enemies.get(j);

                float dx = e2.x - e1.x;
                float dy = e2.y - e1.y;
                float distance = (float) Math.sqrt(dx * dx + dy * dy);
                float minDistance = 40;

                if (distance < minDistance) {
                    float overlap = minDistance - distance;

                    if (distance != 0) {
                        dx /= distance;
                        dy /= distance;
                    } else {
                        dx = 1;
                        dy = 0;
                    }

                    e1.x -= dx * overlap / 2;
                    e1.y -= dy * overlap / 2;
                    e2.x += dx * overlap / 2;
                    e2.y += dy * overlap / 2;
                }
            }
        }
    }

    private void drawDamageRadius() {
        float radius = damageRadius * 2;
        batch.begin();
        batch.draw(damageRadiusTexture, playerX - damageRadius, playerY - damageRadius, damageRadius * 2, damageRadius * 2);
        batch.end();
    }

    private boolean isCollision(float bulletX, float bulletY, float enemyX, float enemyY) {
        return Math.sqrt(Math.pow(bulletX - enemyX, 2) + Math.pow(bulletY - enemyY, 2)) < 25;
    }

    private boolean isCollisionCrystal(float x1, float y1, float x2, float y2, float radiusSum) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2)) < radiusSum;
    }

    private void drawTimer() {
        survivalTime += Gdx.graphics.getDeltaTime();

        int minutes = (int) (survivalTime / 60);
        int seconds = (int) (survivalTime % 60);

        String timeFormatted = String.format("%02d:%02d", minutes, seconds);

        font.getData().setScale(4);
        font.setColor(1, 1, 1, 1);

        batch.begin();
        font.draw(batch, timeFormatted, 1400, 940);

        batch.end();

        font.setColor(1, 1, 1, 1);
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
    }

    @Override
    public void hide() {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();
        font.dispose();

    }
}
