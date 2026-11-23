from dataclasses import dataclass
from datetime import date
from functools import lru_cache
from pathlib import Path

import yaml
from jinja2 import Environment, FileSystemLoader, StrictUndefined

from models import EmailMessage, EmailType

BASE_DIR = Path(__file__).parent
DEFAULT_LOCALE = "en"

TEMPLATES: dict[EmailType, str] = {
    EmailType.USER_CONFIRMATION: "user_confirmation",
}

env_html = Environment(
    loader = FileSystemLoader(BASE_DIR / "templates"),
    autoescape=True,
    undefined=StrictUndefined,
    trim_blocks=True,
    lstrip_blocks=True,
)

env_text = Environment(
    loader=FileSystemLoader(BASE_DIR / "templates"),
    autoescape=False,
    undefined=StrictUndefined,
    trim_blocks=True,
    lstrip_blocks=True,
)

env_string = Environment(autoescape=False, undefined=StrictUndefined)

@lru_cache
def load_locale(locale: str) -> dict:
    with (BASE_DIR / "locales" / f"{locale}.yml").open(encoding="utf-8") as f:
        return yaml.safe_load(f) or {}

def resolve_locale(locale: str) -> str:
    """es-ES/ es_ES / es -> el fichero que exista, con fallback a DEFAULT_LOCALE"""
    normalized = locale.strip().replace("-","_")
    for candidate in (normalized, normalized.split("_")[0], DEFAULT_LOCALE):
        if (BASE_DIR / "locales" / f"{candidate}.yml").exists():
            return candidate
    raise FileNotFoundError(f"No hay locales para {locale!r} ni fallback {DEFAULT_LOCALE!r}")

@dataclass(frozen=True)
class RenderedEmail:
    subject: str
    html: str
    text: str
    locale: str

def render(message: EmailMessage, brand_name: str) -> RenderedEmail:
    locale = resolve_locale(message.locale)
    template_name = TEMPLATES[message.type]

    translations = load_locale(locale)[message.type.value]

    globals_ = {
        "brand_name": brand_name,
        "year": date.today().year,
        "locale": locale
    }

    context_for_texts = {**message.variables, **globals_}
    t = {
        key: env_string.from_string(value).render(**context_for_texts)
        for key, value in translations.items()
    }

    context = {**message.variables, **globals_, "t": t}

    html = env_html.get_template(f"{template_name}.html.j2").render(**context)
    text = env_text.get_template(f"{template_name}.txt.j2").render(**context)

    subject = " ".join(t["subject"].split())

    return RenderedEmail(subject=subject, html=html, text=text, locale=locale)