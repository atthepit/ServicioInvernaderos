javac iInvernadero.java; javac iSensor.java; javac iActuador.java; javac Invernadero.java; javac Sensor.java; javac Actuador.java; rmic Invernadero; rmic Sensor; rmic Actuador; jar cvf cliente.jar iInvernadero.class Invernadero_Stub.class iSensor.class Sensor_Stub.class iActuador.class Actuador_Stub.class;

