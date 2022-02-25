import websockets
import psutil
import asyncio
import json

async def event_generator(websocket, path):
    while True:
        await asyncio.sleep(1)
        cpu_percent = psutil.cpu_percent()
        used_ram = psutil.virtual_memory().percent
        await websocket.send(json.dumps({"cpu": cpu_percent, "ram": used_ram}))

if __name__ == '__main__':
    start_server = websockets.serve(event_generator, "127.0.0.1", 8444)
    asyncio.get_event_loop().run_until_complete(start_server)
    asyncio.get_event_loop().run_forever()
