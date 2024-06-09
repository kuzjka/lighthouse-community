#include <Arduino.h>

#include <ESP8266WiFi.h>
#include <ESP8266WiFiMulti.h>

#include <ESP8266HTTPClient.h>

#include <WiFiClient.h>

#include "settings.h"

ESP8266WiFiMulti WiFiMulti;

void setup() {
  Serial.begin(115200);

  Serial.println();
  Serial.println();
  Serial.println();

  for (uint8_t t = 4; t > 0; t--) {
    Serial.printf("[SETUP] WAIT %d...\n", t);
    Serial.flush();
    delay(1000);
  }

  WiFi.mode(WIFI_STA);
  WiFiMulti.addAP(WIFI_SSID, WIFI_PASSWORD);

  pinMode(LED_BUILTIN, OUTPUT);
}

void loop() {
  // wait for WiFi connection
  if ((WiFiMulti.run() == WL_CONNECTED)) {

    WiFiClient client;
    HTTPClient http;

    String url = String(SERVER_URL);
    url.concat("/api/sensor/report");

    if (http.begin(client, url)) {

      Serial.print("Sending request to the server... ");
      int httpCode = http.POST("");

      // httpCode will be negative on error
      if (httpCode == HTTP_CODE_OK) {
        Serial.println("Successful");
        blink(1);
      } else {
        Serial.printf("ERROR: %d\n", httpCode);
        blink(3);
      }

      http.end();
    } else {
      Serial.println("ERROR: Unable to connect");
      blink(3);
    }
  } else {
    Serial.println("WiFi is not connected");
  }

  delay(REQUEST_RATE);
}

void blink(int times) {
    for (int i = 0; i < times; ++i) {
      digitalWrite(LED_BUILTIN, LOW);
      delay(100);
      digitalWrite(LED_BUILTIN, HIGH);
      delay(300);
    }
}
