package com.example.carewave1

import android.content.Context
import org.tensorflow.lite.Interpreter
import java.nio.ByteBuffer
import java.io.InputStream

class MLModelExecutor(context: Context) {

    private var tfliteInterpreter: Interpreter? = null
    private val numOutputClasses = 2 // Assuming binary classification (0 or 1)
    private val alertManager: AlertManager = AlertManager(context)

    init {
        loadModelFile(context)
    }

    private fun loadModelFile(context: Context) {
        try {
            // Load model file from assets folder
            val modelFileName = "model.tflite"
            val inputStream: InputStream = context.assets.open(modelFileName)
            val modelBytes = inputStream.readBytes()
            val modelByteBuffer = ByteBuffer.allocateDirect(modelBytes.size)
            modelByteBuffer.put(modelBytes)
            modelByteBuffer.rewind()
            val options = Interpreter.Options()
            tfliteInterpreter = Interpreter(modelByteBuffer, options)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun executeModel(inputData: FloatArray): Int {
        try {
            val inputTensorIndex = 0 // Assuming input tensor index is 0
            val outputTensorIndex = 0 // Assuming output tensor index is 0
            val outputShape = tfliteInterpreter?.getOutputTensor(outputTensorIndex)?.shape() // Get the output tensor shape
            val output = Array(1) { FloatArray(outputShape!![1]) } // Initialize the output array with the correct shape

            // Run inference
            tfliteInterpreter?.run(inputData, output)

            // Assuming binary classification, return 1 if output[0][1] > output[0][0], otherwise return 0
            val isAnomaly = output[0][1] > output[0][0] // Assuming anomaly class has index 1

            if (isAnomaly) {
                // Call triggerAlert from AlertManager when anomaly detected
                alertManager.triggerAlert()
            }

            return if (isAnomaly) 1 else 0
        } catch (e: Exception) {
            e.printStackTrace()
            return -1 // Return a default value or handle the error appropriately
        }
    }

}


