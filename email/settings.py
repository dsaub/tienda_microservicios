from pydantic import Field
from pydantic_settings import BaseSettings, SettingsConfigDict

class Settings(BaseSettings):
    model_config = SettingsConfigDict(extra="ignore", frozen=True)

    redis_host: str = Field(min_length=1)
    redis_port: int = Field(default=6379, ge=1, le=65535)
    brand_name: str = Field(min_length=1)
    debug: bool = Field(default=False)
settings = Settings()