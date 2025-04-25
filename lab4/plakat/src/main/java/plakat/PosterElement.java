package plakat;

import java.awt.*;
import java.awt.geom.*;

public abstract class PosterElement {

    protected final AffineTransform transform = new AffineTransform();
    public abstract void paint(Graphics2D g2d);
    protected abstract Shape getLocalShape();

    public boolean hit(Point2D point){
        try{
            Point2D loc = transform.createInverse().transform(point,null);
            return getLocalShape().contains(loc);
        } catch(Exception e) {
            return false;
        }
    }

    public Rectangle2D getTransformedBounds(){
        return transform.createTransformedShape(getLocalShape()).getBounds2D();
    }

    public AffineTransform getTransform() {
        return new AffineTransform(transform);
    }

    public void setTransform(AffineTransform affineTrans) {
        transform.setTransform(affineTrans);
    }

    public void translate(double deltaX,double deltaY) {
        transform.translate(deltaX, deltaY);
    }

}
