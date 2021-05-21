import os
import pandas as pd

def merger(pathname, resultName):
    column_name = ['BSSID', 'DATANUM', 'RSSI', 'REGION']
    combinedData = pd.DataFrame(columns = column_name) # combined dataset 
    print(combinedData)

    for (dirpath, dirnames, filenames) in os.walk(pathname):
        count = 1
        print(dirpath)
        current_dirname = dirpath.split('\\')[-1]
        if "구역" in current_dirname and "사이" not in current_dirname: # if this directory contains csv file to be combined 
            for filename in filenames:
                fname, ext = os.path.splitext(filename)
                if ext == '.csv': # if it is csv file
                    file_abs = os.path.join(dirpath, filename)
                    datafile = pd.read_csv(file_abs, sep=',')
                    datafile['REGION'] = 'REGION' + str(int(current_dirname[-1]) + 9) # region 10 ~ 18
                    # print(datafile.head())
                    combinedData = combinedData.append(datafile, ignore_index = True) # add dataframe to combinedData
            
    combinedData.to_csv(resultName, header=True, index=False)

if __name__ == '__main__':
    pass