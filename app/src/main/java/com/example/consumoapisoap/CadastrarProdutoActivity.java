package com.example.consumoapisoap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.accessibility.AccessibilityViewCommand;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.consumoapisoap.models.Produto;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

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
            Toast.makeText(this, "editar", Toast.LENGTH_SHORT).show();
        } else if (intent.hasExtra("idProduto")) {
            int id = intent.getIntExtra("idProduto", 0);
            Toast.makeText(this, "" + id, Toast.LENGTH_SHORT).show();
            Produto produto = new Produto();
            produto.setId(id);
            produto.setNome(edtNome.getText().toString());
            produto.setPreco(Double.parseDouble(edtNome.getText().toString()));
            produto.setEstoque(Integer.parseInt(edtEstoque.getText().toString()));
            produto.setDescricao(edtDescricao.getText().toString());
            btnSalvar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    novoProduto(produto);
                }
            });
        }
    }

    private void novoProduto(Produto produto) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                BufferedReader reader;
                try {
                    URL url = new URL(MainActivity.BASE_URL+"/Post");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-Type", "text/xml");
                    conn.setRequestProperty("charset", "UTF-8");


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}