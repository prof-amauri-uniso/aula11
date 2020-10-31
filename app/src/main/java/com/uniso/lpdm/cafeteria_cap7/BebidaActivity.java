package com.uniso.lpdm.cafeteria_cap7;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/*Na última aula utilizamos uma classe auxiliar para exemplificar o uso de adaptadores.
* Dessa forma, passsavamos para a classe BebidaActivity na intenção o id do item no
* vetor da classe auxiliar, para podemormos popular os itens do layout da BebidaActivity.
* A interação entre as classes ainda será esse, a CategoriaBebidaActivity ainda irá
* passar um id de bebida para a BebidaActivity, mas agora a BebidaActivity, ao invés
* de utilizar a classe auxiliar para obter os dados irá utilizar o banco de dados
* que criamos. */
public class BebidaActivity extends AppCompatActivity {

    public static final String EXTRA_BEBIDA_ID = "bebida_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bebida);

        /*A intereação entre as atividades CategoriaBebidaActivity e BebidaActivity ainda é
        * o mesmo, por isso a parte de comunicação entre elas com a intenção e os extras da intenção
        * ainda são os mesmos. O que muda é que não precisamos mais da referencia para a classe
        * auxiliar Bebida, que será subistituida pelo banco de dados.*/
        int id_bebida = (Integer) getIntent().getExtras().get(EXTRA_BEBIDA_ID);

        /*Inicialmente temos que obter uma referencia para o banco de dados. Para isso precisamos
        * do nosso SQLiteOpenHelper (que para nós é a classe  DatabaseHelper) que cuida de toda a
        * parte de interação com o banco de dados.*/
        SQLiteOpenHelper databaseHelper = new DatabaseHelper(this);

        /*Após termos a referencia para o banco com o SQLiteOpenHelper, vamos precisar obter o
        * banco de dados especificamente. Se essa operação falhar, será lançada uma exceção
        *  SQLiteException. Isso pode ocorrer por exemplo, se você tentar usar uma instancia
        * editavel do banco de dados (com getWritableDatabase, comentado mais abaixo), mas não
        * ser possível escrever no banco pelo fato do disco do dispositivo estar cheio.
        * Para tratar essa execeção/erro, precisamos utilizar a estrutura de tratamento de
        * exceções try/catch
        * Dentro do try colocamos o código "perigoso", que pode conter alguma exceção. No catch
        * colocamos o que fazer se o problema realmente ocorrer. No nosso caso estamos colocando
        * uma mensagem com um Toast e exibindo uma mensagem no console na forma de um log.
        * É possível fazer uma analogia com a estrutura if Se uma falha grave ocorrer
        * nesse trecho de código (try) faça isso especificamente (catch).
        * O uso dessa estrutura permite que nossa aplicação não sofra fechamentos abruptos e
        * inesperados por exemplo, e permite que possamos informar ao usuário o que está acontecendo*/
        try{
            /*Após pegar a referencia para o banco, precisamos recuperar ele especificamente. Podemos
            * recuperar o banco de duas formas: uma instancia que pode ser modificada/escrita (ou seja,
            * para fazer atualizações) ou uma  instancia apenas para leitura de dados. Como aqui
            * vamos apenas recuperar informações para exibir na tela, não precisamos de uma instancia
            * que possa ser modificada.
            * Para se obter uma instancia para ler: getReadableDatabase()
            * Para se obter uma instancia para escrever: getWritableDatabase*/
            SQLiteDatabase db = databaseHelper.getReadableDatabase();


            /*CURSOR
            * Para recuperarmos dados de tabelas no banco de dados precisamos de um cursor. Quando
            * você especifica os dados que devem ser acessados, o cursor retira os registros
            * correspondentes do banco de dados. Em seguida, será possível navegar pelos registros
            * fornecidos pelo cursor.
            *
            * Para criarmos um cursor precisamos usar uma consulta no banco de dados. Essa consulta
            * é criada utilizando o método query do banco de dados que recuperamos com o getReadableDatabase
            * A estrutura de parametros que a query utiliza é bem parecido com a estrutura de um SELECT
            * no banco de dados, isso porque no final essas informações serão convertidas em um SELECT.
            * Parametros na ordem:
            * tabela: uma string com o nome da tabela no banco de dados que estamos recuperando
            * campos: um vetor de strings com os campos que serão recuperados. Se quisermos todos os campos, passmos null
            * seleção: a condição para retornar os dados. Quem campos serão analisados para retornar os dados.
            * utilizamos a sintaxe em uma string, com o nome do campo e uma interrogação, pois o valor que
            * queremos que o campo contenha para ser retornado é definido no proximo parametro.
            * valores para comparação: que valores queremos que os campos do atributo anterios assumam
            * para serem retornados. Eles serão retornados subistiuidos nas interrogações do parametro
            * anterior e na ordem em que forem inseridos, portanto cuidado.
            * agrupamento: quando precisamos agrupar os dados retornados de acordo com algum atributo especifico.
            * Maioria das vezes utilizarmos null.
            * having: uma variação do where do sql, onde podemos utilizar funções agregadas. A clausula where é
            * onde especificamos as condições para recuperar nossos dados. Aqui na queru, ela é composta pelos parametros
            * com a seleção e com os valores de comparação. Normalmente utilizamos null para esse parametro
            * ****Para mais informações vocês podem dar uma olhada em SELECT, WHERE, HAVING e funções agregadas em SQL.
            * ordenação: os resultados normalmente são recuperados de acordo com a sequencia que foram
            * inseridos. Se queremos eles em uma ordem crescente ou decrescente por algum campo ou campos especificos,
            * especificamos aqui. Precisamos especificar o campo e se a ordenação será crescente ou decrescente. Para
            * isso colocamos o nome do campo e as palavras ASC (de ascedente) para ordenar em ordem crescente ou
            * DESC (de descendente) para ordenem em ordem decrescente. Por exemplo, queremos que os dados sejam
            * retornados do menor id para o maior, ou vice versa. Ou ordenar por datas mais recentes, etc. Como
            * aqui teremos apenas um registro de resposta não faz sentido, então passamos como null
            * */
            Cursor cursor = db.query("BEBIDA",
                    new String[] {"nome", "descricao", "imagem_resource_id"},
                    "_id = ?",
                    new String[] {Integer.toString(id_bebida)},
                    null, null, null);

            /*Agora que temos o cursor com o dado desejado, podemos utiliza-lo para percorrermos os dados.
            * Para obter um dos registros é preciso de movimentar o cursor até o registro. Para fazermos
            * isso e navegar pelos registros em um cursos temos 4 métodos principais:
            * movoToFirst - irá para o primeiro registro recuperado. Aqui, como teremos apenas um,
            * ele será suficiente para trabalharmos com os dados do registro.
            * moveToLast - irá para o último registro recuperado.
            * moveToNext - avança para o próximo registro em relação ao atual que ele estiver apontando.
            * moveToPrevious - avança para o registro anterior em relação ao atual que ele estiver apontando.
            *
            * Existe a possibilidade de a consulta feito com o cursor não trazer resultado algum que atenda
            * os parametros passados. Caso isso aconteça, o método retorna false. Assim podemos tratar a
            * existencia ou não de dados com um condicional.
            * */
            if(cursor.moveToFirst()){
                /*Quando o crusor aponta para um registro ele contém uma estrutura na forma de um vetor,
                * onde cada item do vetor é um dos campos do registro. A ordem nesse vetor, ou seja,
                * o indice em que cada campo estará dependerá da ordem que que foram listados lá em cima
                * na query. Nesse exemplo então temos no vetor:
                * cursor[0] = o nome.
                * cursor[1] = a descricao.
                * cursor[2] = o resource_id da imagem associada a bebida.
                *
                * Precisamos saber a ordem, pois precisamos saber o tipo de dado que deverá ser recuperado,
                * pois temos um método especifico para recuperar os tipos de dados diferentes. Os dois
                * primeiros campos do exemplo são strings, o último é um inteiro.
                * */
                String nomeString = cursor.getString(0);
                String descricaoString = cursor.getString(1);
                int fotoId = cursor.getInt(2);

                /*Depois dos dados recuperados a atribuição aos elementos visuais é como já
                * estamos acostumados, e bastante similiar a quando estavamos utilizando
                * a classe Bebida auxiliar.*/
                TextView nome = (TextView) findViewById(R.id.nome);
                nome.setText(nomeString);

                TextView descricao = (TextView) findViewById(R.id.descricao);
                descricao.setText(descricaoString);

                ImageView foto = (ImageView) findViewById(R.id.foto);
                foto.setImageResource(fotoId);
                foto.setContentDescription(nomeString);
            }

            /*Uma vez terminado de trabalhar com um cursor, precisamos fecha-lo para liberar os recursos
            * que são utilizados pelo sistema para mante-lo*/
            cursor.close();
            /*O mesmo vale para o banco, ao terminar de usar, fechar a conexão.*/
            db.close();
        }catch (SQLiteException e){
            /*Caso tenhamos um problema ao conectar com o banco, será lançada uma exceção, e
            * para informar o usuário que ocorreu um problema, criamos um Toast com uma mensagem.*/
            Toast toast = Toast.makeText(this, "Banco indisponivel", Toast.LENGTH_SHORT);
            /*O objeto Log nos permite integragir com o console de execução da aplicação, assim podemos
            * colocar mensagens de log. Quando uma excessão é lançada, o sistema, no caso o android,
            * passa como parametro um objeto da exceção, que contém uma série de informações sobre o erro.
            * Esse objeto possui o método getMessage, que pode ser util para quem for verificar
            * o que aconteceu. Mas essa mensagem é bastante técnica e especifica geralmente, então
            * não é interessante mostrarmos para o usuário, então geramos um log */
            Log.d("Erro de banco de dados", e.getMessage());
            toast.show();
        }

    }
}