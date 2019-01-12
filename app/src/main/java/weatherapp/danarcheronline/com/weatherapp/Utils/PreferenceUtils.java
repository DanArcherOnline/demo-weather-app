package weatherapp.danarcheronline.com.weatherapp.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;

public class PreferenceUtils {

    /**
     * Gets the location string from preferences
     * @param context
     * @return
     */
    public static String getPreferenceLocation(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString("location", "London");
    }


    /**
     * Check whether the units preference is set to imperial or not (metric is default choice)
     * @param context
     * @return
     */
    public static boolean isImperialPreferred(Context context) {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        String imperialString = "imperial";
        boolean imperialIsPrefered;
        if(imperialString.equals(sharedPreferences.getString("units", imperialString))) {
            imperialIsPrefered = true;
        }
        else{
            imperialIsPrefered = false;
        }
        return  imperialIsPrefered;
    }


    /**
     * Take any preference except a checkbox (because a checkbox preferences summary can be set in xml)
     * and sets the summary to its current value.
     * Should be called when the preferences screen is shown and in preference changed listener.
     * @param sharedPreferences
     * @param preference
     */
    public static void setPreferenceSummary(SharedPreferences sharedPreferences, Preference preference) {
//        makes sure the preference exists and is not a checkbox
        if(preference != null && !(preference instanceof CheckBoxPreference)) {

//            gets the string value of what the preference is set to
            String value = sharedPreferences.getString(preference.getKey(),"");

//            checks if the preference is a list preference
            if(preference instanceof ListPreference) {

                ListPreference listPreference = (ListPreference) preference;

//                gets the index of the selected list item from the list items in the list preference
                int listPreferencesValueIndex = listPreference.findIndexOfValue(value);

//                makes sure there is at least one list item in the list preference
                if(listPreferencesValueIndex >= 0) {

//                    gets the label of the selected list item in the list preference
                    String label = (String) listPreference.getEntries()[listPreferencesValueIndex];
                    listPreference.setSummary(label);
                }
            }
//            checks if the preference is an edit text preference
            if(preference instanceof EditTextPreference) {
//                simply sets the summary of the preference to what ever was typed in the edit text preference
                preference.setSummary(value);
            }
        }
    }
}
