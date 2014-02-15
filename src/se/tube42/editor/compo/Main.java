package se.tube42.editor.compo;

import se.tube42.editor.compo.data.*;
import se.tube42.editor.compo.service.*;

public class Main
{
    public static void main(String [] args)
    {
        // DEBUG
        if(args.length == 0) {
            Database.regions.add("base");
            Database.regions.add("menu_area");
        }
        
        MainWindow mw = new MainWindow();        
        
        for(String arg : args)
            ServiceProvider.load(arg);
        
        mw.everythingChanged();
            
    }
}
