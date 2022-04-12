from flask import Flask
from flask import render_template

app = Flask(__name__)

CODE = 'NULL'
TIME = None


@app.route('/')
def hello_world():
    return render_template('index.html', code=CODE)


@app.route('/update/<code>')
def update(code):
    global CODE
    CODE = code
    return code


if __name__ == '__main__':
    app.run(debug=False, host='0.0.0.0', port=39123)
