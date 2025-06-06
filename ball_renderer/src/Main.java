import java.awt.image.BufferedImage;

public class Main {

    public static void main(String[] commandLineArguments) throws Exception {

        if (commandLineArguments.length == 0) {
            System.err.println("Scene description file missing");
            System.exit(1);
        }

        Scene parsedScene = SceneFileParser.parse(commandLineArguments[0]);
        BufferedImage renderedImage = Renderer.render(parsedScene);

        javax.imageio.ImageIO.write(renderedImage, "png", new java.io.File(parsedScene.outputName));

        ImageDisplayFrame imageDisplayFrame = new ImageDisplayFrame(renderedImage);
        imageDisplayFrame.setVisible(true);
    }
}
