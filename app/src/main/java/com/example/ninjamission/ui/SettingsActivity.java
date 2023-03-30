package com.example.ninjamission.ui;

import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.CheckBoxPreference;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;

import com.example.ninjamission.R;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class SettingsActivity extends AppCompatActivity {
    private static final String SOUND_FX = "SOUND_FX";
    private static final String MATH_LEVEL = "MATH_LEVEL";
    private static final String DIFFICULTY_MODE = "DIFFICULTY_MODE";
    private static final String RAPID_FIRE_DEPTHCHARGE = "RAPID_FIRE_DEPTHCHARGE";
    private static final String SUB_MOVE_HORIZON = "PLANES_MOVE_HORIZON";
    private static final String PLANES_MOVE_HORIZON = "PLANES_MOVE_HORIZON";
    private static final String FRUGALITY_MODE = "FRUGALITY_MODE";
    private static final String BATTLESHIP_MOVE = "BATTLESHIP_MOVE";
    private static final String NUM_PLANES = "NUM_PLANES";
    private static final String LEVEL = "LEVEL";
    private static final String AVERAGE_PLANE_SPEED = "AVERAGE_PLANE_SPEED";
    private static final String AVERAGE_SUB_SPEED = "AVERAGE_SUB_SPEED";
    private static final String PLANE_DIRECTION = "PLANE_DIRECTION";
    private static final String SUB_DIRECTION = "SUB_DIRECTION";
    private static final String DARK_MODE = "DARK_MODE";
    private static final String VIBRANT_MODE = "VIBRANT_MODE";
    private static final String BACKGROUND_MUSIC = "BACKGROUND_MUSIC";
    private static final String COUNT_DOWN = "COUNT_DOWN";
    private static Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        static int level;
        private void getLevel() {
            try(Scanner s = new Scanner(getContext().openFileInput("level.txt"));) {
                level = s.nextInt();
                //s.close();
            } catch (FileNotFoundException e) {
                level = 1;//new install
            }

            if (level > 10 ) level = 10;
        }
        @Override
        public void onCreatePreferences(Bundle b, String s) {
            context = getPreferenceManager().getContext();
            PreferenceScreen screen = getPreferenceManager().createPreferenceScreen(context);
            getLevel();



            //TODO add preference widgets here

            String[] modeValues = {"0.02f","0.01f","0.005f"};
            var difficulty = setUpListPrefs("Difficulty",getResources().getStringArray(R.array.mode_entries),modeValues,modeValues[1],DIFFICULTY_MODE);

            String[] mathValues = {"10","100","1000"};
            var mathLevel = new ListPreference(context);
            mathLevel.setTitle("Math Level");
            mathLevel.setEntries(R.array.math_entries);
            mathLevel.setEntryValues(mathValues);
            mathLevel.setDefaultValue("10");
            mathLevel.setKey(MATH_LEVEL);

            String[] timerValues = {"180","120","60"};
            var timer = new ListPreference(context);
            timer.setTitle("Set Game Duration");
            timer.setEntryValues(timerValues);

            String[] levels = new String[level];
            for (int i = 1; i <= level; i++) {
                levels[i-1] = i+"";
            }
            var levelPrefs = new ListPreference(context);
            levelPrefs.setTitle("Select level");
            levelPrefs.setEntryValues(levels);
            levelPrefs.setEntries(levels);
            levelPrefs.setDefaultValue(""+level);
            levelPrefs.setKey(LEVEL);

            CheckBoxPreference bgMusic = setUpBoxPrefs("Background Music",BACKGROUND_MUSIC);

            setPreferenceScreen(screen);
            screen.addPreference(difficulty);
            screen.addPreference(mathLevel);
            screen.addPreference(levelPrefs);
            screen.addPreference(bgMusic);
        }

        //Difficulty
        public static float getDifficulty(Context c){
            var tmp = PreferenceManager.getDefaultSharedPreferences(c).getString(DIFFICULTY_MODE,"0.01f");
            return Float.parseFloat(tmp);
        }

        public static int getLevelPref(Context c){
            var tmp = PreferenceManager.getDefaultSharedPreferences(c).getString(LEVEL,""+level);
            return Integer.parseInt(tmp);
        }

        public static int  mathLevel(Context c){
            var tmp = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(c).getString(MATH_LEVEL,"10"));
            return tmp;
        }
        public static boolean getBgMusicPrefs(Context c){
            return PreferenceManager.getDefaultSharedPreferences(c).getBoolean(BACKGROUND_MUSIC,true);
        }



        private String getExtStr(int id){
            return getResources().getString(id);
        }
        private String[]getArrPrefs(int id){ return getResources().getStringArray(id);}

        private SwitchPreference setUpSwitchPrefs(String title, String key){
            var a = new SwitchPreference(context);
            a.setTitle(title);
            a.setSummaryOn(getExtStr(R.string.ON));
            a.setSummaryOff(getExtStr(R.string.OFF));
            a.setKey(key);
            return a;
        }
        private CheckBoxPreference setUpBoxPrefs(String title, String key){
            var a = new CheckBoxPreference(context);
            a.setTitle(title);
            a.setSummaryOn(getExtStr(R.string.ON));
            a.setSummaryOff(getExtStr(R.string.OFF));
            a.setKey(key);
            return a;
        }

        private ListPreference setUpListPrefs(String title, String[] entries, String[] entryValues, String defaultValue, String key){
            ListPreference a = new ListPreference(context);
            a.setTitle(title);
            a.setEntries(entries);
            a.setEntryValues(entryValues);
            a.setDefaultValue(defaultValue);
            a.setKey(key);
            return a;
        }


        public static String  numPlanesSet(Context c){
            return PreferenceManager.getDefaultSharedPreferences(c).getString(NUM_PLANES,"3");
        }

    }
}