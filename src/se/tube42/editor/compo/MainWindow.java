package se.tube42.editor.compo;

import java.awt.*;
import java.awt.event.*;

import se.tube42.editor.compo.data.*;
import se.tube42.editor.compo.ui.*;

public class MainWindow extends Frame
{
    private EditWindow ew;
    private FormatPanel fp;
    private RegionPanel rp;
    private PropertyPanel pp;
    private MainMenuBar mb;

    public MainWindow()
    {
        super("[Composition]");

        setLayout(new GridLayout(1, 3));
        add( fp = new FormatPanel(this));
        add( rp = new RegionPanel(this));
        add( pp = new PropertyPanel(this));

        setMenuBar( mb = new MainMenuBar(this));

        // Frame stuff
        final WindowAdapter wc = new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                on_close();
            }
        };

        setSize(800, 600);
        setVisible(true);
        setLocation(0, 0);
        addWindowListener(wc);

        ew = new EditWindow(this);
        ew.setLocation(getWidth(), 0);

        everythingChanged(); // force a complete update
    }

    // -----------------------------------
    public void everythingChanged()
    {
        if(fp != null) fp.dataChanged();
        formatChanged();
    }

    public void formatChanged()
    {
        if(rp != null) rp.dataChanged();
        if(pp != null) pp.formatChanged();
        if(ew != null) ew.formatChanged();
        regionChanged();
    }

    public void regionChanged()
    {
        if(pp != null) pp.regionChanged();
        if(mb != null) mb.stateChanged();

        setTitle(
                 "[Composition    " +
                 (Database.current_format == null ? "-" : Database.current_format.name)
                 + "    " +
                 (Database.current_region == null ? "-" : Database.current_region)
                 + "]");
        propertyChanged();
    }

    public void propertyChanged()
    {
        if(pp != null) pp.propertyChanged();
        if(ew != null) ew.regionChanged();
    }

    public void displayChanged()
    {
        if(ew != null) ew.displayChanged();
        propertyChanged();
    }

    // -----------------------------------
    private void on_close()
    {
        System.exit(0);
    }
}
