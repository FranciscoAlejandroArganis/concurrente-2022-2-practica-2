package unam.ciencias.computoconcurrente;

import java.util.Random;

/**
 * Clase que representa un filósofo que se ejecuta en un hilo
 */

public class Philosopher implements Runnable {

    private static Random rng = new Random (); // Generador de números aleatorios
    private static long maxTime = 0;           // Tiempo máximo que pasará un filósofo pensando o comiendo

    private DiningTable table; // Cada filósofo puede ver la mesa
    private int index;         // Cada filósofo sabe el índice de su asiento

    /**
     * Construye un nuevo objeto Philosopher
     * @param table es la mesa donde están sentados los filósofos
     * @param index es el índice del asiento donde está sentado este filósofo
     */

    public Philosopher (DiningTable table, int index) {
        this.table = table;
        this.index = index;
        if (maxTime == 0) maxTime = 400 * table.getTableSize ();
    }

    /**
     * Ciclo de la ejecución del hilo correspondiente a este filósofo
     * El ciclo consiste de 4 etapas:
     * 1. El filósofo piensa hasta que le da hambre
     * 2. El filósofo hambriente espera hasta que puede puede tomar sus dos palillos
     * 3. El filósofo come
     * 4. El filósofo libera los palillos cuando termina de comer
     */

    public void run () {
        try {
            System.out.println ("Filósofo " + (index + 1) + " está pensando");
            while (true) {
                think ();
                table.takeChopsticks (index);
                eat ();
                table.putDownChopsticks (index);
            }
        } catch (Exception e) {
            e.printStackTrace ();
        }
    }

    /**
     * El hilo de este filósofo se duerme por una cantidad aleatoria de tiempo, acotada por maxTime
     */

    private void sleepRandomTime () throws InterruptedException {
        long time = rng.nextLong () % maxTime;
        if (time < 0) time += maxTime;
        Thread.sleep (time);
    }

    /**
     * El filósofo piensa por una cantidad aleatoria de tiempo
     */

    private void think () throws InterruptedException {
        sleepRandomTime ();
    }

    /**
     * El filósofo come por una cantidad aleatoria de tiempo
     */

    private void eat () throws InterruptedException {
        if (! table.getAllAteOnce ()) table.updateAteOnce (index);
        sleepRandomTime ();
    }

}