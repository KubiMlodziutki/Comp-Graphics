public class Sphere {

    public final Vector3D center;
    public final double radius;
    public final ColorRGB diff;
    public final ColorRGB spec;
    public final ColorRGB emissive;
    public final double shininess;

    public Sphere(Vector3D center, double radius, ColorRGB diff, ColorRGB spec, ColorRGB emissive, double shininess) {
        this.center = center;
        this.radius = radius;
        this.diff = diff;
        this.spec = spec;
        this.emissive = emissive;
        this.shininess = shininess;
    }

    public boolean hitParallel(Vector3D P0, Vector3D d, double[] tHit) {
        Vector3D oc = P0.subtract(center);

        double a = d.dot(d);
        double b = 2.0 * oc.dot(d);
        double c = oc.dot(oc) - radius * radius;

        double disc = b*b - 4*a*c;
        if (disc < 0) return false;

        double sqrtD = Math.sqrt(disc);
        double t = (-b - sqrtD) / (2 * a);
        if (t < 0) t = (-b + sqrtD) / (2 * a);
        if (t < 0) return false;

        tHit[0] = t;
        return true;
    }

    public Vector3D normal(Vector3D p) {
        return p.subtract(center).normalize();
    }
}
