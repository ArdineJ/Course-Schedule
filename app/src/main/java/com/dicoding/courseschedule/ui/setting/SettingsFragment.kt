package com.dicoding.courseschedule.ui.setting

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.notification.DailyReminder
import com.dicoding.courseschedule.util.NightMode

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        //TODO 10 : Update theme based on value in ListPreference
        val prefTheme = findPreference<ListPreference>(getString(R.string.pref_key_dark))
        prefTheme?.setOnPreferenceChangeListener { _, newValue ->
            val nightMode =
                when((newValue as String).uppercase()){
                    NightMode.ON.name -> NightMode.ON
                    NightMode.OFF.name -> NightMode.OFF
                    else -> NightMode.AUTO
                }
            updateTheme(nightMode.value)
            true
        }

        //TODO 11 : Schedule and cancel notification in DailyReminder based on SwitchPreference
        val prefNotification = findPreference<SwitchPreference>(getString(R.string.pref_key_notify))
        val dailyReminder = DailyReminder()
        prefNotification?.setOnPreferenceChangeListener { _, switchValue ->
            val notficationEnabled :Boolean = switchValue as Boolean
            if (notficationEnabled){
                context?.let { dailyReminder.setDailyReminder(it) }
            } else {
                context?.let { dailyReminder.cancelAlarm(it) }
            }
            true
        }
    }

    private fun updateTheme(nightMode: Int): Boolean {
        AppCompatDelegate.setDefaultNightMode(nightMode)
        requireActivity().recreate()
        return true
    }
}