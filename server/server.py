from flask import Flask, request
from flask_restful import Resource, Api
from sqlalchemy import create_engine
from flask import jsonify, abort
import uuid
import json
from Crypto.PublicKey import RSA
from Crypto.Random import get_random_bytes
from Crypto.Cipher import AES, PKCS1_OAEP
import base64

app = Flask(__name__)        

@app.route('/users/register', methods=['POST'])
def register_user():    
    new_user = {
        "public_key": request.form['public_key'],
        "username": request.form['username'],
        "fullname": request.form['fullname'],
        "card_number": request.form['card_number'],
        "card_expiration": request.form['card_expiration'],
        "card_cv2": request.form['card_cv2'],
        "transaction_ids": [],
        "voucher_ids": [],
        "total_spending": 0,
        "acumulated_discount": 0
    }

    new_uuid = uuid.uuid4()
    user_uuid = str(new_uuid)

    data = {}
    with open('data/users.json') as json_file:
        data = json.load(json_file)
        data[user_uuid] = new_user

    with open('data/users.json', 'w') as json_file:
            json.dump(data, json_file)

    
    public = open("public_key.txt", "rb")
    public_key = public.read()
    public_key_str = str(public_key)

    return {"user_uuid": user_uuid, "server_public_key": public_key_str}, 201

@app.route('/isalive', methods=['GET'])
def isalive():
    return {}, 200

@app.route('/items', methods=['GET'])
def get_items():
    ret = {}
    
    private = open("private_key.txt", "rb")
    private_key_str = private.read()
    private_key = RSA.importKey(private_key_str)
    print(private_key)

    with open('data/items.json') as json_file:
        data = json.load(json_file)
        for key in data.keys():
            item = data[key]
            item['id'] = key
            item_str = json.dumps(item)
            item_bytes = bytes(item_str, "utf-8")
            ret[key] = str(base64.b64encode(item_bytes))

    return ret, 200

@app.route('/coupons', methods=['GET'])
def get_coupons():
    ret = {}
    
    private = open("private_key.txt", "rb")
    private_key_str = private.read()
    private_key = RSA.importKey(private_key_str)
    print(private_key)

    with open('data/items.json') as json_file:
        data = json.load(json_file)
        for key in data.keys():
            item = data[key]
            item_bytes = bytes(str(item), "utf-8")
            cipher_rsa = PKCS1_OAEP.new(private_key)
            encrypted_item = cipher_rsa.encrypt(item_bytes)
            ret[key] = str(encrypted_item)

    return ret, 200

@app.route('/transactions', methods=['GET'])
def get_transactions():
    ret = {}
    
    private = open("private_key.txt", "rb")
    private_key_str = private.read()
    private_key = RSA.importKey(private_key_str)
    print(private_key)

    with open('data/items.json') as json_file:
        data = json.load(json_file)
        for key in data.keys():
            item = data[key]
            item_bytes = bytes(str(item), "utf-8")
            cipher_rsa = PKCS1_OAEP.new(private_key)
            encrypted_item = cipher_rsa.encrypt(item_bytes)
            ret[key] = str(encrypted_item)

    return ret, 200

@app.route('/checkout', methods=['POST'])
def checkout():
    ret = {}
    
    user = request.form['user']
    items = request.form['items']
    voucher = request.form['voucher']
    useDiscount = request.form['useDiscount']

    ret['spent'] = 0
    ret['status'] = "Sucess"

    return ret, 200


def create_keys():
    private = open("private_key.txt", "wb")
    public = open("public_key.txt", "wb")

    key = RSA.generate(2048)
    public.write(key.publickey().export_key())
    public.close()

    private.write(key.export_key())
    private.close()


#only ran once
#create_keys()

if __name__ == '__main__':
     app.run(host= '0.0.0.0',port='5000')



