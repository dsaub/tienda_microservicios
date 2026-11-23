# syntax=docker/dockerfile:1

# ---------- build: resuelve e instala dependencias con uv ----------
FROM python:3.15-slim AS builder

ENV UV_COMPILE_BYTECODE=1 \
    UV_LINK_MODE=copy \
    UV_PYTHON_DOWNLOADS=never \
    UV_PROJECT_ENVIRONMENT=/app/.venv

RUN pip install --no-cache-dir uv==0.13.7

WORKDIR /app

# Manifiestos primero: si no cambian, la capa de dependencias se reutiliza
COPY pyproject.toml uv.lock .python-version ./
RUN uv sync --frozen --no-dev --no-install-project

# Código, plantillas y locales
COPY . .
RUN uv sync --frozen --no-dev

# ---------- runtime ----------
FROM python:3.15-slim

LABEL org.opencontainers.image.source="https://github.com/dsaub/tienda_microservicios"

ENV PYTHONUNBUFFERED=1 \
    PYTHONDONTWRITEBYTECODE=1 \
    PATH="/app/.venv/bin:$PATH"

WORKDIR /app

RUN groupadd --system --gid 10001 app \
 && useradd --system --uid 10001 --gid app --home-dir /app --no-create-home app

COPY --from=builder --chown=app:app /app/.venv /app/.venv
COPY --chown=app:app . /app

USER app

# Worker: no expone puerto, solo consume de Redis
CMD ["python", "main.py"]
