package com.konor.HomeWorkThread.part1;

/**
 * 
 * Присоединение (ожидание) потоков
 *
 */
public class Task07
{
    @SuppressWarnings("unused")
    public static void main(String[] args)
    {
        Sleeper sleepy1 = new Sleeper("Спящий 1", 1500);
        Sleeper sleepy2 = new Sleeper("Спящий 2", 2000);
        Joiner joiner1 = new Joiner("Присоединенный к спящему 1", sleepy1);
        Joiner joiner2 = new Joiner("Присоединенный к спящему 2", sleepy2);
        // sleepy1.interrupt();
        // joiner2.getThread().interrupt();
    }
}

class Sleeper extends Thread
{
    private int duration;

    public Sleeper(String name, int sleepTime)
    {
        super(name);
        duration = sleepTime;
        start();
    }

    public void run()
    {
        System.out.println(getName() + " начат.");
        try
        {
            sleep(duration);
        }
        catch (InterruptedException e)
        {
            System.out.println(getName() + " прерван");
            return;
        }
        System.out.println(getName() + " активизировался.");
    }
}

class Joiner implements Runnable
{
    private Thread sleeper;

    private Thread thread;

    public Joiner(String name, Thread sleeper)
    {
        this.sleeper = sleeper;
        thread = new Thread(this);
        thread.setName(name);
        thread.start();
    }

    public void run()
    {
        try
        {
            sleeper.join(); // присоеденение к спящему потоку
            System.out.println(thread.getName() + " завершен.");
        }
        catch (InterruptedException e)
        {
            System.out.println(thread.getName() + " прерван.");
        }
    }

    public Thread getThread()
    {
        return thread;
    }
}