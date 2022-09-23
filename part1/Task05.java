package com.konor.HomeWorkThread.part1;

/**
 * 
 * Приостановка потока Thread.sleep
 * Побочный поток изменяет значение общей переменной
 *
 */
public class Task05
{
    // Переменая, которой оперирует инкременатор
    public static int counter = 0;

    public static void main(String[] args)
    {
        CounterThread incrementator = new CounterThread(); // Создание потока

        System.out.print("Значение = ");

        incrementator.start(); // Запуск потока

        // Троекратное изменение действия инкременатора
        // с интервалом в i*2 секунд
        for (int i = 1; i <= 3; i++)
        {
            
            try
            {
                //TimeUnit.SECONDS.sleep(1); // Ожидание в течении 1 сек.
                Thread.sleep(i * 2000); // Ожидание в течении i*2 сек.
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }

            incrementator.changeAction(); // Переключение действия в паралельном потоке
        }

        incrementator.finish(); // Инициация завершения побочного потока
    }
    
    static class CounterThread extends Thread
    {
        // О ключевом слове volatile - чуть ниже
        private volatile boolean isIncrement = true;

        private volatile boolean isFinish = false;

        public void changeAction() // Меняет действие на противоположное
        {
            isIncrement = !isIncrement;
        }

        public void finish() // Инициирует завершение потока
        {
            isFinish = true;
        }

        @Override
        public void run()
        {
            do
            {
                if (!isFinish) // Проверка на необходимость завершения
                {
                    if (isIncrement)
                        counter++; // Инкремент
                    else
                        counter--; // Декремент

                    // Вывод текущего значения переменной
                    System.out.print(Task05.counter + " ");
                }
                else
                    return; // Завершение потока

                try
                {
                    Thread.sleep(1000); // Приостановка потока на 1 сек.
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            } while (true);
        }
    }
}