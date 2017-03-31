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
    private final GlyphLayout layout;

    Texto(String archivo) {
        font = new BitmapFont(Gdx.files.internal(archivo));
        skin = new Skin();
        buttonAtlas = new TextureAtlas();
        layout = new GlyphLayout();
    }

    TextButton.TextButtonStyle generarTexto(Color colorFont, Color colorPushed, int escala) {
        font.getData().setScale(escala);
        skin.addRegions(buttonAtlas);
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.fontColor = colorFont;
        textButtonStyle.downFontColor = colorPushed;
        return textButtonStyle;
    }

    public void mostrarMensaje(SpriteBatch batch, String mensaje, float x, float y, Color color) {
        font.setColor(color);
        layout.setText(font, mensaje);
        font.draw(batch, layout, x, y);
    }
}
