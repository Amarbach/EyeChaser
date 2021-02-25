/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package components.net;

/**
 *
 * @author Artur
 */
public class Message {

    private boolean ready;
    private int x;
    private int y;
    private long ping;
    private boolean ended;
    private int won;
    private boolean start;
    private int width;
    private int height;
    private int roundsToPlay;
    private int numberOfTargets;
    private boolean isCorrect;
    private String map;
    private boolean focused;
    private int crateHP;
    private int catchCooldown;

    public Message(){
        ready = false;
        x = 0;
        y = 0;
        ping = Long.MAX_VALUE;
        ended = false;
        won = 0;
        start = false;
        width = 0;
        height = 0;
        isCorrect = true;
        map = "null";
        focused = false;
    }

    public Message(String netMsg, long ping){
        String[] splitMessage = netMsg.split(":");
        if (splitMessage.length >= 15) {
            this.ping = ping;
            int numReady = Integer.parseInt(splitMessage[0]);
            x = Integer.parseInt(splitMessage[1]);
            y = Integer.parseInt(splitMessage[2]);
            int numFocused = Integer.parseInt(splitMessage[3]);
            int numEnded = Integer.parseInt(splitMessage[4]);
            won = Integer.parseInt(splitMessage[5]);
            int numStart = Integer.parseInt(splitMessage[6]);
            width = Integer.parseInt(splitMessage[7]);
            height = Integer.parseInt(splitMessage[8]);
            roundsToPlay = Integer.parseInt(splitMessage[9]);
            numberOfTargets = Integer.parseInt(splitMessage[10]);
            crateHP = Integer.parseInt(splitMessage[11]);
            catchCooldown = Integer.parseInt(splitMessage[12]);
            map = splitMessage[13];
            focused = numFocused >= 1;
            ready = numReady >= 1;
            ended = numEnded >= 1;
            start = numStart >= 1;
            isCorrect = true;
        }else{
            isCorrect = false;
        }
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public long getPing() {
        return ping;
    }

    public void setPing(long ping) {
        this.ping = ping;
    }

    public boolean isEnded() {
        return ended;
    }

    public void setEnded(boolean ended) {
        this.ended = ended;
    }

    public int getWon() {
        return won;
    }

    public void setWon(int won) {
        this.won = won;
    }

    public boolean isStart() {
        return start;
    }

    public void setStart(boolean start) {
        this.start = start;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public boolean isFocused() {
        return focused;
    }

    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    public int getCrateHP() {
        return crateHP;
    }

    public void setCrateHP(int crateHP) {
        this.crateHP = crateHP;
    }

    public int getCatchCooldown() {
        return catchCooldown;
    }

    public void setCatchCooldown(int catchCooldown) {
        this.catchCooldown = catchCooldown;
    }

    @Override
    public String toString(){
        if (isCorrect) {
            String ret = "";
            if (ready) {
                ret += "1:";
            } else {
                ret += "0:";
            }
            ret += Integer.toString(x);
            ret += ":";
            ret += Integer.toString(y);
            ret += ":";
            if(focused){
                ret += "1:";
            } else {
                ret += "0:";
            }
            if (ended) {
                ret += "1:";
            } else {
                ret += "0:";
            }
            ret += Integer.toString(won);
            ret += ":";
            if (start) {
                ret += "1:";
            } else {
                ret += "0:";
            }
            ret += Integer.toString(width);
            ret += ":";
            ret += Integer.toString(height);
            ret += ":";
            ret += Integer.toString(roundsToPlay);
            ret += ":";
            ret += Integer.toString(numberOfTargets);
            ret += ":";
            ret += Integer.toString(crateHP);
            ret += ":";
            ret += Integer.toString(catchCooldown);
            ret += ":";
            ret += map;
            ret += ":";
            ret += Long.toHexString(System.currentTimeMillis());
            return ret;
        }
        return "";
    }

    public int getRoundsToPlay() {
        return roundsToPlay;
    }

    public void setRoundsToPlay(int roundsToPlay) {
        this.roundsToPlay = roundsToPlay;
    }

    public int getMinutesToPlay() {
        return numberOfTargets;
    }

    public void setNumberOfTargets(int value) {
        this.numberOfTargets = value;
    }

    public String getMap() {
        return map;
    }

    public void setMap(String map) {
        this.map = map;
    }
}
