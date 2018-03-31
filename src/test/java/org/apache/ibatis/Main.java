package org.apache.ibatis;

import org.apache.ibatis.session.SqlSessionManager;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 项目名称：  mybatis-3<br>
 * 类名称：  Main<br>
 * 描述：
 *
 * @author wangqiang
 * 创建时间：  2018/3/7 0007 16:28
 */
public class Main {

    @Test
    public void testInterruption(){
        Object object = new Object();

        Thread thread = new Thread(() -> {

            try {

                synchronized (object) {
                    System.out.println("sleep");

                    object.wait();
                    System.out.println("wait");
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        });
        thread.start();

        thread.interrupt();
        new Thread(() -> {
            synchronized (object) {

                System.out.println("come on");
                object.notifyAll();
            }
        }).start();


        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void test1() {
        Dog dog = new Dog();
        Man man = dog::say;
        man.say();
    }


    public static class Dog {
        void say() {
            System.out.println("w,w,w");
        }
    }

    public interface Man {
        void say();
    }

    @Test
    public void test2() {

        try {
            Runtime.getRuntime().exec("calc");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Test
    public void testInstanceOf() {
        AtomicInteger integer = new AtomicInteger();
        CountDownLatch countDownLatch = new CountDownLatch(2);
        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                integer.addAndGet(10);
            }
            countDownLatch.countDown();
        }).start();

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                integer.addAndGet(1);
            }
            countDownLatch.countDown();
        }).start();
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(integer.get());
    }

    @Test
    public void test3() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(2);

        new Thread(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    Name.set(Thread.currentThread().getName() + ":" + i);

                    Thread.sleep(ThreadLocalRandom.current().nextInt(10) * 100);
                    System.out.println(Name.get());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                countDownLatch.countDown();
            }
        }).start();

        new Thread(() -> {
            try {
                for (int i = 0; i < 10; i++) {
                    Name.set(Thread.currentThread().getName() + ":" + i);

                    Thread.sleep(ThreadLocalRandom.current().nextInt(10) * 100);
                    System.out.println(Name.get());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                countDownLatch.countDown();
            }
        }).start();

        countDownLatch.await();
    }


    static class Name {
        private static final ThreadLocal<String> LOCAL = new ThreadLocal<>();

        public static String get() {
            return LOCAL.get();
        }

        public static void set(String name) {
            LOCAL.set(name);
        }

    }

}


