#include <Wire.h>
#include "MAX30105.h"
#include "spo2_algorithm.h"

#include <WiFi.h>
#include <Firebase_ESP_Client.h>
#include "addons/TokenHelper.h"

// Insert your network credentials
#define WIFI_SSID "Kunjus"
#define WIFI_PASSWORD "Kunju123"

// Insert Firebase project API Key
#define API_KEY "AIzaSyCB1-JI-59CVAsnemUw0YFtsIG8jSek9w4"

// Insert Realtime Database URL
#define DATABASE_URL "https://carewave1-2e995-default-rtdb.firebaseio.com/"

// Define Firebase Data object
FirebaseData fbdo;
FirebaseAuth auth;
FirebaseConfig config;

MAX30105 particleSensor;

#if defined(__AVR_ATmega328P__) || defined(__AVR_ATmega168__)
uint16_t irBuffer[100]; // infrared LED sensor data
uint16_t redBuffer[100]; // red LED sensor data
#else
uint32_t irBuffer[100]; // infrared LED sensor data
uint32_t redBuffer[100]; // red LED sensor data
#endif

int32_t bufferLength; // data length
int32_t spo2; // SPO2 value
int8_t validSPO2; // indicator to show if the SPO2 calculation is valid
int32_t heartRate; // heart rate value
int8_t validHeartRate; // indicator to show if the heart rate calculation is valid

const int AVERAGE_WINDOW = 5; // 5 seconds
int32_t heartRateBuffer[AVERAGE_WINDOW]; // Buffer to store heart rate values for averaging
int32_t spo2Buffer[AVERAGE_WINDOW]; // Buffer to store SpO2 values for averaging
int bufferIndex = 0; // Index to keep track of the buffer position

unsigned long previousMillis = 0; // Previous time for updating Firebase
const unsigned long updateInterval = 1000; // Update interval (1 second)

void setup()
{
  Serial.begin(115200);
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("Connecting to Wi-Fi");
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(300);
  }
  Serial.println();
  Serial.print("Connected with IP: ");
  Serial.println(WiFi.localIP());
  Serial.println();

  config.api_key = API_KEY;
  config.database_url = DATABASE_URL;
  config.token_status_callback = tokenStatusCallback; // see addons/TokenHelper.h
  Firebase.begin(&config, &auth);
  Firebase.reconnectWiFi(true);

  // Enable anonymous authentication (no need for email/password)
  if (Firebase.signUp(&config, &auth, "", "")) {
    Serial.println("Anonymous authentication successful");
  } else {
    Serial.printf("%s\n", config.signer.signupError.message.c_str());
  }

  Serial.println("Initializing...");
  if (!particleSensor.begin(Wire, I2C_SPEED_FAST)) {
    Serial.println("MAX30105 was not found. Please check wiring/power.");
    while (1);
  }

  byte ledBrightness = 60; // Options: 0=Off to 255=50mA
  byte sampleAverage = 4; // Options: 1, 2, 4, 8, 16, 32
  byte ledMode = 2; // Options: 1 = Red only, 2 = Red + IR, 3 = Red + IR + Green
  byte sampleRate = 100; // Options: 50, 100, 200, 400, 800, 1000, 1600, 3200
  int pulseWidth = 411; // Options: 69, 118, 215, 411
  int adcRange = 4096; // Options: 2048, 4096, 8192, 16384

  particleSensor.setup(ledBrightness, sampleAverage, ledMode, sampleRate, pulseWidth, adcRange); // Configure sensor with these settings
  bufferLength = 100; // buffer length of 100 stores 4 seconds of samples running at 25sps

 Serial.println("Place your finger on the sensor...");
}

void loop()
{
  unsigned long currentMillis = millis();

  // Read the first 100 samples and determine the signal range
  for (byte i = 0; i < bufferLength; i++)
  {
    while (particleSensor.available() == false)
      particleSensor.check(); // Check the sensor for new data

    redBuffer[i] = particleSensor.getRed();
    irBuffer[i] = particleSensor.getIR();
    particleSensor.nextSample(); // Move to the next sample
  }

  // Calculate heart rate and SpO2 after the first 100 samples (first 4 seconds of samples)
  maxim_heart_rate_and_oxygen_saturation(irBuffer, bufferLength, redBuffer, &spo2, &validSPO2, &heartRate, &validHeartRate);

  // Store the heart rate and SpO2 values in the buffers
  heartRateBuffer[bufferIndex] = heartRate;
  spo2Buffer[bufferIndex] = spo2;
  bufferIndex = (bufferIndex + 1) % AVERAGE_WINDOW;

  // Calculate the average heart rate and SpO2 over the past 5 seconds
  int32_t avgHeartRate = 0;
  int32_t avgSpo2 = 0;
  for (int i = 0; i < AVERAGE_WINDOW; i++) {
    avgHeartRate += heartRateBuffer[i];
    avgSpo2 += spo2Buffer[i];
  }
  avgHeartRate /= AVERAGE_WINDOW;
  avgSpo2 /= AVERAGE_WINDOW;

  // Ensure heart rate and SpO2 values are not negative before sending to Firebase
  heartRate = max(heartRate, 0);
  spo2 = max(spo2, 0);
  avgHeartRate = max(avgHeartRate, 0);
  avgSpo2 = max(avgSpo2, 0);

  // Send the data to Firebase Realtime Database every second
  if (currentMillis - previousMillis >= updateInterval)
  {
    previousMillis = currentMillis;

    // Create a JSON object with the sensor data and timestamp
    FirebaseJson json;
    json.set("beatsPerMinute", heartRate);
    json.set("heartRate", avgHeartRate);
    json.set("spo2", spo2);
    json.set("avgSpo2", avgSpo2);
    json.set("timestamp", currentMillis / 1000); // Unix timestamp in seconds

    // Send the data to Firebase Realtime Database
    if (Firebase.RTDB.setJSON(&fbdo, "/sensor_data", &json)) {
      Serial.println("Data sent to Firebase Realtime Database successfully");
    } else {
      Serial.println("Failed to send data to Firebase Realtime Database");
      Serial.println(fbdo.errorReason());
    }
  }

  // Print sensor data and calculation results to the serial monitor
  Serial.print("IR=");
  Serial.print(particleSensor.getIR());
  Serial.print(", Red=");
  Serial.print(particleSensor.getRed());
  Serial.print(", BPM=");
  Serial.print(heartRate);
  Serial.print(", Avg BPM=");
  Serial.print(avgHeartRate);
  Serial.print(", SPO2=");
  Serial.print(spo2);
  Serial.print(", Avg SPO2=");
  Serial.print(avgSpo2);
  Serial.print(", HRValid=");
  Serial.print(validHeartRate);
  Serial.print(", SPO2Valid=");
  Serial.println(validSPO2);

  // Continuously taking samples from MAX30105 and updating heart rate and SpO2
  // Shift the last 75 sets of samples to the top
  for (byte i = 75; i < 100; i++)
  {
    while (particleSensor.available() == false)
      particleSensor.check(); // Check the sensor for new data

    redBuffer[i] = particleSensor.getRed();
    irBuffer[i] = particleSensor.getIR();
  }
}
