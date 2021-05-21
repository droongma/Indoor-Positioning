from remove_ext_from_folder import remove_ext_folder
from move_excel_file import move_excel_file
from xls_to_csv import xls_to_csv
from column_changer import column_changer
from merger_science2 import merger
import os



if __name__ == "__main__":
    pathname = "D:\\joohyung\\google_drive_backup\\joohyung\\성대\\3학년\\1학기\\캡스톤\\제2과학관 데이터_수정"
    remove_ext_folder(pathname)
    move_excel_file(pathname)
    xls_to_csv(pathname)
    column_changer(pathname)
    merger(pathname, "science2_combined_dataset.csv")