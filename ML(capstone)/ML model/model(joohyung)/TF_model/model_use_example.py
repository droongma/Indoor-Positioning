import tensorflow as tf
import numpy as np
import pandas as pd
import csv

# squeeze() : If we use this function, then [[1, 2, 3, 4]] becomes [1, 2, 3, 4]
# np.expand_dims(scaled_test_data, axis=0) : If we use this function, then [1, 2, 3, 4] becomes [[1, 2, 3, 4]]

# ------------------------------- part 1 : input preprocessing ------------------------------------

# We assume variable 'test_data' is the Wifi fingerprint data we get from users

with open('ap_mapping.csv', mode='r') as inp:
    reader = csv.reader(inp)
    ap_mapping = {rows[0]:rows[1] for rows in reader}

test_data = {'f4:d9:fb:d0:0c:a4': -110,
     'f4:d9:fb:d6:31:4a': -110,
     'f4:d9:fb:d0:3a:62': -110,
     'f4:d9:fb:d6:09:4c': -110,
     'f4:d9:fb:d2:7e:0e': -97,
     '00:26:66:c5:e0:c8': -110,
     'f4:d9:fb:d6:09:4b': -110,
     'f4:d9:fb:d3:eb:0c': -74,
     '20:db:ab:92:a7:2e': -110,
     'c6:a5:11:98:c6:00': -150,
     '20:db:ab:92:9a:64': -77,
     'f4:d9:fb:d0:0f:dd': -90,
     'ww:ww:ww:ww:ww:dsd': -550,
     'aa:ww:ww:ww:ww:dsd' : -85}

unique_ap_converted = list(ap_mapping.values())
original_BSSIDs = tuple(ap_mapping.keys())
converted_row = {} # convert result of user data

for k in unique_ap_converted:
    converted_row[k] = -110 # default RSSI value
for k, v in test_data.items():
    if k in original_BSSIDs: # If this BSSID is in our AP mapping result
        converted_row[ap_mapping[k]] = v if v >= -110 else -110 # minimum RSSI value is -110

test_data = np.array(tuple(converted_row.values())) # now it is an array of 1598(total number of APs) elements, where each element is RSSI value of corresponding AP
# print(test_data.shape)

# df = pd.read_csv('training_dataset_remove_duplicates.csv')
# num_row, num_col = df.shape
# X = df.iloc[:,0:num_col-1]
# Y = df.iloc[:, num_col-1]

# feature scaling of input data using feature_scale_list.csv(because model does not have scaling information)
scale_list = pd.read_csv("feature_scale_list.csv", header=None).values.squeeze()
print("scale list : ", scale_list)
scaled_test_data = (test_data + 110) * scale_list
# print(scaled_test_data)

print('-------------- input part ---------------')
print('original data : ', test_data)

input_data = np.expand_dims(scaled_test_data, axis=0)
input_data = np.array(input_data, dtype=np.float32)
print("model input data : ", input_data)

# ------------------- part 2 : Load TF Lite model and predict output using that model from our input data ---------------------

# Load TFLite model and allocate tensors.
interpreter = tf.lite.Interpreter(model_path="model_tflite_version.tflite")
interpreter.allocate_tensors()

# Get input and output tensors.
input_details = interpreter.get_input_details()
output_details = interpreter.get_output_details()

input_shape = input_details[0]['shape']
print("input shape : ", input_shape)

interpreter.set_tensor(input_details[0]['index'], input_data)
interpreter.invoke()

print('-------------- output part ---------------')
# The function `get_tensor()` returns a copy of the tensor data.
# Use `tensor()` in order to get a pointer to the tensor.
output_data = interpreter.get_tensor(output_details[0]['index'])

# -------------------------------- part 3 : postprocess output ------------------------------------

label_mapping = pd.read_csv("label_mapping.csv", header=None, names = ["mapped_result", "original"])
output_data = np.argmax(output_data.squeeze())

# This is our final output, i.e. predicted location using our model
predicted_location = label_mapping[label_mapping['mapped_result'] == output_data]['original'].iloc[0] 



print(output_data)
print("predicted location : ", predicted_location)



