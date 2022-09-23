package com.konor.HomeWorkThread.part2;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Пример производитель/потребитель. Здесь определен класс магазина, потребителя
 * и покупателя. Производитель в методе run() добавляет в объект Store с помощью
 * его метода put() 6 товаров. Потребитель в методе run() в цикле обращается к
 * методу get объекта Store для получения этих товаров. Оба метода Store - put и
 * get являются синхронизированными.
 * 
 * Для отслеживания наличия товаров в классе Store проверяем значение переменной
 * product. По умолчанию товара нет, поэтому переменная равна 0. Метод get() -
 * получение товара должен срабатывать только при наличии хотя бы одного товара.
 * 
 */
public class Task07
{
    public static void main(String[] args)
    {
        Store store = new Store();
        for (int i = 1; i <= 1; i++)
        {
            Producer producer = new Producer(String.valueOf(i), store);
            new Thread(producer).start();
        }
        for (int i = 1; i <= 3; i++)
        {
            Consumer consumer = new Consumer(String.valueOf(i), store);
            new Thread(consumer).start();
        }
    }
}

// Класс Магазин, хранящий произведенные товары
class Store
{
    private boolean open = true;

    private int product = 0;

    public synchronized boolean get(String name)
    {
        if (open)
        {
            while (product < 1 && open)
            {
                try
                {
                    wait();
                }
                catch (InterruptedException e)
                {
                }
            }
            if (product < 1)
                return false;
        }
        product--;
        System.out.println("--- Покупатель " + name + " купил товар");
        System.out.println("Товаров на складе: " + product);
        return true;
    }

    public synchronized void put()
    {
        while (product >= 10)
        {
            try
            {
                wait();
            }
            catch (InterruptedException e)
            {
            }
        }
        product++;
        System.out.println("Производитель добавил 1 товар");
        System.out.println("Товаров на складе: " + product);
        notifyAll();
        try
        {
            Thread.sleep(10);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

    }

    public synchronized void close()
    {
        open = false;
    }

    public synchronized boolean isOpen()
    {
        return open;
    }

    public synchronized int getProduct()
    {
        return product;
    }
}

// класс Производитель
class Producer implements Runnable
{
    private static final int PRODUCT_NUBER = 50;

    private List<Consumer> consumers = new ArrayList<>();

    private Store store;

    private String name;

    public Producer(String name, Store store)
    {
        this.name = name;
        this.store = store;
    }

    public void addConsumer(Consumer consumer)
    {
        consumers.add(consumer);
    }

    public void run()
    {
        System.out.println("Призводитель " + name + " начал работать");
        for (int i = 1; i <= PRODUCT_NUBER; i++)
        {
            System.out.println(name + " произвел товар " + i);
            store.put();
        }
        System.out.println(
                "++++++ Производитель " + name + " закончил работу ++++");
        store.close();
    }
}

// Класс Потребитель
class Consumer implements Runnable
{
    private int sell;

    private String name;

    private Store store;

    public Consumer(String name, Store store)
    {
        this.name = name;
        this.store = store;
    }

    public void run()
    {
        System.out.println("Покупатель " + name + " закончил работу."
                + " получено продуктов " + sell);
        Random random = new Random();
        while (store.isOpen())
        {
            if (store.get(name))
            {
                sell++;
            }
            try
            {
                Thread.sleep(5 + random.nextInt(10));
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        while (store.getProduct() > 0)
        {
            if (store.get(name))
            {
                sell++;
            }
        }
        synchronized (store)
        {
            store.notifyAll();
        }
        System.out.println("Покупатель " + name + " закончил работу."
                + " получено продуктов " + sell);
    }
}