
package com.alarmclock.shake.app.model;





public class ShakeAlarmClock {

    private int id;

    private String time = "";

    private String name = "";

    private boolean isRepeat, isVibrate, isOpen;

    private int repeat, vibrate, open;

    private String ringName, ringUri;

    private boolean dayBoolean[] = {
            true, true, true, true, true, true, true, true
    };;

    private int dayInt[] = {
            1, 1, 1, 1, 1, 1, 1, 1
    };

    private String dayString = "11111111";

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRepeat() {
        return isRepeat;
    }

    public void setRepeat(boolean isRepeat) {
        this.isRepeat = isRepeat;
        this.repeat = getInt(isRepeat);
    }

    public boolean isVibrate() {
        return isVibrate;
    }

    public void setVibrate(boolean isVibrate) {
        this.isVibrate = isVibrate;
        this.vibrate = getInt(isVibrate);
    }

    public int getRepeat() {
        return repeat;
    }

    public void setRepeat(int repeat) {
        this.repeat = repeat;
        this.isRepeat = getBoolean(repeat);
    }

    public int getVibrate() {
        return vibrate;
    }

    public void setVibrate(int vibrate) {
        this.vibrate = vibrate;
        this.isVibrate = getBoolean(vibrate);
    }

    public boolean[] getDayBoolean() {
        return dayBoolean;
    }

    public void setDayBoolean(boolean[] dayBoolean) {
        this.dayBoolean = dayBoolean;
        for (int i = 0; i < dayBoolean.length; i++) {
            this.dayInt[i] = getInt(dayBoolean[i]);
        }
    }

    public int[] getDayInt() {
        return dayInt;
    }

    public void setDayInt(int[] dayInt) {
        this.dayInt = dayInt;
        for (int i = 0; i < dayInt.length; i++) {
            this.dayBoolean[i] = getBoolean(dayInt[i]);
        }
    }

    public void setDayInt(int i, int index) {
        this.dayInt[index] = i;
        this.dayBoolean[index] = getBoolean(i);
        String temp = "";
        for (int j = 0; j < dayInt.length; j++) {
            temp += String.valueOf(dayInt[j]);
        }
        this.dayString = temp;
    }

    public void setDayBoolean(boolean b, int index) {
        this.dayBoolean[index] = b;
        this.dayInt[index] = getInt(b);
        String temp = "";
        for (int j = 0; j < dayInt.length; j++) {
            temp += String.valueOf(dayInt[j]);
        }
        this.dayString = temp;
    }

    public String getDayString() {
        return dayString;
    }

    public void setDayString(String dayString) {
        this.dayString = dayString;
        for (int i = 0; i < dayString.length(); i++) {
            this.dayInt[i] = Integer.valueOf(String.valueOf(dayString.charAt(i))).intValue();
            this.dayBoolean[i] = getBoolean(this.dayInt[i]);
        }
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean isOpen) {
        this.isOpen = isOpen;
        this.open = getInt(isOpen);
    }

    public int getOpen() {
        return open;
    }

    public void setOpen(int open) {
        this.open = open;
        this.isOpen = getBoolean(open);
    }

    public String getRingName() {
        return ringName;
    }

    public void setRingName(String ringName) {
        this.ringName = ringName;
    }

    public String getRingUri() {
        return ringUri;
    }

    public void setRingUri(String ringUri) {
        this.ringUri = ringUri;
    }

    private boolean getBoolean(int i) {
        return i == 0 ? false : true;
    }

    private int getInt(boolean b) {
        return b ? 1 : 0;
    }




}
