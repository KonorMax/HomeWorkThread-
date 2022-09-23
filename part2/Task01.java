package com.konor.HomeWorkThread.part2;

/**
 * 
 * Гонка потоков. Синхронизация методов
 *
 */
public class Task01
{
    public static void main(String[] args) throws InterruptedException
    {
        final Counter counter = new Counter();

        for (int i = 0; i < 10; i++)
        {
            MyThread t1 = new MyThread(counter, i);
            Thread t = new Thread(t1);
            t.start();
        }
        Thread.sleep(5000);
        System.out.println(counter.getCount());
    }
}

class Counter
{
    //FIXME раскоментируй volatile по указанию преподователя 
    private /* volatile */ long count;

    //FIXME раскоментируй synchronized по указанию преподователя
    public /* synchronized */ void increment()
    {
        count++;
    }
    
    //FIXME раскоментируй synchronized по указанию преподователя
    public /* synchronized */ void decrement()
    {
        count--;
    }

    public long getCount()
    {
        return count;
    }
}

class MyThread implements Runnable
{
    private /*volatile*/ Counter counter;

    private int id;

    public MyThread(Counter counter, int id)
    {
        this.counter = counter;
        this.id = id;
    }

    @Override
    public void run()
    {
        //System.out.println("Thread id = " + id + " start");
        for (int i = 0; i < 100; i++)
        {
            if (id % 2 == 0)
            {
                counter.increment();
            }
/*            else
            {
                counter.decrement();
            }*/
            try
            {
                Thread.currentThread();
                Thread.sleep(1);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        //System.out.println("Thread id = " + id + " end");
    }
}