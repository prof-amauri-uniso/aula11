package com.uniso.lpdm.cafeteria_cap7;

/*Essa classe será utilizada como auxiliar apenas para exemplificar inicialmente o uso de Adapters
* com o ListView. Na verdade ela será subsitituída pelo banco de dados que criamos.*/
public class Bebida {
    /*Para cada bebida temos o nome dela, a descrição e a sua referencia de imagem*/
    private String nome;
    private String descricao;
    private int imagem;

    /*Aqui criamos um vetor constante para simular os dados armazenados em uma tabela do banco de
    * dados*/
    public static final Bebida[] bebidas = {
            new Bebida("Latte", "Um cafe com leite", R.drawable.latte),
            new Bebida("Cappuccino", "Um Cappuccino", R.drawable.cappuccino),
            new Bebida("Filtrado", "Um cafe filtrado", R.drawable.filtrado)
    };

    /*Criamos um construtor para simplificar a criação das novas bebidas*/
    private Bebida(String nome, String descricao, int imagem){
        this.nome = nome;
        this.descricao = descricao;
        this.imagem = imagem;
    }

    /*Criamos os getters para podermos recurperar as informações de cada bebida*/
    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public int getImagem() {
        return imagem;
    }

    @Override
    public String toString() {
        return nome;
    }
}
