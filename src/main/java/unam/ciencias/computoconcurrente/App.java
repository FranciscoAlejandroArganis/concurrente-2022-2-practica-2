package unam.ciencias.computoconcurrente;

/**
 * Clase que implementa una solución al problema Dining Philosophers
 * El programa debe recibir como argumento por la línea de comandos un entero mayor o igual a 5
 * El entero recibido se toma como la cantidad de filósofos en la mesa
 */

public class App {

    /**
     * Inicia la ejecución del programa
     * @param args son los argumentos de la línea de comandos
     * @throws InterruptedException
     */

    public static void main (String [] args) throws InterruptedException {
        int size = 5;
        try {
            size = Integer.parseInt (args [0]);
            if (size < 5) throw new Exception ();
        } catch (Exception e) {
            System.out.println ("El programa debe recibir como argumento un entero mayor o igual a 5 que representa la cantidad de filósofos");
            System.exit (0);
        }

        // Se crea la mesa y los hilos de los filósofos
        DiningTable table = new DiningTable (size);
        Thread [] philosophers = new Thread [size];
        int index = 0;
        while (index < size) {
            philosophers [index] = new Thread (new Philosopher (table, index));
            index ++;
        }

        // Inicia la ejecución de los hilos
        for (Thread thread : philosophers) thread.start ();
    }
}
