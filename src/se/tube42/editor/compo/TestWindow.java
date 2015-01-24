package se.tube42.editor.compo;

import java.awt.*;
import java.awt.event.*;

import se.tube42.editor.compo.ui.*;
import se.tube42.lib.compo.*;

public class TestWindow
extends Frame
implements ItemListener
{
    private TestCanvas canvas;
    private Checkbox flipv, fliph;
    private Choice scalet, scale;
    private Composition compo;


    public TestWindow(Composition c)
    {
        super("[Composition test]");

        this.compo = c;

        add(canvas = new TestCanvas(c), BorderLayout.CENTER);

        Panel p = new Panel();
        add(p, BorderLayout.NORTH);
        p.add(fliph = new Checkbox("Flip horizontal"));
        p.add(flipv = new Checkbox("Flip vertical"));
        p.add(new Label("  Scale type"));
        p.add(scalet = new Choice());


        p.add(new Label("  Scale"));
        p.add(scale = new Choice());
        scale.add("1/1");
        scale.add("1/2");
        scale.add("1/4");
        scale.add("1/8");


        fliph.addItemListener(this);
        flipv.addItemListener(this);
        scalet.addItemListener(this);
        scale.addItemListener(this);

        scalet.add("None");
        scalet.add("Power of two");
        scalet.add("Any");
        scalet.select(2);

        // Frame stuff
        final WindowAdapter wc = new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                setVisible(false);
            }
        };

        addWindowListener(wc);
        setSize(500, 500);
        setVisible(true);

        set();
    }

    public void update(Graphics g)
    {
        paint(g);
    }

    public void itemStateChanged(ItemEvent e)
    {
        set();
        canvas.repaint();
    }

    private void set()
    {
        compo.configure(fliph.getState(), flipv.getState(),
                  scalet.getSelectedIndex());
        compo.resize(canvas.getWidth(), canvas.getHeight());

        // Set scale
        double s = Math.pow(2, -scale.getSelectedIndex());
        canvas.setScale(s);
    }
}
