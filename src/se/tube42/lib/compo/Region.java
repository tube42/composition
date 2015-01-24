
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

        final int tot = horiz ? w : h;
        final int gap = Math.min(maxgap,
                  (tot - size * count) / (count - 1));

        return (horiz ? x : y) + (tot - size * count -  gap * (count-1) ) / 2 + index * (size + gap);
    }

    public int center(boolean horiz, int size)
    {
        return horiz ?
              (x + (w - size) / 2) :
              (y + (h - size) / 2);
    }

    public int position(boolean horiz, boolean start, int size, int gap)
    {
        if(horiz) {
            return x + (start ? gap : (w - size - gap));
        } else {
            return y + (start ? gap : (h - size - gap));
        }
    }
}
