package com.example.currenciesapp.room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Flowable;

@Dao
public interface RoomExchangeRateDao {

    @Query("Select * from exchange_rates")
    Flowable<List<RoomExchangeRate>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<RoomExchangeRate> list);
}
