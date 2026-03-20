import os
from PIL import Image

dataset_path = "Dataset"

for root, dirs, files in os.walk(dataset_path):
    for file in files:
        file_path = os.path.join(root, file)
        try:
            img = Image.open(file_path)
            img.verify()  # check if image is valid
        except:
            print("Removing corrupted image:", file_path)
            os.remove(file_path)