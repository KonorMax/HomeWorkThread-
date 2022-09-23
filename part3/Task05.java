package com.konor.HomeWorkThread.part3;

import java.util.concurrent.Exchanger;

/**
 * 
 * Обменник.
 * Есть два грузовика: один едет из пункта A в пункт D, другой из
 * пункта B в пункт С. Дороги AD и BC пересекаются в пункте E. Из пунктов A и B
 * нужно доставить посылки в пункты C и D. Для этого грузовики в пункте E должны
 * встретиться и обменяться соответствующими посылками.
 */
public class Task05
{
    private static final Exchanger<String> EXCHANGER = new Exchanger<>();

    public static void main(String[] args) throws InterruptedException
    {
        // Формируем груз для 1-го грузовика
        String[] p1 = new String[] { "{посылка A->D}", "{посылка A->C}" };
        // Формируем груз для 2-го грузовика
        String[] p2 = new String[] { "{посылка B->C}", "{посылка B->D}" };
        // Отправляем 1-й грузовик из А в D
        new Thread(new Truck(1, "A", "D", p1)).start();
        Thread.sleep(100);
        // Отправляем 2-й грузовик из В в С
        new Thread(new Truck(2, "B", "C", p2)).start();
    }

    public static class Truck implements Runnable
    {
        private int number;

        private String dep;

        private String dest;

        private String[] parcels;

        public Truck(int number, String departure, String destination,
                String[] parcels)
        {
            this.number = number;
            this.dep = departure;
            this.dest = destination;
            this.parcels = parcels;
        }

        @Override
        public void run()
        {
            try
            {
                System.out.printf("В грузовик №%d погрузили: %s и %s.\n",
                        number, parcels[0], parcels[1]);
                System.out.printf(
                        "Грузовик №%d выехал из пункта %s в пункт %s.\n",
                        number, dep, dest);
                Thread.sleep(1000 + (long) Math.random() * 5000);
                System.out.printf("Грузовик №%d приехал в пункт Е.\n", number);
                // При вызове exchange() поток блокируется и ждет
                parcels[1] = EXCHANGER.exchange(parcels[1]);
                // пока другой поток вызовет exchange(), после этого произойдет
                // обмен посылками
                System.out.printf(
                        "В грузовик №%d переместили посылку для пункта %s.\n",
                        number, dest);
                Thread.sleep(1000 + (long) Math.random() * 5000);
                System.out.printf(
                        "Грузовик №%d приехал в %s и доставил: %s и %s.\n",
                        number, dest, parcels[0], parcels[1]);
            }
            catch (InterruptedException e)
            {
            }
        }
    }
}