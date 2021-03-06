package com.exemple.android.clima


import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.LoaderManager
import android.support.v4.content.AsyncTaskLoader
import android.support.v4.content.Loader
import android.support.v7.widget.LinearLayoutManager
import android.test.TouchUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View

import android.text.format.DateFormat
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL

import java.util.*

class MainActivity : AppCompatActivity(),PrevisaoAdapter.PrevisaoItemClickListener, LoaderManager.LoaderCallbacks<Array<String?>?> {

    companion object {
        val DADOS_PREVISAO_LOADER = 1000
    }

    var previsaoAdapter: PrevisaoAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        previsaoAdapter = PrevisaoAdapter(null, this)
        val layoutManager = LinearLayoutManager(this)
        rv_clima.layoutManager = layoutManager
        rv_clima.adapter = previsaoAdapter
        supportLoaderManager.initLoader(DADOS_PREVISAO_LOADER, null, this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<Array<String?>?> {

        val loader = object : AsyncTaskLoader<Array<String?>?>(this) {
            var dadosPrevisao: Array<String?>? = null

            override fun onStartLoading() {
                if (dadosPrevisao != null) {
                    deliverResult(dadosPrevisao);

                } else {
                    exibirProgressBar()
                    forceLoad()
                }
            }

            override fun loadInBackground(): Array<String?>? {

                try {
                    val localizacao = ClimaPreferencias
                            .getLocalizacaoSalva(this@MainActivity)
                    val url = NetworkUtils.construirUrl(localizacao)

                    if (url != null) {
                        val resultado = NetworkUtils.obterRespostaDaUrlHttp(url)
                        val dadosClima = JsonUtils
                                .getSimplesStringsDeClimaDoJson(this@MainActivity,
                                        resultado!!)
                        return dadosClima
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
                return null
            }

            override fun deliverResult(data: Array<String?>?) {
                super.deliverResult(data)
                dadosPrevisao = data
            }
        }
        return loader
    }

    override fun onLoadFinished(loader: Loader<Array<String?>?>?,
                                dadosClima: Array<String?>?) {
        previsaoAdapter?.setDadosClima(dadosClima)

        if (dadosClima != null) {
            exibirResultado()
        } else {
            exibirMensagemErro()
        }
    }

    override fun onLoaderReset(loader: Loader<Array<String?>?>?) {
    }

    override fun onItemClick(index: Int) {
        val previsao = previsaoAdapter!!.getDadosClima()!!.get(index)
        val intentDetalhes = Intent(this, DetalhesActivity::class.java)
        intentDetalhes.putExtra(DetalhesActivity.DADOS_PREVISAO, previsao)

        startActivity(intentDetalhes)
    }

    fun abrirMapa() {
        val addressString = "Campo Mourão, Paraná, Brasil"
        val uriGeo = Uri.parse("geo:0,0?q=$addressString")
        val intentMapa = Intent(Intent.ACTION_VIEW)
        intentMapa.data = uriGeo

        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intentMapa)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.clima, menu)
        return true
    }



    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if (item?.itemId === R.id.acao_atualizar) {

            supportLoaderManager.restartLoader(DADOS_PREVISAO_LOADER, null, this)

            return true

        }

        if (item?.itemId === R.id.acao_mapa) {

            abrirMapa()

            return true

        }

        return super.onOptionsItemSelected(item)

    }



    fun exibirResultado() {

        rv_clima.visibility = View.VISIBLE

        tv_mensagem_erro.visibility = View.INVISIBLE

        pb_aguarde.visibility = View.INVISIBLE

    }



    fun exibirMensagemErro() {

        rv_clima.visibility = View.INVISIBLE

        tv_mensagem_erro.visibility = View.VISIBLE

        pb_aguarde.visibility = View.INVISIBLE

    }



    fun exibirProgressBar() {

        rv_clima.visibility = View.INVISIBLE

        tv_mensagem_erro.visibility = View.INVISIBLE

        pb_aguarde.visibility = View.VISIBLE

    }


}
