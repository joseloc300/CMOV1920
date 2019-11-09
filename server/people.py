from datetime import datetime
import json

def get_timestamp():
    return datetime.now().strftime(("%Y-%m-%d %H:%M:%S"))

# Create a handler for our read (GET) people
def read():
    """
    This function responds to a request for /api/people
    with the complete lists of people

    :return:        sorted list of people
    """
    # Create the list of people from our data
    with open('data/users.json') as json_file:
        users = json.load(json_file)
        for u in users:
            print(u)
            print('public_key: ' + users[u]['public_key'])
            print('birthdate: ' + users[u]['birthdate'])
            print('transaction_ids: ' + str(users[u]['transaction_ids']))
            print('')
        return users

    #return [PEOPLE[key] for key in sorted(PEOPLE.keys())]
    

def register():
    pass

def login():
    pass