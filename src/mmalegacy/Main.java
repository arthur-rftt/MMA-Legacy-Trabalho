package mmalegacy;

import mmalegacy.view.TelaJogo;
import javax.swing.SwingUtilities;

public final class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaJogo().setVisible(true));
    }
}
