package com.exemple.android.clima
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class  PrevisaoAdapter :  RecyclerView.Adapter<PrevisaoAdapter.PrevisaoViewHolder>   {

    var dadosClima : Array<String?>?



    constructor(dadosClima : Array<String?>?){
        this.dadosClima = dadosClima




    }

     inner class PrevisaoViewHolder : RecyclerView.ViewHolder{

        var tvDadosPrevisao : TextView?

         constructor(itemView: View?): super(itemView){
             tvDadosPrevisao = itemView?.findViewById(R.id.tv_dados_previsao)
         }

    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): PrevisaoViewHolder {
        val previsaoListItem = LayoutInflater.from(parent?.context).inflate(R.layout.previsao_lista_item, parent, false)
        val previsaoViewHolder = PrevisaoViewHolder(previsaoListItem)
        return previsaoViewHolder
    }

    override fun onBindViewHolder(holder: PrevisaoViewHolder?, position: Int) {

        val posicao = dadosClima?.get(position)
        holder?.tvDadosPrevisao?.text = posicao.toString()
    }

    override fun getItemCount(): Int {
            if (dadosClima != null){
            return dadosClima!!.size
            }else{
                return 0
            }
    }

    fun setDadosdoClima(dados: Array<String?>?){
        dadosClima = dados
        notifyDataSetChanged()
    }


}