import os

def move_excel_file(pathname):
    for (dirpath, dirnames, filenames) in os.walk(pathname):
        print(dirpath)
        print(os.path.dirname(dirpath))
        for dirname in dirnames:
            print(dirname, end = ' ')
            # fname, ext = os.path.splitext(dirname)
            # if ext == '.xls': # if folder name has extension, remove it
            #     os.rename(os.path.join(dirpath, dirname), os.path.join(dirpath, fname))


        for filename in filenames:
            fname, ext = os.path.splitext(filename)
            if ext == '.xls': # move excel file to parent folder
                os.replace(os.path.join(dirpath, filename),   os.path.join(os.path.dirname(dirpath), filename))
            
        print('\n---------------------')

if __name__ == '__main__':
    pass