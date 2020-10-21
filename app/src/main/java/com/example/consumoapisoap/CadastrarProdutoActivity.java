package com.example.consumoapisoap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.accessibility.AccessibilityViewCommand;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.consumoapisoap.models.Produto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class CadastrarProdutoActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_produto);
        EditText edtNome = findViewById(R.id.cadastrar_produto_txv_nome);
        EditText edtPreco = findViewById(R.id.cadastrar_produto_txv_preco);
        EditText edtEstoque = findViewById(R.id.cadastrar_produto_txv_estoque);
        EditText edtDescricao = findViewById(R.id.cadastrar_produto_txv_descricao);
        Button btnSalvar = findViewById(R.id.cadastrar_produto_btn_salvar);

        Intent intent = getIntent();
        if (intent.hasExtra("produto")) {
            Produto produto = (Produto) intent.getSerializableExtra("produto");
            int id = produto.getId();
            edtNome.setText(produto.getNome());
            edtPreco.setText(String.valueOf(produto.getPreco()));
            edtEstoque.setText(String.valueOf(produto.getEstoque()));
            edtDescricao.setText(produto.getDescricao());
            btnSalvar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Produto produto = new Produto();
                    produto.setId(id);
                    produto.setNome(edtNome.getText().toString());
                    produto.setPreco(Double.parseDouble(edtPreco.getText().toString()));
                    produto.setEstoque(Integer.parseInt(edtEstoque.getText().toString()));
                    produto.setDescricao(edtDescricao.getText().toString());
                    editaProduto(produto);
                }
            });
        } else if (intent.hasExtra("idProduto")) {
            int id = intent.getIntExtra("idProduto", 0);
            Toast.makeText(this, "" + id, Toast.LENGTH_SHORT).show();
            btnSalvar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Produto produto = new Produto();
                    produto.setId(id);
                    produto.setNome(edtNome.getText().toString());
                    produto.setPreco(Double.parseDouble(edtPreco.getText().toString()));
                    produto.setEstoque(Integer.parseInt(edtEstoque.getText().toString()));
                    produto.setDescricao(edtDescricao.getText().toString());
                    novoProduto(produto);
                }
            });
        }
    }

    private void editaProduto(Produto produto) {
        String params = "Id=" + produto.getId() + "&Nome=" + produto.getNome() + "&Preco=" + produto.getPreco() +
                "&Estoque=" + produto.getEstoque() + "&Descricao=" + produto.getDescricao();
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                BufferedReader reader;
                OutputStreamWriter writer;
                try {
                    URL url = new URL(MainActivity.BASE_URL+"/Put");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
                    conn.setRequestProperty("charset", "UTF-8");

                    writer = new OutputStreamWriter(conn.getOutputStream());
                    writer.write(params);
                    writer.flush();
                    reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void novoProduto(Produto produto) {
        String params = "Id=" + produto.getId() + "&Nome=" + produto.getNome() + "&Preco=" + produto.getPreco() +
                "&Estoque=" + produto.getEstoque() + "&Descricao=" + produto.getDescricao();
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                BufferedReader reader = null;
                OutputStreamWriter writer = null;
                try {
                    URL url = new URL(MainActivity.BASE_URL + "/Post");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    conn.setRequestProperty("charset", "UTF-8");

                    writer = new OutputStreamWriter(conn.getOutputStream());
                    writer.write(params);
                    writer.flush();
                    String line;
                    reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    Log.i("Code Response", "postDados: " + conn.getResponseCode());

                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}