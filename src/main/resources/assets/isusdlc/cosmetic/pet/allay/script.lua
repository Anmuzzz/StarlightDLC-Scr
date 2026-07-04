local allay = models.alay42
allay:setParentType("World")
allay:setVisible(false)
allay:setScale(0.6, 0.6, 0.6)

local posLerp      = 0.12
local rotLerp      = 0.1
local bobAmp       = 0.15
local bobSpeed      = 0.1
local teleportDist  = 10
local colRadius     = 0.4
local blendSpeed    = 0.1
local spinChance    = 0.003

local distOptions = { 0.5, 1.0, 1.5, 2.0, 3.0, 4.0, 5.0 }
local distIdx     = 3
local distMult    = distOptions[distIdx]

local soundModes = {
    { name = "Off",    min = 0,   max = 0   },
    { name = "Rare",   min = 400, max = 800 },
    { name = "Normal", min = 200, max = 400 },
    { name = "Often",  min = 60,  max = 150 },
}
local soundMode = 3
local soundCD   = 0
local soundNext = math.random(200, 400)

local positions = {
    left   = vec(-1.0, 1.8,  0.3),
    right  = vec( 1.0, 1.8,  0.3),
    above  = vec( 0.0, 2.5,  0.0),
    behind = vec( 0.0, 1.8, -1.2),
    front  = vec( 0.0, 1.6,  1.0),
}
local posOrder = { "left", "right", "above", "behind", "front" }
local curPos   = "left"

local aIdle  = animations.alay42.IdleWithoutBlock
local aFly   = animations.alay42.FlyMoveWithoutBlock
local aIdleB = animations.alay42.IdleWithblock
local aFlyB  = animations.alay42.FlyWithMove
local aBlink = animations.alay42.Blink

local w  = { idle = 1, fly = 0, idleB = 0, flyB = 0 }
local tw = { idle = 1, fly = 0, idleB = 0, flyB = 0 }

local function startAnims()
    for _, a in pairs({ aIdle, aFly, aIdleB, aFlyB }) do
        if a then a:setLoop("LOOP"); a:setBlend(0); a:play() end
    end
    if aIdle then aIdle:setBlend(1) end
end
startAnims()

local pos, prevPos     = vec(0,0,0), vec(0,0,0)
local target           = vec(0,0,0)
local yaw, prevYaw     = 0, 0
local targetYaw        = 0
local ready            = false
local ticks            = 0
local moving, hasBlock = false, false
local hidden           = false
local prevPP           = vec(0,0,0)
local blinkCD          = 0
local blinkNext        = math.random(60, 180)
local spinning         = false
local spinTick         = 0
local spinYaw          = 0
local orbit            = false
local orbitSpeed       = 0.02
local orbitRadius      = 1.5

local function clamp(v, lo, hi) return math.max(lo, math.min(hi, v)) end
local function lerp(a, b, t) return a + (b - a) * t end

local function lerpAngle(a, b, t)
    local d = b - a
    while d > 180 do d = d - 360 end
    while d < -180 do d = d + 360 end
    return a + d * t
end

local function solid(p)
    local b = world.getBlockState(p)
    if not b or b:isAir() then return false end
    local s = b:getCollisionShape()
    return s ~= nil and #s > 0
end

local function collides(p)
    local r = colRadius
    for _, o in pairs({
        vec(0,0,0), vec(r,0,0), vec(-r,0,0),
        vec(0,r,0), vec(0,-r,0), vec(0,0,r), vec(0,0,-r)
    }) do
        if solid(p + o) then return true end
    end
    return false
end

local function fixCollision(old, new)
    if not collides(new) then return new end
    for _, alt in pairs({
        vec(old.x, new.y, new.z),
        vec(new.x, old.y, new.z),
        vec(new.x, new.y, old.z),
        vec(new.x, new.y + 0.3, new.z),
        vec(new.x, new.y - 0.3, new.z),
    }) do
        if not collides(alt) then return alt end
    end
    return old
end

local function pickAnim(m, b)
    tw.idle = 0; tw.fly = 0; tw.idleB = 0; tw.flyB = 0
    if b then
        if m then tw.flyB = 1 else tw.idleB = 1 end
    else
        if m then tw.fly = 1 else tw.idle = 1 end
    end
end

local function blendAnims()
    w.idle  = lerp(w.idle,  tw.idle,  blendSpeed)
    w.fly   = lerp(w.fly,   tw.fly,   blendSpeed)
    w.idleB = lerp(w.idleB, tw.idleB, blendSpeed)
    w.flyB  = lerp(w.flyB,  tw.flyB,  blendSpeed)
    if aIdle  then aIdle:setBlend(w.idle)   end
    if aFly   then aFly:setBlend(w.fly)     end
    if aIdleB then aIdleB:setBlend(w.idleB) end
    if aFlyB  then aFlyB:setBlend(w.flyB)   end
end

function events.tick()
    if hidden then return end
    ticks = ticks + 1

    local pp = player:getPos()
    if not pp then return end

    allay:setLight(world.getBlockLightLevel(pos), world.getSkyLightLevel(pos))

    local bodyYaw = player:getBodyYaw()
    local rad = math.rad(bodyYaw)
    local sy, cy = math.sin(rad), math.cos(rad)

    local fwdX, fwdZ = -sy, cy
    local rgtX, rgtZ = -cy, -sy

    local off = positions[curPos]
    local sx, sz = off.x * distMult, off.z * distMult

    local ox, oz
    if orbit then
        local a = ticks * orbitSpeed
        ox = math.cos(a) * orbitRadius * distMult
        oz = math.sin(a) * orbitRadius * distMult
    else
        ox = sx * rgtX + sz * fwdX
        oz = sx * rgtZ + sz * fwdZ
    end

    local bob = math.sin(ticks * bobSpeed) * bobAmp
    target = vec(pp.x + ox, pp.y + off.y + bob, pp.z + oz)

    if spinning then
        spinTick = spinTick + 1
        spinYaw = spinYaw + 18
        if spinTick >= 20 then
            spinning = false
            spinTick = 0
            spinYaw = 0
        end
        targetYaw = -bodyYaw - 180 + spinYaw
    else
        targetYaw = -bodyYaw - 180
        if not moving and math.random() < spinChance then
            spinning = true
            spinTick = 0
            spinYaw = 0
        end
    end

    if not ready then
        pos = target:copy()
        prevPos = pos:copy()
        prevPP = pp:copy()
        yaw = targetYaw
        prevYaw = targetYaw
        ready = true
    end

    prevPos = pos:copy()
    prevYaw = yaw

    local dist = (target - pos):length()
    local t = dist > 2 and clamp(posLerp * dist / 2, posLerp, 0.5) or posLerp

    if dist > teleportDist then
        pos = target:copy()
        prevPos = pos:copy()
        yaw = targetYaw
        prevYaw = targetYaw
    else
        pos = fixCollision(pos, pos + (target - pos) * t)
    end

    yaw = lerpAngle(yaw, targetYaw, rotLerp)

    local pv = (pp - prevPP):length()
    prevPP = pp:copy()

    local wasMoving = moving
    moving = pv > 0.01 or dist > 0.3
    if moving ~= wasMoving then pickAnim(moving, hasBlock) end
    blendAnims()

    blinkCD = blinkCD + 1
    if blinkCD >= blinkNext and aBlink then
        blinkCD = 0
        blinkNext = math.random(60, 180)
        aBlink:stop()
        aBlink:play()
    end

    local sm = soundModes[soundMode]
    if sm.min > 0 then
        soundCD = soundCD + 1
        if soundCD >= soundNext then
            soundCD = 0
            soundNext = math.random(sm.min, sm.max)
            sounds:playSound(
                hasBlock and "minecraft:entity.allay.ambient_with_item"
                         or "minecraft:entity.allay.ambient_without_item",
                pos, 0.3, 0.9 + math.random() * 0.2
            )
        end
    end
end

function events.render(delta)
    if not ready or hidden then
        allay:setVisible(false)
        return
    end
    allay:setVisible(true)
    allay:setPos((prevPos + (pos - prevPos) * delta) * 16)
    allay:setRot(0, lerpAngle(prevYaw, yaw, delta), 0)
end

-- =========================================
-- НАСТРОЙКА КРУГОВОГО МЕНЮ И УВЕДОМЛЕНИЙ
-- =========================================

local page = action_wheel:newPage("Allay")
action_wheel:setPage(page)

page:newAction()
    :title("§eHeld Item")
    :item("minecraft:diamond")
    :onLeftClick(function()
        hasBlock = not hasBlock
        pickAnim(moving, hasBlock)
        local state = hasBlock and "§aEnabled" or "§cDisabled"
        host:setActionbar("§eHeld Item: " .. state)
    end)

page:newAction()
    :title("§bPosition")
    :item("minecraft:compass")
    :onLeftClick(function()
        local i = 1
        for j, k in ipairs(posOrder) do if k == curPos then i = j break end end
        curPos = posOrder[i % #posOrder + 1]
        host:setActionbar("§bPosition: §f" .. curPos)
    end)

page:newAction()
    :title("§cVisibility")
    :item("minecraft:ender_eye")
    :onLeftClick(function()
        hidden = not hidden
        if hidden then
            allay:setVisible(false)
            host:setActionbar("§cVisibility: Hidden")
        else
            ready = false
            startAnims()
            pickAnim(moving, hasBlock)
            host:setActionbar("§aVisibility: Visible")
        end
    end)

page:newAction()
    :title("§6Distance")
    :item("minecraft:spyglass")
    :onLeftClick(function()
        distIdx = distIdx % #distOptions + 1
        distMult = distOptions[distIdx]
        host:setActionbar("§6Distance: §f" .. distMult)
    end)
    :onRightClick(function()
        distIdx = distIdx - 1
        if distIdx < 1 then distIdx = #distOptions end
        distMult = distOptions[distIdx]
        host:setActionbar("§6Distance: §f" .. distMult)
    end)

page:newAction()
    :title("§aSounds")
    :item("minecraft:note_block")
    :onLeftClick(function()
        soundMode = soundMode % #soundModes + 1
        soundCD = 0
        local sm = soundModes[soundMode]
        if sm.min > 0 then soundNext = math.random(sm.min, sm.max) end
        host:setActionbar("§aSounds: §f" .. sm.name)
    end)
    :onRightClick(function()
        soundMode = soundMode - 1
        if soundMode < 1 then soundMode = #soundModes end
        soundCD = 0
        local sm = soundModes[soundMode]
        if sm.min > 0 then soundNext = math.random(sm.min, sm.max) end
        host:setActionbar("§aSounds: §f" .. sm.name)
    end)

page:newAction()
    :title("§6Orbit")
    :item("minecraft:clock")
    :onLeftClick(function()
        orbit = not orbit
        local state = orbit and "§aEnabled" or "§cDisabled"
        host:setActionbar("§6Orbit: " .. state)
    end)