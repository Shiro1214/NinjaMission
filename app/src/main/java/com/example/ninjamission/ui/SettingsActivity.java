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
import com.google.android.gms.ads.AdView;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class SettingsActivity extends AppCompatActivity {
    private static final String MATH_LEVEL = "MATH_LEVEL";
    private static final String DIFFICULTY_MODE = "DIFFICULTY_MODE";
    private static final String LEVEL = "LEVEL";
    private static final String BACKGROUND_MUSIC = "BACKGROUND_MUSIC";
    private static final String WRONG_COUNT = "WRONG_COUNT";
    private static final String IN_COUNT = "IN_COUNT";
    private static final String RAPID_FIRE = "RAPID_FIRE";
    private static final String GAME_DURATION = "GAME_DURATION";
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
            var difficulty = setUpListPrefs(getExtStr(R.string.GhostSpeed),getResources().getStringArray(R.array.mode_entries),modeValues,modeValues[1],DIFFICULTY_MODE);

            String[] fireValues = {"1","2","3"};
            ListPreference rapidFire = setUpListPrefs(getExtStr(R.string.ShootLimits),getResources().getStringArray(R.array.rapidFire_entries),fireValues,fireValues[0],RAPID_FIRE);

            String[] mathValues = {"10","100","1000"};
            var mathLevel = new ListPreference(context);
            mathLevel.setTitle(getExtStr(R.string.MathLevel));
            mathLevel.setEntries(R.array.math_entries);
            mathLevel.setEntryValues(mathValues);
            mathLevel.setDefaultValue("10");
            mathLevel.setKey(MATH_LEVEL);

            String[] failValues = {"5","10","15"};
            var wrongCounts = new ListPreference(context);
            wrongCounts.setTitle(getExtStr(R.string.WrongLimit));
            wrongCounts.setEntries(R.array.wrong_entries);
            wrongCounts.setEntryValues(failValues);
            wrongCounts.setDefaultValue("5");
            wrongCounts.setKey(WRONG_COUNT);

            var inCounts = new ListPreference(context);
            inCounts.setTitle(getExtStr(R.string.InLimit));
            inCounts.setEntries(R.array.wrong_entries);
            inCounts.setEntryValues(failValues);
            inCounts.setDefaultValue("5");
            inCounts.setKey(IN_COUNT);

            String[] timerValues = {"180","120","60"};
            var timer = new ListPreference(context);
            timer.setTitle(getExtStr(R.string.SetGameDuration));
            timer.setEntryValues(timerValues);
            timer.setEntries(timerValues);
            timer.setDefaultValue("180");
            timer.setKey(GAME_DURATION);


            String[] levels = new String[level];
            for (int i = 1; i <= level; i++) {
                levels[i-1] = i+"";
            }
            var levelPrefs = new ListPreference(context);
            levelPrefs.setTitle(getExtStr(R.string.SelectLevel));
            levelPrefs.setEntryValues(levels);
            levelPrefs.setEntries(levels);
            levelPrefs.setDefaultValue(""+level);
            levelPrefs.setKey(LEVEL);

            CheckBoxPreference bgMusic = setUpBoxPrefs(getExtStr(R.string.background_music),BACKGROUND_MUSIC);

            setPreferenceScreen(screen);
            screen.addPreference(difficulty);
            screen.addPreference(rapidFire);
            screen.addPreference(inCounts);
            screen.addPreference(wrongCounts);
            screen.addPreference(mathLevel);
            screen.addPreference(levelPrefs);
            screen.addPreference(bgMusic);
            screen.addPreference(timer);
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
        public static int  wrongCountLimit(Context c){
            var tmp = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(c).getString(WRONG_COUNT,"5"));
            return tmp;
        }
        public static int  inCountLimit(Context c){
            var tmp = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(c).getString(IN_COUNT,"5"));
            return tmp;
        }
        public static int  rapidLimit(Context c){
            var tmp = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(c).getString(RAPID_FIRE,"1"));
            return tmp;
        }
        public static int  mathLevel(Context c){
            var tmp = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(c).getString(MATH_LEVEL,"10"));
            return tmp;
        }
        public static int  getGameDuration(Context c){
            var tmp = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(c).getString(GAME_DURATION,"180"));
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


    }
}