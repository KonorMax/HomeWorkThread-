package com.konor.HomeWorkThread.part3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Использование AtomicLong для гернерации уникального id для множества потоков.
 *
 */
public class Task01
{
    static long count;

    final static AtomicLong orderIdGenerator = new AtomicLong(0);

    public static void main(String[] args)
    {
        final List<Item> orders = Collections
                .synchronizedList(new ArrayList<Item>());

        for (int i = 0; i < 10; i++)
        {
            Thread orderCreationThread = new Thread(new Runnable()
            {
                public void run()
                {
                    for (int i = 0; i < 10; i++)
                    {
                        count++;
                        long orderId = orderIdGenerator.incrementAndGet();
                        Item order = new Item(Thread.currentThread().getName(),
                                orderId, count);
                        orders.add(order);
                    }
                }
            });
            orderCreationThread.setName("Order Creation Thread " + i);
            orderCreationThread.start();
        }
        try
        {
            Thread.sleep(1000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        Set<Long> orderIds = new HashSet<Long>();
        for (Item order : orders)
        {
            orderIds.add(order.getID());
            System.out.println("Order id:" + order.getID() + " " + count);
        }
    }
}

class Item
{
    private String itemName;

    private long id;

    private long count;

    public Item(String name, long id, long count)
    {
        this.itemName = name;
        this.id = id;
        this.count = count;
    }

    public String getItemName()
    {
        return itemName;
    }

    public long getID()
    {
        return id;
    }
}
