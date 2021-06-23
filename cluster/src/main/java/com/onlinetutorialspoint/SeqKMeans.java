package com.onlinetutorialspoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class SeqKMeans {

    //Threshold for convergence(set to 1%)
    private final double THRESHOLD = 0.01;
    private List<List<Double>> centroids = new ArrayList<>();
    public final int k = 6;
    public HashMap<List<Double>, List<List<Double>>> clusterMap = new HashMap<>();
    private final List<List<Double>> records;
    //Initialize the termination to false
    private boolean runIteration = false;

    public SeqKMeans(List<List<Double>> records) {
        this.records = records;
    }

    public SeqKMeans(List<List<Double>> centroids, HashMap<List<Double>, List<List<Double>>> clusterMap, List<List<Double>> records) {
//        this.centroids = centroids;
//        this.clusterMap = clusterMap;
        this.records = records;
    }

    //Euclidian distance
    private Double distance(List<Double> centroid, List<Double> record) {
        double sum = 0.0;
        int size = centroid.size();
        // ignoring the last elememt ... which is the actual label for now
        for (int i = 0; i < size - 1; i++) {
            sum += Math.pow(centroid.get(i) - record.get(i), 2);
        }
        return Math.sqrt(sum);
    }


    void selectRandomCentroids(int k) {
        // Selecting k centroids at random
        int i = 0;
        while (i < k) {
            Random random = new Random();
            List<Double> centroid = records.get(random.nextInt(records.size()));
            centroids.add(centroid);
            i++;

        }
    }

    void evaluateClusterMap() {

        clusterMap.clear();

        // creating k entries in hashmap, one for each centroid
        for (List<Double> c : centroids) {
            clusterMap.put(c, new ArrayList<>());
        }
    }

    private List<Double> initializeList(int size) {
        List<Double> a = new ArrayList<>();
        for (int j = 0; j < size; j++) {
            a.add(0.0);
        }

        return a;
    }

    private void evaluateCentroids() {

        //CLear the previous centroids
        centroids.clear();

        for (List<Double> c : clusterMap.keySet()) {

            //create initial centroid
            List<Double> new_c = initializeList(clusterMap.get(c).get(0).size());

            for (List<Double> r : clusterMap.get(c)) {
                //start from first column
                int i = 0;

                //loop until last column, each column denotes one feature
                //ignore the last column which is the feature: label
                while (i < r.size() - 1) {

                    //calculate the average of the column under consideration
                    new_c.set(i, new_c.get(i) + (r.get(i) / clusterMap.get(c).size()));
                    i++;
                }
            }

            //check for convergence
            if (distance(c, new_c) / distance(initializeList(c.size()), c) > THRESHOLD) {
                runIteration = true;
            }
            centroids.add(new_c);
        }
    }

    List<List<Double>> kMeans() {
        selectRandomCentroids(k);
        evaluateClusterMap();
        // running k means

        //Run the iteration based on the flag set during convergence
        do {
            runIteration = false;
            for (List<Double> r : records) {

                Double min_val = Double.MAX_VALUE;
                List<Double> selected_c = null;
                Double d;

                for (List<Double> c : centroids) {
                    d = distance(c, r);
                    if (d < min_val) {
                        selected_c = c;
                        min_val = d;
                    }
                }
                clusterMap.get(selected_c).add(r);
            }

            //To recompute new centroids by averaging the records assigned to each
            //Sets the flag to false if converged
            evaluateCentroids();

            //Clear the previous contents of the clusterMap if we have a next iteration
            if (runIteration) {
                evaluateClusterMap();
            }


        } while (runIteration);

        return centroids;
    }
}
