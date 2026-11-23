import redis
import json
from models import EmailMessage
from renderer import render
from settings import settings
import logging

from jinja2 import Environment

log = logging.getLogger(__name__)


r = redis.Redis(
    host=settings.redis_host,
    port=settings.redis_port,
    db=0
)

while True:
    metadata = r.brpop('alarm_queue')
    message_info = json.loads(metadata[1].decode('utf-8'))
    if message_info['type'] != "EMAIL":
        continue
    full_message = r.hgetall(f"message:email: {message_info['id']}")

    log.info(f"Processing message ID: {message_info['id']} from sender {message_info['sender_id']}")

    raw = {k.decode(): v.decode() for k, v in full_message.items()}

    message = EmailMessage(
        id=message_info["id"],
        recipient_id=message_info["recipient_id"],
        type=raw["type"],
        locale=raw["locale"],
        variables=json.loads(raw["variables"])
    )

    email = render(message, brand_name=settings.brand_name)
    log.info("Renderd %s in %s: %r", message.type, email.locale, email.subject)
    