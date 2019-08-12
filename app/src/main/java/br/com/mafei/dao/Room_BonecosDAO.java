package br.com.mafei.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import br.com.mafei.modelo.Bonecos;

@Dao
public interface Room_BonecosDAO {

    @Insert
    void salvar(Bonecos bonecos);

    @Update
    void alterar(Bonecos bonecos);

    @Delete
    void remover(Bonecos bonecos);

    @Query("Select * from bonecos")
    List<Bonecos> todos();

}