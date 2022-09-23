package com.konor.HomeWorkThread.part3;

/**
 * 
 * Исключительные ситуации в потоках
 *
 */
public class Task09
{
    public static void main(String[] args)
    {
        Thread thread = new Thread(new Runnable()
        {
            public void run()
            {
                throw new RuntimeException();
            }
        });  

        Thread thread1 = new Thread(new Runnable()
        {
            public void run()
            {
                throw new RuntimeException();
            }
        });
        thread.start();
        thread1.start();
    }
}
