from flask_cors import CORS
from flask import Flask, jsonify, request, make_response
from listen_and_send import process_content, parse_content
import json

app = Flask(__name__)
CORS(app)

@app.route('/', methods=["POST"])
def index():

    data = json.loads(request.data)
    content = parse_content(data['query'])
    if content:
        return make_response({'response': content})
    return make_response({'response': 'dummy'})

if __name__ == '__main__':
    app.run(debug=True)
