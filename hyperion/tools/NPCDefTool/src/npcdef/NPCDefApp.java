/*
 * NPCDefApp.java
 */

package npcdef;

import npcdef.resources.NPCDefinition;
import npcdef.resources.NPCXML;
import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;

/**
 * The main class of the application.
 */
public class NPCDefApp extends SingleFrameApplication {

    /**
     * At startup create and show the main frame of the application.
     */
    @Override protected void startup() {
        System.out.println("Loading File...");
        //NPCXML.loadDefinitions();
        NPCXML.loadBinary();
        System.out.println("Loading Cache Definitions.");
        NPCDefinition.loadNPCDefinition();
        show(new NPCDefView(this));
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override protected void configureWindow(java.awt.Window root) {
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of NPCDefApp
     */
    public static NPCDefApp getApplication() {
        return Application.getInstance(NPCDefApp.class);
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        launch(NPCDefApp.class, args);
    }
}
