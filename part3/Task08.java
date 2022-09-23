package com.konor.HomeWorkThread.part3;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

/**
 * Использование потокобезопасного HashMap
 * 
 * @author VUnguryan
 *
 */
public class Task08
{
    static Set<Thread> updateThreads = new HashSet<Thread>();

    public static void main(String[] args) throws InterruptedException
    {
        Map<Integer, String> map = new HashMap<Integer, String>();
        //ConcurrentMap<Integer, String> map = new ConcurrentHashMap<Integer, String>();
        for (int i = 0; i < 1000; i++)
        {
            startUpdateThread(i, map);
        }
        Thread.sleep(1000);
        for (Map.Entry<Integer, String> entry : map.entrySet())
        {
            System.out.println("Key :" + entry.getKey() + " Value:" + entry.getValue());
        }
        
        Thread.sleep(100);
        for (Thread thread : updateThreads)
        {
            thread.interrupt();
        }
    }

    private static void startUpdateThread(int i, final Map<Integer, String> map)
    {
        Thread thread = new Thread(new Runnable()
        {
            public void run()
            {
                while (!Thread.interrupted())
                {
                    Random random = new Random();
                    int randomInt = random.nextInt(20);
                    if (!map.containsKey(randomInt))
                    {
                        map.put(randomInt, UUID.randomUUID().toString());                        
                    }
                }
            }
        });
        thread.setName("Update Thread " + i);
        updateThreads.add(thread);
        thread.start();
    }
}