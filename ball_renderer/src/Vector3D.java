public class Vector3D {
    public final double x;
    public final double y;
    public final double z;

    public Vector3D(double xCoordinate, double yCoordinate, double zCoordinate) {
        this.x = xCoordinate;
        this.y = yCoordinate;
        this.z = zCoordinate;
    }

    public Vector3D add(Vector3D otherVector) {
        return new Vector3D(x + otherVector.x, y + otherVector.y, z + otherVector.z);
    }

    public Vector3D subtract(Vector3D otherVector) {
        return new Vector3D(x - otherVector.x, y - otherVector.y, z - otherVector.z);
    }

    public Vector3D scale(double scaleFactor) {
        return new Vector3D(x * scaleFactor, y * scaleFactor, z * scaleFactor);
    }

    public double dot(Vector3D otherVector) {
        return x * otherVector.x + y * otherVector.y + z * otherVector.z;
    }

    public Vector3D normalize() {
        double vectorLength = Math.sqrt(x * x + y * y + z * z);
        return new Vector3D(x / vectorLength, y / vectorLength, z / vectorLength);
    }
}
