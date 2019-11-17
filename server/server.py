from flask import Flask, request
from flask_restful import Resource, Api
from sqlalchemy import create_engine
from json import dumps
from flask import jsonify, abort
import uuid

app = Flask(__name__)        

@app.route('/users/register', methods=['POST'])
def create_task():
    if not request.json or not 'title' in request.json:
        abort(400)
    
    new_user = {
        "public_key": request.json['public_key'],
        "fullname": request.json['fullname'],
        "card_number": request.json['card_number'],
        "card_exp_date": request.json['card_exp_date'],
        "card_verify": request.json['card_verify'],
        "transaction_ids": [],
        "voucher_ids": [],
        "total_spent": 0
    }

    

    new_uuid = uuid.uuid4()
    user_uuid = new_uuid.bytes()

    return user_uuid, 201

@app.route('/isalive', methods=['GET'])
def isalive():
    return {}, 200

@app.route('/todo/api/v1.0/tasks3', methods=['GET'])
def create_task3():
    # if not request.json or not 'title' in request.json:
    #     abort(400)
    # task = {
    #     'id': tasks[-1]['id'] + 1,
    #     'title': request.json['title'],
    #     'description': request.json.get('description', ""),
    #     'done': False
    # }
    # tasks.append(task)
    # return jsonify({'task': task}), 201
    return "vuncionou3"

if __name__ == '__main__':
     app.run(host= '0.0.0.0',port='5000')