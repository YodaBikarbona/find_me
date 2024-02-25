from main.api.user.model.user import User


def dump(user: User) -> dict:
    return {
        "id": user.id,
        "username": user.username,
        "email": user.email,
        "activated": user.activated,
        "banned": user.banned,
    }
