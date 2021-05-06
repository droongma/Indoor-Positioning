import os
import pandas as pd

def column_changer(pathname):
    for (dirpath, dirnames, filenames) in os.walk(pathname):
        count = 1
        print(dirpath)
        current_dirname = dirpath.split('\\')[-1]
        if "구역" in current_dirname: # if this directory contains csv file to be combined 
            for filename in filenames:
                fname, ext = os.path.splitext(filename)
                if ext == '.csv': # if it is csv file
                    file_abs = os.path.join(dirpath, filename)
                    csvdata = pd.read_csv(file_abs, sep=',')
                    csvdata.columns = ['BSSID', 'DATANUM', 'RSSI']
                    csvdata['DATANUM'] = count # 몇번째 데이터인지 표시
                    count += 1
                    # print(csvdata.head())
                    csvdata.to_csv(file_abs, header=True, index=False)
                    
            
        print('\n---------------------')

if __name__ == '__main__':
    pass