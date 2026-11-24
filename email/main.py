import redis
import json
from models import EmailMessage
from renderer import render
from settings import settings
import logging

from jinja2 import Environment
if settings.debug:
    logging.basicConfig(
        level=logging.DEBUG,
        format="%(asctime)s %(levelname)s %(name)s: %(message)s"
    )
else:
    logging.basicConfig(
        level=logging.INFO,
        format="%(asctime)s %(levelname)s %(name)s: %(message)s"
    )
log = logging.getLogger(__name__)


r = redis.Redis(
    host=settings.redis_host,
    port=settings.redis_port,
    db=0,
    socket_timeout=20,
    socket_connect_timeout=5,
    socket_keepalive=True,
    health_check_interval=30
)
logging.info("Entering Worker Loop")
while True:
    metadata = r.brpop('alarm_queue', timeout=15)
    if metadata is None:
        log.debug("Timeout reached, trying again...")
        continue
    log.info("Received new message, starting process")
    message_info = json.loads(metadata[1].decode('utf-8'))
    log.debug("Metadata: %s", message_info)
    if message_info['type'] != "EMAIL":
        log.debug("Message type is not email, ignoring it.")
        continue
    full_message = r.hgetall(f"message:email: {message_info['id']}")
    log.debug("Full Message: %s", full_message)
    log.info(f"Processing message ID: {message_info['id']} from sender {message_info['recipient_id']}")

    raw = {k.decode(): v.decode() for k, v in full_message.items()}

    message = EmailMessage(
        id=message_info["id"],
        recipient_id=message_info["recipient_id"],
        type=raw["type"],
        locale=raw["locale"],
        variables=json.loads(raw["variables"])
    )
    log.debug("Message: %s", message.model_dump(mode="json"))
    email = render(message, brand_name=settings.brand_name)
    log.info("Rendered %s in %s: %r", message.type, email.locale, email.subject)
    