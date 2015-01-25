
package se.tube42.lib.compo;

public final class Region
{
    /* package */ int x, y, w, h, flags;
    private String name;

    /* package */ Region(String name)
    {
        this.name = name;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getW() { return w; }
    public int getH() { return h; }

    public int getFlags() { return flags; }

    public String getName() { return name; }


    /* helper functions */

    public int oneOf(boolean horiz, int index, int count,
              int size, int maxgap)
    {
        if(count < 2)
            return center(horiz, size);

        final int gap = computeGap(horiz, count, size, maxgap);
        final int start = computeStart(horiz, count, size, gap);
        return start + index * (size + gap);
    }

    /* center it within this region */
    public int center(boolean horiz, int size)
    {
        return horiz ?
              (x + (w - size) / 2) :
              (y + (h - size) / 2);
    }

    /* position it at one end of this region */
    public int position(boolean horiz, boolean start, int size, int gap)
    {
        if(horiz) {
            return x + (start ? gap : (w - size - gap));
        } else {
            return y + (start ? gap : (h - size - gap));
        }
    }

    /* compute the gap for a series of these objects */
    public int computeGap(boolean horiz, int count, int size, int maxgap)
    {
        int r = ((horiz ? w : h) - size * count) / Math.max(1, count - 1);
        return (maxgap > 0 && r > maxgap) ? maxgap : r;
    }

    /* compute the start for a series of these objects centered here*/
    public int computeStart(boolean horiz, int count, int size, int gap)
    {
        if(horiz) {
            return x + (w - size * count - gap * (count - 1)) / 2;
        } else {
            return y + (h - size * count - gap * (count - 1)) / 2;
        }
    }
}
