#!/usr/bin/env python
# coding: utf-8


# This file explains how to predict location using our model from BSSID and RSSI data of user

# We assume user's wifi fingerprint data(BSSID and RSSI data) is python dictionary type:
# For example, we assume data is like {BSSID1 : RSSI1, BSSID2 : RSSI2, ...}



from pycaret.classification import *
import pandas as pd
import csv, pickle
import numpy as np

def using_model():

    loc_prediction_model = load_model('loc_prediction_model')

    # load dataset
    dataset = pd.read_csv("training_dataset_remove_duplicates.csv")
    with open('ap_mapping.csv', mode='r') as inp:
        reader = csv.reader(inp)
        ap_mapping = {rows[0]:rows[1] for rows in reader}

    ff = open('test_dict.pickle', 'rb')
    test_dict = pickle.load(ff)  # This is our example user data! 

    # test_dict = {'f4:d9:fb:d0:0c:a4': -110,
    #  'f4:d9:fb:d6:31:4a': -110,
    #  'f4:d9:fb:d0:3a:62': -110,
    #  'f4:d9:fb:d6:09:4c': -110,
    #  'f4:d9:fb:d2:7e:0e': -97,
    #  '00:26:66:c5:e0:c8': -110,
    #  'f4:d9:fb:d6:09:4b': -110,
    #  'f4:d9:fb:d3:eb:0c': -74,
    #  '20:db:ab:92:a7:2e': -110,
    #  'c6:a5:11:98:c6:00': -110,
    #  '20:db:ab:92:9a:64': -77,
    #  'f4:d9:fb:d0:0f:29': -110,}

    unique_ap_converted = list(ap_mapping.values())
    df = pd.DataFrame(columns = list(unique_ap_converted)) # This will be the input of our model
    converted_row = {} # convert result of user data

    for k in unique_ap_converted:
        converted_row.setdefault(k, -110)
    for k, v in test_dict.items():
        converted_row[ap_mapping[k]] = v

    df = df.append(converted_row, ignore_index = True)
    predictions = predict_model(loc_prediction_model, data = df)
    result = predictions['Label'].loc[0] # prediction result
    return result

if __name__ == "__main__":
    print(using_model())
    