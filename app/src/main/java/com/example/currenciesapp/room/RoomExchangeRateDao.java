package com.example.currenciesapp.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

@Dao
public interface RoomExchangeRateDao {

    @Query("Select * from exchange_rates")
    Flowable<RoomExchangeRate> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(List<RoomExchangeRate> list);
}
