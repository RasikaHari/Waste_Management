import os
import shutil

base_dir = "Dataset"

splits = ["train", "val"]

for split in splits:
    split_path = os.path.join(base_dir, split)

    for category in ["biodegradable", "non_biodegradable"]:
        category_path = os.path.join(split_path, category)

        if not os.path.exists(category_path):
            continue

        for subfolder in os.listdir(category_path):
            src = os.path.join(category_path, subfolder)
            dst = os.path.join(split_path, subfolder)

            if os.path.isdir(src):
                shutil.move(src, dst)

        # remove empty folder
        os.rmdir(category_path)

print("Dataset restructuring complete!")