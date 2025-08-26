package it.italiandudes.jamazing_centralina.utils.models;

import java.util.Arrays;

public final class DoubleArrayPile {
    private int capacity;
    private int size;
    private double[] elements;

    public DoubleArrayPile(int capacity) {
        this.capacity = capacity;
        this.size = 0;
        this.elements = new double[capacity];
    }

    public void addElement (double element){
        if(size >= capacity-1){
            for(int i=0; i<capacity-1; i++){
                this.elements[i] = this.elements[i+1];
            }
            this.elements[size] = element;
        }else{
            elements[size] = element;
            size++;
        }
    }

    public double[] getElements(){
        return Arrays.copyOfRange(elements, 0, size);
    }

    public double getLastElement(){
        return elements[this.size];
    }

    @Override
    public String toString() {
        return Arrays.toString(this.elements);
    }
}
