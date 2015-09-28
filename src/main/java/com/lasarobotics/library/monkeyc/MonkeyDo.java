package com.lasarobotics.library.monkeyc;

import android.content.Context;

import com.lasarobotics.library.util.Constants;
import com.lasarobotics.library.util.Timers;

import java.util.ArrayList;

/**
 * The MonkeyDo library handles executing commands generated by MonkeyC.
 */
public class MonkeyDo {

    private ArrayList<MonkeyData> commands;
    private Timers t;
    private String filename;

    public MonkeyDo(String filename, Context context) {
        commands = MonkeyUtil.readFile(filename, context);
        t = new Timers();
        t.startClock("global");
        this.filename = filename;
    }

    public MonkeyData getNextCommand() {
        long time = t.getClockValue("global");
        if (time < commands.get(0).getTime()) {
            return new MonkeyData(null, null, Constants.MONKEYC_STARTING_CONSTANT);
        }
        for (int i = 0; i < commands.size(); i++) {
            if (i + 1 == commands.size()) {
                return new MonkeyData();
            }
            MonkeyData next = commands.get(i + 1);
            MonkeyData current = commands.get(i);
            if (next.getTime() >= time && time >= current.getTime()) {
                return current;
            }
        }
        return new MonkeyData();
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void onStart() {
        t.resetClock("global");
    }
}