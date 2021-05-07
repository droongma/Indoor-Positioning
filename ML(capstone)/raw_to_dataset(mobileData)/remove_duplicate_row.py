import pandas as pd

original = pd.read_csv("training_dataset.csv", sep=",")
print(original.shape[0])

changed = original.drop_duplicates()
print(changed.shape[0])

changed.to_csv("training_dataset_remove_duplicates.csv", index=False)