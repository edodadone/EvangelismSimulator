package dev;

import gui.Game;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Edoardo De Matteis
 */
public abstract class Player {
    public Color flag;
    Integer x, y;
    public Rectangle rec;

    boolean alive;
    int counterMuslim, counterChristian, counterJew;

    /**
     * Inizializza le coordinate x e y con due valori casuali
     */
    void initPos() {
        this.x = (ThreadLocalRandom.current().nextInt(0, Util.width));
        this.y = (ThreadLocalRandom.current().nextInt(0, Util.heigth));
        this.x = this.x - (this.x % Util.l);
        this.y = this.y - (this.y % Util.l);
    }

    /**
     * @return la posizione in coordinate di un oggetto
     */
    List<Integer> position() {
        return Arrays.asList(this.x, this.y);
    }

    /**
     * @return la nuova posizione in coordinate di un oggetto
     */
    public List<Integer> move() {
        Integer x = ThreadLocalRandom.current().nextInt(-Util.l, Util.l+1);
        Integer y = ThreadLocalRandom.current().nextInt(-Util.l, Util.l+1);
        x = x - (x % Util.l);
        y = y - (y % Util.l);
        return Arrays.asList(x, y);
    }

    /**
     * @return booleano se un oggetto si trova all'interno dei confini del pannello
     */
    private boolean inside() {
        return Game.root.getLayoutBounds().contains(this.rec.getBoundsInParent());
    }

    /**
     *Itera la lista con tutti gli oggetti e fa muovere ognuno di questi, se un oggetto è morto o
     * è fuori dai confini viene eliminato dalla lista
     */
    public static void walk() {
        for (Iterator<Player> i = Game.select.values().iterator(); i.hasNext(); ) {
            Player p = i.next();
            if (!p.alive || !p.inside()) {
                Rectangle rec = new Rectangle(p.position().get(0),p.position().get(1),Util.l,Util.l);
                rec.setFill(Util.VATICAN);
                Game.draw(rec);
                Game.root.getChildren().remove(p.rec);
                i.remove();
            } else {
                p.check();
                p.doThings();
            }
        }
        Game.update();
    }

    /**
     * Controlla agli estremi di una cella e vede se ci sono oggetti adiacenti
     */
    private void check() {
        for (int y = this.y - Util.l; y <= this.y + Util.l; y += Util.l) {
            for (int x = this.x - Util.l; x <= this.x + Util.l; x += Util.l) {
                try {
                    Color c = Game.getColor(Arrays.asList(x, y));
                    assert c != null;
                    if (c.equals(Color.PURPLE)) counterChristian++;
                    else if (c.equals(Color.DARKGREEN)) counterMuslim++;
                    else if (c.equals(Color.ROYALBLUE)) counterJew++;
                } catch (NullPointerException ignored) {}
            }
        }
    }

    /**
     * Definisce le azioni che un oggetto compie quando viene a contatto con altri {@link Player}
     */
    public abstract void doThings();
}