package br.com.mafei.bonecos;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.interpolator.view.animation.FastOutSlowInInterpolator;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import br.com.mafei.adapter.lista_adapter_recycler;
import br.com.mafei.dao.Room_BonecosDAO;
import br.com.mafei.database.BonecosDatabase;
import br.com.mafei.modelo.Bonecos;

import static br.com.mafei.constantes.constantes.chaveAcao;
import static br.com.mafei.constantes.constantes.chaveBoneco;
import static br.com.mafei.constantes.constantes.cst_editar;
import static br.com.mafei.constantes.constantes.cst_inserir;
import static br.com.mafei.constantes.constantes.tituloBar;

public class MainActivity extends AppCompatActivity {

    private Room_BonecosDAO bonecosDAO;
    private lista_adapter_recycler adapter;

    // Classe para unica execucao precisa extender Application

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        configurarDatabase();
        setTitle(tituloBar);
        mostrarLista();
        FloatingActionButton botaoInserir = findViewById(R.id.botaoInserir);
        botaoInserir(botaoInserir);
    }

    private void configurarDatabase() {
        BonecosDatabase database = BonecosDatabase.getInstance(this);
        bonecosDAO = database.getRoomBonecosDAO();
    }

    private void mostrarLista() {
        RecyclerView listaColecao = findViewById(R.id.listaColecao);
        configurarAdapter(listaColecao);
        registerForContextMenu(listaColecao);
    }

    private void configurarAdapter(RecyclerView listaColecao) {

        //new buscarBonecosTask(bonecosDAO, adapter).execute();

        adapter = new lista_adapter_recycler(this,bonecosDAO.todos());
        listaColecao.setAdapter(adapter);

        // Click rapido para alterar
        adapter.setOnItemClickListener((bonecos, posicao) -> alterarBonecos(bonecos));

        // Click longo para excluir
        adapter.setOnItemLongClickListener((bonecos, posicao) -> removerBonecos(bonecos));

    }

    private void removerBonecos(Bonecos bonecosRemover) {
        new AlertDialog
                .Builder(this)
                .setTitle("Remover Boneco")
                .setMessage("Deseja remover este boneco?")
                .setPositiveButton("Sim", (dialog, which) -> {
                    bonecosDAO.remover(bonecosRemover);
                    atualizarBonecos();
                })
                .setNegativeButton("Não", null)
                .show();
    }

    private void alterarBonecos(Bonecos bonecosEditar) {
        Intent telaEditar = new Intent(MainActivity.this, cadastro.class);
        telaEditar.putExtra(chaveBoneco, bonecosEditar);
        telaEditar.putExtra(chaveAcao, cst_editar);
        startActivity(telaEditar);
    }

    private void botaoInserir(FloatingActionButton botaoInserir) {
        botaoInserir.setOnClickListener((View v) -> {
            Intent telaCadastro = new Intent(MainActivity.this, cadastro.class);
            telaCadastro.putExtra(chaveAcao, cst_inserir);
            startActivity(telaCadastro);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        atualizarBonecos();
    }

    private void atualizarBonecos() {
        //new buscarBonecosTask(bonecosDAO, adapter).execute();
        adapter.atualizar(bonecosDAO.todos());
    }
}