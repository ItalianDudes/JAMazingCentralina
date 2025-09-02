package it.italiandudes.jamazing_centralina.utils.models;

import java.util.Arrays;

public final class IntArrayPile {
    private final int capacity;
    private int size;
    private final int[] elements;

    public IntArrayPile(int capacity) {
        this.capacity = capacity;
        this.size = 0;
        this.elements = new int[capacity];
    }

    public void addElement (int element){
        if(size == capacity-1){
            for(int i=0; i<capacity-1; i++){
                this.elements[i] = this.elements[i+1];
            }
            this.elements[size] = element;
        }else{
            elements[size] = element;
            size++;
        }
    }

    public int[] getElements(){
        return Arrays.copyOfRange(elements, 0, size);
    }

    public int getLastElement(){
        return elements[this.size];
    }

    @Override
    public String toString() {
        return Arrays.toString(this.elements);
    }
}
