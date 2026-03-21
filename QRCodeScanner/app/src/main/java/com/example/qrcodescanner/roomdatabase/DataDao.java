package com.example.qrcodescanner.roomdatabase;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DataDao {
    @Query("SELECT * FROM data WHERE history_type = :historyType ORDER BY uid DESC")
    List<Data> getByType(int historyType);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Data data);

    @Delete
    void delete(Data data);
}
