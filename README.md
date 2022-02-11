# Indoor-Positioning

## Introduction
**This repository is the result of 'Indoor Positioning Using WIFI Fingerprint' project.**

The project consists of three stages:

**1. Collecting WIFI fingerprint(BSSID and RSSI) data using collector**  
**2. Create deep learning model to predict location using data from stage 1**  
**3. Create Android app to provide indoor positioning service**

## 1. Collector
To implement collector, we used the following techniques:
+ Used WifiManager class in Android Studio
+ Collected BSSID, RSSI data
+ Collected data is stored as Excel files (.xls)

You can see demonstration video of collector in the following URL:
https://vimeo.com/676350573


## 2. Deep Learning Model
You can see our deep learning model to predict location based on BSSID and RSSI values here: [Model file](https://github.com/droongma/Indoor-Positioning/blob/main/ML(capstone)/ML%20model/model(joohyung)/TF_model/model_tflite_version.tflite)  
The structure of our model is as follows:  

![image14](https://user-images.githubusercontent.com/11453455/153637907-2f132131-ab3b-4b8f-83f2-9b5ded47fbf1.png)

It is basically a multi-class classification model. Since the number of data is too small, we don't make extra test set.  
Instead, we use cross-validation(CV) set to measure accuracy.
Our accuracy result is as follows:  

![image13](https://user-images.githubusercontent.com/11453455/153638216-6c8b2df0-556d-4768-9b45-e6481782fce1.png)




You can see demonstration video of collector in the following URL:
https://vimeo.com/676350573



