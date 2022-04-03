package unam.ciencias.computoconcurrente.synch;

/**
 * Clase que implementa el TDA de semáforo
 */

public class Semaphore {
    
    private int count;

    /**
     * Construye un nuevo semáforo con la cantidad especificada de permisos
     * @param init es la cantidad de permisos iniciales del semáforo
     * @throws IllegalArgumentException si la cantidad de permisos es negativa
     */

    public Semaphore (int init) {
        if (init < 0) throw new IllegalArgumentException ();
        count = init;
    }

    /**
     * Regresa la cuenta actual de permisos del semáforo
     * @return los permisos del semáforo
     */

    public synchronized int getPermits () {
        return count;
    }

    /**
     * Establece los permisos del semáforo a la cantidad especificada
     * @param permits es la nueva cantidad de permisos del semáforo
     * @throws IllegalArgumentException si la cantidad de permisos es negativa
     */

    public synchronized void setPermits (int permits) {
        if (permits < 0) throw new IllegalArgumentException ();
        count = permits;
    }

    /**
     * Intenta usar uno de los permisos del semáforo
     * Si no hay permisos, el proceso que llama este método se bloquea
     * @throws InterruptedException
     */

    public synchronized void acquire () throws InterruptedException {
        if (count == 0) wait ();
        count --;
    }

    /**
     * Otorga un nuevo permiso al semáforo
     * Si hay procesos bloqueados en el semáforo, se despierta uno aletario
     */
    
    public synchronized void release () {
        if (count == 0) notify ();
        count ++;
    }

}
