import os

def remove_ext_folder(pathname):
    for (dirpath, dirnames, filenames) in os.walk(pathname):
        print(dirpath)

        for dirname in dirnames:
            print(dirname, end = ' ')
            fname, ext = os.path.splitext(dirname)
            if ext == '.xls': # if folder name has extension, remove it
                os.rename(os.path.join(dirpath, dirname), os.path.join(dirpath, fname))


        for filename in filenames:
            print(filename, end = ' ')

        print('-----------------')