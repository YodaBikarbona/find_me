from main.api.base.base import Session
from main.api.profile.model.profile import Profile


def new_profile(session: Session, user_id: int) -> Profile:
    profile = Profile(user_id=user_id)
    session.add(profile)
    session.flush()
    return profile
