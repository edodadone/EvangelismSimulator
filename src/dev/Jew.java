package dev;

import gui.Game;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Edoardo De Matteis
 */
public class Jew extends Player {

    /**
     * Costruttore che aggiunge direttamente l'oggetto alla lista in {@link Game}con posizione casuale
     */
     private Jew() {
        this.alive = true;
        this.flag = Color.ROYALBLUE;
        this.initPos();
        rec = new Rectangle(this.x, this.y, Util.l, Util.l);
        rec.setFill(this.flag);
        Game.draw(this.rec);
        Game.select.put(Arrays.asList(this.x, this.y), this);
    }

    /**
     * Costruttore che aggiunge direttamente l'oggetto alla lista in {@link Game}
     *
     * @param x ascissa
     * @param y ordinata
     */
    public Jew(int x, int y) {
        this.alive = true;
        this.flag = Color.ROYALBLUE;
        this.x = x-(x%Util.l);
        this.y = y-(y%Util.l);
        rec = new Rectangle(this.x, this.y, Util.l, Util.l);
        rec.setFill(this.flag);
        Game.draw(this.rec);
        Game.select.put(Arrays.asList(this.x, this.y), this);
    }

    /**
     * Popola la lista in {@link Game} con oggetti con posizione casuale
     */
    public static void folk() {
        for(int i = 0; i < ThreadLocalRandom.current().nextInt(0, Util.max+1); i++) {
            new Jew();
        }
    }

    /**
     * Definisce le azioni che un oggetto compie quando viene a contatto con altri {@link Player}
     */
    @Override
    public void doThings() {
        if (this.counterMuslim > 0 && this.counterMuslim < 4) this.alive = false;
        else if (this.counterMuslim >= 4) {
            this.alive = false;
            List<Integer> pos = this.position();
            Game.root.getChildren().remove(this.rec);
            new Muslim(pos.get(0), pos.get(1));
        }

        if (this.counterChristian == 1 || this.counterChristian == 2) this.alive = false;
        else if (this.counterChristian > 2) {
            this.alive = false;
            List<Integer> pos = this.position();
            Game.root.getChildren().remove(this.rec);
            new Christian(pos.get(0), pos.get(1));
        }

        if(this.counterJew > 2) this.alive = false;

        this.counterMuslim = 0;
        this.counterChristian = 0;
        this.counterJew = 0;
    }

}
