package br.com.mafei.bonecos;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import br.com.mafei.adapter.lista_adapter_recycler;
import br.com.mafei.dao.Room_BonecosDAO;
import br.com.mafei.database.BonecosDatabase;
import br.com.mafei.firebase.PersistenciaFirebase;
import br.com.mafei.modelo.Bonecos;

import static br.com.mafei.constantes.constantes.TABELA_FIREBASE;
import static br.com.mafei.constantes.constantes.chaveAcao;
import static br.com.mafei.constantes.constantes.chaveBoneco;
import static br.com.mafei.constantes.constantes.cst_editar;
import static br.com.mafei.constantes.constantes.cst_inserir;
import static br.com.mafei.constantes.constantes.tituloBar;

public class MainActivity extends AppCompatActivity {

    private Room_BonecosDAO bonecosDAO;
    private lista_adapter_recycler adapter;
    private PersistenciaFirebase firebase;

    // Classe para unica execucao precisa extender Application

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        configurarFirebase();
        configurarDatabase();
        setTitle(tituloBar);
        mostrarLista();
        FloatingActionButton botaoInserir = findViewById(R.id.botaoInserir);
        botaoInserir(botaoInserir);
    }

    private void configurarFirebase() {
        firebase = new PersistenciaFirebase();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        ref.child(TABELA_FIREBASE).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot single : dataSnapshot.getChildren()) {
                    if (single.exists()) {
                        Bonecos bonecosFirebase = single.getValue(Bonecos.class);
                        assert bonecosFirebase != null;
                        List<Bonecos> existeBoneco = bonecosDAO.findBychave(bonecosFirebase.getChave());

                        if (existeBoneco.size() > 0) {
                            // boneco encontrado no SQLite entao sera atualizado
                            bonecosDAO.alterar(bonecosFirebase);
                        }else {
                            // nao encontrou o boneco no SQLite entao sera inserido
                            bonecosDAO.salvar(bonecosFirebase);
                        }

                    } else {
                        Log.i("MeuLOG", "erro na captura");
                    }
                }
                atualizarBonecos(); // chamada novamente para atualizar os dados vindos do Firebase
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void configurarDatabase() {
        BonecosDatabase database = BonecosDatabase.getInstance(this);
        bonecosDAO = database.getRoomBonecosDAO();
    }

    private void mostrarLista() {
        RecyclerView listaColecao = findViewById(R.id.listaColecao);
        listaColecao.setHasFixedSize(true); // informando que o recyclerview tera tamanho fixo para todos
        configurarAdapter(listaColecao);
        registerForContextMenu(listaColecao);
    }

    private void configurarAdapter(RecyclerView listaColecao) {
        adapter = new lista_adapter_recycler(this, bonecosDAO.todos());
        listaColecao.setAdapter(adapter);

        // Click rapido para alterar
        adapter.setOnItemClickListener(this::alterarBonecos);

        // Click longo para excluir
        adapter.setOnItemLongClickListener(this::removerBonecos);

    }

    private void removerBonecos(Bonecos bonecosRemover) {
        new AlertDialog
                .Builder(this)
                .setTitle("Remover Boneco")
                .setMessage("Deseja remover este boneco?")
                .setPositiveButton("Sim", (dialog, which) -> remover(bonecosRemover))
                .setNegativeButton("Não", null)
                .show();
    }

    private void remover(Bonecos bonecos) {
        bonecosDAO.remover(bonecos);
        firebase.excluirBonecosFirebase(bonecos);
        atualizarBonecos();
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
        adapter.atualizar(bonecosDAO.todos());
    }
}