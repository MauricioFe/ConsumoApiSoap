package com.example.consumoapisoap;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.consumoapisoap.models.Produto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
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
                new AlertDialog.Builder(context).setTitle("Exclusão de usuários")
                        .setMessage("Você realmente deseja realizar a exclusão?")
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteProduto(getItemId(position), position);
                            }
                        }).setNegativeButton("Não", null)
                        .show();
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

    private void deleteProduto(long id, int position) {
        String params = "id=" + id;
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                OutputStreamWriter writer;
                try {
                    URL url = new URL(MainActivity.BASE_URL + "/Delete");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("contetnt-type", "application/x-www-form-urlencoded");
                    conn.setDoOutput(true);
                    writer = new OutputStreamWriter(conn.getOutputStream());
                    writer.write(params);
                    writer.flush();
                    new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    produtoList.remove(position);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        notifyDataSetChanged();
    }
}
