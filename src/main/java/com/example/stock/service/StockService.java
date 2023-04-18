package com.example.stock.service;

import com.example.stock.domain.Stock;
import com.example.stock.repository.StockRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StockService {

    private StockRepository stockRepository;

    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    /**
     * synchronized를 붙이게 되면
     * 하나의 스레드만 접근이 가능하게됨!
     * ->Race Condition 사전 방지 가능
     * But, @Transactional 어노테이션의 특성상 db에 변경 값이 반영되기 전에
     * decrease 메소드를 다시 실행하게 된다면 db에 반영 전의 값이 쓰임
     * -> 근본적인 해결책이 될 수 없음
     */
//    @Transactional
    public synchronized void decrease(Long id, Long quantity) {
        final Stock stock = stockRepository.findById(id).orElseThrow();
        stock.decrease(quantity);
        stockRepository.saveAndFlush(stock);
    }



}
