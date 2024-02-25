from fastapi import FastAPI
from starlette.middleware.cors import CORSMiddleware
from starlette_prometheus import (
    PrometheusMiddleware,
    metrics,
)

from main.api.base.base import (
    Base,
    engine,
)
from main.api.config import settings
from main.api.user.controller.login import router as login_router
from main.api.user.controller.register import router as register_router
from main.api.user.controller.user import router as user_router
from main.api.utils.response import ok_response

route = settings.route


def init_db():
    Base.metadata.create_all(engine)


init_db()


app = FastAPI()

app.add_middleware(
    CORSMiddleware,
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

app.add_middleware(PrometheusMiddleware, filter_unhandled_paths=True)
app.add_route(f"/metrics", metrics)

# Routers
app.include_router(register_router)
app.include_router(login_router)
app.include_router(user_router)


@app.get(route)
async def server_liveness():
    """
    Server liveness probe.
    Used by automated tools to check that the server is online.
    """
    return ok_response(message="The Rest server is alive!")
