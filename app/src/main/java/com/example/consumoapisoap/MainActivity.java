package com.example.consumoapisoap;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.consumoapisoap.models.Produto;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends AppCompatActivity {

    public static final String BASE_URL = "http://192.168.0.109:50210/ProdutoService.asmx";
    ListView listaProduto;
    FloatingActionButton btnAdicionar;
    List<Produto> produtoList;
    int idProduto = 0;
    ProdutoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listaProduto = findViewById(R.id.list_produtos);
        btnAdicionar = findViewById(R.id.list_produtos_fab_add);
        produtoList = new ArrayList<>();
        getProdutos();
        adapter = new ProdutoAdapter(getApplicationContext(), produtoList);
        listaProduto.setAdapter(adapter);
        for (Produto item : produtoList) {
            idProduto = item.getId();
        }
        btnAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CadastrarProdutoActivity.class);
                idProduto++;
                intent.putExtra("idProduto", idProduto);
                startActivity(intent);
            }
        });
    }

    private void getProdutos() {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                BufferedReader reader;
                try {
                    URL url = new URL(BASE_URL + "/GetProdutos");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Content-type", "text/xml");
                    conn.setDoOutput(true);
                    String line;
                    StringBuilder stringBuilder = new StringBuilder();
                    reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line = reader.readLine()) != null) {
                        stringBuilder.append(line);
                    }
                    adapter.updateProdutos(ParseXml(stringBuilder.toString()));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private List<Produto> ParseXml(String content) {
        List<Produto> produtoList = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document xml = builder.parse(new InputSource(new StringReader(content)));
            NodeList nList = xml.getElementsByTagName("Produtos");
            for (int i = 0; i < nList.getLength(); i++) {
                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    Produto produto = new Produto();
                    produto.setId(Integer.parseInt(element.getElementsByTagName("Id").item(0).getTextContent()));
                    produto.setNome(element.getElementsByTagName("Nome").item(0).getTextContent());
                    produto.setPreco(Double.parseDouble(element.getElementsByTagName("Preco").item(0).getTextContent()));
                    produto.setEstoque(Integer.parseInt(element.getElementsByTagName("Estoque").item(0).getTextContent()));
                    produto.setDescricao(element.getElementsByTagName("Descricao").item(0).getTextContent());
                    produtoList.add(produto);
                }
            }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
        return produtoList;
    }
}