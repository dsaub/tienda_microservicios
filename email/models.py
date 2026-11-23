from enum import StrEnum

from pydantic import BaseModel, Field

class EmailType(StrEnum):
    USER_CONFIRMATION = "USER_CONFIRMATION"
    PASSWORD_RESET = "PASSWORD_RESET"

class EmailMessage(BaseModel):
    id: str
    recipient_id: int
    type: EmailType
    locale: str
    variables: dict[str, str] = Field(default_factory=dict)