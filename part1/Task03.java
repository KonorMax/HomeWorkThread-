package com.konor.HomeWorkThread.part1;

/**
 * 
 * Создание потоков посредством реализации интерфейса Runnable 
 *
 */
public class Task03
{
    public static void main(String[] args)
    {
        Thread thread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                for (int i = 0; i < 100; i++)
                {
                    try
                    {
                        // усыпляет поток
                        Thread.sleep(1);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                    System.out.println("A =" + i);
                }
            }
        });
        thread.start();

        thread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                for (int i = 100; i > 0; i--)
                {
                    try
                    {
                        Thread.sleep(1);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }

                    System.out.println("B =" + i);
                }
            }
        });
        thread.start();

    }
}
