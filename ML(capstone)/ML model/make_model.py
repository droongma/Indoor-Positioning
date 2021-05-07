import pandas as pd
import numpy as np
from pycaret.classification import *

df = pd.read_csv("C:/Users/rhatn/Desktop/SKKU/21-1/CapstoneDesignProject/ML/training_dataset_remove_duplicates.csv")
columns = list(df.columns)
expl = setup(df, target = 'target', train_size = 0.9, handle_unknown_categorical = True, categorical_features = columns[:-1], fold = 5)

compare_models(round=4, n_select=5, verbose = True)
