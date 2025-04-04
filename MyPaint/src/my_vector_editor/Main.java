package my_vector_editor;

import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import java.awt.BorderLayout;

public class Main extends JFrame {

    public Main() {
        super("Vector editor");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);

        VectorEditorPanel editorPanel = new VectorEditorPanel();
        EditorControlPanel controlPanel = new EditorControlPanel(editorPanel);

        setLayout(new BorderLayout());
        add(editorPanel, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::new);
    }
}

