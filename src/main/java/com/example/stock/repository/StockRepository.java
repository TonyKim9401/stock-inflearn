package com.example.stock.repository;

import com.example.stock.domain.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.LockModeType;

public interface StockRepository extends JpaRepository<Stock, Long> {

    /**
     * Pessimistic Lock
     * 충돌이 자주 일어난다면 Optimistic Lock 보다 성능이 좋을 수 있음
     * Lock을 통해 업데이트를 제어하기 때문에 데이터 정합성이 어느정도 보장됨
     *
     * 별도로 Lock을 걸기 떄문에 성능상 감소가 발생 할 수 있음
     * */
    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query("select s from Stock s where s.id = :id")
    Stock findByIdWithPessimisticLock(Long id);


    @Lock(value = LockModeType.OPTIMISTIC)
    @Query("select s from Stock s where s.id = :id")
    Stock findByIdWithOptimisticLock(Long id);

}
