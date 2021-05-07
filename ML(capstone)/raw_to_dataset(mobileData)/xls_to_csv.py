import pandas as pd
import os

def xls_to_csv(pathname):
    for (dirpath, dirnames, filenames) in os.walk(pathname):
        print(dirpath)
        current_dirname = dirpath.split('\\')[-1]
        if "구역" in current_dirname: # if this directory contains excel file to be combined 
            for filename in filenames:
                fname, ext = os.path.splitext(filename)
                if ext == '.xls': # if it is excel data file 
                    file_abs = os.path.join(dirpath, filename)
                    new_name = fname + ".csv" # new file name

                    # convert xls to csv
                    read_file = pd.read_excel(file_abs)
                    read_file.to_csv(os.path.join(dirpath, new_name), index = None)
            
        print('\n---------------------')
    
    

# xls to csv

if __name__ == "__main__":
    pass