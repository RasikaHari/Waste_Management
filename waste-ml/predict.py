import tensorflow as tf
import numpy as np

print("🔄 Loading model...")

model = tf.keras.models.load_model("working_model.h5", compile=False)

print("✅ Model loaded successfully!")

class_names = [
    "ewaste",
    "food_waste",
    "leaf_waste",
    "metal_cans",
    "paper_waste",
    "plastic_bags",
    "plastic_bottles",
    "wood_waste"
]

def predict_image(img_path):
    img = tf.keras.utils.load_img(img_path, target_size=(224, 224))
    img_array = tf.keras.utils.img_to_array(img)
    img_array = np.expand_dims(img_array, axis=0) / 255.0

    predictions = model.predict(img_array)

    index = np.argmax(predictions)
    confidence = predictions[0][index] * 100

    print("\n🧠 Prediction:", class_names[index])
    print("📊 Confidence: {:.2f}%".format(confidence))


predict_image("sample.jpg")
predict_image("Food1.jpeg")