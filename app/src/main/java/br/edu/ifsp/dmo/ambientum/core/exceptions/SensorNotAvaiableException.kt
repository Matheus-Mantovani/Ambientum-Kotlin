package br.edu.ifsp.dmo.ambientum.core.exceptions

class SensorNotAvailableException(sensorName: String) :
    RuntimeException("Sensor not available: $sensorName")