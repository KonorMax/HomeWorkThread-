package com.konor.HomeWorkThread.part3;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Использование lock на запись и чтение
 * 
 */
public class Task07
{
    private static long counter = 0;

    private static volatile boolean isActive = true;

    // Use Fair Locking Mode
    private final static ReentrantReadWriteLock lock = new ReentrantReadWriteLock(
            true);

    private final static Lock readLock = lock.readLock();

    private final static Lock writeLock = lock.writeLock();

    public static long increment()
    {
        writeLock.lock();
        try
        {
            counter++;
            return counter;
        }
        finally
        {
            writeLock.unlock();
        }
    }

    public static long getCounter()
    {
        readLock.lock();
        try
        {
            return counter;
        }
        finally
        {
            readLock.unlock();
        }
    }

    public static void main(String[] args)
    {
        Thread reader1 = new Thread(new Reader(), "Читатель 1");
        Thread reader2 = new Thread(new Reader(), "Читатель 2");
        Thread reader3 = new Thread(new Reader(), "Читатель 3");
        Thread reader4 = new Thread(new Reader(), "Читатель 4");
        Thread reader5 = new Thread(new Reader(), "Читатель 5");

        Thread writer1 = new Thread(new Writer(), "Писатель 1");
        Thread writer2 = new Thread(new Writer(), "Писатель 2");

        writer1.start();
        writer2.start();

        reader1.start();
        reader2.start();
        reader3.start();
        reader4.start();
        reader5.start();

        try
        {
            Thread.sleep(100);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        isActive = false;
    }
    /**
     * 
     * Класс читателя
     *
     */
    private static class Reader implements Runnable
    {
        public void run()
        {
            // tight loop using volatile variable as active flag for proper
            // shutdown
            while (isActive)
            {
                try
                {
                    readCounter();
                }
                catch (Exception e)
                {
                    System.out.format("%s прерван\n",
                            Thread.currentThread().getName());
                    e.printStackTrace();
                }
            }
        }

        private void readCounter()
        {
            long c = getCounter();
            System.out.format("%s: Чтение counter = %05d\n",
                    Thread.currentThread().getName(), c);
            try
            {
                Thread.sleep(5);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
    
    /**
     * 
     * Класс писателя. Изменяыет значение счетчика
     *
     */
    private static class Writer implements Runnable
    {
        public void run()
        {
            // tight loop using volatile variable as active flag for proper
            // shutdown
            while (isActive)
            {
                try
                {
                    writeCounter();
                }
                catch (Exception e)
                {
                    System.out.format("%s прерван\n",
                            Thread.currentThread().getName());
                    e.printStackTrace();
                }
            }
        }

        private void writeCounter()
        {
            long c = increment();
            System.out.format("%s: Увеличиен counter = %05d\n",
                    Thread.currentThread().getName(), c);
            try
            {
                Thread.sleep(15);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }
}