# Indoor-Positioning

## Introduction
This repository is the result of 'Indoor Positioning Using WIFI Fingerprint' project.  

**The goal of this project is to predict user's current indoor location using WIFI fingerprint information and deep learning model.  
The project consists of three stages:**

**1. To make dataset, collect WIFI fingerprint(BSSID and RSSI) data using collector**  
**2. Create deep learning model to predict location using data from stage 1**  
**3. Create Android app to provide indoor positioning service**

**BSSID** means MAC address of WIFI AP. **BSSID also means unique identifier of each AP.**    
**RSSI** means strength of received signal from each AP

**You can see the introduction video in the following URL:**
https://vimeo.com/676350483

## 1. Collector & Dataset
To implement collector, we used the following techniques:
+ Used WifiManager class in Android Studio
+ Collected BSSID, RSSI data
+ Collected data is stored as Excel files (.xls)

As a result, we have 1598 APs in our result dataset.  
The result dataset consists of 1599 columns(1598 for APs and 1 for location) and 287 rows.  
**Each row represents signal information collected from specific location.**  
**The last column represents location(that we want to predict) , and all other columns represents received signal strength of each AP.**

The result dataset is here : [dataset](https://github.com/droongma/Indoor-Positioning/blob/main/ML(capstone)/ML%20model/model(joohyung)/TF_model/training_dataset_remove_duplicates.csv)

**You can see the demonstration video of collector in the following URL:**
https://vimeo.com/676350573


## 2. Deep Learning Model
**It is a multi-class classification model.**  
**Our deep learning model(TFLite model) to predict location based on the result dataset is here: [click to see model file](https://github.com/droongma/Indoor-Positioning/blob/main/ML(capstone)/ML%20model/model(joohyung)/TF_model/model_tflite_version.tflite)**  

The structure of our model is as follows:  

![image14](https://user-images.githubusercontent.com/11453455/153637907-2f132131-ab3b-4b8f-83f2-9b5ded47fbf1.png)
 Since the number of data is too small, we didn't make extra test set.  
Instead, we used cross-validation(CV) set to measure accuracy.
Our accuracy result is as follows:  

![image13](https://user-images.githubusercontent.com/11453455/153638216-6c8b2df0-556d-4768-9b45-e6481782fce1.png)


## 3. Final Application
The execution process of our final application is as follows:  

<img width="459" alt="image17" src="https://user-images.githubusercontent.com/11453455/153641620-cc808c77-b5fb-4b69-ae88-acd8b77469b2.png">


**You can see the demonstration video of final application in the following URL:**
https://vimeo.com/676350630
