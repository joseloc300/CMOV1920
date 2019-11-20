from flask import Flask, request
from flask_restful import Resource, Api
from sqlalchemy import create_engine
from flask import jsonify, abort
import uuid
import json
from Crypto.PublicKey import RSA

app = Flask(__name__)        

@app.route('/users/register', methods=['POST'])
def create_task():    
    new_user = {
        "public_key": request.form['public_key'],
        "username": request.form['username'],
        "fullname": request.form['fullname'],
        "card_number": request.form['card_number'],
        "card_expiration": request.form['card_expiration'],
        "card_cv2": request.form['card_cv2'],
        "transaction_ids": [],
        "voucher_ids": [],
        "acumulated_discount": 0
    }

    new_uuid = uuid.uuid4()
    user_uuid = str(new_uuid.bytes)

    data = {}
    with open('data/users.json') as json_file:
        data = json.load(json_file)
        data[user_uuid] = new_user

    with open('data/users.json', 'w') as json_file:
            json.dump(data, json_file)

    public_key = ""
    with open('keys.json') as json_file:
        data = json.load(json_file)
        public_key = data['public']

    return {"user_uuid": user_uuid, "server_public_key": public_key}, 201

@app.route('/isalive', methods=['GET'])
def isalive():
    return {}, 200


def check_keys():
    write = False
    with open('keys.json') as json_file:
        data = json.load(json_file)
        if data['public'] == "":
            write = True
        
    if write == True:
        data = {}
        with open('keys.json', 'w') as json_file:
            key = RSA.generate(2048)
            data['public'] = str(key.publickey().export_key())
            data['private'] = str(key.export_key())
            json.dump(data, json_file)



check_keys()

if __name__ == '__main__':
     app.run(host= '0.0.0.0',port='5000')



