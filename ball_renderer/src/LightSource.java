public class LightSource {

    public final Vector3D position;
    public final ColorRGB intensity;
    public final double c0, c1, c2;

    public LightSource(Vector3D position, ColorRGB intensity, double c0, double c1, double c2) {
        this.position = position;
        this.intensity = intensity;
        this.c0 = c0;
        this.c1 = c1;
        this.c2 = c2;
    }

    public double attenuation(double r) {
        return Math.min(1.0 / (c2 * r * r + c1 * r + c0), 1.0);
    }
}
