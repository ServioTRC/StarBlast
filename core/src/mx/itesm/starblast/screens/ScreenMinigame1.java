package mx.itesm.starblast.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.ArrayList;
import java.util.Random;

import mx.itesm.starblast.Constant;
import mx.itesm.starblast.PreferencesSB;
import mx.itesm.starblast.StarBlast;
import mx.itesm.starblast.Text;

class ScreenMinigame1 extends ScreenSB implements InputProcessor {

    private final StarBlast menu;

    //SpriteBatch
    private SpriteBatch batch;

    //Escenas
    private Stage minigame1Scene;

    private boolean won = false;
    private boolean isStoryMode = false;
    private final ArrayList<Place> notDonePlaces = new ArrayList<Place>(25);
    private ArrayList<Piece> notDonePieces = new ArrayList<Piece>(25);
    private ArrayList<Piece> donePieces = new ArrayList<Piece>(25);
    private Piece selectedPiece;
    private static final float BOARD_START_X = 275;
    private static final float BOARD_START_Y = 733;
    private static final float PIECE_WIDTH = 136;
    private static final float PIECE_HEIGHT = 136;

    private long startingTime;
    private Text textScore;

    private Sprite backButtonSprite;
    private Sprite endingSprite;
    private boolean ended = false;

    private class Place {
        Vector2 place;
        Piece piece;
        int idx;

        Place(float x, float y, int idx) {
            this.place = new Vector2(x, y);
            this.idx = idx;
        }
    }

    private class Piece {
        Place place;
        Sprite sprite;
        int idx;

        Piece(Texture texture, int idx) {
            this.sprite = new Sprite(texture);
            this.idx = idx;
        }
    }


    ScreenMinigame1(StarBlast menu, boolean isStoryMode) {
        this.menu = menu;
        this.isStoryMode = isStoryMode;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        minigame1Scene = new Stage(view, batch);
        Image imgFondo = new Image(Constant.MANAGER.get("Minigame1Screen/Minigame1Background.jpg", Texture.class));
        endingSprite = new Sprite(Constant.MANAGER.get("Minigame1Screen/SplashMinigame1Win.png", Texture.class));
        endingSprite.setY(Constant.SCREEN_HEIGTH / 2 - endingSprite.getHeight() / 2);
        endingSprite.setX(Constant.SCREEN_WIDTH / 2 - endingSprite.getWidth() / 2);
        minigame1Scene.addActor(imgFondo);
        Random r = new Random();
        ArrayList<Place> centers = new ArrayList<Place>(25);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                notDonePlaces.add(new Place(BOARD_START_X + PIECE_WIDTH * (j + 1f / 2f), BOARD_START_Y - PIECE_HEIGHT * (i + 1f / 2f), i * 5 + j));
                notDonePieces.add(new Piece(Constant.MANAGER.get("Minigame1Screen/PuzzlePieces/Pieza" + String.format("%c", 'A' + i) + (j + 1) + ".png", Texture.class), i * 5 + j));
                centers.add(notDonePlaces.get(i * 5 + j));
//                pieces[i * 5 + j].setCenter(BOARD_START_X + r.nextFloat() * PIECE_WIDTH * 5, BOARD_START_Y - r.nextFloat() * PIECE_HEIGHT * 5);
            }
        }
        for (Piece piece : notDonePieces) {
            Place center;
            do {
                center = centers.get(r.nextInt(centers.size()));
            } while (center.idx == piece.idx);
            centers.remove(center);
            piece.sprite.setCenter(center.place.x, center.place.y);
            center.piece = piece;
            piece.place = center;
        }
        textScore = new Text(Constant.SOURCE_TEXT);
        createBackButton();
        Gdx.input.setCatchBackKey(true);
        Gdx.input.setInputProcessor(this);
        startingTime = TimeUtils.millis();
    }

    private void createBackButton() {
        backButtonSprite = new Sprite(Constant.MANAGER.get("SettingsScreen/Back.png", Texture.class));
        backButtonSprite.setX(12 * Constant.SCREEN_WIDTH / 13);
        backButtonSprite.setY(9 * Constant.SCREEN_HEIGTH / 10);
    }

    @Override
    public void render(float delta) {
        clearScreen();
        minigame1Scene.draw();
        batch.begin();

        if (won) {
            ended = true;
        } else if ((TimeUtils.millis() - startingTime) >= 90000) {
            endingSprite.setTexture(Constant.MANAGER.get("Minigame1Screen/SplashMinigameLoss.png", Texture.class));
            ended = true;
        }

        for (Piece piece : donePieces) {
            piece.sprite.draw(batch);
        }
        for (Piece piece : notDonePieces) {
            piece.sprite.draw(batch);
        }

        if (selectedPiece != null) {
            selectedPiece.sprite.draw(batch);
        }

        backButtonSprite.draw(batch);
        if (ended) {
            PreferencesSB.saveMinigameProgress(1);
            endingSprite.draw(batch);
        } else {
            textScore.showMessage(batch, Long.toString((90000 - (TimeUtils.millis() - startingTime)) / 1000),
                    Constant.SCREEN_WIDTH / 2 - 60, Constant.SCREEN_HEIGTH - 20, Color.GREEN);
        }
        batch.end();
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Input.Keys.BACK) {
            PreferencesSB.clickedSound();
            menu.setScreen(isStoryMode ? new ScreenMenu(menu) : new ScreenMinigamesSelection(menu, false));
            return true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 vector = camera.unproject(new Vector3(screenX, screenY, 0));
        for (Piece piece : notDonePieces) {
            if (piece.sprite.getBoundingRectangle().contains(vector.x, vector.y)) {
                selectedPiece = piece;
                break;
            }
        }
        if (backButtonSprite.getBoundingRectangle().contains(vector.x, vector.y)) {
            backButtonSprite.setTexture(Constant.MANAGER.get("SettingsScreen/BackYellow.png", Texture.class));
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        Vector3 vector = camera.unproject(new Vector3(screenX, screenY, 0));
        if (selectedPiece != null) {
            boolean validMove = false;
            for (Place place : notDonePlaces) {
                if (place.place.dst(vector.x, vector.y) < PIECE_WIDTH / 2) {
                    place.piece.sprite.setCenter(selectedPiece.place.place.x, selectedPiece.place.place.y);
                    selectedPiece.place.piece = place.piece;
                    place.piece.place = selectedPiece.place;
                    if (selectedPiece.place.idx == place.piece.idx) {
                        notDonePieces.remove(place.piece);
                        donePieces.add(place.piece);
                        notDonePlaces.remove(selectedPiece.place);
                    }

                    place.piece = selectedPiece;
                    selectedPiece.place = place;
                    selectedPiece.sprite.setCenter(place.place.x, place.place.y);
                    if (place.idx == selectedPiece.idx) {
                        notDonePieces.remove(selectedPiece);
                        donePieces.add(selectedPiece);
                        notDonePlaces.remove(place);
                    }

                    won = notDonePieces.size() == 0;
                    validMove = true;
                    break;
                }
            }
            if (!validMove) {
                selectedPiece.sprite.setCenter(selectedPiece.place.place.x, selectedPiece.place.place.y);
            }
//            if (notDonePlaces[selectedPieceIdx].dst(vector.x, vector.y) < PIECE_WIDTH / 2) {
//                pieces[selectedPieceIdx].setCenter(notDonePlaces[selectedPieceIdx].x, notDonePlaces[selectedPieceIdx].y);
//                done[selectedPieceIdx] = true;
//                notDonePieces.remove(pieces[selectedPieceIdx]);
//                donePieces.add(pieces[selectedPieceIdx]);
//                won = notDonePieces.size() == 0;
//            }
            selectedPiece = null;
        }
        if (backButtonSprite.getTexture().equals(Constant.MANAGER.get("SettingsScreen/BackYellow.png", Texture.class))) {
            backButtonSprite.setTexture(Constant.MANAGER.get("SettingsScreen/Back.png", Texture.class));
        }

        if (backButtonSprite.getBoundingRectangle().contains(vector.x, vector.y)) {
            PreferencesSB.clickedSound();
            menu.setScreen(isStoryMode ? new ScreenMenu(menu) : new ScreenMinigamesSelection(menu, false));
        }

        if (endingSprite.getBoundingRectangle().contains(vector.x, vector.y) && ended) {
            PreferencesSB.clickedSound();
            menu.setScreen(isStoryMode ? new ScreenLoading(menu, Constant.Screens.NEXT_LEVEL) : new ScreenMinigamesSelection(menu, false));
        }

        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (selectedPiece == null) {
            return true;
        }
        Vector3 vector = camera.unproject(new Vector3(screenX, screenY, 0));
        selectedPiece.sprite.setCenter(vector.x, vector.y);
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
