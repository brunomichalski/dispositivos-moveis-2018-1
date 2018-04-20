package com.exemple.android.exemetas

class Metas {
     var titulo: String = ""
    var descricao: String = ""
    var data: String = ""
    var situacao: String = "Em Andamento"



    override fun toString(): String {
        return "Titulo: $titulo\n" +
                "Descrição: $descricao\n" +
                "DataLimite: $data\n" +
                "Situação: $situacao"
    }
}