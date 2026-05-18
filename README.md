# ImmediatelyFast Backport for Minecraft 1.16.5

This is a Fabric client backport of ImmediatelyFast for Minecraft 1.16.5.

The project keeps the mod id `immediatelyfast` and uses the latest upstream release version as its base version: `1.16.5-backport`.

Backported from latest upstream:

- Universal immediate mode batching for 1.16.5 `VertexConsumerProvider.immediate`.
- Map atlas generation, font atlas resizing, fast text lookup, text buffer lookup caching, texture cleanup, redundant framebuffer and viewport suppression, and text translucency sorting skip.
