local squapi = require("SquAPI")

local idle = animations.model.BirbIdle:play()

squapi.smoothHead:new(
    {
        models.model.Birb.BirbBody,
        models.model.Birb.BirbBody.BirbHead
    },
    {0.25, 0.25},
    0.25
)

squapi.hoverPoint:new(
    models.model.Birb,    --element
    vec(-1,2,0.5),    --(vec(0,0,0))elementOffset
    nil,    --(0.2) springStrength
    nil,    --(5) mass
    nil,    --(1) resistance
    nil,    --(0.05) rotationSpeed
    nil,    --(true) rotateWithPlayer
    nil     --(false) doCollisions
)