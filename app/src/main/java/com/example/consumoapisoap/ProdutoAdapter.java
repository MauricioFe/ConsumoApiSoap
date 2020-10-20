package com.example.consumoapisoap;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.consumoapisoap.models.Produto;

import java.util.List;

public class ProdutoAdapter extends BaseAdapter {
    Context context;
    private List<Produto> produtoList;

    public ProdutoAdapter(Context context, List<Produto> produtoList) {
        this.context = context;
        this.produtoList = produtoList;
    }

    @Override
    public int getCount() {
        return produtoList.size();
    }

    @Override
    public Object getItem(int position) {
        return produtoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return produtoList.get(position).getId();
    }
    public void updateProdutos(List<Produto> produtos) {
        produtoList = produtos;
        notifyDataSetChanged();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_list_produto, parent, false);

        TextView txtNome = convertView.findViewById(R.id.item_list_txt_nome);
        TextView txtPreco = convertView.findViewById(R.id.item_list_txt_preco);
        ImageView imgDelete = convertView.findViewById(R.id.item_list_btn_delete);
        ImageView imgEdit = convertView.findViewById(R.id.item_list_btn_edit);

        Produto produto = (Produto) getItem(position);
        txtNome.setText(produto.getNome());
        txtPreco.setText(String.valueOf(produto.getPreco()));

        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "deletando", Toast.LENGTH_SHORT).show();
            }
        });
        imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CadastrarProdutoActivity.class);
                intent.putExtra("produto", produto);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        return convertView;
    }
}
