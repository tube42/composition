package se.tube42.editor.compo;

import java.awt.*;
import java.awt.event.*;

import se.tube42.editor.compo.ui.*;
import se.tube42.editor.compo.data.*;

public class EditWindow extends Frame
{
    private MainWindow mw;
    private EditCanvas canvas;
    private EditFormat format;

    public EditWindow(MainWindow mw)
    {
        super("[Composition edit]");

        this.mw = mw;

        add(format = new EditFormat(mw), BorderLayout.NORTH);
        // add(canvas = new EditCanvas(mw), BorderLayout.CENTER);


        ScrollPane sp = new ScrollPane();
        sp.add(canvas = new EditCanvas(mw));
        add(sp, BorderLayout.CENTER);

        setSize(500, 700);
        setVisible(true);
    }

    public void update(Graphics g)
    {
        paint(g);
    }

    public void formatChanged()
    {
        displayChanged();
    }

    public void displayChanged()
    {

        canvas.displayChanged();

        // for the scroll pane
        pack();
        pack();
        pack();

        regionChanged();
    }

    public void regionChanged()
    {
        canvas.repaint();
    }
}
