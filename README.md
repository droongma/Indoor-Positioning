# Indoor-Positioning

## Requirement
Collector is created based on Windows 10.
Server is developed based on Linux(Ubuntu-18.04).

Install Anaconda for Windows. Then implment Anaconda Prompt with **Administration mode** and move to project folder(Indoor-positioning).


## Collector
Now follow the steps below within Anaconda Prompt.

First, create virtual environment.
```shell
> conda env create -f environment.yaml
```


Now the virtual environment named **capstone** is created and you should activate it.
```shell
> conda activate capstone
```


Then, run the main.py.
```shell
> python main.py
```

## Server
First, change your working directory to backend and create a virtual environment.
```shell
> cd backend
> python3 -m venv venv
```
- The name of virtual environment should be venv.

Then, activate the virtual environment.
```shell
> source venv/bin/activate
```
- This virtual environment **must be activated** whenever you try to run server.
- If it is successfully activated, (venv) would be displayed at the leftmost of your shell.

Next, install packages required for the server.
``` shell
> pip install -r deploy/requirements.txt
```

Lastly, you should create secret.key file. Django project requires secret.key but it is not included in this repo because of the security issue.
```python
import string, random


# Get ascii Characters numbers and punctuation (minus quote characters as they could terminate string).
chars = ''.join([string.ascii_letters, string.digits, string.punctuation]).replace('\'', '').replace('"', '').replace('\\', '')

SECRET_KEY = ''.join([random.SystemRandom().choice(chars) for i in range(50)])

print(SECRET_KEY)
```
- Using Python, create random string and store it into data/config/secret.key. (You should create secret.key file)

Now you can run server with following command.
```shell
python3 manage.py runserver
```


