package weatherapp.danarcheronline.com.weatherapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceScreen;

import weatherapp.danarcheronline.com.weatherapp.Utils.PreferenceUtils;

public class SettingsFragment extends PreferenceFragmentCompat implements
        SharedPreferences.OnSharedPreferenceChangeListener {

//    tag for debugging purposes
    private static final String TAG = SettingsFragment.class.getSimpleName();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        register the preference changed listener
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }


    @Override
    public void onCreatePreferences(Bundle bundle, String s) {

//        load the preferences defined an a preference screen xml file
        addPreferencesFromResource(R.xml.settings_fragment_preferences);

        PreferenceScreen preferenceScreen = getPreferenceScreen();
        SharedPreferences sharedPreferences = preferenceScreen.getSharedPreferences();

//        get the number of preferences in shared preferences
        int preferencesCount = preferenceScreen.getPreferenceCount();

//        loop through all preferences and set their summaries
        for(int i = 0; i < preferencesCount; i++) {
            Preference preference = preferenceScreen.getPreference(i);
            PreferenceUtils.setPreferenceSummary(sharedPreferences, preference);
        }
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String preferenceKey) {
//        find the preference that was changed
        Preference preference = findPreference(preferenceKey);
//        update the preferences summary
        PreferenceUtils.setPreferenceSummary(sharedPreferences, preference);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //        unregister the preference change listener
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);

    }
}
