package com.konor.HomeWorkThread.part3;

import java.util.ArrayList;
import java.util.concurrent.Phaser;

/**
 * Фазер. 
 * Есть пять остановок. На первых четырех из них могут стоять пассажиры и
 * ждать автобуса. Автобус выезжает из парка и останавливается на каждой
 * остановке на некоторое время. После конечной остановки автобус едет в парк.
 * Нам нужно забрать пассажиров и высадить их на нужных остановках.
 */
public class Task06
{
    private static final Phaser PHASER = new Phaser(1);// Сразу регистрируем
                                                       // главный поток
    // Фазы 0 и 6 - это автобусный парк, 1 - 5 остановки

    public static void main(String[] args) throws InterruptedException
    {
        ArrayList<Passenger> passengers = new ArrayList<>();

        // Сгенерируем пассажиров на остановках
        for (int i = 1; i < 5; i++)
        {
            if ((int) (Math.random() * 2) > 0)
                // Этот пассажир выходит на следующей
                passengers.add(new Passenger(i, i + 1));

            if ((int) (Math.random() * 2) > 0)
            {
                // Этот пассажир выходит на конечной
                passengers.add(new Passenger(i, 5));
            }
        }

        for (int i = 0; i < 7; i++)
        {
            switch (i)
            {
                case 0:
                    System.out.println("Автобус выехал из парка.");
                    PHASER.arrive();// В фазе 0 всего 1 участник - автобус
                    break;
                case 6:
                    System.out.println("Автобус уехал в парк.");
                    PHASER.arriveAndDeregister(); // Снимаем главный поток,
                                                  // ломаем барьер
                    break;
                default:
                    int currentBusStop = PHASER.getPhase();
                    System.out.println("Остановка № " + currentBusStop);

                    // Проверяем, есть ли пассажиры на остановке
                    for (Passenger passenger : passengers)
                        if (passenger.departure == currentBusStop)
                        {
                            // Регистрируем поток, который будет участвовать в
                            // фазах и запускаем
                            PHASER.register();
                            passenger.start();
                        }
                    // Сообщаем о своей готовности
                    PHASER.arriveAndAwaitAdvance();
            }
        }
    }

    public static class Passenger extends Thread
    {
        private int departure;

        private int destination;

        public Passenger(int departure, int destination)
        {
            this.departure = departure;
            this.destination = destination;
            System.out.println(this + " ждёт на остановке № " + this.departure);
        }

        @Override
        public void run()
        {
            try
            {
                System.out.println(this + " сел в автобус.");

                // Пока автобус не приедет на нужную остановку(фазу)
                while (PHASER.getPhase() < destination)
                {
                    // заявляем в каждой фазе о готовности и ждем
                    PHASER.arriveAndAwaitAdvance();
                }
                Thread.sleep(1);
                System.out.println(this + " покинул автобус.");
                // Отменяем регистрацию на нужной фазе
                PHASER.arriveAndDeregister();
            }
            catch (InterruptedException e)
            {
            }
        }

        @Override
        public String toString()
        {
            return "Пассажир{" + departure + " -> " + destination + '}';
        }
    }
}