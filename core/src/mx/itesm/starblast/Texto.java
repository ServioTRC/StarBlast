package mx.itesm.starblast;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

/**
 * Created by Servio T on 08/02/2017.
 */

public class Texto {
    private BitmapFont font;
    private Skin skin;
    private TextureAtlas buttonAtlas;

    public Texto(String archivo){
        font = new BitmapFont(Gdx.files.internal(archivo));
        skin = new Skin();
        buttonAtlas = new TextureAtlas();
    }

    public void mostrarMensaje(SpriteBatch batch, String mensaje, float x, float y, Color color, int escala){
        font.getData().setScale(escala);
        GlyphLayout glyp = new GlyphLayout();
        glyp.setText(font, mensaje);
        float anchoTexto = glyp.width;
        font.setColor(color);
        font.draw(batch, glyp, x-anchoTexto/2, y);
    }

    public TextButton.TextButtonStyle generarTexto(Color color, int escala){
        font.getData().setScale(escala);
        skin.addRegions(buttonAtlas);
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = font;
        textButtonStyle.fontColor = color;
        return textButtonStyle;
    }
}
