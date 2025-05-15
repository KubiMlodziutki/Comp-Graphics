import java.io.BufferedReader;
import java.io.FileReader;
import java.util.StringTokenizer;

public class SceneFileParser {
    public static Scene parseSceneDescriptionFile(String sceneDescriptionFilePath) throws Exception {
        Scene parsedScene = new Scene();
        try (BufferedReader fileBufferedReader = new BufferedReader(new FileReader(sceneDescriptionFilePath))) {
            String currentLine;
            int expectedLightSourceCount = 0;
            while ((currentLine = fileBufferedReader.readLine()) != null) {
                currentLine = currentLine.trim();
                if (currentLine.isEmpty()) {
                    continue;
                }
                if (currentLine.startsWith("LIGHT_SOURCES")) {
                    String insideBraces = currentLine.substring(currentLine.indexOf('{') + 1, currentLine.indexOf('}'));
                    expectedLightSourceCount = Integer.parseInt(insideBraces.trim());
                } else if (currentLine.startsWith("POSITION_AND_LIGHT_INTENSITY")) {
                    for (int i = 0; i < expectedLightSourceCount; i++) {
                        String lightDefinitionLine = fileBufferedReader.readLine().trim();
                        StringTokenizer elementTokenizer = new StringTokenizer(lightDefinitionLine, ",");
                        double positionX = Double.parseDouble(elementTokenizer.nextToken());
                        double positionY = Double.parseDouble(elementTokenizer.nextToken());
                        double positionZ = Double.parseDouble(elementTokenizer.nextToken());
                        double redIntensity = Double.parseDouble(elementTokenizer.nextToken());
                        double greenIntensity = Double.parseDouble(elementTokenizer.nextToken());
                        double blueIntensity = Double.parseDouble(elementTokenizer.nextToken());
                        parsedScene.lightSourceList.add(new LightSource(
                                new Vector3D(positionX, positionY, positionZ),
                                new ColorRGB(redIntensity, greenIntensity, blueIntensity)));
                    }
                }
                else if (currentLine.startsWith("SPHERE_SURFACE_PARAMETERS")) {
                    String centerLine = fileBufferedReader.readLine().trim();
                    String radiusLine = fileBufferedReader.readLine().trim();
                    String diffuseLine = fileBufferedReader.readLine().trim();
                    String specularLine = fileBufferedReader.readLine().trim();
                    String shininessLine = fileBufferedReader.readLine().trim();
                    double centerX = Double.parseDouble(centerLine.split(":")[1].split(",")[0]);
                    double centerY = Double.parseDouble(centerLine.split(":")[1].split(",")[1]);
                    double centerZ = Double.parseDouble(centerLine.split(":")[1].split(",")[2]);
                    double radiusValue = Double.parseDouble(radiusLine.split(":")[1]);
                    String[] diffuseComponents = diffuseLine.split(":")[1].split(",");
                    String[] specularComponents = specularLine.split(":")[1].split(",");
                    double diffuseRed = Double.parseDouble(diffuseComponents[0]);
                    double diffuseGreen = Double.parseDouble(diffuseComponents[1]);
                    double diffuseBlue = Double.parseDouble(diffuseComponents[2]);
                    double specularRed = Double.parseDouble(specularComponents[0]);
                    double specularGreen = Double.parseDouble(specularComponents[1]);
                    double specularBlue = Double.parseDouble(specularComponents[2]);
                    double shininessValue = Double.parseDouble(shininessLine.split(":")[1]);
                    parsedScene.sceneSphere = new Sphere(new Vector3D(centerX, centerY, centerZ),
                            radiusValue, new ColorRGB(diffuseRed, diffuseGreen, diffuseBlue),
                            new ColorRGB(specularRed, specularGreen, specularBlue), shininessValue);
                }
                else if (currentLine.startsWith("AMBIENT_LIGHT_INTENSITY")) {
                    String insideBraces = currentLine.substring(currentLine.indexOf('{') + 1, currentLine.indexOf('}'));
                    String[] componentValues = insideBraces.split(",");
                    parsedScene.ambientLightIntensityColor = new ColorRGB(
                            Double.parseDouble(componentValues[0]),
                            Double.parseDouble(componentValues[1]),
                            Double.parseDouble(componentValues[2]));
                }
                else if (currentLine.startsWith("RENDERED_IMAGE_RESOLUTION")) {
                    String insideBraces = currentLine.substring(currentLine.indexOf('{') + 1, currentLine.indexOf('}'));
                    String[] resolutionValues = insideBraces.split(",");
                    parsedScene.renderedImageWidth = Integer.parseInt(resolutionValues[0].trim());
                    parsedScene.renderedImageHeight = Integer.parseInt(resolutionValues[1].trim());
                }
            }
        }
        return parsedScene;
    }
}