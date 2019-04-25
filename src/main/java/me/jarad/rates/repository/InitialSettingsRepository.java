package me.jarad.rates.repository;

import me.jarad.rates.model.InitialSettingsEntity;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Vitalii Yarmolenko (yarmol@gmail.com) on 24.04.19.
 */
@Repository
public interface InitialSettingsRepository extends PagingAndSortingRepository<InitialSettingsEntity,Long> {

}
