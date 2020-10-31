package com.uniso.lpdm.cafeteria_cap7;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

/*Para ler os comentários desse código, o ideal é começar pela BebidaActivity e depois a
* CategoriaBebidaActivity*/
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, //Este é o view clicado, no caso, o list view
                                    View view, // O view que foi clicado, o elemento clicado
                                    int posicao, // a posicao no listview, começando em 0
                                    long id) { // o id da linha

                if(posicao == 0){
                    Intent intent = new Intent(MainActivity.this, CategoriaBebidaActivity.class);
                    startActivity(intent);
                }
            }
        };

        ListView listview = (ListView) findViewById(R.id.lista_opcoes);
        listview.setOnItemClickListener(itemClickListener);
    }


}