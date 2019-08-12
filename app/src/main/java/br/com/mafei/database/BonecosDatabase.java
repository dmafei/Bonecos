package br.com.mafei.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import br.com.mafei.dao.Room_BonecosDAO;
import br.com.mafei.modelo.Bonecos;

import static br.com.mafei.constantes.constantes.TABELA_BONECOS;


@Database(entities = {Bonecos.class}, version = 1, exportSchema = false)
public abstract class BonecosDatabase extends RoomDatabase {

    public abstract Room_BonecosDAO getRoomBonecosDAO();

    public static BonecosDatabase getInstance(Context context) {
        return Room.databaseBuilder(context, BonecosDatabase.class, TABELA_BONECOS)
                .allowMainThreadQueries()
                .build();
    }

}
