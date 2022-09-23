package com.konor.HomeWorkThread.part1;

/**
 * 
 * Объединени потоков в группу.
 *
 */
public class Task04
{
    public static void main(String[] args)
    {
        ThreadGroup group = new ThreadGroup("группа А");
        for (int i = 0; i < 10; i++)
        {
            Thread thread = new Thread(group, new MainThread((char)('A' + i)));
            thread.start();
        }
        //Текущее количество активных потоков в группе, к которой принадлежит поток
        System.out.println(group.activeCount());
        group.list();
    }

    static class MainThread implements Runnable
    {
        private char id;
        public MainThread(char id)
        {
            this.id = id;
        }

        @Override
        public void run()
        {
            for (int i = 100; i > 0; i--)
            {
                System.out.println(id + " = " + i);
            }
        }
    }
}
