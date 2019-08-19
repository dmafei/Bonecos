package br.com.mafei.bonecos;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import br.com.mafei.dao.Room_BonecosDAO;
import br.com.mafei.database.BonecosDatabase;
import br.com.mafei.firebase.PersistenciaFirebase;
import br.com.mafei.modelo.Bonecos;
import de.hdodenhof.circleimageview.CircleImageView;

import static br.com.mafei.constantes.constantes.chaveAcao;
import static br.com.mafei.constantes.constantes.chaveBoneco;
import static br.com.mafei.constantes.constantes.cst_editar;
import static br.com.mafei.constantes.constantes.cst_inserir;
import static br.com.mafei.constantes.constantes.tituloBarEdit;
import static br.com.mafei.constantes.constantes.tituloBarInserir;

public class cadastro extends AppCompatActivity {

    private static final int TIRA_FOTO = 0;
    private Room_BonecosDAO bonecosDAO;
    private PersistenciaFirebase firebase;
    private Bonecos bonecos;

    private String acao;

    // campos da tabela
    private EditText txtNome;
    private Spinner comboMarca;
    private EditText txtModelo;
    private EditText txtFilme;
    private CircleImageView imgFoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        // sqlite
        BonecosDatabase database = BonecosDatabase.getInstance(this);
        bonecosDAO = database.getRoomBonecosDAO();

         firebase = new PersistenciaFirebase();

        atribuiComponentes();
        extrairParametrosIntent();

        imgFoto.setOnClickListener(v -> tirarFoto());

    }

    private void tirarFoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, TIRA_FOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TIRA_FOTO && resultCode == RESULT_OK) {
            Bundle extras = Objects.requireNonNull(data).getExtras();
            Bitmap imageBitmap = (Bitmap) Objects.requireNonNull(extras).get("data");
            imgFoto.setImageBitmap(imageBitmap);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_salvar, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_Salvar) {
            finalizarFormulario();
        }
        return super.onOptionsItemSelected(item);
    }

    private void extrairParametrosIntent() {

        // Extraindo variavies da Activity anterior
        Intent dados = getIntent();
        if (dados.hasExtra(chaveBoneco)) {
            setTitle(tituloBarEdit);
            bonecos = (Bonecos) dados.getSerializableExtra(chaveBoneco);
            preencherCampos();

        } else {
            setTitle(tituloBarInserir);
            bonecos = new Bonecos();
        }

        if (dados.hasExtra(chaveAcao)) {
            acao = dados.getStringExtra(chaveAcao);
        }
    }

    private void finalizarFormulario() {

        if (camposObrigatorio()) {
            preencherBonecos();
            if (acao.equals(cst_editar)) {
                alterarBonecos(bonecos);

            } else if (acao.equals(cst_inserir)) {
                salvarBonecos(bonecos);
            }
            finish();
        }
        txtNome.setFocusable(true);
    }

    private void salvarBonecos(Bonecos bonecos) {
        // primeiro persistir no Firebase para obter a chave
        String chave = firebase.salvarBonecosFirebase(bonecos);
        bonecos.setChave(chave); // atribuindo a chave para persistir no room
        bonecosDAO.salvar(bonecos);
    }

    private void alterarBonecos(Bonecos bonecos) {
        bonecosDAO.alterar(bonecos);
        firebase.alterarFirebase(bonecos);
    }

    private boolean camposObrigatorio() {

        if ((txtNome.getText().toString().isEmpty()) || (txtFilme.getText().toString().isEmpty()) || (txtModelo.getText().toString().isEmpty()))
        {
            Toast.makeText(cadastro.this, "Campo Obrigatório", Toast.LENGTH_SHORT).show();
            return false;

        }
        else {
            return true;
        }

    }

    private void atribuiComponentes() {

        txtNome = findViewById(R.id.Nome);
        comboMarca = findViewById(R.id.comboMarca);
        txtModelo = findViewById(R.id.Modelo);
        txtFilme = findViewById(R.id.Filme);

        imgFoto = findViewById(R.id.imgFoto);

        // Retornar as marcas no Spinner (comboBox)
        retornarMarcas();

    }

    private void retornarMarcas() {
        String[] lsMarca = getResources().getStringArray(R.array.listarMarca);
        comboMarca.setAdapter(new ArrayAdapter<>(cadastro.this, R.layout.support_simple_spinner_dropdown_item, lsMarca));
    }

    private void preencherCampos() {
        txtNome.setText(bonecos.getNomeBoneco());
        txtModelo.setText(bonecos.getNomeModelo());
        txtFilme.setText(bonecos.getNomeFilme());

        selecionarMarcas();

        //imgFoto.setImageBitmap(bonecos.getFoto());
    }

    // laço para verificar o registro do banco e identificar qual posicao esta no spinner
    // encontrando a posicao irá mostrar o conteudo
    private void selecionarMarcas() {

        for (int i = 0; i < comboMarca.getCount(); i++) {

            if (comboMarca.getItemAtPosition(i).equals(bonecos.getNomeMarca())) {
                comboMarca.setSelection(i);
            }
        }
    }

    private void preencherBonecos() {
        bonecos.setNomeBoneco(txtNome.getText().toString());
        bonecos.setNomeMarca(comboMarca.getSelectedItem().toString());
        bonecos.setNomeModelo(txtModelo.getText().toString());
        bonecos.setNomeFilme(txtFilme.getText().toString());

        //bonecos.setFoto(imgFoto);
    }
}