package com.konor.HomeWorkThread.part4;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Task03 implements Callable<String>
{
    @Override
    public String call() throws Exception
    {
        Thread.sleep(1000);
        return Thread.currentThread().getName();
    }

    public static void main(String args[])
    {
        // Получает служебный класс ExecutorService из Executors, размер пула
        // потоков - 10
        ExecutorService executor = Executors.newFixedThreadPool(10);
        // Создается список для хранения объекта Future, связанного с Callable
        List<Future<String>> list = new ArrayList<Future<String>>();
        // Create MyCallable instance
        Callable<String> callable = new Task03();
        for (int i = 0; i < 100; i++)
        {
            // Submit task, которые должны выполняться пулом потоков
            Future<String> future = executor.submit(callable);
            // добавляет Future в список, для того, чтобы мы могли обрашаться к ним в будущем 
            list.add(future);
        }
        for (Future<String> fut : list)
        {
            try
            {
                // выводит в консоль значение Future, обратите внимание на
                // задержку вывода в консоли
                // потому что Future.get() ждет завершения задачи
                System.out.println(new Date() + "::" + fut.get());
            }
            catch (InterruptedException | ExecutionException e)
            {
                e.printStackTrace();
            }
        }
        //Отключает executor 
        executor.shutdown();
    }
}