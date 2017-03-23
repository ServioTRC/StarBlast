package mx.itesm.starblast;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

class AutoAnimation {

    float x;
    float y;
    float time = 0;
    Animation animation;

    AutoAnimation(String route, float rate, float x, float y, int hx, int hy, SpriteBatch batch){
        TextureRegion[][] region = (new TextureRegion(new Texture(route))).split(hx,hy);
        this.x = x-hx/2f;
        this.y = y-hy/2f;
        animation = new Animation(rate, region[0]);
    }

    public boolean draw(SpriteBatch batch, float deltaTime){
        time += deltaTime;
        if(animation.isAnimationFinished(time)){
            return true;
        }
        batch.draw((TextureRegion) animation.getKeyFrame(time),x,y);
        return false;
    }
}
