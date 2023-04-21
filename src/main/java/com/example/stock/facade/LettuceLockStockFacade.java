package com.example.stock.facade;

import com.example.stock.repository.RedisLockRepository;
import com.example.stock.service.StockService;
import org.springframework.stereotype.Component;

@Component
public class LettuceLockStockFacade {

    private RedisLockRepository redisLockRepository;

    private StockService stockService;

    public LettuceLockStockFacade(RedisLockRepository redisLockRepository, StockService stockService) {
        this.redisLockRepository = redisLockRepository;
        this.stockService = stockService;
    }

    public void decrease(Long key, Long quantity) throws InterruptedException {

        // lock 획득 시도
        while (!redisLockRepository.lock(key)) {
            // 실패시 100 ms 슬립으로 redis 에 갈 수 있는 부하를 줄여줌
            Thread.sleep(100);
        }

        try {
            stockService.decrease(key, quantity);
        } finally {
            redisLockRepository.unlock(key);
        }

    }


}
