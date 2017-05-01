package mx.itesm.starblast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class Text {
    private final BitmapFont font;
    private final Skin skin;
    private final TextureAtlas buttonAtlas;
    private final GlyphLayout layout;
    private GlyphLayout staticMessage = null;

    public Text(String files) {
        font = new BitmapFont(Gdx.files.internal(files));
        skin = new Skin();
        buttonAtlas = new TextureAtlas();
        layout = new GlyphLayout();
    }

    public TextButton.TextButtonStyle generateText(Color colorFont, Color colorPushed, float scale) {
        font.getData().setScale(scale);
        skin.addRegions(buttonAtlas);
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.fontColor = colorFont;
        textButtonStyle.downFontColor = colorPushed;
        return textButtonStyle;
    }

    public void showMessage(SpriteBatch batch, String message, float x, float y, Color color) {
        font.setColor(color);
        layout.setText(font, message);
        font.draw(batch, layout, x, y);
    }

    public void generateStaticMessage(String message, Color color, float scale){
        font.getData().setScale(scale);
        font.setColor(color);
        staticMessage = new GlyphLayout();
        staticMessage.setText(font, message);
    }

    public void showStaticMessage(Batch batch , float x, float y){
        if(staticMessage == null){
            return;
        }
        font.draw(batch, staticMessage, x, y);
    }
}
