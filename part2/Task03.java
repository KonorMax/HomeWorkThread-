package com.konor.HomeWorkThread.part2;

/**
 * 
 * Условием для продолжения работы потока в WaitingThread является
 * data.value!=0. Если это условие не выполняется – поток ждет. Причем СНАЧАЛА
 * он получает монитор объекта, на котором идет синхронизация – sync, – а уж
 * потом проверяет условие. Это сделано для синхронизации данных, а именно –
 * значения data.value. Далее вызывается sync.wait(). Основной же поток
 * задерживается на небольшое время, чтобы дать второму потоку стартовать, после
 * чего выставляет нужное значение (перед этим захватив монитор!) и вызывает
 * sync.notify(). После этого второй поток продолжает работу и, как мы видим, у
 * него значение data.value = 1.
 */
public class Task03
{
    public static void main(String[] args)
    {
        Object sync = new Object(); // монитор
        Data data = new Data();
        Thread t = new Thread(new WaitingThread(sync, data), "Первый поток");
        t.start();
        t = new Thread(new WaitingThread(sync, data), "Второй поток");
        t.start();
        try
        {
            System.out.println("Главный поток заснул");
            Thread.sleep(500);
        }
        catch (InterruptedException ex)
        {
            System.err.println("Главный поток Прерван: " + ex.getMessage());
        }
        synchronized (sync) // захват монитора в основном потоке
        {
            System.out.println("Главный поток установка значения  value в 1");
            data.value = 1;
            System.out.println("Главный поток будет вызван notify");
            sync.notify();
            //sync.notifyAll();
            System.out.println("Главный поток вызвал notify");
        }
    }

    static class Data
    {
        public int value = 0;
    }

    static class WaitingThread implements Runnable
    {
        private Object sync; // монитор

        private Data data;

        public WaitingThread(Object sync, Data data)
        {
            this.sync = sync;
            this.data = data;
        }

        public void run()
        {
            String threadName = Thread.currentThread().getName();
            System.out.println(threadName + " запушен");
            synchronized (sync)
            {
                if (data.value == 0)
                {
                    try
                    {
                        System.out.println(threadName + " Ждет");
                        sync.wait(); // отпускаем монитор и ждем
                        //sync.wait(100); // если ждать мало, то главный поток не успевает 
                        System.out.println(threadName + " Запушен снова");
                        System.out.println("own:: data.value = " + data.value);
                    }
                    catch (InterruptedException ex)
                    {
                        System.err.println(threadName + " прерван: " + ex.getMessage());
                    }
                }
            }
        }
    }
}
/**
 * Если поток останавливается внутри метода wait, то системный монитор
 * отпускается! В противном случае будет невозможно вызвать notify.
 */
