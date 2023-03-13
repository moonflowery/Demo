package com.ch.threadpool;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author chenghao
 * @purpose：
 * @备注：
 * @data 2023年03月06日 11:37
 */
public class TestPool {
    public static void main(String[] args) {
        Threadpool threadpool = new Threadpool(2,1000,TimeUnit.MILLISECONDS,10);
        for (int i = 0; i < 5; i++) {
            int j = i;
            threadpool.execute(()->{
                System.out.println(j);
            });
        }
    }
}

/*
 * @Description:
 * @Author: chenghao
 * @Date: 2023/3/6 13:28
 * @param null
 * @return: null
 **/
class Threadpool{
    //为什么这个是安全的
    private BlockingQueue<Runnable> taskQueue;
    //用来存储线程的，每个worker都是一个线程
    // 这个是不安全的
    private HashSet<Worker> workers = new HashSet<>();
    private int coreSize;
    //如果长时间没有任务，就让这个线程结束
    private long timeout;
    private TimeUnit unit;



    public Threadpool( int coreSize, long timeout, TimeUnit unit, int queueCapcity) {
        this.taskQueue = new BlockingQueue<>(queueCapcity);
        this.coreSize = coreSize;
        this.timeout = timeout;
        this.unit = unit;
    }
    public void execute(Runnable task){
        synchronized (workers) {
            if (workers.size() < coreSize){
                Worker worker = new Worker(task);
                System.out.println("新增worker"+  worker + "，"+task);
                workers.add(worker);
                worker.start();
            }
            else {
                System.out.println("加入任务队列" + task);
                taskQueue.put(task);
            }
        }
    }

    /*
        这个类就是线程，里边的成员变量传递的是任务，
        在run方法中执行，执行是while循环，没有任务时会停止
     */
    class Worker extends Thread {
        private Runnable task;
        private Worker(Runnable task) {
            this.task = task;
        }
        @Override
        public void run() {
            //这里的任务来源于excute中两个情况，
            // 任务数小于coreSize，直接创建线程执行
            //否则加入到任务队列中，所以条件中需要从任务队列中取出poll
            while (task != null || (task = taskQueue.poll(timeout,unit)) != null){
                try {
                    System.out.println("正在执行..." + task);
                    task.run();
                }finally {
                    //执行成功后删除任务
                    task = null;
                }
            }
            synchronized (workers){
                System.out.println("worker被移除"+this);
                //执行成功后从线程队列中移除该线程
                workers.remove(this);
            }
        }
    }

}


class BlockingQueue<T> {
    // 1.任务队列
    private Deque<T> queue = new ArrayDeque<>();

    //2.锁：该阻塞队列到时候是多个线程来执行，需要加锁：灵活一点就是用ReentrantLock
    ReentrantLock lock = new ReentrantLock();

    /*
    条件变量：对消费者而言（线程池中的线程），任务队列种可能没有任务，那么就需要继续等下去，等待就需要被唤醒，唤醒就需要条件变量
            对于生产者而言当前，当前任务队列肯定是有容量上限的，满的时候也需等待。
        注意：为什么需要等待？ 不等待怎么办？线程不去执行任务了？生产者不去制造任务了？
     */

    //队列满时：生产者的条件变量
    private Condition fullWaitSet = lock.newCondition();
    //队列为空时：消费者的条件变量
    private Condition emptyWaitSet = lock.newCondition();
    //必须是有界限的队列，要不然怎么阻塞，一直创建下去吗，违背初衷
    private int capcity;
    //阻塞队列的容量初始化
    public BlockingQueue(int capcity) {
        this.capcity = capcity;
    }

    /*
     带超时的阻塞获取
     */
    public T poll(long timeout, TimeUnit unit){
        lock.lock();
        try{
            long nanos = unit.toNanos(timeout);
            while (queue.isEmpty()){
                try {
                    /*
                     这里会不会出现，我们之前说的虚假唤醒的问题？
                     有人说会，但是我觉得，不会，因为signal唤醒是精准唤醒
                     */
                    //todo：虚假唤醒问题
                    //这里就相当于条件 emptyWaitSet 接管了，最后看测试
                    if (nanos <= 0){
                        return null;
                    }
                    nanos = emptyWaitSet.awaitNanos(nanos);  //它和 Object的wait方法不一样，那个我们之前解决虚假唤醒问题时，时间是自己计算的剩余时间，而该方法该方法直接返回f剩余时间

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            T t = queue.removeFirst();
            fullWaitSet.signal();
            return t;
        }
        finally {
            lock.unlock();
        }
        //todo
    }

    /*
    队列当然需要取出和添加：操作的的对象是任务，需要考虑加锁情况
     */
    public T take(){
        lock.lock();
        try {
            System.out.println();
            //任务队列为空时，需要等待
            while (queue.isEmpty()){                //while循环：阻塞获取的必要条件，区别于if一次性。
                try {
                    emptyWaitSet.await();            //
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            T t = queue.removeFirst();
            //取出一个元素后，需要唤醒生产者线程继续放入
            fullWaitSet.signal();
            return t;
        }
        finally {
            lock.unlock();
        }

    }
    //取出
    void put(T element){
        lock.lock();
        try{
            //阻塞放入，
            while (queue.size() == capcity){
                try {
                    System.out.println("等待加入队列中{}"+element);
                    fullWaitSet.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("加入任务队列{}"+ element);
            queue.addLast(element);             //放到最后
            //放入一个线程需要通知 消费者线程继续获取任务
            emptyWaitSet.signal();
        }
        finally {
            lock.unlock();
        }


    }
    public int size(){
        lock.lock();
        try {
            return queue.size();
        }finally {
            lock.unlock();
        }
    }

}
