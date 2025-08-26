package it.italiandudes.jamazing_centralina.utils.models;

import org.jetbrains.annotations.NotNull;

public final class LoadedDataHandler {

    private ParsedSerialData parsedSerialData;
    private IntArrayPile distanceDataBatch;
    private IntArrayPile humidityDataBatch;
    private IntArrayPile temperatureDataBatch;
    private IntArrayPile pressureDataBatch;
    private DoubleArrayPile accelerationDataBatch;
    private DoubleArrayPile velocityDataBatch;
    private DoubleArrayPile degreeRateBatch;
    private double[] pastAcc;
    private double alpha;

    public void initData(ParsedSerialData parsedSerialData, int maxDistanceBatchSize, int maxHumidityBatchSize,
                            int maxTemperatureBatchSize, int maxPressureBatchSize, int maxAccelerationBatchSize,
                            int maxDegreeRateBatchSize, double alpha){
        this.parsedSerialData = new ParsedSerialData(parsedSerialData);
        this.distanceDataBatch = new IntArrayPile(maxDistanceBatchSize);
        this.humidityDataBatch = new IntArrayPile(maxHumidityBatchSize);
        this.temperatureDataBatch = new IntArrayPile(maxTemperatureBatchSize);
        this.pressureDataBatch = new IntArrayPile(maxPressureBatchSize);
        this.accelerationDataBatch = new DoubleArrayPile(maxAccelerationBatchSize);
        this.velocityDataBatch = new DoubleArrayPile(maxAccelerationBatchSize);
        this.degreeRateBatch = new DoubleArrayPile(maxDegreeRateBatchSize);
        this.alpha = alpha;

        distanceDataBatch.addElement(parsedSerialData.getDistance());
        humidityDataBatch.addElement(parsedSerialData.getHumidity());
        temperatureDataBatch.addElement(parsedSerialData.getTemperature());
        pressureDataBatch.addElement(parsedSerialData.getPressure());
        accelerationDataBatch.addElement(0.0);
        velocityDataBatch.addElement(0.0);

        int[] accelerations = parsedSerialData.getAcceleration();
        double[] presentAcc = new double[3];
        presentAcc[0] = Math.pow(accelerations[0], -3);
        presentAcc[1] = Math.pow(accelerations[1], -3);
        presentAcc[2] = Math.pow(accelerations[2], -3);
        degreeRateBatch.addElement(getPitchAngle(presentAcc[0], presentAcc[1], presentAcc[2],
                parsedSerialData.getDegreeRates()[0], parsedSerialData.getTimePeriod()));
        pastAcc = presentAcc.clone();
    }

    public void updateData(ParsedSerialData parsedSerialData){
        this.parsedSerialData = new ParsedSerialData(parsedSerialData);

        distanceDataBatch.addElement(parsedSerialData.getDistance());
        humidityDataBatch.addElement(parsedSerialData.getHumidity());
        temperatureDataBatch.addElement(parsedSerialData.getTemperature());
        pressureDataBatch.addElement(parsedSerialData.getPressure());

        int[] accelerations = parsedSerialData.getAcceleration();
        double[] presentAcc = new double[3];
        presentAcc[0] = Math.pow(accelerations[0], -3);
        presentAcc[1] = Math.pow(accelerations[1], -3);
        presentAcc[2] = Math.pow(accelerations[2], -3);

        accelerationDataBatch.addElement(getAccModule(presentAcc[0], presentAcc[1], presentAcc[2]));
        velocityDataBatch.addElement(getVelocityModule(presentAcc, parsedSerialData.getTimePeriod()));
        degreeRateBatch.addElement(getPitchAngle(presentAcc[0], presentAcc[1], presentAcc[2],
                Math.pow(parsedSerialData.getDegreeRates()[0], -3), parsedSerialData.getTimePeriod()));

        pastAcc = presentAcc.clone();
    }

    private double getPitchAngle(double accX, double accY, double accZ, double gyroX, int dt) {
        double accelPitch = Math.atan2(-accX, Math.sqrt(accY * accY + accZ * accZ)) * (180.0 / Math.PI);

        double pitchAngle = gyroX * Math.pow(dt, -6);

        pitchAngle = this.alpha * pitchAngle + (1.0 - this.alpha) * accelPitch;

        return pitchAngle;
    }

    // This method takes into consideration the fact that the velocity is actually a three-dimensional
    // vector. It returns a double only containing the actual velocity module after using the past and present
    // accelerations to obtain the velocity between 2 points in a 3D space.
    private double getVelocityModule(double @NotNull [] presentAcc, int dt){
        double actualPresentAccX = presentAcc[0] * 9.81;
        double actualPresentAccY = presentAcc[1] * 9.81;
        double actualPresentAccZ = presentAcc[2] * 9.81;
        double actualDt = Math.pow(dt, -6);

        double[] accAvg = new double[3];
        accAvg[0] = (actualPresentAccX + (this.pastAcc[0] * 9.81))/2.0;
        accAvg[1] = (actualPresentAccY + (this.pastAcc[1] * 9.81))/2.0;
        accAvg[2] = (actualPresentAccZ + (this.pastAcc[2] * 9.81))/2.0;

        double[] finalVectorialVel = new double[3];
        finalVectorialVel[0] = accAvg[0] * actualDt;
        finalVectorialVel[1] = accAvg[1] * actualDt;
        finalVectorialVel[2] = accAvg[2] * actualDt;

        return Math.sqrt(finalVectorialVel[0] * finalVectorialVel[0] + finalVectorialVel[1] * finalVectorialVel[1] +
                finalVectorialVel[2] * finalVectorialVel[2]);
    }

    // This method takes into consideration the fact that the acceleration is actually a three-dimensional
    // vector. It returns a double only containing the actual acceleration module.
    private double getAccModule(double accX, double accY, double accZ){
        return Math.sqrt(accX * accX + accY * accY + accZ * accZ);
    }

    public IntArrayPile getDistanceDataBatch() {
        return distanceDataBatch;
    }

    public IntArrayPile getHumidityDataBatch() {
        return humidityDataBatch;
    }

    public IntArrayPile getTemperatureDataBatch() {
        return temperatureDataBatch;
    }

    public IntArrayPile getPressureDataBatch() {
        return pressureDataBatch;
    }

    public DoubleArrayPile getAccelerationDataBatch() {
        return accelerationDataBatch;
    }

    public DoubleArrayPile getVelocityDataBatch() {
        return velocityDataBatch;
    }

    public DoubleArrayPile getDegreeRateBatch() {
        return degreeRateBatch;
    }
}
