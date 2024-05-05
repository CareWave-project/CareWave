package com.example.carewave1
import android.content.Context
import org.tensorflow.lite.Interpreter
import java.nio.ByteBuffer
import java.nio.ByteOrder
import org.tensorflow.lite.support.common.FileUtil

class MLModelExecutor(private val context: Context) {

    private lateinit var interpreter: Interpreter
    private lateinit var scalerMean: FloatArray
    private lateinit var scalerStd: FloatArray

    fun loadModel(modelPath: String, scalerPath: String) {
        // Load TFLite model
        val modelBuffer = FileUtil.loadMappedFile(context, modelPath)
        interpreter = Interpreter(modelBuffer)

        // Load scaler
        val scalerBytes = context.assets.open(scalerPath).readBytes()
        val scalerArray = ByteBuffer.wrap(scalerBytes).order(ByteOrder.nativeOrder()).asFloatBuffer()
        scalerMean = FloatArray(scalerArray.capacity() / 2)
        scalerStd = FloatArray(scalerArray.capacity() / 2)
        scalerArray.get(scalerMean)
        scalerArray.get(scalerStd)
    }

    fun preprocessInput(heartRate: Int, spo2: Int): ByteBuffer {
        val inputData = floatArrayOf(heartRate.toFloat(), spo2.toFloat())
        val preprocessedData = ByteBuffer.allocateDirect(inputData.size * 4)
        preprocessedData.order(ByteOrder.nativeOrder())
        for (value in inputData) {
            preprocessedData.putFloat((value - scalerMean[0]) / scalerStd[0])
        }
        return preprocessedData
    }

    fun execute(heartRate: Int, spo2: Int): Int {
        // Preprocess input data
        val preprocessedData = preprocessInput(heartRate, spo2)

        // Perform inference
        val outputArray = Array(1) { FloatArray(1) }
        interpreter.run(preprocessedData, outputArray)

        // Output the prediction
        return if (outputArray[0][0] < 0.5) {
            0 // Normal
        } else {
            1 // Anomaly
        }
    }

    fun closeInterpreter() {
        interpreter.close()
    }
}