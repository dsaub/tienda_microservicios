from smtplib import SMTP, SMTP_SSL
from settings import settings
import logging
import ssl
from email.message import EmailMessage

log = logging.getLogger(__name__)

def send_email(to: str, subject: str, html: str, text: str):
    contexto = ssl.create_default_context()
    if settings.smtp_ssl:
        # Estamos usando SSL
        raise NotImplementedError()

    else:
        # Estamos usando TLS
        with SMTP(host=settings.smtp_host, port=settings.smtp_port) as smtp:
            smtp.starttls(context=contexto)
            smtp.login(settings.smtp_user, settings.smtp_password)
            msg = EmailMessage()
            msg["Subject"] = subject
            msg["From"] = settings.smtp_from
            msg["To"] = to

            msg.set_content(text)
            msg.add_alternative(html, subtype="html")

            smtp.send_message(msg)