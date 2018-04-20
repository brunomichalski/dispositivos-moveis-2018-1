package com.exemple.android.exemetas

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var lv: ListView? = null
    var listaMeta = ArrayList<Metas>()
    var posicao = 0
    var operacao = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        criarListview()

        exibirMenuAcoes()
    }

    //cria o menu com as opções atualizar e inserir
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    //açoes de atualizar e inserir metas
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.acao_inserir) {
            operacao = 1
            inserir(listaMeta.size)
            Toast.makeText(this, "Inseriu uma meta!", Toast.LENGTH_LONG).show()
        }
        if (item?.itemId == R.id.acao_atualizar) {
            inserir(posicao)
            Toast.makeText(this, "Atualizou uma meta!", Toast.LENGTH_LONG).show()
        }
        return super.onOptionsItemSelected(item)
    }

    //ações que aparecerão quando clicar nas metas ja criadas
    override fun onContextItemSelected(item: MenuItem?): Boolean {



        val menuList = item?.getMenuInfo() as AdapterView.AdapterContextMenuInfo
        posicao = menuList.position

        if (item?.itemId == R.id.acao_remover) {
            excluir()
        }
        if (item?.itemId == R.id.acao_modificar) {
            metaSelecionada()
        }
        if (item?.itemId == R.id.acao_finalizar) {
            finalizar()
        }
        return super.onContextItemSelected(item)
    }

    //cria a listview
    fun criarListview() {
        lv = findViewById(R.id.lv_lista)
    }

    //inseri uma meta a listView
    fun inserir(numero: Int) {

        val meta = Metas()
        meta.titulo = titulo.text.toString()
        meta.descricao = descricao.text.toString()
        meta.data = data.text.toString()


        if (operacao == 1) {
            listaMeta.add(numero, meta)
            operacao  = 0
        } else {
            listaMeta[numero] = meta
        }

        exibirLista()
        limparCampos()
    }

    //exclui uma meta
    fun excluir() {
        listaMeta.removeAt(posicao)
        exibirLista()
    }

    // informa uma que a meta esta finalizada
    fun finalizar() {
        listaMeta[posicao].situacao = "Finalizado"
        exibirLista()
    }

    //limpa os campos depois de acontecer operação
    fun limparCampos() {
        titulo.setText("")
        descricao.setText("")
        data.setText("")
    }

    //seleciona a meta pra voltar aos campos edittext
    fun metaSelecionada() {
        titulo.append(listaMeta[posicao].titulo)
        descricao.append(listaMeta[posicao].descricao)
        data.append(listaMeta[posicao].data)
    }

    //exibe o list view
    fun exibirLista() {
        lv?.setAdapter(ArrayAdapter<Metas>(this, android.R.layout.simple_list_item_1, listaMeta))
    }

    //exibe as açoes
    fun exibirMenuAcoes() {
        lv?.setOnCreateContextMenuListener({ contextMenu, view, contextMenuInfo ->
            menuInflater.inflate(R.menu.menu_acoes, contextMenu)
            contextMenu.setHeaderTitle("Selecione uma opção")
        })
    }
}
