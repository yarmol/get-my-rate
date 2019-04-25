package me.jarad.rates.repository;

import me.jarad.rates.model.RateEntryEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Created by Vitalii Yarmolenko (yarmol@gmail.com) on 24.04.19.
 */
@Repository
public interface RateEntryRepository extends PagingAndSortingRepository<RateEntryEntity,Long> {

    List<RateEntryEntity> findByDateBetweenOrderByDateAsc(LocalDate dateFrom, LocalDate dateTo);

    Optional<RateEntryEntity> findTop1ByDateLessThanEqualOrderByDateDesc(LocalDate date);

}
