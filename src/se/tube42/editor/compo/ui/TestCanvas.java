package se.tube42.editor.compo.ui;

import java.awt.*;
import java.awt.geom.*;

import se.tube42.lib.compo.*;

public class TestCanvas
extends DBCanvas
{
    private Composition c;
    private AffineTransform at;

    public TestCanvas(Composition c)
    {
        this.c = c;
        this.at = new AffineTransform();
    }

    public void setScale(double scale)
    {
        at.setToIdentity();
        at.scale(scale, scale);
        clearAll();
        repaint();
    }

    public void bufferedPaint(Graphics g)
    {
        final int w = (int)(getWidth() / at.getScaleX());
        final int h = (int)(getHeight() / at.getScaleY());

        // scale and draw board
	Graphics2D g2d = (Graphics2D)g;
	g2d.setTransform(at);


        g.setColor(Color.WHITE);
        g.fillRect(0, 0, w, h);

        if(!c.resize(w, h)) {
            g.setColor(Color.RED);
            g.drawString("No templates match this screen :(", 20, 20);
        }

        g.setColor(Color.BLACK);
        g.drawRect(0, 0, w-1, h-1);
        g.drawString("Dimensions: " + w + "x" + h, 20, 20);
        g.drawString("Template: " + c.getName() + ", scale: " + c.getScale(), 20, 50);


        //
        final int s1 = c.getScale();
        final int w1 = c.getFormatWidth() * s1;
        final int h1 = c.getFormatHeight() * s1;

        g.setColor(Color.GRAY);
        g.drawRect( (w - w1) / 2, (h - h1) / 2, w1, h1);

        //
        final Region [] rs = c.getAll();
        for(int i = 0; i < rs.length; i++) {
            final Region r = rs[i];

            final int x = r.getX();
            final int y = r.getY();
            final int w2 = r.getW();
            final int h2 = r.getH();

            g.setColor( UI.REGION_COLORS[i % UI.REGION_COLORS.length]);

            for(int j = -1; j < 2; j++)
                g.drawRect(x + j, y + j, w2 - j * 2, h2 - j * 2);

            g.drawString(r.getName(), x + 20, y + 20);
        }
    }

}
