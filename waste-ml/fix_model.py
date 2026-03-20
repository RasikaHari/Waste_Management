import tensorflow as tf
from tensorflow.keras.applications import MobileNetV2
from tensorflow.keras.layers import Dense, GlobalAveragePooling2D, Dropout
from tensorflow.keras.models import Model

print("🔄 Rebuilding model...")

# Recreate SAME architecture
base_model = MobileNetV2(
    weights=None,   # IMPORTANT (no imagenet now)
    include_top=False,
    input_shape=(224, 224, 3)
)

x = base_model.output
x = GlobalAveragePooling2D()(x)
x = Dropout(0.5)(x)
x = Dense(128, activation='relu')(x)
output = Dense(8, activation='softmax')(x)

model = Model(inputs=base_model.input, outputs=output)

print("✅ Architecture rebuilt!")

# Load weights from your model file
print("🔄 Loading weights...")

model.load_weights("final_model.h5")

print("✅ Weights loaded successfully!")

# Save clean model
model.save("working_model.h5")

print("🎉 Saved as working_model.h5")