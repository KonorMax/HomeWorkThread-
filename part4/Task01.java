package com.konor.HomeWorkThread.part4;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 
 * Callable и Исполнитель. Concurrency API вводит понятие сервиса-исполнителя
 * (ExecutorService) — высокоуровневую замену работе с потоками напрямую.
 * Исполнители выполняют задачи асинхронно и обычно используют пул потоков, так
 * что нам не надо создавать их вручную. Все потоки из пула будут использованы
 * повторно после выполнения задачи, а значит, мы можем создать в приложении
 * столько задач, сколько хотим, используя один исполнитель.
 */
public class Task01
{
    public static void main(String[] args)
            throws InterruptedException, ExecutionException
    {
        int n = 1000;
        final long[] data = new long[n];
        for (int i = 0; i < n; i++)
        {
            data[i] = i;
        }

        Callable<Long> callable = new Callable<Long>()
        {
            // код метода выполниться в паралельном потоке
            @Override
            public Long call() throws Exception
            {
                long sum = 0;
                for (long value : data)
                {
                    sum += value;
                }
                return sum;
            }
        };
        // одиночный исполнитель
        ExecutorService execuror = Executors.newSingleThreadExecutor();
        Future<Long> future = execuror.submit(callable);
        System.out.println(future.get());
        /**
         * shutdown(), ждет завершения запущенных задач, и
         * shutdownNow(), останавливает исполнитель немедленно.
         */
        //execuror.shutdownNow();
        execuror.shutdown();
    }
}