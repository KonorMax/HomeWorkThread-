package com.konor.HomeWorkThread.part3;

import java.util.Random;
import java.util.concurrent.Semaphore;

/**
 * 
 * Семафор. Есть несколько потоков которые создают документ и отправляют его на 
 * печать. Семафор ограничивает работу потоков с обшим ресурсом (принтером). 
 * Только один паток может печатать. Остальные ждут.
 * 
 */
public class Task02
{
    static class PrintQueue
    {
        private static final Random rnd = new Random();

        private final Semaphore semaphore;

        public PrintQueue()
        {
            this.semaphore = new Semaphore(1);
        }

        public void printJob(Object document)
        {
            try
            {
                semaphore.acquire(); // захват семафора
                long duration = rnd.nextInt(1000);
                System.out.println(Thread.currentThread().getName()
                        + " создан запрос на печать " + duration + " мсек");
                Thread.sleep(duration);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            finally
            {
                System.out.println(Thread.currentThread().getName()
                        + " отправил документ на печать");
                semaphore.release(); // освобождение 
                System.out.println(
                        Thread.currentThread().getName() + " печатает");
            }
        }
    }

    static class Job implements Runnable
    {
        private PrintQueue queue;

        public Job(PrintQueue queue)
        {
            this.queue = queue;
        }

        @Override
        public void run()
        {
            queue.printJob(new Object());
        }
    }

    public static void main(String[] args)
    {
        PrintQueue q = new PrintQueue();
        for (int i = 0; i < 5; i++)
        {
            new Thread(new Job(q), "Thread: " + i).start();
        }
    }
}