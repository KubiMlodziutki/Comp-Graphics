import java.awt.image.BufferedImage;

public class Main {
    public static void main(String[] commandLineArguments) throws Exception {
        if (commandLineArguments.length == 0) {
            System.err.println("Scene description file missing");
            System.exit(1);
        }
        Scene parsedScene = SceneFileParser.parseSceneDescriptionFile(commandLineArguments[0]);
        BufferedImage renderedImage = Renderer.createRenderedImage(parsedScene);
        ImageDisplayFrame imageDisplayFrame = new ImageDisplayFrame(renderedImage);
        imageDisplayFrame.setVisible(true);
    }
}
