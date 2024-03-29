package br.com.mafei.firebase;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

import br.com.mafei.modelo.Bonecos;

import static br.com.mafei.constantes.constantes.TABELA_FIREBASE;

public class PersistenciaFirebase {

    private DatabaseReference dbBonecos;

    public void alterarFirebase(Bonecos bonecosEditar) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference();
        DatabaseReference usersRef = ref.child(TABELA_FIREBASE);
        DatabaseReference hopperRef = usersRef.child(bonecosEditar.getChave());
        Map<String, Object> hopperUpdates = new HashMap<>();
        hopperUpdates.put("nomeBoneco", bonecosEditar.getNomeBoneco());
        hopperUpdates.put("nomeFilme", bonecosEditar.getNomeFilme());
        hopperUpdates.put("nomeMarca", bonecosEditar.getNomeMarca());
        hopperUpdates.put("nomeModelo", bonecosEditar.getNomeModelo());
        hopperUpdates.put("chave", bonecosEditar.getChave());
        hopperUpdates.put("codigo", bonecosEditar.getCodigo());
        hopperRef.updateChildren(hopperUpdates);
    }

    public void excluirBonecosFirebase(Bonecos bonecosExcluir) {
        String key = bonecosExcluir.getChave();
        dbBonecos = FirebaseDatabase.getInstance().getReference(TABELA_FIREBASE).child(key);
        dbBonecos.removeValue();
    }

    public String salvarBonecosFirebase(Bonecos bonecosInserir) {
        dbBonecos = FirebaseDatabase.getInstance().getReference(TABELA_FIREBASE);
        String id = dbBonecos.push().getKey();
        if (id == null) throw new AssertionError();
        bonecosInserir.setChave(id); // add a chave do registro
        dbBonecos.child(id).setValue(bonecosInserir);
        return id;
    }
}