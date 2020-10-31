package com.uniso.lpdm.cafeteria_cap7;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class CategoriaBebidaActivity extends AppCompatActivity {

    /*Declaramos as variavies para armazenar o cursor e o banco de dados para leitura aqui
    * porque eles vão precisar ficar abertos por quanto tempo a aplicação ATIVIDADE existir. Isso
    * porque pode se ter muitos dados obtidos no cursor, e se isso ocorre ele não recupera todos os
    * dados de uma só vez, mas sim em partes conforme o usuário solicitar. Então só vamos poder
    * fechar essas conexões quando a atividade for destruída, lembrando do ciclo de vida da atividade */
    private SQLiteDatabase db;
    private Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria_bebida);

        /*Como a atruibuição do adaptador nesse caso estará dentro do try/catch, é necessário
        * se recuperar o ListView antes*/
        ListView listView = findViewById(R.id.lista_bebidas);

        /*O ListView é uma classe que é filha de AdapterView. Por isso podemos utilizar o método que
        * vimos na MainActivity para configurar o listener de clicar em um item. Outro exemplo de
        * uma classe que também é um adapterView é o spinner, que vimos lá no primeiro conteúdo
        * com o app de sugestão de cervejas. Os AdapterViews são elementos que interagem com adaptadores.
        * Para exibir dados em um ListView (ou em outro AdapterView) provenientes de uma fonte
        * que não seja um recurso em strings.xml, como um array em uma classe java ou um banco de dados,
        * será necessário o uso de um adaptador. O adaptador serve como uma ponte entre a fonte
        * de dados e o ListView.
        *
        * Aula passada utilizamos um adaptador para utilizar dados de um Array/vetor. Agora, vamos
        * fazer isso utilizando os dados do banco de dados. Para isso, vamos precisar de um adaptador
        * que consiga interagir com os registros de um banco de dados, e consequentemente que consiga
        * interagir com um cursor. Existem algumas opções de adaptadores para fazer isso, mas para nosso
        * caso o SimpleCursorAdapter será suficiente. Para saber mais sobre esse cursor e sua árvore
        * de herança, é possível consultar a documentação: https://developer.android.com/reference/android/widget/SimpleCursorAdapter
        *
        *
        * Aqui, iremos precisar da conexão com o banco de dados, de uma instancia para leitura de
        * banco de dados, a query e o cursor para os registros. As explicações são como as que estão
        * na BebidaActivity, com a diferença de que o que temos a query deverá retornar todos os registros
        * então não teremos os atributos com a seleção e com os valores para subitituição.*/

        SQLiteOpenHelper databaseHelper = new DatabaseHelper(this);

        /*Novamente precisamos de um try/catch pois podemos ter problemas SEMPRE que vamos tentar
        * trabalhar com o banco de dados.*/
        try {
            db = databaseHelper.getReadableDatabase();
            /* Caso queiramos ou precisemos fazer uma consulta SQL diretamente, podemos utilizar o método rawQuery.
            cursor = db.rawQuery("SELECT _id, nome from bebida", null, null);*/
            cursor = db.query(
                  "bebida",
                  new String[] {"_id", "nome"},
                    //Como queremos todos os registros, todos os parametros que filtram dados são nulos,
                    // Se quiermos podemos ordenar os dados.
                  null, null, null, null, "nome ASC"
            );

            /*Uma vez criado o cursor, pordemos criar o adaptador que será relacionado com ele.
            * O adaptador que utilizaremos é o SimpleCursor Adapter. Os dois primeiros parametros são
            * como o do ArrayAdapter, o contexto, e o layout onde se encontra os a forma como os
            * itens do ListView serão apresentados (aqui estamos utilizando um já disponivel pelo
            * android, mas como conversamos, pode ser customizado. Caso seja necessário utilizar
            * itens que não sejam TextViews para exibir os itens, muito provalmente será necessário
            * criar um adaptador customizado. */
            SimpleCursorAdapter listAdapter = new SimpleCursorAdapter(this,
                    android.R.layout.simple_list_item_1,
                    cursor, // Aqui passamos o cursor com os dados que serão associados ao adaptdador
                    new String[] {"nome"}, // Aqui listamos os campos que serão apresentados no ListView. Nosso cursor seleciona dois campos, mas vamos exibir um só
                    new int[] {android.R.id.text1}, // Aqui listamos as views, os elementos visuais especificamente, que serão associados com cadea
                                                    // campo que deverá ser exibido. Ou seja, no segundo parametro, colocamos o arquivo de layout, aqui colocamos que textoviews
                                                    // naquele layout será utilizado. Também estamos utilizando um defult do android.
                    0 //Algumas flags que podem afetar o comportamento do elemento. Na verdade não precisamos passar esse parametro, mas o construtor sem ele está depreciado.
            );

            /*Aqui, como o ArrayAdapter, fazemos a conexão entre o adaptador e o ListView*/
            listView.setAdapter(listAdapter);
        }catch(SQLiteException e){
            /*Mesmas observações que foram feitas na classe BebidaActivity*/
            Toast toast = Toast.makeText(this, "Banco indisponivel", Toast.LENGTH_SHORT);
            Log.d("Erro de banco de dados", e.getMessage());
            toast.show();
        }


        /*Aqui temos o tratamento de eventos como conversado na aula sobre Listeners*/
        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int posicao, long id) {
                Intent intent = new Intent(CategoriaBebidaActivity.this, BebidaActivity.class);
                intent.putExtra(BebidaActivity.EXTRA_BEBIDA_ID, (int) id);
                startActivity(intent);
            }
        };

        listView.setOnItemClickListener(itemClickListener);
    }

    @Override
    /*Como não podemos fechar o cursor e o banco até que a atividade seja destruída, fazemos isso
    * onDestroy*/
    protected void onDestroy() {
        super.onDestroy();
        cursor.close();
        db.close();
    }
}