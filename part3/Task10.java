package com.konor.HomeWorkThread.part3;

import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 
 * Hashtable против synchronizedMap и против ConcurrentHashMap
 *
 */
public class Task10
{
    public final static int THREAD_POOL_SIZE = 5;

    public static Map<String, Integer> crunchifyHashTableObject = null;

    public static Map<String, Integer> crunchifySynchronizedMapObject = null;

    public static Map<String, Integer> crunchifyConcurrentHashMapObject = null;

    public static void main(String[] args) throws InterruptedException
    {
        // Тестирование с Hashtable Object
        crunchifyHashTableObject = new Hashtable<String, Integer>();
        crunchifyPerformTest(crunchifyHashTableObject);

        // Тестирование с synchronizedMap Object
        crunchifySynchronizedMapObject = Collections
                .synchronizedMap(new HashMap<String, Integer>());
        crunchifyPerformTest(crunchifySynchronizedMapObject);

        // Тестирование с ConcurrentHashMap Object
        crunchifyConcurrentHashMapObject = new ConcurrentHashMap<String, Integer>();
        crunchifyPerformTest(crunchifyConcurrentHashMapObject);
    }

    public static void crunchifyPerformTest(
            final Map<String, Integer> crunchifyThreads)
            throws InterruptedException
    {

        System.out.println("Тест начат для: " + crunchifyThreads.getClass());
        long averageTime = 0;
        for (int i = 0; i < 5; i++)
        {

            long startTime = System.nanoTime();
            ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

            for (int j = 0; j < THREAD_POOL_SIZE; j++)
            {
                executor.execute(new Runnable()
                {
                    @SuppressWarnings("unused")
                    @Override
                    public void run()
                    {

                        for (int i = 0; i < 500_000; i++)
                        {
                            Integer crunchifyRandomNumber = (int) Math
                                    .ceil(Math.random() * 550000);

                            // Извлечение значений. 
                            Integer crunchifyValue = crunchifyThreads
                                    .get(String.valueOf(crunchifyRandomNumber));

                            // Вставка пары ключ/значение
                            crunchifyThreads.put(
                                    String.valueOf(crunchifyRandomNumber),
                                    crunchifyRandomNumber);
                        }
                    }
                });
            }

            // останавливаем Executor
            executor.shutdown();

            // Blocks until all tasks have completed execution after a shutdown
            // request
            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);

            long entTime = System.nanoTime();
            long totalTime = (entTime - startTime) / 1000000L;
            averageTime += totalTime;
            System.out.println(
                    "2500K данных добавлено/считывано в " + totalTime + " ms");
        }
        System.out.println("Для " + crunchifyThreads.getClass()
                + " среднее время " + averageTime / 5 + " ms\n");
    }
}
