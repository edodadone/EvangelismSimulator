package dev;

import javafx.scene.paint.Color;

/**
 * @author Edoardo De Matteis
 */
public class Util {
    /**
     * @param label     testo con le regole del gioco
     * @param Vatican   un colore molto bello
     * @param width     larghezza del pannello
     * @param heigth    altezza del pannello
     * @param l         lato di una cella
     * @param max       il numero massimo di celle in un'estrazione casuale
     */
    public static final String label =
            "-In principium\t\tpremilo per avviare un'evangelizzazione\n"
                    + "-Dies septimus\tchiunque merita del riposo\n"
                    + "-Dixitque Deus\trichiedi un intervento divino\n"
                    + "-Ite, missa est\t\tfa' tornare le greggi all'ovile\n"
                    + "-Salvator mundi\tdipingi un ritratto";

    final static Color VATICAN = Color.rgb(255, 224, 0);

    public final static int width = Util.l * 200;
    public final static int heigth = Util.l * 100;
    public final static int l = 5;
    final static int max = Util.l * 10000;
}