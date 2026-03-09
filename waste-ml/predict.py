import os
os.environ['TF_CPP_MIN_LOG_LEVEL'] = '3'  # hide info/warnings

import tensorflow as tf
tf.get_logger().setLevel('ERROR')  # hide TF/Keras logs

import numpy as np
from tensorflow.keras.preprocessing import image

# Load model quietly
model = tf.keras.models.load_model("waste_mobilenet_model.keras", compile=False)

# Save the model (directly, no need for _)
model.save("waste_mobilenet_model_saved.keras")

# Classes
classes = [
    "ewaste",
    "food_waste",
    "leaf_waste",
    "metal_cans",
    "paper_waste",
    "plastic_bags",
    "plastic_bottles",
    "wood_waste"
]

# Load and preprocess image
img = image.load_img("sample.jpg", target_size=(224,224))
img_array = np.expand_dims(np.array(img)/255.0, axis=0)

# Predict quietly
prediction = model.predict(img_array, verbose=0)

print("Prediction:", classes[np.argmax(prediction)])