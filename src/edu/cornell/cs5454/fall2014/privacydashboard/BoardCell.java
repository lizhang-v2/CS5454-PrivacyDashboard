package edu.cornell.cs5454.fall2014.privacydashboard;

/**
 * Created by zeyuec on 12/9/14.
 */
public class BoardCell implements Comparable {
    public String name;
    public long lastRunning;
    public long total;

    public BoardCell(String n, long t, long l) {
        name = n;
        lastRunning = l;
        total = t;
    }

    @Override
    public int compareTo(Object o) {
        BoardCell other = (BoardCell) o;
        if (total == other.total) {
            return 0;
        } else if (total > other.total) {
            return -1;
        } else {
            return 1;
        }
    }
}
