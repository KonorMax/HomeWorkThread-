package com.konor.HomeWorkThread.part3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * 
 * Защелка предназначена для одновременного запуска или одновременного окончания
 * множества поотков. Имеются множество потоков (лошади) которые ждут открытие
 * дверей перед стартом. После старта потоки перемещают лошадь на случайную
 * дистанцию. После достижения финиша потоки ждут остальных для расчета
 * результата.
 * 
 */
public class Task03
{
    public static void main(String[] args)
            throws InterruptedException, java.io.IOException
    {
        System.out.println("Подготовка...");

        Race r = new Race("Буденый", "Буцефал", "Конек-горбунок", "Лошарик",
                "Боливар", "Плотва", "Росинант", "Холстомер", "Сивка-бурка");

        System.out.println("Это гонка на дистанцию: " + r.getDistance());

        System.out.println("Нажмите Enter чтобы начать гонку");
        System.in.read();
        r.run();
    }
}

class Race
{
    private Random rand = new Random();

    private int distance = rand.nextInt(250);

    private List<String> horses = new ArrayList<String>();

    public Race(String... names)
    {
        this.horses.addAll(Arrays.asList(names));
    }

    public int getDistance()
    {
        return distance;
    }

    public void run() throws InterruptedException
    {
        System.out.println("Все кони должны подходят к воротам");
        final CountDownLatch start = new CountDownLatch(1);
        final CountDownLatch finish = new CountDownLatch(horses.size());
        final List<String> places = Collections
                .synchronizedList(new ArrayList<String>());

        for (final String horseName : horses)
        {
            new Thread(new Runnable()
            {
                public void run()
                {
                    try
                    {
                        System.out.println(horseName + " начинает скачку...");
                        start.await();

                        int traveled = 0;
                        while (traveled < distance)
                        {
                            // через 0-2 секунды....
                            Thread.sleep(rand.nextInt(3) * 1000);

                            // ... лошадь проходит дистанцию 1-15 пунктов
                            traveled += 1 + rand.nextInt(15);
                            System.out.println(horseName + " прошла дистанцию "
                                    + traveled);
                        }
                        finish.countDown();
                        System.out.println(horseName + " пересек финиш!\n");
                        places.add(horseName);
                    }
                    catch (InterruptedException intEx)
                    {
                        System.out.println("Гонка прервана!!!");
                        intEx.printStackTrace();
                    }
                }
            }).start();
        }

        System.out.println("Ииии... Начали!");
        start.countDown(); // отпускаем потоки

        finish.await(); // ждем потоки
        System.out.println("=========================================");
        System.out.println("И мы имеем победителей!");
        System.out.println(places.get(0) + " выйграл золото...");
        System.out.println(places.get(1) + " получил серебро...");
        System.out.println("и " + places.get(2) + " отправляется домой с бронзой.");
    }
}