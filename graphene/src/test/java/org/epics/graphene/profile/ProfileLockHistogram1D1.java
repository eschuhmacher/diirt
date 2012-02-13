/*
 * Copyright 2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.graphene.profile;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.epics.graphene.*;

/**
 *
 * @author carcassi
 */
public class ProfileLockHistogram1D1 {

    public static void main(String[] args) {
        int nSamples = 1000;
        final int nTries = 2000;
        final int imageWidth = 300;
        final int imageHeight = 200;
        Random rand = new Random();
        
        int nThreads = 1;
                
        final Dataset1D dataset = new Dataset1DArray(nSamples);
        Dataset1DUpdater update = dataset.update();
        for (int i = 0; i < nSamples; i++) {
            update.addData(rand.nextGaussian());
        }
        update.commit();
        
        ExecutorService executor = Executors.newFixedThreadPool(nThreads);
        for (int i = 0; i < nThreads; i++) {
        executor.execute(new Runnable() {

            @Override
            public void run() {

                Histogram1D histogram = Histograms.createHistogram(dataset);
                histogram.update(new Histogram1DUpdate().imageWidth(imageWidth).imageHeight(imageHeight));
                Histogram1DRenderer renderer = new Histogram1DRenderer();

                StopWatch stopWatch = new StopWatch(nTries);

                for (int i = 0; i < nTries; i++) {
                    stopWatch.start();
                    synchronized(dataset) {
                    histogram.update(new Histogram1DUpdate().recalculateFrom(dataset));
                    BufferedImage image = new BufferedImage(histogram.getImageWidth(), histogram.getImageHeight(), BufferedImage.TYPE_3BYTE_BGR);
                    Graphics2D graphics = image.createGraphics();
                    renderer.draw(graphics, histogram);

                    if (image.getRGB(0, 0) == 0) {
                        System.out.println("Black");
                    }
                    }
                    stopWatch.stop();
                }

                System.out.println("average " + stopWatch.getAverageMs() + " ms");
            }
        });
        }
//        Dataset1D timings = new Dataset1DArray(nTries);
//        timings.update().addData(Arrays.copyOfRange(stopWatch.getData(), 1, nTries)).commit();
//        Histogram1D hist = Histograms.createHistogram(timings);
//        hist.update(new Histogram1DUpdate().imageWidth(800).imageHeight(600));
//        ShowResizableImage.showHistogram(hist);
        executor.shutdown();
    }
}
