public class Sphere {
    public final Vector3D centerPositionVector;
    public final double radiusValue;
    public final ColorRGB diffuseReflectionColor;
    public final ColorRGB specularReflectionColor;
    public final double shininessCoefficient;

    public Sphere(Vector3D sphereCenterPositionVector, double sphereRadiusValue, ColorRGB sphereDiffuseReflectionColor,
                  ColorRGB sphereSpecularReflectionColor, double sphereShininessCoefficient) {
        this.centerPositionVector = sphereCenterPositionVector;
        this.radiusValue = sphereRadiusValue;
        this.diffuseReflectionColor = sphereDiffuseReflectionColor;
        this.specularReflectionColor = sphereSpecularReflectionColor;
        this.shininessCoefficient = sphereShininessCoefficient;
    }

    public boolean intersectParallelRay(Vector3D rayOriginVector, Vector3D rayDirectionVector, double[] intersectionDistanceArray) {
        Vector3D originToCenterVector = rayOriginVector.subtract(centerPositionVector);
        double aParameter = rayDirectionVector.dot(rayDirectionVector);
        double bParameter = 2.0 * originToCenterVector.dot(rayDirectionVector);
        double cParameter = originToCenterVector.dot(originToCenterVector) - radiusValue * radiusValue;
        double discriminantValue = bParameter * bParameter - 4.0 * aParameter * cParameter;
        if (discriminantValue < 0.0) {
            return false;
        }
        double squareRootDiscriminant = Math.sqrt(discriminantValue);
        double firstSolution = (-bParameter - squareRootDiscriminant) / (2.0 * aParameter);
        double secondSolution = (-bParameter + squareRootDiscriminant) / (2.0 * aParameter);
        double chosenSolution = firstSolution;
        if (chosenSolution < 0.0) {
            chosenSolution = secondSolution;
        }
        if (chosenSolution < 0.0) {
            return false;
        }
        intersectionDistanceArray[0] = chosenSolution;
        return true;
    }

    public Vector3D computeSurfaceNormal(Vector3D surfacePointVector) {
        return surfacePointVector.subtract(centerPositionVector).normalize();
    }
}
