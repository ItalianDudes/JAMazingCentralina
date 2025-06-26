package it.italiandudes.jamazing_centralina.utils.models;

import org.jetbrains.annotations.NotNull;

public final class ParsedSerialData {

    private int distance;
    private int humidity;
    private int temperature;
    private int pressure;
    private int[] acceleration;
    private int[] degreeRates;
    private int timePeriod;

    public ParsedSerialData(){
        this.distance = 0;
        this.humidity = 0;
        this.temperature = 0;
        this.pressure = 0;
        this.acceleration = new int[3];
        this.degreeRates = new int[3];
        this.timePeriod = 0;
    }

    public ParsedSerialData(ParsedSerialData parsedSerialData){
        this.distance = parsedSerialData.getDistance();
        this.humidity = parsedSerialData.getHumidity();
        this.temperature = parsedSerialData.getTemperature();
        this.pressure = parsedSerialData.getPressure();
        this.acceleration = parsedSerialData.getAcceleration();
        this.degreeRates = parsedSerialData.getDegreeRates();
        this.timePeriod = parsedSerialData.getTimePeriod();
    }

    public void parseData(@NotNull String data){
        String[] firstBatch = data.split(",");
        this.distance = Integer.parseInt(firstBatch[0]);
        this.humidity = Integer.parseInt(firstBatch[3]);
        this.temperature = Integer.parseInt(firstBatch[4]);
        this.pressure = Integer.parseInt(firstBatch[5]);
        this.timePeriod = Integer.parseInt(firstBatch[6]);

        String[] accBatch = firstBatch[1].split("/");
        this.acceleration[0] = Integer.parseInt(accBatch[0]);
        this.acceleration[1] = Integer.parseInt(accBatch[1]);
        this.acceleration[2] = Integer.parseInt(accBatch[2]);

        String[] degreeRatesBatch = firstBatch[2].split("/");
        this.degreeRates[0] = Integer.parseInt(degreeRatesBatch[0]);
        this.degreeRates[1] = Integer.parseInt(degreeRatesBatch[1]);
        this.degreeRates[2] = Integer.parseInt(degreeRatesBatch[2]);
    }

    public int getDistance() {
        return distance;
    }

    public int getHumidity() {
        return humidity;
    }

    public int getTemperature() {
        return temperature;
    }

    public int getPressure() {
        return pressure;
    }

    public int[] getAcceleration() {
        return acceleration.clone();
    }

    public int[] getDegreeRates() {
        return degreeRates.clone();
    }

    public int getTimePeriod() {
        return timePeriod;
    }
}
