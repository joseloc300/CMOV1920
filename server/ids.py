import uuid
import json

uuid1 = uuid.UUID("71bb0e0d-111f-4441-98b4-13ba3b3d8ca0")

uuid2 = uuid.UUID("d5181446-e7e8-4750-a858-1293ca67118f")

uuid3 = uuid.UUID("c1ef30a2-017e-4d6f-87f8-e3d9f5a56631")

uuid4 = uuid.UUID("265df551-4890-4797-82c1-42cf8eb46f89")

uuid5 = uuid.UUID("383268c1-3aa7-46c6-9280-e70e5fb09265")

uuid6 = uuid.UUID("d9966064-3406-483e-b851-73f3b9065c13")

ids = [str(uuid1), str(uuid2), str(uuid3), str(uuid4), str(uuid5), str(uuid6)]


data = {}
with open('./data/items.json') as json_file:
    data = json.load(json_file)

newdata = data.copy()

for key in data.keys():
    newdata[ids[0]] = data[key]
    ids.pop(0)
    
with open('./data/items.json', 'w') as json_file:
    json.dump(newdata, json_file)