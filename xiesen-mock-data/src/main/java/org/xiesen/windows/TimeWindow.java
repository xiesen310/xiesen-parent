package org.xiesen.windows;

public class TimeWindow {
    private final long start;
    private final long end;

    public TimeWindow(long start, long end) {
        this.start = start;
        this.end = end;
    }

    /**
     * Gets the starting timestamp of the window. This is the first timestamp that belongs
     * to this window.
     *
     * @return The starting timestamp of this window.
     */
    public long getStart() {
        return start;
    }

    /**
     * Gets the end timestamp of this window. The end timestamp is exclusive, meaning it
     * is the first timestamp that does not belong to this window any more.
     *
     * @return The exclusive end timestamp of this window.
     */
    public long getEnd() {
        return end;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TimeWindow window = (TimeWindow) o;

        return end == window.end && start == window.start;
    }

    @Override
    public int hashCode() {
        return longToIntWithBitMixing(start + end);
    }

    public static int longToIntWithBitMixing(long in) {
        in = (in ^ in >>> 30) * -4658895280553007687L;
        in = (in ^ in >>> 27) * -7723592293110705685L;
        in ^= in >>> 31;
        return (int) in;
    }

    @Override
    public String toString() {
        return "TimeWindow{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }
}
