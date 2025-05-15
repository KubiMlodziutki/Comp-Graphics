public class LightSource {
    public final Vector3D positionVector;
    public final ColorRGB intensityColor;

    public LightSource(Vector3D lightPositionVector, ColorRGB lightIntensityColor) {
        this.positionVector = lightPositionVector;
        this.intensityColor = lightIntensityColor;
    }
}
