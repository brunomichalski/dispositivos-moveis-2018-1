package com.exemple.android.buscadorgithub

import android.content.SharedPreferences
import android.os.Bundle
import android.support.v7.preference.ListPreference
import android.support.v7.preference.PreferenceFragmentCompat
import android.support.v7.preference.Preference

class ConfiguracaoFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {


    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.pref_github)

        for (i in 0 until preferenceScreen.preferenceCount){
            val preference = preferenceScreen.getPreference(i)
            atualizarPreferenceSummary(preference)
        }
     }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        val preference = findPreference(key)
        if (preference != null){
            atualizarPreferenceSummary(preference)
        }
    }

    fun atualizarPreferenceSummary(preference: Preference){
        if (preference is ListPreference){
            val corSelecionada = preferenceScreen.sharedPreferences.getString(preference.key,"")

            val indexSelecionado = preference.findIndexOfValue(corSelecionada)
            val tituloSelecionado = preference.entries[indexSelecionado]
            preference.summary = tituloSelecionado
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }
}