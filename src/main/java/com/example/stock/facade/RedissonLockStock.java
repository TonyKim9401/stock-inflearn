package com.example.stock.facade;

import com.example.stock.service.StockService;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedissonLockStock {

    private RedissonClient redissonClient;
    private StockService stockService;

    public RedissonLockStock(RedissonClient redissonClient, StockService stockService) {
        this.redissonClient = redissonClient;
        this.stockService = stockService;
    }

    public void decrease(Long key, Long quantity) {

        // key 값으로 lock 객체를 만듦
        final RLock lock = redissonClient.getLock(key.toString());

        try {
            // lock 획득 시도
            final boolean available = lock.tryLock(5, 1, TimeUnit.SECONDS);

            // lock 획득 실패시
            if (!available) {
                System.out.println("lock 획득 실패");
                return;
            }

            // lock 획득시 decrease 로직 실행
            stockService.decrease(key, quantity);

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            // lock 해제
            lock.unlock();
        }

    }
}
