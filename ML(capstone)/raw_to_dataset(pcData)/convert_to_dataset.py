import csv
import numpy as np
import pandas as pd

# load raw data
rawdata = pd.read_csv('combined_signal_all.csv', sep=',',header=None, usecols = [i for i in range(5) if i != 3],
    names = ['AP_MAC_ADDRESS', 'RSSI', 'timestamp', 'potato', 'location'])


unique_location = set(rawdata['location']) # find all locations we found in collector
unique_AP = list(set(rawdata['AP_MAC_ADDRESS'])) # original AP MAC address
Unique_AP_converted = [] # AP_1, AP_2, ... :  converted result of original AP MAC address
ap_mapping = {} # Change original AP MAC address to the names of AP_1, AP_2, ... (key : original AP MAC address, value : AP_1, AP_2, ...)


count = 1
for ap in unique_AP:
    ap_mapping[ap] = "AP_" + str(count)
    Unique_AP_converted.append("AP_" + str(count))
    count += 1

print(len(unique_AP))
print(len(Unique_AP_converted))
print(np.nan in list(ap_mapping.keys()))



# Make AP_mapping.csv which shows how mapping looks like
with open("ap_mapping.csv", 'w', newline='', encoding='utf-8') as f:
    cf = csv.writer(f)
    for key, value in ap_mapping.items():
        cf.writerow([key, value])


df = pd.DataFrame(columns = Unique_AP_converted + ["target"]) # this is our result dataset
# COL_NUM = len(df.columns)

for location in unique_location: # for each location, we make data whose target value is that location
    sub_rawdata = rawdata.loc[rawdata['location'] == location]
    unique_timestamp = set(sub_rawdata['timestamp']) # one exection(which has same timestamp) of collector corresponds to one data(row)
    for timestamp in unique_timestamp: 
        sub2_rawdata = sub_rawdata[sub_rawdata['timestamp'] == timestamp]
        df_row = {} # this is one row of our dataset

        for k in Unique_AP_converted:
            df_row.setdefault(k, 0) # If we didn't get this AP information, then we assume its RSSI value is 0
        df_row.setdefault("target", location)
        for idx, row in sub2_rawdata.iterrows():
            df_row[ap_mapping[row['AP_MAC_ADDRESS']] ] = row['RSSI'] # If we get this AP information, then insert corresponding RSSI value
        
        df = df.append(df_row, ignore_index = True)
        
# convert to CSV file
df.to_csv('dataset.csv', index=False)




