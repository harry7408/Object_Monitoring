import pandas as pd
import json
import sys

# About FastAPI
from fastapi import FastAPI, File
from fastapi.responses import RedirectResponse
from fastapi.middleware.cors import CORSMiddleware
from fastapi.exceptions import HTTPException

from PIL import Image
from loguru import logger

from util import get_image
from util import process_image

app = FastAPI()
origins = [
    "http://localhost/object:8081"
]

app.add_middleware(
    CORSMiddleware,
    allow_origins= origins,
    allow_credentials = True,
    allow_methods = ["*"],
    allow_headers = ["*"]
)

logger.remove()
logger.add(sys.stderr, colorize= True,
           format = "<green>{time:HH:mm:ss}</green> | <level>{message}</level>",
           level=10)


# Swagger로 Redirect
@app.get("/",include_in_schema = False)
async def redirect():
        return RedirectResponse("/docs")

@app.post("/object_monitor")
async def detect_image(file: bytes = File(...)):
    try:
        input_image = get_image(file)

    except Exception as e:
        raise HTTPException(status_code = 404, detail=str(e))
        
    predict = process_image(input_image)
    result = {
         "result" : json.loads(
              predict[['item', 'confidence', 'left','top','right','bottom']].to_json(orient='records')
         )
    }

    logger.info(f"Results : {result}")
    return result