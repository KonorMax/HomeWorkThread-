package com.konor.HomeWorkThread.part1;

/**
 * 
 * Прерывание работы потока
 *
 */
public class Task06
{
    public static void main(String[] args)
    {
        MyThred thred = new MyThred("A");
        thred.start();
        try
        {
            Thread.sleep(1);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        thred.interrupt();
        // thred.stop(); // не используется
        System.out.println("End main");
    }

    private static class MyThred extends Thread
    {
        private final String name;

        public MyThred(String name)
        {
            this.name = name;
        }

        @Override
        public void run()
        {
            for (int i = 0; i <= 100; i++)
            {
                if (Thread.currentThread().isInterrupted()) // проверка
                                                            // прерывания потока
                {
                    System.out.printf("Thread %s interapted.", name);
                    break;
                }
                System.out.println(name + " " + i);
                // Thread.currentThread().sleep(1);
            }
        }
    }
}