import sys
import json
from ultralytics import YOLO

MODEL_PATH = "ml_model/best.pt"

model = YOLO(MODEL_PATH)

def predict(image_path):
    results = model(image_path, conf=0.25)

    detections = []

    for result in results:
        for box in result.boxes:
            class_id = int(box.cls[0])
            class_name = result.names[class_id]
            confidence = float(box.conf[0])

            x1, y1, x2, y2 = box.xyxy[0].tolist()

            detections.append({
                "class_id": class_id,
                "class_name": class_name,
                "confidence": round(confidence, 4),
                "box": {
                    "x1": round(x1, 2),
                    "y1": round(y1, 2),
                    "x2": round(x2, 2),
                    "y2": round(y2, 2)
                }
            })

    return detections


if __name__ == "__main__":
    image_path = sys.argv[1]
    output = predict(image_path)
    print(json.dumps(output, ensure_ascii=False))