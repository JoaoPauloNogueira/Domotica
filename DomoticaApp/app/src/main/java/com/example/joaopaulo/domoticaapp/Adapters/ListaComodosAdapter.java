package com.example.joaopaulo.domoticaapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.joaopaulo.domoticaapp.data.Comodo;
import com.example.joaopaulo.domoticaapp.PrincipalActivity;
import com.example.joaopaulo.domoticaapp.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListaComodosAdapter extends RecyclerView.Adapter<ListaComodosAdapter.ItemViewHolder> {

    private PrincipalActivity mActivity;
    private List<Comodo> mListaComodos;

    public ListaComodosAdapter(PrincipalActivity inicialActivity, List<Comodo> listaComodos) {
        mActivity = inicialActivity;
        mListaComodos = listaComodos;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comodo, parent, false);
        return new ItemViewHolder(mActivity, view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {

        holder.atualizaInformacoesDoComodo(mListaComodos.get(position), position + 1);
    }

    @Override
    public int getItemCount() {
        return mListaComodos.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.img_cardview)
        ImageView imageViewPoster;
        @BindView(R.id.txt_cardview)
        TextView textViewTitulo;
        @BindView(R.id.txt_cardiview_position)
        TextView textViewPosicao;
        @BindView(R.id.txt_cardiview_data)
        TextView textViewData;

        Comodo comodo;
        private PrincipalActivity activity;

        ItemViewHolder(final PrincipalActivity activity, View view) {
            super(view);
            this.activity = activity;
            ButterKnife.bind(this, view);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.detalhaComodo(comodo);
                }
            });
        }
        private void atualizaInformacoesDoComodo(Comodo comodo, int posicao) {

            /*this.comodo = comodo;
            textViewTitulo.setText(comodo.getTitulo());

            String textoPosicao = posicao + ".";
            String textoData = "";

            if (comodo.getDataAcesso() != null) {

                textoData = "Acesso: " + android.text.format.DateFormat.format("dd/MM/yyyy - HH:mm", comodo.getDataAcesso());

            } else if (comodo.getLancamento() != null) {

                textoData = "Lanc.: " + android.text.format.DateFormat.format("dd/MM/yyyy", comodo.getLancamento());
            }

            textViewPosicao.setText(textoPosicao);
            textViewData.setText(textoData);*/
        }
    }
}

