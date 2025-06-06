public class Vector3D {
    public final double x, y, z;

    public Vector3D(double x, double y, double z) {
        this.x = x;  this.y = y;  this.z = z;
    }

    public Vector3D add(Vector3D v) {
        return new Vector3D(x + v.x, y + v.y, z + v.z);
    }

    public Vector3D subtract(Vector3D v) {
        return new Vector3D(x - v.x, y - v.y, z - v.z);
    }

    public Vector3D scale(double k) {
        return new Vector3D(x * k, y * k, z * k);
    }

    public double dot(Vector3D v) {
        return x * v.x + y * v.y + z * v.z;
    }

    public double length() {
        return Math.sqrt(this.dot(this));
    }

    public Vector3D normalize() {
        double len = length();
        return (len == 0) ? this : scale(1.0 / len);
    }
}
