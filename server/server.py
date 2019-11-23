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
        "vouchers": [],
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

@app.route('/vouchers', methods=['GET'])
def get_vouchers():
    ret = {}
    ret['vouchers'] = []
    user_uuid = request.form['user_uuid']
    counter = 0
    with open('data/users.json') as json_file:
        data = json.load(json_file)
        for key in data.keys():
            if key == user_uuid:
                ret['vouchers'] = data[key]['vouchers']
                ret['total'] = len(data[key]['vouchers'])   
                break

    return ret, 200

@app.route('/checkoutInfo', methods=['POST'])
def get_checkoutInfo():
    ret = {}
    ret['vouchers'] = []
    user_uuid = request.form['user_uuid']
    counter = 0
    with open('data/users.json') as json_file:
        data = json.load(json_file)
        for key in data.keys():
            if key == user_uuid:
                if len(data[key]['vouchers']) > 0:
                    ret['voucher'] = data[key]['vouchers'][0]
                else:
                    ret['voucher'] = "null"
                ret['total'] = len(data[key]['vouchers'])   
                ret['storedDiscount'] = data[key]['acumulated_discount']
                break

    return ret, 200

@app.route('/transactions', methods=['GET'])
def get_transactions():    
    ret = {}
    ret['transactions'] = []
    user_uuid = request.form['user_uuid']
    counter = 0
    with open('data/transactions.json') as json_file:
        data = json.load(json_file)
        for key in data.keys():
            transaction = data[key]
            if transaction['owner'] == user_uuid:
                ret['transactions'].append(transaction)
                counter += 1
        
        ret['total'] = counter

    return ret, 200

@app.route('/checkout', methods=['POST'])
def checkout():
    ret = {}
    transaction = {}
    
    user_uuid = request.form['user_uuid']
    items = request.form['items']
    voucher = request.form['voucher']
    useDiscount = request.form['useDiscount']

    itemsJSON = json.loads(items)

    total_spent = 0

    print(itemsJSON)

    for key in itemsJSON.keys():
        itemJson = json.loads(itemsJSON[key])
        total_spent += int(itemJson['price'])
    
    data = {}
    
    with open('data/users.json') as json_file:
        data = json.load(json_file)
        if useDiscount == "Y":
            total_spent -= data[user_uuid]['acumulated_discount']
            data[user_uuid]['acumulated_discount'] = 0
            if total_spent < 0:
                total_spent = 0
        
        if voucher != "null":
            data[user_uuid]['acumulated_discount'] += int(total_spent * 0.15)
            data[user_uuid]['vouchers'].remove(voucher)

        old_total_spent = data[user_uuid]['total_spending']
        data[user_uuid]['total_spending'] += total_spent
        new_total_spent = data[user_uuid]['total_spending']
        old = old_total_spent / 100 / 100
        new = new_total_spent / 100 / 100
        diff = int(new - old)
        for i in range(0, diff):
            new_voucher = str(uuid.uuid4())
            data[user_uuid]['vouchers'].append(new_voucher)
            
    with open('data/users.json', 'w') as json_file:
        json.dump(data, json_file)

    transaction['owner'] = user_uuid
    transaction['items'] = items
    transaction['total'] = total_spent
    transaction['used_voucher'] = voucher

    next_id = 0
    with open('data/transactions.json') as json_file:
        data = json.load(json_file)
        next_id = data['next_id']
    
    data['next_id'] = next_id + 1
    data["transaction" + str(next_id)] = transaction

    with open('data/transactions.json', 'w') as json_file:
        json.dump(data, json_file)

    ret['spent'] = total_spent
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



