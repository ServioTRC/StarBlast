package mx.itesm.starblast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

class Texto {
    private final BitmapFont font;
    private final Skin skin;
    private final TextureAtlas buttonAtlas;

    Texto(String archivo){
        font = new BitmapFont(Gdx.files.internal(archivo));
        skin = new Skin();
        buttonAtlas = new TextureAtlas();
    }

    TextButton.TextButtonStyle generarTexto(Color colorFont, Color colorPushed, int escala){
        font.getData().setScale(escala);
        skin.addRegions(buttonAtlas);
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.fontColor = colorFont;
        textButtonStyle.downFontColor = colorPushed;
        return textButtonStyle;
    }
}
