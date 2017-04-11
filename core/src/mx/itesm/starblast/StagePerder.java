package mx.itesm.starblast;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;

class StagePerder extends Stage {

    private final StarBlast menu;
    private AnimatedImage countdownAnimation;

    StagePerder(Viewport viewport, Batch batch, StarBlast menu) {
        super(viewport, batch);
        this.menu = menu;
        Image background = new Image(Constant.MANAGER.get("PantallaPerder/FondoDerribado.jpg", Texture.class));
        background.setPosition(Constant.SCREEN_WIDTH / 2 - background.getWidth() / 2,
                Constant.SCREEN_HEIGTH / 2 - background.getHeight() / 2);
        addActor(background);
        background.addListener(new ClickListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                //TODO reiniciar nivel creo que haré una Interfaz que sea IReiniciable o algo así
                return true;
            }
        });
        countdownAnimation = new AnimatedImage(new Animation<TextureRegion>(1f, new TextureRegion(Constant.MANAGER.get("PantallaPerder/Countdown.png", Texture.class)).split(282, 280)[0]));
        countdownAnimation.setPosition(Constant.SCREEN_WIDTH / 2, 230, Align.center);
        addActor(countdownAnimation);
    }

    @Override
    public void draw() {
        if (countdownAnimation.stateTime >= 10) {
            menu.setScreen(new ScreenMenu(menu));
        }
        super.draw();
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
