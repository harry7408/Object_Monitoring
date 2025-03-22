import io
import pandas as pd
import numpy as np
from loguru import logger

from PIL import Image

from ultralytics import YOLO
from ultralytics.utils.plotting import Annotator, colors

model = YOLO("./models/yolo11n.pt")

def get_image(image_bytes: bytes) -> Image :
    input_image=Image.open(io.BytesIO(image_bytes)).convert("RGB")
    return input_image

def transform_result_to_df(results: list, labels_dict: dict) -> pd.DataFrame:
    predict = pd.DataFrame(results[0].to("cpu").numpy().boxes.xyxy, columns=['left','top','right','bottom'])
    predict['confidence'] = results[0].to("cpu").numpy().boxes.conf
    predict['class'] = (results[0].to("cpu").numpy().boxes.cls).astype(int)
    predict['item'] = predict["class"].replace(labels_dict)
    return predict


def get_model_predict(model: YOLO, input_image: Image, save: bool = False, image_size: int = 1248, conf: float = 0.6, augment: bool= False) -> pd.DataFrame:
    predictions = model.predict(
                        imgsz=image_size, 
                        source=input_image, 
                        conf=conf,
                        save=save, 
                        augment=augment,
                        flipud= 0.0,
                        fliplr= 0.0,
                        mosaic = 0.0,
                        )
    

    predictions = transform_result_to_df(predictions, model.model.names)
    return predictions


def process_image(input_image: Image) -> pd.DataFrame:

    result=get_model_predict(
        model=model,
        input_image=input_image,
        save=False,
        image_size=640,
        augment=False,
        conf=0.6
    )
    return result