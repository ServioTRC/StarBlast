package mx.itesm.starblast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;

class StageLost extends Stage {

    private final StarBlast menu;
    private AnimatedImage countdownAnimation;

    StageLost(Viewport viewport, Batch batch, StarBlast menuConstruct) {
        super(viewport, batch);
        this.menu = menuConstruct;
        Image background = new Image(Constant.MANAGER.get("PantallaPerder/FondoDerribado.jpg", Texture.class));
        background.setPosition(Constant.SCREEN_WIDTH / 2 - background.getWidth() / 2,
                Constant.SCREEN_HEIGTH / 2 - background.getHeight() / 2);
        addActor(background);
        countdownAnimation = new AnimatedImage(new Animation<TextureRegion>(1f, new TextureRegion(Constant.MANAGER.get("PantallaPerder/Countdown.png", Texture.class)).split(282, 280)[0]));
        countdownAnimation.setPosition(Constant.SCREEN_WIDTH / 2, 230, Align.center);
        addActor(countdownAnimation);
    }

    @Override
    public void draw() {
        if (countdownAnimation.stateTime >= 9) {
            Gdx.app.log("StageLost ", "Going to Menu");
            menu.setScreen(new ScreenMenu(menu));
        }
        super.draw();
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        int level = PreferencesSB.readingLevelProgress();
        if(level == 1) {
            Gdx.app.log("StageLost ", "Going to Level1");
            menu.setScreen(new ScreenLoading(menu, Constant.Screens.LEVEL1));
        } else if (level == 2) {
            Gdx.app.log("StageLost ","Going to Level2");
            menu.setScreen(new ScreenLoading(menu, Constant.Screens.LEVEL2));
        } else if (level == 3) {
            Gdx.app.log("StageLost ", "Going to Level3");
            menu.setScreen(new ScreenLoading(menu, Constant.Screens.LEVEL3));
        }
        return true;
    }

    private class AnimatedImage extends Image {
        Animation<TextureRegion> animation = null;
        float stateTime = 0;

        AnimatedImage(Animation<TextureRegion> animation) {
            super(animation.getKeyFrame(0));
            this.animation = animation;
        }

        @Override
        public void act(float delta) {
            ((TextureRegionDrawable) getDrawable()).setRegion(animation.getKeyFrame(stateTime += delta, true));
            super.act(delta);
        }
    }

}
