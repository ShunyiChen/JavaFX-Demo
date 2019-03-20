package com.maxtree.screenshot;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class RotateAnimation extends Transition {

    public RotateAnimation(ImageView imageView, Duration duration) {
        this.imageView = imageView;
  
        setCycleDuration(duration);
        setInterpolator(Interpolator.LINEAR);
    }

    protected void interpolate(double k) {
    	imageView.setRotate((k * 360d));
    }
    
    private ImageView imageView;
}