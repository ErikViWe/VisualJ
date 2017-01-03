package edu.kit.ipd.sdq.visualj.util;

import org.junit.Test;

public class DialogTest {
    
    /**
     * Opens some dialog windows using {@link DialogUtils}.
     */
    @Test
    public void test() {
        DialogUtils.showExceptionDialog(null, new NullPointerException("<Message>"),
                "Ein schwerwiegender Fehler ist aufgetreten. Bitte melden Sie diesen Fehler an das VisualJ-Team:");
        DialogUtils.showExceptionDialog(null, new NullPointerException("<Message>"), "Error occured",
                "Ein schwerwiegender Fehler ist aufgetreten. Bitte melden Sie diesen Fehler an das VisualJ-Team:");
    }
    
}
