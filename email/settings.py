from pydantic import Field
from pydantic_settings import BaseSettings, SettingsConfigDict

class Settings(BaseSettings):
    model_config = SettingsConfigDict(extra="ignore", frozen=True)

    redis_host: str = Field(min_length=1)
    redis_port: int = Field(default=6379, ge=1, le=65535)
    brand_name: str = Field(min_length=1)
    smtp_host: str = Field(min_length=1)
    smtp_port: str = Field(min_length=1)
    smtp_from: str = Field(min_length=1)
    smtp_user: str = Field(min_length=1)
    smtp_password: str = Field(min_length=1)
    smtp_ssl: bool = Field(default=False)
    smtp_tls: bool = Field(default=True)
    debug: bool = Field(default=False)
settings = Settings()