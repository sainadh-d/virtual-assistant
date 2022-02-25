import speech_recognition as sr
import asyncio
import datetime
import random
import websockets
import datetime
import pyautogui
import subprocess
import re

def open_dir(dir):
    subprocess.run(["open", dir], cwd="/Users/saidev")

def parse_content(content):
    match = re.match("open (.*)", content)
    if match:
        dir = match.group(1)
        open_dir(dir)
        return f"Opening {dir}"

def process_content(content):
    if "hello" in content.lower():
        return "Hi"
    if "full screen" in content.lower():
        pyautogui.hotkey('command', 'f')
    if any([x in content.lower() for x in ["stop video", "play", "resume"]]):
        pyautogui.press('space')
    if "escape" in content.lower():
        pyautogui.press('esc')
    if "open finder" in content.lower():
        subprocess.run(["open", "."], cwd="/Users/saidev")
    if "open downloads" in content.lower():
        subprocess.run(["open", "downloads"], cwd="/Users/saidev")

async def listen_and_send(websocket):
    while True:
        with mic as source:
            try:
                # Todo: close websocket server connection if client disconnects
                # Duration in secs it waits to judge ambient noise
                r.adjust_for_ambient_noise(source, 0.5)
                audio = r.listen(source, timeout=3, phrase_time_limit=5)
                content = r.recognize_google(audio, language='en-in')
                print(f"Recognized: {content}")
                await websocket.send(content)
                if "stop listening" in content.lower():
                    await websocket.send("Bye!")
                    break
                processed = process_content(content)
                if processed:
                    await websocket.send(processed)
            except sr.WaitTimeoutError:
                print("No input, listening again")
            except sr.UnknownValueError:
                print("Cannot understand what you said")
            except Exception as e:
                print("Some unknown exception " + str(e))

async def time(websocket):
    while True:
        now = datetime.datetime.utcnow().isoformat() + "Z"
        await websocket.send(now)
        await asyncio.sleep(random.random() * 3)

async def listen_and_send2(websocket):
    while True:
        await websocket.send(random.choice(["Hello", "Hey", "Bro"]))
        await asyncio.sleep(3)

async def router(websocket, path):
    if path == "/speech":
        await listen_and_send(websocket)
    elif path == "/time":
        await time(websocket)

if __name__ == '__main__':
    r = sr.Recognizer()
    mic = sr.Microphone()

    start_server = websockets.serve(router, "127.0.0.1", 8443)
    asyncio.get_event_loop().run_until_complete(start_server)
    asyncio.get_event_loop().run_forever()
