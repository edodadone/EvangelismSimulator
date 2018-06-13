package dev;

import gui.Game;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Muslim extends Player {

    /**
     * Costruttore che aggiunge direttamente l'oggetto alla lista in {@link Game} con posizione casuale
     */
     private Muslim() {
        this.alive = true;
        this.flag = Color.DARKGREEN;
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
    public Muslim(int x, int y) {
        this.alive = true;
        this.flag = Color.DARKGREEN;
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
            new Muslim();
        }
    }

    /**
     * Definisce le azioni che un oggetto compie quando viene a contatto con altri {@link Player}
     */
    @Override
    public void doThings() {
        if(this.counterMuslim >= 3) this.alive = false;

        if(this.counterChristian ==1 || this.counterChristian == 2) this.alive = false;
        else if(this.counterChristian >= 3) {
            this.alive = false;
            List<Integer> pos = this.position();
            Game.root.getChildren().remove(this.rec);
            new Christian(pos.get(0), pos.get(1));
        }

        if(this.counterJew > 0) this.alive = false;

        this.counterMuslim = 0;
        this.counterChristian = 0;
        this.counterJew = 0;
    }

}