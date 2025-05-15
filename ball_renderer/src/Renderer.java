import java.awt.image.BufferedImage;

public class Renderer {
    public static BufferedImage createRenderedImage(Scene sceneInstance) {
        int imageWidth = sceneInstance.renderedImageWidth;
        int imageHeight = sceneInstance.renderedImageHeight;
        BufferedImage renderedImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
        double aspectRatio = (double) imageWidth / (double) imageHeight;
        for (int pixelY = 0; pixelY < imageHeight; pixelY++) {
            for (int pixelX = 0; pixelX < imageWidth; pixelX++) {
                double normalizedX = (pixelX + 0.5) / imageWidth;
                double normalizedY = (pixelY + 0.5) / imageHeight;
                double screenSpaceX = (2.0 * normalizedX - 1.0) * aspectRatio;
                double screenSpaceY = 1.0 - 2.0 * normalizedY;
                Vector3D rayOriginVector = new Vector3D(screenSpaceX, screenSpaceY, 5.0);
                Vector3D rayDirectionVector = new Vector3D(0.0, 0.0, -1.0);
                double[] intersectionDistanceArray = new double[1];
                ColorRGB finalPixelColor = new ColorRGB(0.0, 0.0, 0.0);
                if (sceneInstance.sceneSphere.intersectParallelRay(rayOriginVector, rayDirectionVector, intersectionDistanceArray)) {
                    Vector3D intersectionPointVector = rayOriginVector.add(rayDirectionVector.scale(intersectionDistanceArray[0]));
                    Vector3D surfaceNormalVector = sceneInstance.sceneSphere.computeSurfaceNormal(intersectionPointVector);
                    ColorRGB ambientComponentColor = sceneInstance.ambientLightIntensityColor.multiplyByColor(
                            sceneInstance.sceneSphere.diffuseReflectionColor);
                    finalPixelColor = finalPixelColor.add(ambientComponentColor);
                    for (LightSource lightSourceInstance : sceneInstance.lightSourceList) {
                        Vector3D lightDirectionVector = lightSourceInstance.positionVector.subtract(intersectionPointVector).normalize();
                        double diffuseFactor = Math.max(surfaceNormalVector.dot(lightDirectionVector), 0.0);
                        ColorRGB diffuseComponentColor = lightSourceInstance.intensityColor
                                .multiplyByColor(sceneInstance.sceneSphere.diffuseReflectionColor)
                                .multiplyByScalar(diffuseFactor);
                        finalPixelColor = finalPixelColor.add(diffuseComponentColor);
                        Vector3D reflectionDirectionVector = surfaceNormalVector.scale(2.0 * surfaceNormalVector.dot(lightDirectionVector))
                                .subtract(lightDirectionVector).normalize();
                        Vector3D viewDirectionVector = rayDirectionVector.scale(-1.0);
                        double specularFactor = Math.pow(Math.max(viewDirectionVector.dot(reflectionDirectionVector), 0.0),
                                sceneInstance.sceneSphere.shininessCoefficient);
                        ColorRGB specularComponentColor = lightSourceInstance.intensityColor
                                .multiplyByColor(sceneInstance.sceneSphere.specularReflectionColor)
                                .multiplyByScalar(specularFactor);
                        finalPixelColor = finalPixelColor.add(specularComponentColor);
                    }
                }
                renderedImage.setRGB(pixelX, pixelY, finalPixelColor.toARGB());
            }
        }
        return renderedImage;
    }
}
