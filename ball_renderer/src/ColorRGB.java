public class ColorRGB {
    public final double redComponent;
    public final double greenComponent;
    public final double blueComponent;

    public ColorRGB(double redValue, double greenValue, double blueValue) {
        this.redComponent = redValue;
        this.greenComponent = greenValue;
        this.blueComponent = blueValue;
    }

    public ColorRGB add(ColorRGB otherColor) {
        return new ColorRGB(redComponent + otherColor.redComponent,
                greenComponent + otherColor.greenComponent,
                blueComponent + otherColor.blueComponent);
    }

    public ColorRGB multiplyByScalar(double scalarValue) {
        return new ColorRGB(redComponent * scalarValue,
                greenComponent * scalarValue,
                blueComponent * scalarValue);
    }

    public ColorRGB multiplyByColor(ColorRGB otherColor) {
        return new ColorRGB(redComponent * otherColor.redComponent,
                greenComponent * otherColor.greenComponent,
                blueComponent * otherColor.blueComponent);
    }

    public int toARGB() {
        int redInteger = (int) Math.round(Math.min(redComponent, 1.0) * 255.0);
        int greenInteger = (int) Math.round(Math.min(greenComponent, 1.0) * 255.0);
        int blueInteger = (int) Math.round(Math.min(blueComponent, 1.0) * 255.0);
        return (255 << 24) | (redInteger << 16) | (greenInteger << 8) | blueInteger;
    }
}
