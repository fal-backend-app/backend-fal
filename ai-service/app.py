import os
import tempfile
from fastapi import FastAPI, UploadFile, File
from ultralytics import YOLO

app = FastAPI()

MODEL_PATH = "ml_model/best.pt"
model = YOLO(MODEL_PATH)

@app.post("/predict")
async def predict(file: UploadFile = File(...)):
    suffix = os.path.splitext(file.filename)[1] or ".jpg"

    with tempfile.NamedTemporaryFile(delete=False, suffix=suffix) as temp:
        temp.write(await file.read())
        temp_path = temp.name

    try:
        results = model(temp_path, conf=0.25)

        detections = []

        for result in results:
            for box in result.boxes:
                class_id = int(box.cls[0])
                class_name = result.names[class_id]
                confidence = float(box.conf[0])
                x1, y1, x2, y2 = box.xyxy[0].tolist()

                detections.append({
                    "classId": class_id,
                    "className": class_name,
                    "confidence": round(confidence, 4),
                    "box": {
                        "x1": round(x1, 2),
                        "y1": round(y1, 2),
                        "x2": round(x2, 2),
                        "y2": round(y2, 2)
                    }
                })

        return {"detections": detections}

    finally:
        if os.path.exists(temp_path):
            os.remove(temp_path)