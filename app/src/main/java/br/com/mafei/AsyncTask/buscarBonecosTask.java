package br.com.mafei.AsyncTask;

import android.os.AsyncTask;

import java.util.List;

import br.com.mafei.adapter.lista_adapter_recycler;
import br.com.mafei.dao.Room_BonecosDAO;
import br.com.mafei.modelo.Bonecos;

public class buscarBonecosTask extends AsyncTask {
    private final Room_BonecosDAO bonecosDAO;
    private lista_adapter_recycler adapter;

    public buscarBonecosTask(Room_BonecosDAO bonecosDAO, lista_adapter_recycler adapter) {

        this.bonecosDAO = bonecosDAO;
        this.adapter = adapter;
    }

    @Override
    protected Object doInBackground(Object[] objects) {
        return bonecosDAO.todos();
    }

    @Override
    protected void onPostExecute(Object todosBonecos) {
        super.onPostExecute(todosBonecos);
        adapter.atualizar((List<Bonecos>) todosBonecos);
    }
}
