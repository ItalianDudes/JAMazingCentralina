package it.italiandudes.jamazing_centralina.utils.models;

public final class LoadedDataHandler {

    private ParsedSerialData parsedSerialData;
    private IntArrayPile distanceDataBatch;
    private IntArrayPile humidityDataBatch;
    private IntArrayPile temperatureDataBatch;
    private IntArrayPile pressureDataBatch;
    private DoubleArrayPile accelerationDataBatch;
    private DoubleArrayPile velocityDataBatch;
    private DoubleArrayPile degreeRateBatch;
    private int[] pastAcc;
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
        degreeRateBatch.addElement(getPitchAngle(accelerations[0], accelerations[1], accelerations[2],
                parsedSerialData.getDegreeRates()[0], parsedSerialData.getTimePeriod()));
        pastAcc = accelerations.clone();
    }

    public void updateData(ParsedSerialData parsedSerialData){
        this.parsedSerialData = new ParsedSerialData(parsedSerialData);

        distanceDataBatch.addElement(parsedSerialData.getDistance());
        humidityDataBatch.addElement(parsedSerialData.getHumidity());
        temperatureDataBatch.addElement(parsedSerialData.getTemperature());
        pressureDataBatch.addElement(parsedSerialData.getPressure());

        int[] presentAcc = parsedSerialData.getAcceleration();

        accelerationDataBatch.addElement(getAccModule(presentAcc[0], presentAcc[1], presentAcc[2]));
        velocityDataBatch.addElement(getVelocityModule(presentAcc, parsedSerialData.getTimePeriod()));
        degreeRateBatch.addElement(getPitchAngle(presentAcc[0], presentAcc[1], presentAcc[2],
                parsedSerialData.getDegreeRates()[0], parsedSerialData.getTimePeriod()));

        pastAcc = presentAcc.clone();
    }

    private double getPitchAngle(int accX, int accY, int accZ, int gyroX, int dt) {
        double accelPitch = Math.atan2(-accX, Math.sqrt(accY * accY + accZ * accZ)) * (180.0 / Math.PI);

        double pitchAngle = gyroX * (dt / Math.pow(dt, 6));

        pitchAngle = this.alpha * pitchAngle + (1.0 - this.alpha) * accelPitch;

        return pitchAngle;
    }

    // This method takes into consideration the fact that the velocity is actually a three-dimensional
    // vector. It returns a double only containing the actual velocity module after using the past and present
    // accelerations to obtain the velocity between 2 points in a 3D space.
    private double getVelocityModule(int[] presentAcc, int dt){
        double actualPresentAccX = presentAcc[0] * 9.81;
        double actualPresentAccY = presentAcc[1] * 9.81;
        double actualPresentAccZ = presentAcc[2] * 9.81;
        double actualDt = (dt / Math.pow(dt, 6));

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
    private double getAccModule(int accX, int accY, int accZ){
        double actualAccX = accX * 9.81;        // Converts to m/s^2 from g
        double actualAccY = accY * 9.81;        // Converts to m/s^2 from g
        double actualAccZ = accZ * 9.81;        // Converts to m/s^2 from g

        return Math.sqrt(actualAccX * actualAccX + actualAccY * actualAccY + actualAccZ * actualAccZ);
    }
}
