package br.com.mafei.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.mafei.listener.OnItemClickListener;
import br.com.mafei.listener.OnItemLongClickListener;
import br.com.mafei.bonecos.R;
import br.com.mafei.modelo.Bonecos;

public class lista_adapter_recycler extends RecyclerView.Adapter<lista_adapter_recycler.BonecoViewHolder> {

    private final List<Bonecos> listaBonecos;
    private final Context context;
    private OnItemLongClickListener onItemLongClickListener;
    private OnItemClickListener onItemClickListener;

    public lista_adapter_recycler(Context context, List<Bonecos> listaBonecos) {

        this.listaBonecos = listaBonecos;
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    @NonNull
    @Override
    public lista_adapter_recycler.BonecoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View viewCriada = LayoutInflater.from(context).inflate(R.layout.item_bonecos, parent, false);
        return new BonecoViewHolder(viewCriada);
    }

    @Override
    public void onBindViewHolder(@NonNull lista_adapter_recycler.BonecoViewHolder holder, int position) {

        Bonecos bonecos = listaBonecos.get(position);
        holder.vincular(bonecos);
    }

    @Override
    public int getItemCount() {
        return listaBonecos.size();
    }

    public void atualizar(List<Bonecos> todos) {
        this.listaBonecos.clear();
        this.listaBonecos.addAll(todos);
        this.notifyDataSetChanged();
    }

    class BonecoViewHolder extends RecyclerView.ViewHolder {

        private final TextView nomeBoneco;
        private final TextView nomeMarca;
        private final TextView nomeFilme;

        private Bonecos bonecos;

        BonecoViewHolder(View itemView) {
            super(itemView);
            nomeBoneco = itemView.findViewById(R.id.item_Boneco_Nome);
            nomeMarca = itemView.findViewById(R.id.item_Boneco_Marca);
            nomeFilme = itemView.findViewById(R.id.item_Boneco_Filme);

            itemView.setOnClickListener((View v) -> onItemClickListener.onItemClick(bonecos));

            itemView.setOnLongClickListener((View v) -> {
                onItemLongClickListener.onItemLongClick(bonecos);
                return true;
            });
        }

        void vincular(Bonecos bonecosvincular) {
            this.bonecos = bonecosvincular;

            nomeBoneco.setText(bonecosvincular.getNomeBoneco());
            nomeMarca.setText(bonecosvincular.getNomeMarca());
            nomeFilme.setText(bonecosvincular.getNomeFilme());
        }
    }
}