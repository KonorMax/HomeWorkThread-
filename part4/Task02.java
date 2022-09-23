package com.konor.HomeWorkThread.part4;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 
 * Суммирование в разных потоках значений двухмерного массива.
 *
 */
public class Task02
{
    public static void main(String[] args) throws InterruptedException,
            ExecutionException
    {
        Random rnd = new Random();
        int n = 300;
        long[][] data = new long[n][n];
        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < n; j++)
            {
                data[j][i] = rnd.nextInt(Short.MAX_VALUE);
            }
        }
        //System.out.println(Arrays.deepToString(data));
        calculate(data);
        thredCalculate(data);

    }

    private static void calculate(long[][] data)
    {
        long start = System.nanoTime();
        long sum = 0;
        for (int i = 0; i < data.length; i++)
        {
            for (int j = 0; j < data[i].length; j++)
            {
                sum += data[j][i];
            }
        }
        System.out.println();
        System.out.println("Result: " + sum);
        System.out.println("Simple work time = " + (System.nanoTime() - start));

    }

    public static void thredCalculate(long[][] data) throws InterruptedException, ExecutionException
    {
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        ExecutorService pool = Executors.newFixedThreadPool(availableProcessors);
        //ExecutorService pool = Executors.newCachedThreadPool();
        Queue<Future<Integer>> futures = new LinkedList<Future<Integer>>();
        for (int i = 0; i < data.length; i++)
        {
            Callable<Integer> calculator = new RowCalculator(data[i]);
            Future<Integer> feature = pool.submit(calculator);
            futures.add(feature);
        }
        long start = System.nanoTime();
        long sum = 0;
        for (Future<Integer> future : futures)
        {
            sum += future.get(); // ожидает результата
        }
        
        System.out.println();
        System.out.println("Result: " + sum);
        System.out.println("Multi work time  = " + (System.nanoTime() - start));
        pool.shutdown();
    }
}

class RowCalculator implements Callable<Integer>
{
    private long[] data;

    RowCalculator(long[] data)
    {
        this.data = data;
    }

    @Override
    public Integer call() throws Exception
    {
        int summa = 0;
        for (int i = 0; i < data.length; i++)
        {
            summa += data[i];
        }
        return summa;
    }
}