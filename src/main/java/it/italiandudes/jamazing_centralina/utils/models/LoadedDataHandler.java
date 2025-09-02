package it.italiandudes.jamazing_centralina.utils.models;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public final class LoadedDataHandler {

    private final double G = 9.81;

    private ParsedSerialData parsedSerialData;
    private IntArrayPile distanceDataBatch;
    private IntArrayPile humidityDataBatch;
    private IntArrayPile temperatureDataBatch;
    private IntArrayPile pressureDataBatch;
    private DoubleArrayPile timeDataBatch;
    private DoubleArrayPile accelerationDataBatch;
    private DoubleArrayPile velocityDataBatch;
    private DoubleArrayPile degreeRateBatch;
    private double[] pastAcc;
    private double alpha;

    private double actualDt;
    private double timeDataCounter;
    private double rollGyro;
    private double pitchGyro;
    private double roll;
    private double pitch;
    private double filteredPitch;

    private boolean isInitFilter;

    public void initData(ParsedSerialData parsedSerialData, int maxDistanceBatchSize, int maxHumidityBatchSize,
                            int maxTemperatureBatchSize, int maxPressureBatchSize, int maxAccelerationBatchSize,
                            int maxDegreeRateBatchSize, int maxTimeDataBatch, double alpha){
        this.parsedSerialData = new ParsedSerialData(parsedSerialData);
        this.distanceDataBatch = new IntArrayPile(maxDistanceBatchSize);
        this.humidityDataBatch = new IntArrayPile(maxHumidityBatchSize);
        this.temperatureDataBatch = new IntArrayPile(maxTemperatureBatchSize);
        this.pressureDataBatch = new IntArrayPile(maxPressureBatchSize);
        this.timeDataBatch = new DoubleArrayPile(maxTimeDataBatch);
        this.accelerationDataBatch = new DoubleArrayPile(maxAccelerationBatchSize);
        this.velocityDataBatch = new DoubleArrayPile(maxAccelerationBatchSize);
        this.degreeRateBatch = new DoubleArrayPile(maxDegreeRateBatchSize);
        this.alpha = alpha;
        this.actualDt = 0.0;
        this.timeDataCounter = 0.0;
        this.filteredPitch = 0.0;

        distanceDataBatch.addElement(parsedSerialData.getDistance() > 8000 ? 0 : parsedSerialData.getDistance());
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
        degreeRateBatch.addElement(this.pitch);
        pastAcc = presentAcc.clone();

        this.isInitFilter = true;
    }

    public void updateData(ParsedSerialData parsedSerialData){
        this.parsedSerialData = new ParsedSerialData(parsedSerialData);
        this.actualDt = getActualDt(parsedSerialData.getTimePeriod());
        this.timeDataCounter += this.actualDt;

        distanceDataBatch.addElement(parsedSerialData.getDistance() > 8000 ? 0 : parsedSerialData.getDistance());
        humidityDataBatch.addElement(parsedSerialData.getHumidity());
        temperatureDataBatch.addElement(parsedSerialData.getTemperature());
        pressureDataBatch.addElement(parsedSerialData.getPressure());
        timeDataBatch.addElement(this.timeDataCounter);

        int[] accelerations = parsedSerialData.getAcceleration();
        double[] presentAcc = new double[3];
        presentAcc[0] = accelerations[0];
        presentAcc[1] = accelerations[1];
        presentAcc[2] = accelerations[2];

        //System.out.println("Present acc: \n" + Arrays.toString(presentAcc));
        accelerationDataBatch.addElement(getAccModule(presentAcc[0], presentAcc[1], presentAcc[2]));
        velocityDataBatch.addElement(getVelocityModule(presentAcc, parsedSerialData.getDegreeRates()[0],
                parsedSerialData.getDegreeRates()[1], this.isInitFilter));
        if(this.isInitFilter){
            this.isInitFilter = false;
        }
        //System.out.println("\n\nVelocity batch: \n" + velocityDataBatch);
        //System.out.println("Pressure batch: \n" + pressureDataBatch);
        degreeRateBatch.addElement(this.filteredPitch);
    }

    // This method takes into consideration the fact that the velocity is actually a three-dimensional
    // vector. It returns a double only containing the actual velocity module after using the past and present
    // accelerations to obtain the velocity between 2 points in a 3D space.
    private double getVelocityModule(double @NotNull [] presentAcc, double gyroX, double gyroY, boolean isInit){
        double[] actualPresentAcc = new double[3];
        actualPresentAcc[0] = presentAcc[0] * G;
        actualPresentAcc[1] = presentAcc[1] * G;
        actualPresentAcc[2] = presentAcc[2] * G;

        //System.out.println("ActualPresentAcc: \n[" + actualPresentAccX + ", " + actualPresentAccY + ", " + actualPresentAccZ + "]");

        //Complementary filter: used to delete the G component from all axes
        double rollAcc = Math.atan2(actualPresentAcc[1], Math.sqrt(actualPresentAcc[0] * actualPresentAcc[0] + actualPresentAcc[2] * actualPresentAcc[2]));
        double pitchAcc = Math.atan2(-actualPresentAcc[0], Math.sqrt(actualPresentAcc[1] * actualPresentAcc[1] + actualPresentAcc[2] * actualPresentAcc[2]));


        if(isInit){
            this.rollGyro = rollAcc;
            this.roll = rollAcc;
            this.pitchGyro = pitchAcc;
            this.pitch = pitchAcc;
        }else{
            this.rollGyro += gyroX * (Math.PI/180.0) * this.actualDt;
            this.pitchGyro += gyroY * (Math.PI/180.0) * this.actualDt;

            roll = this.alpha * this.rollGyro + (1 - this.alpha) * rollAcc;
            pitch = this.alpha * this.pitchGyro + (1 - this.alpha) * pitchAcc;
        }

        double pitchDegree = Math.toDegrees(pitch);
        if (pitchDegree > 75.0) {
            this.filteredPitch = 75.0;
        } else if (pitchDegree < -75.0) {
            this.filteredPitch = -75.0;
        }else{
            this.filteredPitch = pitchDegree;
        }

        double gX = G * Math.pow(10, 3) * Math.sin(pitch);
        double gY = -G * Math.pow(10, 3) * Math.sin(roll);
        double gZ = G * Math.cos(pitch) * Math.cos(roll);
        
        double[] filteredAcc = new double[3];
        filteredAcc[0] = actualPresentAcc[0] - gX;
        filteredAcc[1] = actualPresentAcc[1] - gY;
        filteredAcc[2] = actualPresentAcc[2] - gZ;

        double[] accAvg = new double[3];
        if(isInitFilter) {
            accAvg[0] = filteredAcc[0];
            accAvg[1] = filteredAcc[1];
            accAvg[2] = filteredAcc[2];
        } else {
            accAvg[0] = (filteredAcc[0] + (this.pastAcc[0]))/2.0;
            accAvg[1] = (filteredAcc[1] + (this.pastAcc[1]))/2.0;
            accAvg[2] = (filteredAcc[2] + (this.pastAcc[2]))/2.0;
        }

        //System.out.println("AccAvg: \n" + Arrays.toString(accAvg));

        double[] finalVectorialVel = new double[3];
        finalVectorialVel[0] = accAvg[0] * this.actualDt;
        finalVectorialVel[1] = accAvg[1] * this.actualDt;
        finalVectorialVel[2] = accAvg[2] * this.actualDt;

        //System.out.println("FinalVectorialVel: \n" + Arrays.toString(finalVectorialVel));

        this.pastAcc = accAvg.clone();
        double finalVelocityModule = Math.sqrt(finalVectorialVel[0] * finalVectorialVel[0] + finalVectorialVel[1] * finalVectorialVel[1] +
                finalVectorialVel[2] * finalVectorialVel[2]);
        if (finalVelocityModule > 10.0) {
            return finalVelocityModule;
        } else {
            return 0.0;
        }
    }

    // This method takes into consideration the fact that the acceleration is actually a three-dimensional
    // vector. It returns a double only containing the actual acceleration module.
    private double getAccModule(double accX, double accY, double accZ){
        return Math.sqrt(accX * accX + accY * accY + accZ * accZ);
    }

    private double getActualDt(int dt){
        return dt * Math.pow(10, -6);
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

    public DoubleArrayPile getTimeDataBatch() {
        return timeDataBatch;
    }
}
