package unam.ciencias.computoconcurrente;

import java.util.concurrent.Semaphore;

/**
 * Clase que representa la mesa donde se sientan a comer los filósofos
 */

public class DiningTable {

    private byte [] state;          // Arreglo con el estado de cada filósofo (0 es pensando, 1 es hambriento y 2 es comiendo)
    private Semaphore [] semaphore; // Arreglo con el semáforo de cada filósofo
    private boolean [] ateOnce;     // Arreglo con la bandera de cada filósofo que indica si ya comió al menos una vez
    private boolean allAteOnce;     // Bandera que indica si todos los filósofos ya comieron al menos una vez
    private Semaphore mutex;        // Mutex para controlar acceso a la sección crítica
    
    /**
     * Construye un nuevo objeto DinningTable con size asientos en la mesa
     * @param size es la cantidad de asientos en la mesa, que es igual a la cantidad de platos y la cantidad de palillos
     */

    public DiningTable (int size) {
        state = new byte [size];
        semaphore = new Semaphore [size];
        int index = 0;
        while (index < size) {
            semaphore [index] = new Semaphore (0);
            index ++;
        }
        ateOnce = new boolean [size];
        mutex = new Semaphore (1);
    }

    /**
     * El filósofo en el asiento index intenta tomar sus dos palillos
     * Si ambos paillos están disponibles procede a comer
     * Si no lo están, esperará a que estén disponibles y después empezará a comer
     * @param index es el índice del asiento del filósofo
     * @throws InterruptedException
     */

    public void takeChopsticks (int index) throws InterruptedException {
        mutex.acquire ();             /* INICIA LA SECCIÓN CRÍTICA */
        state [index] = 1;            // El filósofo deja de pensar y ahora está hambriento
        chopsticksAvailable (index);  // El filósofo revisa si ambos palillos están disponibles
        mutex.release ();             /* TERMINA LA SECCIÓN CRÍTICA */
        semaphore [index].acquire (); // Si los palillos no estaban disponibles debe esperar
    }

    /**
     * El filósofo en el asiento index termina de comer y libera sus dos palillos
     * Si había filósofos esperando por sus palillos, les avisa que ya está disponible para ellos un palillo
     * El filósofo regresa a pensar
     * @param index es el índice del asiento del filósofo
     * @thows InterruptedException
     */

    public void putDownChopsticks (int index) throws InterruptedException {
        mutex.acquire ();                    /* INICIA LA SECCIÓN CRÍTICA */
        state [index] = 0;                   // El filósofo deja de comer y ahora está pensando
        System.out.println ("Filósofo " + (index + 1) + " está pensando");
        chopsticksAvailable (left (index));  // Le avisa al filósofo a su izquierda y este vuelve a revisar si ya puede comer
        chopsticksAvailable (right (index)); // Le avisa al filósofo a su derecha y este vuelve a revisar si ya puede comer
        mutex.release ();                    /* TERMINA LA SECCIÓN CRÍTICA */
    }

    /**
     * Consulta la bandera allAteOnce que indica si todos los filósofos sentados en la mesa ya comieron
     * @return true si y solo si todos los filósofos ya comieron al menos una vez
     */

    public boolean getAllAteOnce () {
        return allAteOnce;
    }

    /**
     * Consulta el tamaño de la mesa
     * @return la cantidad de asientos en la mesa
     */

    public int getTableSize () {
        return state.length;
    }

    /**
     * Actualiza las banderas para que reflejen que el filósofo en el asiento index ya comió al menos una vez
     * @param index es el índice del asiento del filósofo
     * @throws InterruptedException
     */

    public void updateAteOnce (int index) throws InterruptedException {
        mutex.acquire (); /* INICIA LA SECCIÓN CRÍTICA */
        ateOnce [index] = true;
        index = 0;
        while (index < state.length) {
            if (! ateOnce [index]) {
                mutex.release ();
                return;
            }
            index ++;
        }
        allAteOnce = true;
        System.out.println ("Todos los filósofos han comido al menos una vez");
        mutex.release (); /* TERMINA LA SECCIÓN CRÍTICA */
    }

    /**
     * Regresa el índice del asiento en la mesa a la izquierda de index
     */

    private int left (int index) {
        index --;
        return index == -1 ? state.length - 1 : index;
    }

    /**
     * Regresa el índice del asiento en la mesa a la derecha de index
     */

    private int right (int index) {
        index ++;
        return index == state.length ? 0 : index;
    }

    /**
     * El filósofo en el asiento index revisa si ya puede comer
     * Si estaba hambriento y ambos palillos están disponibles, procede a comer inmediatamente
     */

    private void chopsticksAvailable (int index) {
        if (
            state [index] == 1 &&         // El filósofo está hambriento y
            state [left (index)] < 2 &&   // el filósofo a su izquierda no está comiendo y
            state [right (index)] < 2     // el filósofo a su derecha no está comiendo
        ) {
            semaphore [index].release (); // Ambos palillos están disponibles y no necesita esperar
            state [index] = 2;            // El filósofo puede comer
            System.out.println ("Filósofo " + (index + 1) + " está comiendo");
        }
    }

}
