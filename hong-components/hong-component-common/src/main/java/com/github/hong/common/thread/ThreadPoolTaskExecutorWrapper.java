package com.github.hong.common.thread;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.ObjectUtils;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池执行器的监控器
 *
 * @author 35533
 * @since 2023/3/20
 */
@Slf4j
public class ThreadPoolTaskExecutorWrapper extends ThreadPoolTaskExecutor {
    /**
     * 打印 多线程信息
     * 在父类的execute、submit等方法里调用，每次有任务被提交到线程池的时候，都会将当前线程池的基本情况打印到日志中
     *
     * @param method 提交任务执行的方法。
     *               submit()：提交任务，返回Future，可以返回结果，可以传入Callable、Runnable。
     *               execute()：提交任务，没有返回值，只可以传入Runnable。
     */
    private void showThreadPoolInfo(String method) {
        ThreadPoolExecutor threadPoolExecutor = getThreadPoolExecutor();
        if (ObjectUtils.isEmpty(threadPoolExecutor)) {
            return;
        }
        log.info("\n\n提交任务前【线程池】情况：\n    {}\n    " +
                        "线程池当前线程数量 = {}\n    " +
                        "当前线程池中正在执行任务的线程数量 = {}\n    " +
                        "队列大小 = {}\n    " +
                        "线程池已执行和未执行的任务总数 = {}\n    " +
                        "已完成的任务数量 = {}\n",
                method,
                threadPoolExecutor.getPoolSize() + 1,
                threadPoolExecutor.getActiveCount() + 1,
                threadPoolExecutor.getQueue().size(),
                threadPoolExecutor.getTaskCount(),
                threadPoolExecutor.getCompletedTaskCount());
    }

    @Override
    public void execute(@NonNull Runnable task) {
        showThreadPoolInfo("do execute 提交任务");
        super.execute(task);
    }

    @Override
    public void execute(@NonNull Runnable task, long startTimeout) {
        showThreadPoolInfo("do execute 提交任务");
        super.execute(task, startTimeout);
    }

    @Override
    public Future<?> submit(@NonNull Runnable task) {
        showThreadPoolInfo("do submit 提交任务");
        return super.submit(task);
    }

    @Override
    public <T> Future<T> submit(@NonNull Callable<T> task) {
        showThreadPoolInfo("do submit 提交任务");
        return super.submit(task);
    }

    @Override
    public ListenableFuture<?> submitListenable(@NonNull Runnable task) {
        showThreadPoolInfo("do submitListenable 提交任务");
        return super.submitListenable(task);
    }

    @Override
    public <T> ListenableFuture<T> submitListenable(@NonNull Callable<T> task) {
        showThreadPoolInfo("do submitListenable 提交任务");
        return super.submitListenable(task);
    }
}
