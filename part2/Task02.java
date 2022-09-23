package com.konor.HomeWorkThread.part2;

/**
 * 
 * Пример взаимной блокировки Deadlock
 * 
 */
public class Task02
{
    public static void main(String[] args)
    {
        A a1 = new A();
        A a2 = new A();
        Thread t1 = new Thread(new Tester(a1, a2));
        Thread t2 = new Thread(new Tester(a2, a1));
        t1.start();
        t2.start();
    }

    public static class Tester implements Runnable
    {
        static int nextId = 1;
        // первый ресурс
        private A obj1;
        // второй ресурс
        private A obj2;

        private int id = 0;

        public Tester(A obj1, A obj2)
        {
            this.obj1 = obj1;
            this.obj2 = obj2;
            id = nextId++;
        }

        public void run()
        {
            print("Устанавливает значение obj1... ");
            obj1.setValue(id);
            print("done.");
            print("Сравнение объектов... ");
            print("Done. Результат: " + ((obj1.equals(obj2)) ? "equal" : "not equal"));
        }

        private void print(String msg)
        {
            System.out.println("Thread #" + id + ": " + msg);
        }
    }

    public static class A
    {
        private int value = 0;

        synchronized void setValue(int value)
        {
            this.value = value;
        }

        synchronized int getValue()
        {
            return value;
        }

        public synchronized boolean equals(Object o)
        {
            A a = (A) o;
            try
            {
                // Задержка нужна для имитация долгой работы метода.
                Thread.sleep(1000);
            }
            catch (InterruptedException ex)
            {
                System.err.println("Прерывание!");
            }
            return value == a.getValue();
        }
    }
}
